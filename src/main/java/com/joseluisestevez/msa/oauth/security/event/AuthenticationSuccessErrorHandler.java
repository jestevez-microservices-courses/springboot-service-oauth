package com.joseluisestevez.msa.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.joseluisestevez.msa.commons.users.dto.UserDto;
import com.joseluisestevez.msa.oauth.services.UserService;

@Component
public class AuthenticationSuccessErrorHandler
		implements
			AuthenticationEventPublisher {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuthenticationSuccessErrorHandler.class);

	@Autowired
	private UserService userService;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();
		LOGGER.info("Success login [{}]", user.getUsername());
		UserDto userDto = userService.findByUsername(authentication.getName());
		if (userDto.getAttempts() != null && userDto.getAttempts() > 0) {
			userDto.setAttempts(0);
			userService.update(userDto, userDto.getId());
		}
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception,
			Authentication authentication) {
		StringBuilder errors = new StringBuilder();
		String message = "Login Error " + exception.getMessage();
		errors.append("- " + message);
		LOGGER.error(message);

		try {
			UserDto userDto = userService
					.findByUsername(authentication.getName());

			if (userDto.getAttempts() == null) {
				userDto.setAttempts(0);
			}

			LOGGER.info("Attempts before [{}] ", userDto.getAttempts());
			userDto.setAttempts(userDto.getAttempts() + 1);
			LOGGER.info("Attempts after [{}] ", userDto.getAttempts());

			errors.append("- Attempts after " + userDto.getAttempts());

			if (userDto.getAttempts() >= 3) {
				errors.append("- User " + userDto.getUsername() + " disabled ");

				userDto.setEnabled(false);
				LOGGER.info("User [{}] disabled ", userDto.getUsername());
			}

			userService.update(userDto, userDto.getId());

		} catch (Exception e) {
			LOGGER.error("User [{}] does not exist in the system",
					authentication.getName());
		}
	}

}
