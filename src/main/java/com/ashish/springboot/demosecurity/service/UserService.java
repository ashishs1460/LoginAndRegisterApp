package com.ashish.springboot.demosecurity.service;

import com.ashish.springboot.demosecurity.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

	User findByUserName(String userName);

	void save(User user);

	List<User> findAll();

	void deleteByUserName(String userName);

	void saveUp(User user);

}
