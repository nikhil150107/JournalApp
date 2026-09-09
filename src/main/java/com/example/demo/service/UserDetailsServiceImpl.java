package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUserName(username);
        if(user!=null){
//            String storedPassword = user.getPassword();
//            System.out.println("[DEBUG loadUserByUsername] username = [" + username + "], storedPassword length = " + (storedPassword != null ? storedPassword.length() : "null") + ", prefix = " + (storedPassword != null && storedPassword.length() >= 7 ? storedPassword.substring(0, 7) : storedPassword));
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();

            return  userDetails;
        }
        throw new UsernameNotFoundException("User not found with username: " + username);

    }
}
