package com.app.service;

import com.app.config.JwtUtil;
import com.app.exception.UserException;
import com.app.modal.Ride;
import com.app.modal.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public User getReqUserProfile(String token) throws UserException {
        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email);

        if(user!=null) {
            return user;
        }

        throw new UserException("invalid token...");
    }

    @Override
    public User findUserById(Integer Id) throws UserException {
        Optional<User> opt=userRepository.findById(Id);

        if(opt.isPresent()) {
            return opt.get();
        }
        throw new UserException("user not found with id "+Id);
    }

//    @Override
//    public User findUserByEmail(String email) throws UserException {
//        return null;
//    }

    @Override
    public List<Ride> completedRids(Integer userId) throws UserException {
        List <Ride> completedRides=userRepository.getCompletedRides(userId);
        return completedRides;
    }
}
