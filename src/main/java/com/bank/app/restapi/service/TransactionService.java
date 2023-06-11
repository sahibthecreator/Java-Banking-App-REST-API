package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.dto.mapper.TransactionMapper;
import com.bank.app.restapi.model.*;
import com.bank.app.restapi.repository.AccountRepository;
import com.bank.app.restapi.repository.TransactionRepository;
import com.bank.app.restapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    @Autowired
    private AccountService accountService;
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    private final Environment environment;

    private TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, AccountRepository accountRepository,
            UserRepository userRepository, Environment environment, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.environment = environment;
        this.transactionMapper = transactionMapper;
    }

    // The service method called by GET /transactions endpoint
    public List<TransactionDTO> getTransactions(String iban, Float minAmount, Float maxAmount, Float exactAmount,
            TransactionType typeOfTransaction, LocalDate startDate, LocalDate endDate) {

        // Setting a default limit of transactions retrieved
        int defaultLimit = Integer.parseInt(environment.getProperty("api.transaction.default-limit", "100"));

        // Retrieving all transactions that satisfy the filters applied
        List<Transaction> transactions = transactionRepository.findAll(
                applyQueryFilter(iban,
                                minAmount,
                                maxAmount,
                                exactAmount,
                                typeOfTransaction,
                                startDate,
                                endDate,
                                null )); // callingUserId here is null because employees can only call getTransactions().
                                                    // But in getTransactionsByUserId an actual value is passed for validation purposes


        return transactions.stream().map(transactionMapper::toDTO).limit((defaultLimit)).toList();

    }

    // Specification criteria builder, used to make queries to the db. Used for the filtering functionalities of some GET methods
    private Specification<Transaction> applyQueryFilter(String iban, Float minAmount, Float maxAmount, Float exactAmount,
            TransactionType typeOfTransaction, LocalDate startDate, LocalDate endDate, UUID callingUserId) {

        Specification<Transaction> specification = Specification.where(null);

        // If the callingUserId is specified, retrieve only transactions related with their own IBANs
        if (callingUserId != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.in(root.get("fromAccount").get("iban")).value(
                            accountRepository.findIbanByUserId(callingUserId)),
                    criteriaBuilder.in(root.get("toAccount").get("iban")).value(
                            accountRepository.findIbanByUserId(callingUserId))
            ));
        }

        // Query based on entered IBAN
        if (iban != null && !iban.isEmpty()) {
            if (callingUserId != null) {
                // Validating if customer is trying to reach IBAN that doesn't belong to them
                validateGetTransactionsLogic(userRepository.findById(callingUserId).orElseThrow(
                                () -> new EntityNotFoundException("Calling user not found")),
                        mapAccountIbanToOwner(accountService.getAccountByIban(iban)));
            }
            // Add results that have the entered IBAN in either the sending or receiving account
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("fromAccount").get("iban"), iban),
                    criteriaBuilder.equal(root.get("toAccount").get("iban"), iban)));
        }

        // Query based on exact amount of transaction, min or max amount
        if (exactAmount != null) {
            specification = specification
                    .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("amount"), exactAmount));
        } else if (minAmount != null && maxAmount != null) {
            // Validating if min amount entered is not bigger that the max amount entered
            if (minAmount <= maxAmount) {
                specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                        .between(root.get("amount"), minAmount, maxAmount));
            } else {
                throw new IllegalArgumentException("minAmount should be less than or equal to maxAmount.");
            }
        } else if (minAmount != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .greaterThanOrEqualTo(root.get("amount"), minAmount));
        } else if (maxAmount != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount));
        }

        // Query based on type of transactions to receive
        if (typeOfTransaction != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .equal(root.get("typeOfTransaction"), typeOfTransaction));
        }

        // Query for starting date of the performed transactions
        if (startDate != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                    root.get("dateOfExecution").as(Date.class),
                    Date.valueOf(startDate)));
        }

        // Query for ending date of the performed transactions
        if (endDate != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                    root.get("dateOfExecution").as(Date.class),
                    Date.valueOf(endDate)));
        }

        return specification;
    }

    // The service method called by GET /transactions/{transactionId} endpoint
    public TransactionDTO getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId).map(transactionMapper::toDTO).orElseThrow(
                () -> new EntityNotFoundException("Transaction not found"));
    }

    // The service method called by GET /transactions/userId/{userId} endpoint
    public List<TransactionDTO> getTransactionsByUserId(UUID userId, String iban, Float minAmount, Float maxAmount, Float exactAmount,
            TransactionType typeOfTransaction, LocalDate startDate, LocalDate endDate, UUID callingUserId) {

        // Setting a default limit of transactions retrieved
        int defaultLimit = Integer.parseInt(environment.getProperty("api.transaction.default-limit", "100"));

        // Retrieving all transactions that satisfy the filters applied
        List<Transaction> transactions = transactionRepository.findAll(
                applyQueryFilter(iban,
                        minAmount,
                        maxAmount,
                        exactAmount,
                        typeOfTransaction,
                        startDate,
                        endDate,
                        callingUserId)); // Here callingUserId is specified getTransactionsByUserId() is supposed to be used by customers
                                         // And we need to set some validations/restrictions

        // Convert the list of Transaction entities to TransactionDTOs
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::toDTO).limit((defaultLimit)).toList();

        return transactionDTOs;
    }

    // The service method called by POST /transactions endpoint
    public TransactionDTO addTransaction(TransactionDTO dto, TransactionType type, UUID performingUserId) {
        // Validation for the account (e.g. if they exist) can be found in the Account Service
        Account sendingAccount = accountService.getAccountByIban(dto.getFromAccount());
        Account receivingAccount = accountService.getAccountByIban(dto.getToAccount());

        // Validating some business logic for the performing user (for the cases when they are customers).
        validatePerformingUserLogic(userRepository.findById(performingUserId).orElseThrow(
                () -> new EntityNotFoundException("Performing user not found")),
                mapAccountIbanToOwner(sendingAccount), mapAccountIbanToOwner(receivingAccount), type);
        // Validating some business logic on the sending and receiving accounts
        validateAccountLogic(sendingAccount, receivingAccount, type);

        Transaction t = new Transaction();
        t.setFromAccount(sendingAccount);
        t.setToAccount(receivingAccount);
        t.setAmount(validateAmount(dto.getAmount())); //Ensuring valid amount is entered
        t.setTypeOfTransaction(type);
        t.setDateOfExecution(LocalDateTime.now());
        // Ensuring the performing user exists
        t.setPerformingUser(userRepository.findById(performingUserId).orElseThrow(
                () -> new EntityNotFoundException("Performing user not found")));
        t.setDescription(dto.getDescription());

        // Deducting money from one account and sending them to another
        deductMoneyFromAccount(t.getFromAccount(), t.getAmount());
        sentMoneyToAccount(t.getToAccount(), t.getAmount());

        Transaction savedTransaction = transactionRepository.save(t);
        return transactionMapper.toDTO(savedTransaction);
    }

    // Method that deducts the entered amount from the sending account
    private void deductMoneyFromAccount(Account fromAccount, float amount) {
        // Checking if the transaction comply with the limits/status given to the sending account
        checkAccountRelatedLimits(fromAccount, amount);
        // Checking if the transaction comply with the limits/status given to the sending customer
        checkCustomerRelatedLimits(fromAccount, amount);

        // Set new (lower) balance to the sending account
        float balance = fromAccount.getBalance();
        balance = balance - amount;
        fromAccount.setBalance(balance);
    }

    // Method that sends the entered amount from the receiving account
    private void sentMoneyToAccount(Account toAccount, float amount) {
        // If the receiving account is deactivated, abort the transaction
        if (!toAccount.isActive()) {
            throw new IllegalArgumentException("Receiving account is deactivated.");
        }

        // Set new (higher) balance to the receiving account
        float balance = toAccount.getBalance();
        balance = balance + amount;
        toAccount.setBalance(balance);
    }

    // Checks for valid entered amount of transaction
    private float validateAmount(float amount) {
        if (Float.isNaN(amount) || amount <= 0) {
            throw new IllegalArgumentException("Amount must be a positive number.");
        }

        return amount;
    }

    // If the transaction is of type deposit, make sure that the receiving account is the BANK's bank account.
    // If the transaction is of type withdrawal, make sure that the sending account is the BANK's bank account.
    // ATM logic applied from the project guide and the q&a session
    private void validateAccountsBasedOnTransactionType(Account accountToVerify, boolean accountToVerifyIsSending,
            TransactionType transactionType) {
        switch (transactionType) {
            case DEPOSIT:
                if (!accountToVerify.equals(accountService.getAccountByIban("NL01INHO0000000001"))
                        && !accountToVerifyIsSending) {
                    throw new AccessDeniedException(
                            "During deposit, transaction cannot be sent to an account other than the BANK's dedicated one");
                }
                break;
            case WITHDRAWAL:
                if (!accountToVerify.equals(accountService.getAccountByIban("NL01INHO0000000001"))
                        && accountToVerifyIsSending) {
                    throw new AccessDeniedException(
                            "During withdrawal, transaction cannot be sent from an account other than the BANK's dedicated one");
                }
                break;
            case TRANSFER:
                break;
            default:
                throw new IllegalArgumentException("Undefined type of transaction");
        }
    }

    // If the sending/receiving accounts are of type SAVINGS, and the transaction is deposit/withdrawal, abort the transaction.
    // If the transaction is regular transfer, but involves account of type SAVINGS, check if
    // both the sending/receiving accounts are on the same user.
    private void validateAccountsBasedOnAccountType(Account sendingAccountToVerify, Account receivingAccountToVerify,
            TransactionType transactionType) {
        if (sendingAccountToVerify.getTypeOfAccount() == AccountType.SAVINGS
                || receivingAccountToVerify.getTypeOfAccount() == AccountType.SAVINGS) {
            if (transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.WITHDRAWAL) {
                throw new AccessDeniedException(
                        "Cannot perform " + transactionType.name() + " transaction, involving a savings account");
            } else if (transactionType == TransactionType.TRANSFER) {
                UUID sendingUser = sendingAccountToVerify.getUser().getId();
                UUID receivingUser = receivingAccountToVerify.getUser().getId();
                if (!sendingUser.equals(receivingUser)) {
                    throw new AccessDeniedException(
                            "When involving a savings account, both the sending and the receiving account must be on the same user.");
                }
            }
        }
    }

    // All the account related logic, combined in one method
    private void validateAccountLogic(Account sendingAccountToVerify, Account receivingAccountToVerify,
            TransactionType transactionType) {
        // For sending account
        validateAccountsBasedOnTransactionType(sendingAccountToVerify, true, transactionType);
        // For receiving account
        validateAccountsBasedOnTransactionType(receivingAccountToVerify, false, transactionType);
        // For sending and receiving - SAVINGS exception logic
        validateAccountsBasedOnAccountType(sendingAccountToVerify, receivingAccountToVerify, transactionType);
        // Preventing self account transfer
        checkSelfAccountTransaction(sendingAccountToVerify, receivingAccountToVerify);
    }

    // Validates rites of the performing user, if they are not employee.
    private void validatePerformingUserLogic(User performingUser, User ownerOfSendingAccount, User ownerOfReceivingAccount, TransactionType transactionType) {
        switch (transactionType) {
            case TRANSFER, DEPOSIT:
                // Checks for case of UserType.USER
                ThrowExceptionForUserRole(performingUser.getRole());
                // If performing user is customer and tries to send money from someone else's account, deny it.
                if (performingUser.getRole() == UserType.CUSTOMER) {
                    if (performingUser.getId() != ownerOfSendingAccount.getId()) {
                        throw new AccessDeniedException("Cannot perform transaction. Customer is not owner of the sending account.");
                    }
                }
                break;
            case WITHDRAWAL:
                // Checks for case of UserType.USER
                ThrowExceptionForUserRole(performingUser.getRole());
                // If performing user is customer and tries to send money to someone else's account, deny it.
                if (performingUser.getRole() == UserType.CUSTOMER) {
                    if (performingUser.getId() != ownerOfReceivingAccount.getId()) {
                        throw new AccessDeniedException("Cannot perform transaction. Customer is not owner of the receiving account.");
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Undefined type of transaction");
        }

    }

    // I have this check for exception throwing repeated on a lot of places, might as well make it a method.
    private void ThrowExceptionForUserRole(UserType role) {
        if (role == UserType.USER) {
            throw new AccessDeniedException("Cannot perform/get transactions. User is not customer.");
        }
    }

    // Validating right of the performing user (when not employee) if they try to access IBAN through query, that doesn't belong to them.
    private void validateGetTransactionsLogic(User callingUser, User ownerOfRequestedIban) {
        // Checks for case of UserType.USER.
        ThrowExceptionForUserRole(callingUser.getRole());
        // If performing user is customer and tries to access someone else's IBAN, deny it.
        if (callingUser.getRole() == UserType.CUSTOMER) {
            if (callingUser.getId() != ownerOfRequestedIban.getId()) {
                throw new AccessDeniedException("Cannot get transaction. Customer has no employee rights to look at other IBANs.");
            }
        }
    }

    // Check is user is trying to perform transaction from one account to the same account.
    private void checkSelfAccountTransaction(Account fromAccount, Account toAccount) {
        if (fromAccount.equals(toAccount)) {
            throw new AccessDeniedException("Cannot make transactions to self.");
        }
    }

    // Checks if transactions complies with the limits/status set to the sending account.
    private void checkAccountRelatedLimits(Account fromAccount, float amount) {
        float balance = fromAccount.getBalance();
        balance = balance - amount;
        // Deny if account is deactivated.
        if (!fromAccount.isActive()) {
            throw new IllegalArgumentException("Sending account is deactivated.");
        }
        // Deny if transaction exceeds account's absolute limit.
        if (fromAccount.getAbsoluteLimit() > balance) {
            throw new IllegalArgumentException("Cannot exceed the account's absolute limit.");
        }
        // Deny if transaction exceeds account's current balance.
        if (amount > fromAccount.getBalance()) {
            throw new IllegalArgumentException("Insufficient funds. Cannot complete transaction.");
        }
    }

    // Checks if transactions complies with the limits/status set to the owner of the sending account.
    private void checkCustomerRelatedLimits(Account fromAccount, float amount) {
        User ownerOfSendingAccount = mapAccountIbanToOwner(fromAccount);

        // Deny if transaction exceeds customer's transaction limit.
        if (amount > ownerOfSendingAccount.getTransactionLimit()) {
            throw new IllegalArgumentException("Transaction exceed the transaction limit.");
        }

        // Deny if transaction exceeds customer's daily limit.
        checkCustomerDailyLimit(ownerOfSendingAccount.getDayLimit(), fromAccount.getIban(), amount);
    }

    // Checks how much is left from the customer's daily limit, and if a transaction exceed what is left from that limit, deny it.
    private void checkCustomerDailyLimit(float dayLimit, String ownerIban, float amountToSend) {
        float ownerDayLimit = dayLimit;

        List<TransactionDTO> todaysTransactionsFromSendingUser = this.getTodaysTransactionsFromSendingUser(ownerIban,
                LocalDate.now());
        float sentMoneyToday = 0;
        for (TransactionDTO t : todaysTransactionsFromSendingUser) {
            sentMoneyToday = sentMoneyToday + t.getAmount();
        }

        if (sentMoneyToday + amountToSend > ownerDayLimit) {
            throw new IllegalArgumentException("Day limit has been reached.");
        }

    }

    // Method that queries all transactions of a user, made today.
    private List<TransactionDTO> getTodaysTransactionsFromSendingUser(String iban, LocalDate today) {
        Specification<Transaction> specification = Specification.where(null);

        if (iban != null && !iban.isEmpty() && today != null) {
            specification = specification.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fromAccount").get("iban"), iban));

            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .equal(root.get("dateOfExecution").as(Date.class), Date.valueOf(today)));

            List<Transaction> transactions = transactionRepository.findAll(specification);
            return transactions.stream().map(transactionMapper::toDTO).toList();
        }

        throw new RuntimeException("Internal error. Could not retrieve IBAN and/or Today's date.");
    }

    // Method that maps account IBAN to a user.
    private User mapAccountIbanToOwner(Account account) {
        Optional<User> ownerOfSendingAccount = userRepository.findById(account.getUser().getId());

        return ownerOfSendingAccount.get();
    }

}