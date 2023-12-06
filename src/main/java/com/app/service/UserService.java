package com.app.service;

import com.app.exception.UserException;
import com.app.modal.Ride;
import com.app.modal.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

//    public User createUser(User user) throws UserException;

    public User getReqUserProfile(String token) throws UserException;

    public User findUserById(Integer Id) throws UserException;

//    public User findUserByEmail(String email) throws UserException;

//    public User findUserByToken(String token) throws UserException;

    public List<Ride> completedRids(Integer userId) throws UserException;
}
