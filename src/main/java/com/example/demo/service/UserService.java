package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
//@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    public  void saveEntry(User user){
//        userRepository.save(user);
//    }
//private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public  boolean saveEntry(User user, boolean isForJournal){
//        String rawPassword = user.getPassword();
//        System.out.println("[DEBUG saveEntry] raw password = [" + rawPassword + "], length = " + (rawPassword != null ? rawPassword.length() : "null"));
//        System.out.println("[DEBUG saveEntry] looksLikeBcrypt = " + (rawPassword != null && rawPassword.startsWith("$2a$")));

        try{
            if(!isForJournal) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        }
        catch (Exception e){
//            logger.info("hahahahahaha");
//            log.debug("hhh");
//            log.info("hhh");
//            log.error("hhh");
//            log.trace("hhh");
//            log.warn("hhh");


            return false;
        }

    }

    public  void saveAdmin(User user, boolean isForJournal){
//        String rawPassword = user.getPassword();
//        System.out.println("[DEBUG saveEntry] raw password = [" + rawPassword + "], length = " + (rawPassword != null ? rawPassword.length() : "null"));
//        System.out.println("[DEBUG saveEntry] looksLikeBcrypt = " + (rawPassword != null && rawPassword.startsWith("$2a$")));
        if(!isForJournal) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    public List<User> getAll(){

        return userRepository.findAll();
    }

    public Optional<User> findbyId(ObjectId id){

        return userRepository.findById(id);
    }

    public void deletebyid(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
}
