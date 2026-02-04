package com.akshay.websockettask.service;

import com.akshay.websockettask.entity.User;
import com.akshay.websockettask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//this user service uses postgres database storing
public class PostgresUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }

}
