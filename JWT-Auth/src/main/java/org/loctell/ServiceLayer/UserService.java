package org.loctell.ServiceLayer;

import org.loctell.Entity.User;
import org.loctell.RepositoryLayer.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password){
        return userRepository.findbyUsernameandPassword(email, password);
    }

    public User insert(User u){
        return userRepository.save(u);
    }
}
