package com.joseluisestevez.msa.oauth.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.joseluisestevez.msa.commons.users.dto.UserDto;

public interface UserService extends UserDetailsService {

	UserDto findByUsername(String username);

	UserDto update(UserDto userDto, Long id);
}
