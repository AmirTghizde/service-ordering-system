package com.Maktab101.SpringProject.security;

import com.Maktab101.SpringProject.model.User;
import com.Maktab101.SpringProject.repository.base.BaseUserRepository;
import com.Maktab101.SpringProject.service.base.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final BaseUserRepository<User> userRepository;

    @Autowired
    public UserDetailsServiceImpl(BaseUserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByEmail(username);

        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            return org.springframework.security.core.userdetails.User.builder()
                    .username(username)
                    .password(user.getPassword())
                    .build();
        }

        throw new UsernameNotFoundException("Bad Credentials");
    }

}
