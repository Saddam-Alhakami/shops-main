package com.shops.dreamshops.service.user;

import com.shops.dreamshops.dto.UserDto;
import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.model.User;
import com.shops.dreamshops.repository.UserRepository;
import com.shops.dreamshops.request.user.CreateUserRequest;
import com.shops.dreamshops.request.user.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId).map(this::convertToDto).orElseThrow(()->new ResourceNotFoundException("NOT FOUND"));
    }

    @Override
    public UserDto createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user=new User();
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    return userRepository.save(user);
                }).map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException(request.getEmail()+"already exists"));

    }

    @Override
    public UserDto updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName((request.getLastName()));
            return userRepository.save(existingUser);
        }).map(this::convertToDto).orElseThrow(()->new ResourceNotFoundException("NOT FOUND"));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,() ->{
                    throw new ResourceNotFoundException("NOT FOUND");
                });
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        return userRepository.findByEmail(email);

    }

    private UserDto convertToDto(User user){
       return modelMapper.map(user,UserDto.class);

    }
}
