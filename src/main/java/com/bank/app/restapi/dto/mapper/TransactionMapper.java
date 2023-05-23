package com.bank.app.restapi.dto.mapper;

import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.model.Transaction;
import com.bank.app.restapi.repository.AccountRepository;
import com.bank.app.restapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionMapper {

    private ModelMapper mapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public TransactionMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setFromAccount(transaction.getFromAccount().getIban());
        transactionDTO.setToAccount(transaction.getToAccount().getIban());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setDateOfExecution(transaction.getDateOfExecution());
        transactionDTO.setTypeOfTransaction(transaction.getTypeOfTransaction());
        transactionDTO.setPerformingUser(transaction.getPerformingUser().getId());
        transactionDTO.setDescription(transaction.getDescription());

        return transactionDTO;
    }

    public Transaction toEntity(TransactionDTO transactionDTO) throws IllegalArgumentException {
        this.isValidDTO(transactionDTO);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(accountRepository.findByIban(transactionDTO.getFromAccount()));
        transaction.setToAccount(accountRepository.findByIban(transactionDTO.getToAccount()));
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDateOfExecution(transactionDTO.getDateOfExecution());
        transaction.setTypeOfTransaction(transactionDTO.getTypeOfTransaction());
        transaction.setPerformingUser(userRepository.findById(transactionDTO.getPerformingUser()).orElseThrow());
        transaction.setDescription(transactionDTO.getDescription());

        return transaction;
    }

    public void isValidDTO(TransactionDTO transactionDTO) throws IllegalArgumentException {

        boolean fromAccountIsValid = transactionDTO.getFromAccount() != null
                && !transactionDTO.getFromAccount().isEmpty();
        boolean toAccountIsValid = transactionDTO.getToAccount() != null && !transactionDTO.getToAccount().isEmpty();
        boolean performingUserIsValid = transactionDTO.getPerformingUser() != null;
        boolean amountIsValid = transactionDTO.getAmount() > 0;
        // boolean typeOfTransactionIsValid = transactionDTO.getTypeOfTransaction() !=
        // null;
        boolean descriptionIsValid = transactionDTO.getDescription() != null;

        if (!(fromAccountIsValid && toAccountIsValid && performingUserIsValid && amountIsValid && descriptionIsValid)) {
            throw new IllegalArgumentException("Not all required fields were provided for creating Transaction");
        }
    }
}
