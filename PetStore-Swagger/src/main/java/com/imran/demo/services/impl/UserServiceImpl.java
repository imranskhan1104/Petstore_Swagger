package com.imran.demo.services.impl;

import com.imran.demo.entities.User;
import com.imran.demo.payloads.UserDto;
import com.imran.demo.repositories.UserRepo;
import com.imran.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userName) {
        User user = this.userRepo.findByUserName(userName);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setUserStatus(userDto.getUserStatus());

        User updateUser = this.userRepo.save(user);
        UserDto userDto1 = this.userToDto(updateUser);

        return userDto1;
    }

    @Override
    public UserDto getUserByUserName(String userName) {
        User user = this.userRepo.findByUserName(userName);
        return this.userToDto(user);
    }

    @Override
    public boolean createUserByArray(UserDto[] userDto) {
        boolean flag = false;
        try {
            for (UserDto a : userDto) {
                for (User b : this.userRepo.findAll()) {
                    if (a.getUserName().equals(b.getUserName())) {
                        flag = false;
                        return flag;
                    }
                }
            }
            for (UserDto userList : userDto) {
                User user = this.dtoToUser(userList);
                this.userRepo.save(user);
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean createUserByList(List<UserDto> listOfUser) {
        boolean flag = false;
        try {
            for (UserDto a : listOfUser) {
                for (User b : this.userRepo.findAll()) {
                    if (a.getUserName().equals(b.getUserName())) {
                        flag = false;
                        return flag;
                    }
                }
            }
            for (UserDto userList : listOfUser) {
                User user = this.dtoToUser(userList);
                this.userRepo.save(user);
                flag = true;
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public void deleteUser(String userName) {
        this.userRepo.deleteByUsername(userName);
    }

    public User dtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUserName(userDto.getUserName());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setUserStatus(userDto.getUserStatus());
        return user;
    }

    public UserDto userToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPhone(user.getPhone());
        userDto.setUserStatus(user.getUserStatus());
        return userDto;
    }
}
