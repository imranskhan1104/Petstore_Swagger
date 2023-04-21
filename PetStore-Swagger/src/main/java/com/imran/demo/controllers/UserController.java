package com.imran.demo.controllers;

import com.imran.demo.entities.User;
import com.imran.demo.repositories.UserRepo;
import com.imran.demo.response.ApiResponse;
import com.imran.demo.payloads.UserDto;
import com.imran.demo.services.UserService;
import com.imran.demo.utils.Variable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Api(value ="USERS",description = "Operations about users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Variable variable;

    @GetMapping("/{userName}")
    @ApiOperation(value = "Get user by user name")
    public ResponseEntity<?> getUserByUserName(@PathVariable("userName") String userName) {
        if (variable.isToken()) {
            Optional<User> user= Optional.ofNullable(this.userRepo.findByUserName(userName));
            if (user.isPresent()) {
                return ResponseEntity.ok(this.userService.getUserByUserName(userName));
            }
            else {
                return new ResponseEntity<ApiResponse>(new ApiResponse("User not present", false), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userName}")
    @ApiOperation(value = "Update user",notes = "This can only be done by the logged in user.")
    public ResponseEntity<?> updateUser(@RequestParam("userName") String userName,
                                        @ApiParam(name = "User Body", value = "Updated user object")
                                        @RequestBody UserDto userDto) {
        if (variable.isToken()) {
            Optional<User> user= Optional.ofNullable(this.userRepo.findByUserName(userName));
            if (user.isPresent()) {
                UserDto updateuser = this.userService.updateUser(userDto, userName);
                return ResponseEntity.ok(updateuser);
            }
            else {
                return new ResponseEntity<ApiResponse>(new ApiResponse("User not present", false), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{userName}")
    @ApiOperation(value = "Delete user",notes ="This can only be done by the logged in user." )
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String userName) {
        if (variable.isToken()) {
            Optional<User> user= Optional.ofNullable(this.userRepo.findByUserName(userName));
            if (user.isPresent())
            {
            this.userService.deleteUser(userName);
            return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<ApiResponse>(new ApiResponse("User not present", false), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/")
    @ApiOperation(value = "Create user")
    public ResponseEntity<?> createUser(@ApiParam(name = "User Body",value = "Created user object")
                                        @RequestBody UserDto userDto) {
        boolean userExists = false;
        UserDto createUserD;
        for (User a : this.userRepo.findAll()) {
            if (userDto.getUserName().equals(a.getUserName())) {
                userExists = true;
                break;
            }
        }
        if (userExists) {
            return new ResponseEntity<ApiResponse>(new ApiResponse("User with the same username is available. Try another username", false), HttpStatus.BAD_REQUEST);
        } else {
            createUserD = this.userService.createUser(userDto);
            return new ResponseEntity<>(createUserD, HttpStatus.CREATED);
        }
    }

    @PostMapping("/create-with-list")
    @ApiOperation(value = "Create list of users with given input array")
    public ResponseEntity<ApiResponse> createUserByList(@ApiParam(name = "User Body",value = "List of user object")
                                                        @RequestBody List<UserDto> listUser) {
        if (variable.isToken()) {
            boolean created = this.userService.createUserByList(listUser);
            if (created) {
                return new ResponseEntity<ApiResponse>(new ApiResponse("Users created Successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<ApiResponse>(new ApiResponse("Input data has some User's already present", true), HttpStatus.BAD_REQUEST);

            }
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create-with-array")
    @ApiOperation(value = "Create list of users with given input array")
    public ResponseEntity<ApiResponse> createUserByArray(@ApiParam(name = "User Body",value = "List of user object")
                                                         @RequestBody UserDto[] userDto) {
        if (variable.isToken()) {
            boolean created = this.userService.createUserByArray(userDto);
            if (created) {
                return new ResponseEntity<ApiResponse>(new ApiResponse("Users created Successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<ApiResponse>(new ApiResponse("Input data has some User's already present", false), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in. Need to Login", false), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login/")
    @ApiOperation(value = "Logs user into the system")
    public ResponseEntity<ApiResponse> login(@ApiParam(name = "userName",value = "The user name for login")
                                             @RequestParam(required = true)  String userName,
                                             @ApiParam(name = "password", value = "The password for login in clear text")
                                             @RequestParam(required = true) String password) {
        User user = this.userRepo.findByUserName(userName);
        if (user == null || user.getUserName() == null || !user.getUserName().equals(userName)) {
            return new ResponseEntity<ApiResponse>(new ApiResponse("No user found with the provided username", false), HttpStatus.BAD_REQUEST);
        } else {
            if ((user.getPassword()).equals(password)) {
                variable.setToken(true);
                return new ResponseEntity<ApiResponse>(new ApiResponse("Logged In Successfully", true), HttpStatus.OK);
            } else {
                return new ResponseEntity<ApiResponse>(new ApiResponse("Wrong Password", false), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/logout")
    @ApiOperation(value = "Logs out current logged in user session")
    public ResponseEntity<ApiResponse> logout() {
        if (variable.isToken() == true) {
            variable.setToken(false);
            return new ResponseEntity<ApiResponse>(new ApiResponse("Logout Successfully", true), HttpStatus.OK);
        } else {
            return new ResponseEntity<ApiResponse>(new ApiResponse("You are not logged in", false), HttpStatus.BAD_REQUEST);
        }
    }
}
