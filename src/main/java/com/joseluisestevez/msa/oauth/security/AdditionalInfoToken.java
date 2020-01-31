package com.joseluisestevez.msa.oauth.security;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.joseluisestevez.msa.commons.users.dto.UserDto;
import com.joseluisestevez.msa.oauth.services.UserService;

@Component
public class AdditionalInfoToken implements TokenEnhancer {

	@Autowired
	private UserService userService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
			OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<>();

		UserDto user = userService.findByUsername(authentication.getName());

		info.put("firstname", user.getFirstname());
		info.put("lastname", user.getLastname());
		info.put("email", user.getEmail());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		return accessToken;
	}

}