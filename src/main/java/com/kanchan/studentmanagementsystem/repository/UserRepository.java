package com.kanchan.studentmanagementsystem.repository;
//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanchan.studentmanagementsystem.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    User findByEmail(String email);
    //List<Student> findAll(Sort sort);

}