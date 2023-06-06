package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.LoginResponseDTO;
import com.bank.app.restapi.dto.RegisterDTO;
import com.bank.app.restapi.dto.TransactionDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.Account;
import com.bank.app.restapi.model.Transaction;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.model.UserType;
import com.bank.app.restapi.repository.AccountRepository;
import com.bank.app.restapi.repository.TransactionRepository;
import com.bank.app.restapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;

import org.modelmapper.internal.bytebuddy.dynamic.TypeResolutionStrategy.Active;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll(
            String firstName,
            String lastName,
            String email,
            LocalDate dateOfBirth,
            String bsn,
            String role,
            String sortDirection,
            int limit) {
        Specification<User> specification = buildSpecification(firstName, lastName, email, dateOfBirth, bsn, role);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "lastName", "firstName");

        Page<User> userPage = userRepository.findAll(specification, PageRequest.of(0, limit, sort));
        List<User> userList = userPage.getContent();

        return userList.stream().map(userMapper::toDTO).toList();
    }

    public UserDTO register(RegisterDTO registerDTO) {
        emailIsRegistered(registerDTO.getEmail());
        bsnIsValid(registerDTO.getBsn());
        User user = userMapper.registerDTOToUser(registerDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID());
        user.setActive(true);
        user = this.userRepository.saveAndFlush(user);
        return userMapper.toDTO(user);
    }

    public LoginResponseDTO login(LoginDTO loginDTO) {
        userIsActive(loginDTO.getEmail());
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        UUID userId = this.userRepository.findIdByEmail(loginDTO.getEmail()).get();
        String jwtToken = jwtService.generateToken(loginDTO.getEmail());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(userId, jwtToken);

        return loginResponseDTO;

    }

    public UserDTO update(UUID userId, UserDTO userDTO) throws EntityNotFoundException {
        User user = userMapper.toEntity(userDTO);
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            BeanUtils.copyProperties(user, existingUser, "id", "password", "active"); // Exclude copying the id ,active, pass property
            System.out.println(existingUser.isActive());
            
            User savedUser = userRepository.save(existingUser);
            return userMapper.toDTO(savedUser);
        } else {
            throw new EntityNotFoundException("No user with following id " + userId + " exists");
        }
    }

    public String updateUserEmail(UUID userId, String email) throws EntityNotFoundException {
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setEmail(email);
            userRepository.save(existingUser);
            return "Email has been updated";
        } else {
            throw new EntityNotFoundException("No user with following id " + userId + " exists");
        }
    }

    public String delete(UUID id) throws EntityNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("No user with following id " + id + " exists");
        }
        List<Account> accounts = accountRepository.findAccountsByUserId(id);
        if (accounts.isEmpty()) {
            this.userRepository.deleteById(id);
            return "User " + id + " has been permanently deleted";
        } else {
            User deactivatedUser = userOptional.get();
            deactivatedUser.setActive(false);
            this.userRepository.saveAndFlush(deactivatedUser);
            return "User " + id + " has been deactivated";
        }
    }

    public UserDTO getUserDTOById(UUID id) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("No user with following id " + id + " exists");
        }
        return userMapper.toDTO(user.get());
    }

    public User getUserById(UUID id) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("No user with following id " + id + " exists");
        }
        return user.get();
    }

    public double getRemainingDayLimit(UUID id) throws EntityNotFoundException {
        User user = getUserById(id); // to check if userid is valid
        List<Transaction> transactions = transactionRepository.findTransactionsByUserId(id);

        LocalDate currentDate = LocalDate.now();
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(t -> {
                    LocalDate executionDate = t.getDateOfExecution().toLocalDate();
                    return executionDate.equals(currentDate);
                }).toList();
        double totalSpentToday = filteredTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        double remainingLimit = user.getDayLimit() - totalSpentToday;
        double roundedRemainingLimit = Math.round(remainingLimit * 100.0) / 100.0;

        return roundedRemainingLimit;
    }

    // Private methods

    private Specification<User> buildSpecification(
            String firstName,
            String lastName,
            String email,
            LocalDate dateOfBirth,
            String bsn,
            String role) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null && !firstName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"));
            }

            if (lastName != null && !lastName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }

            if (dateOfBirth != null) {
                predicates.add(criteriaBuilder.equal(root.get("dateOfBirth"), dateOfBirth));
            }

            if (bsn != null && !bsn.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("bsn"), bsn));
            }

            if (bsn != null && !bsn.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("bsn"), bsn));
            }

            if (role != null) {
                UserType userType = UserType.valueOf(role.toUpperCase());
                predicates.add(criteriaBuilder.equal(root.get("role"), userType));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void emailIsRegistered(String email) throws IllegalArgumentException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
            throw new IllegalArgumentException("Email is already registered");
    }

    private void userIsActive(String email) throws IllegalArgumentException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (!user.get().isActive())
                throw new IllegalArgumentException("Your account is deactivated");
        } else {
            throw new IllegalArgumentException("Email is not registered");
        }

    }

    private void bsnIsValid(String bsn) throws IllegalArgumentException {
        if (bsn == null || bsn.length() != 9 || !bsn.matches("[0-9]+")) {
            throw new IllegalArgumentException("BSN should contain only numbers");
        }

        int[] factors = { 9, 8, 7, 6, 5, 4, 3, 2, -1 };
        int checksum = 0;

        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(bsn.charAt(i));
            checksum += digit * factors[i];
        }

        if (checksum % 11 == 0)
            throw new IllegalArgumentException("BSN is not valid");
    }
}
