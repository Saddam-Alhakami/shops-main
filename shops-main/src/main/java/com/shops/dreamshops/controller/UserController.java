package com.shops.dreamshops.controller;

import com.shops.dreamshops.dto.UserDto;
import com.shops.dreamshops.exception.ResourceNotFoundException;
import com.shops.dreamshops.request.user.CreateUserRequest;
import com.shops.dreamshops.request.user.UserUpdateRequest;
import com.shops.dreamshops.response.ApiResponse;
import com.shops.dreamshops.service.user.IUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
@RestController
public class UserController {

    private final IUserService userService;

    @GetMapping("user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            UserDto user=userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("USER IS ",user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NOT FOUND",null));
        }


    };

    @PostMapping("create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
       try {
           UserDto userDto = userService.createUser(request);
           return ResponseEntity.ok(new ApiResponse("user added ", userDto));
       }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }




    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request,@PathVariable Long userId){
       try {
           UserDto user = userService.updateUser(request, userId);
           return ResponseEntity.ok(new ApiResponse("user updated ", user));
       }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }


    };
    public ResponseEntity<ApiResponse> deleteUser(Long id){
       try{ userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse("user deleted ", null));
    }catch (ResourceNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("NOT FOUND",null));
    }
    };
}
