package com.at.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.at.library.dto.UserDTO;
import com.at.library.service.user.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(method={RequestMethod.GET})
	public List<UserDTO> getAll() {
		log.debug("Buscando todos los usuarios en el sistema");
		return userService.findAll();
	}
	
	@RequestMapping(method={RequestMethod.POST})
	public UserDTO create(@RequestBody UserDTO user){
		log.debug(String.format("Vamos a crear el usuario %s", user));
		return userService.create(user);
	}
	
	@RequestMapping(value="/{id}", method={RequestMethod.GET})
	public UserDTO get(@PathVariable("id") Integer id){
		log.debug(String.format("Recuperando usuario con id: %s",id));
		return userService.getById(id);
	}

	@RequestMapping(value="/{id}", method={RequestMethod.PUT})
	public void update(@PathVariable("id") Integer id, @RequestBody UserDTO user){
		log.debug(String.format("Vamos a modificar el usuario %s", user));
		userService.update(user);
	}
	
	@RequestMapping(value="/{id}", method={RequestMethod.DELETE})
	public void delete(@PathVariable("id") Integer id){
		log.debug(String.format("Vamos a modificar el usuario con id %s", id));
		userService.delete(id);
	}

}
