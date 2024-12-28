package com.uexcel.authserver.config;

import com.uexcel.authserver.model.Authority;
import com.uexcel.authserver.model.Customer;
import com.uexcel.authserver.persistence.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor

public class AuthServerUserDetailService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

     Set<GrantedAuthority> authorities = customer.getAuthority()
             .stream().map(Authority::getName).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new User(customer.getEmail(), customer.getPwd(),authorities);
    }
}
