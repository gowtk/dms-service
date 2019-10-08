package com.dms.document.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dms.document.services.UserService;
import com.dms.document.views.UserDetailsView;
import com.dms.web.beans.Response;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/users/{user}")
public class UserController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Upload array of files")
	@GetMapping("/details")
	public Response<UserDetailsView> viewUserDetails(@PathVariable String user) {
		return userService.viewUserDetails(user);
	}

	@ApiOperation(value = "Upload array of files")
	@PostMapping("/uploads")
	public Response<UserDetailsView> uploadFiles(@RequestParam("file") MultipartFile file, @PathVariable String user) {
		return userService.uploadFiles(file, user);
	}

}