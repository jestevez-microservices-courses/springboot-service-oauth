package com.joseluisestevez.msa.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joseluisestevez.msa.commons.users.dto.UserDto;

@FeignClient(name = "service-users")
public interface UserFeignClient {

	@GetMapping("/users/search/find-username")
	UserDto findByUsername(@RequestParam("username") String username);
}
