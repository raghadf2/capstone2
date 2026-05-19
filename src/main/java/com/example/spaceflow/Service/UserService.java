package com.example.spaceflow.Service;

import com.example.spaceflow.Api.ApiException;
import com.example.spaceflow.Model.User;
import com.example.spaceflow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    //Helper Method
    public User checkUserRole(Integer userId, String role){
        User user = userRepository.findUserById(userId);

        if(user == null){
            throw new ApiException("User not found");
        }

        if(!user.getRole().equals(role)){
            throw new ApiException("You are not authorized to perform this action");
        }

        return user;
    }


    //CRUD
    public void addUser(User user){
        if(userRepository.findUserByEmail(user.getEmail()) != null){
            throw new ApiException("Email Already Exists");
        }

        if(userRepository.findUserByUserName(user.getUserName()) != null){
            throw new ApiException("Username Already Exists");
        }

        if(userRepository.findUserByPhone(user.getPhone()) != null) {
            throw new ApiException("Phone Number Already Exists");
        }

        userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void updateUser(Integer id,User user){
        User oldUser = userRepository.findUserById(id);

        if(oldUser == null){
            throw new ApiException("User Not Exists");
        }

        User emailUser = userRepository.findUserByEmail(user.getEmail());

        if(emailUser != null && !emailUser.getId().equals(id)){
            throw new ApiException("Email Already Exists");
        }

        User usernameUser = userRepository.findUserByUserName(user.getUserName());

        if(usernameUser != null && !usernameUser.getId().equals(id)){
            throw new ApiException("Username Already Exists");
        }

        User phoneUser = userRepository.findUserByPhone(user.getPhone());

        if(phoneUser != null && !phoneUser.getId().equals(id)){
            throw new ApiException("Phone Number Already Exists");
        }

        oldUser.setName(user.getName());
        oldUser.setUserName(user.getUserName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setPhone(user.getPhone());
        oldUser.setRole(user.getRole());
        userRepository.save(oldUser);
    }

    public void deleteUser(Integer id){
        User user = userRepository.findUserById(id);

        if(user == null){
            throw new ApiException("User Not Found");
        }
        userRepository.delete(user);
    }

    //----------------

    //Login
    public User logIn(String email, String password){
        User user = userRepository.findUserByEmail(email);

        if(user == null){
            throw new ApiException("Invalid email or password");
        }

        if(!user.getPassword().equals(password)){
            throw new ApiException("Invalid email or password");
        }

        return user;
    }
}
