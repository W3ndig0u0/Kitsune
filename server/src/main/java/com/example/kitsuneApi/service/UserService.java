package com.example.kitsuneApi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Viktigt för update/delete

import com.example.kitsuneApi.model.User;
import com.example.kitsuneApi.model.UserDto;
import com.example.kitsuneApi.repository.UserRepository;

@Service
@Transactional // Garanterar databas-integritet
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto registerUser(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username '" + dto.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        userRepository.save(user);
        return dto;
    }

    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        return new UserDto(user.getUsername(), user.getEmail(), user.getPassword());
    }

    public UserDto updateUser(Long id, UserDto updatedDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));

        user.setUsername(updatedDto.getUsername());
        user.setEmail(updatedDto.getEmail());
        user.setPassword(updatedDto.getPassword());

        userRepository.save(user);
        return updatedDto;
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getUsername(), user.getEmail(), user.getPassword()))
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID " + id + " not found"));
        return new UserDto(user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Transactional(readOnly = true)
    public boolean authenticateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Cannot delete: User not found");
        }
        userRepository.deleteById(id);
    }
}
