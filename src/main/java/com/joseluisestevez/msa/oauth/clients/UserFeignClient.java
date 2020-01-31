package com.joseluisestevez.msa.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.joseluisestevez.msa.commons.users.dto.UserDto;

@FeignClient(name = "service-users")
public interface UserFeignClient {

	@GetMapping("/users/search/find-username")
	UserDto findByUsername(@RequestParam("username") String username);

	@PutMapping("/users/{id}")
	UserDto update(@RequestBody UserDto userDto, @PathVariable("id") Long id);
}
