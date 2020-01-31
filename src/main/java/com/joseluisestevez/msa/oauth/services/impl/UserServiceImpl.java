package com.joseluisestevez.msa.oauth.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joseluisestevez.msa.commons.users.dto.UserDto;
import com.joseluisestevez.msa.oauth.clients.UserFeignClient;
import com.joseluisestevez.msa.oauth.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	private UserFeignClient userFeignClient;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		UserDto userDto = userFeignClient.findByUsername(username);
		if (userDto == null) {
			LOGGER.error("User [{}] does not exist ", username);
			throw new UsernameNotFoundException(
					"User " + username + " does not exist");
		}
		List<GrantedAuthority> authorities = userDto.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> LOGGER.info("Role: [{}]  ",
						authority.getAuthority()))
				.collect(Collectors.toList());
		return new User(userDto.getUsername(), userDto.getPassword(),
				userDto.getEnabled(), true, true, true, authorities);
	}

	@Override
	public UserDto findByUsername(String username) {
		return userFeignClient.findByUsername(username);
	}
}
