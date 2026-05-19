package com.example.spaceflow.Repository;

import com.example.spaceflow.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserById(Integer id);
    User findUserByEmail(String email);
    User findUserByUserName(String userName);
    User findUserByPhone(String phone);

}
