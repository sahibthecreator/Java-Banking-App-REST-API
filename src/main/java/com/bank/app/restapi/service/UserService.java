package com.bank.app.restapi.service;

import com.bank.app.restapi.dto.LoginDTO;
import com.bank.app.restapi.dto.UserDTO;
import com.bank.app.restapi.dto.mapper.UserMapper;
import com.bank.app.restapi.model.User;
import com.bank.app.restapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll(
            String firstName,
            String lastName,
            String email,
            LocalDate dateOfBirth,
            String bsn,
            String sortDirection,
            int limit) {
        Specification<User> specification = buildSpecification(firstName, lastName, email, dateOfBirth, bsn);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "lastName", "firstName");

        Page<User> userPage = userRepository.findAll(specification, PageRequest.of(0, limit, sort));
        List<User> userList = userPage.getContent();

        return userList.stream().map(userMapper::toDTO).toList();
    }

    public UserDTO register(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID());
        user = this.userRepository.saveAndFlush(user);
        return userMapper.toDTO(user);
    }

    public String login(LoginDTO loginDTO) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        return jwtService.generateToken(loginDTO.getEmail());

    }

    public UserDTO update(UUID userId, UserDTO userDTO) throws EntityNotFoundException {
        User user = userMapper.toEntity(userDTO);
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            BeanUtils.copyProperties(user, existingUser, "id"); // Exclude copying the "id" property
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(existingUser);
            return userMapper.toDTO(savedUser);
        } else {
            throw new EntityNotFoundException("No user with following id " + userId + " exists");
        }
    }

    public boolean delete(UUID id) throws EntityNotFoundException {
        if (!userRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("No user with following id " + id + " exists");
        }
        this.userRepository.deleteById(id);
        return true;
    }

    public boolean userIdExists(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    public UserDTO getUserDTOById(UUID id) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("No user with following id " + id + " exists");
        }
        return userMapper.toDTO(user.get());
    }

    // Private methods
    private Specification<User> buildSpecification(
            String firstName,
            String lastName,
            String email,
            LocalDate dateOfBirth,
            String bsn) {
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

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
