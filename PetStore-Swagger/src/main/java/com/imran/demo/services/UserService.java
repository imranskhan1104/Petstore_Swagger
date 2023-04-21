package com.imran.demo.services;

import com.imran.demo.payloads.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto, String userId);
    UserDto getUserByUserName(String userName);
    boolean createUserByArray(UserDto[] userDto);
    boolean createUserByList(List<UserDto> listOfUser);
    void deleteUser(String userName);
}
