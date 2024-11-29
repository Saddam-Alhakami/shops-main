package com.shops.dreamshops.service.user;

import com.shops.dreamshops.dto.UserDto;
import com.shops.dreamshops.model.User;
import com.shops.dreamshops.request.user.CreateUserRequest;
import com.shops.dreamshops.request.user.UserUpdateRequest;

public interface IUserService {
    UserDto getUserById(Long userId);
    UserDto createUser(CreateUserRequest request);
    UserDto updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long id);

    User getAuthenticatedUser();
}
