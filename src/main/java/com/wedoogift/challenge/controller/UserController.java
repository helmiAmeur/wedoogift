package com.wedoogift.challenge.controller;


import com.wedoogift.challenge.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * REST controller for managing {@link com.wedoogift.challenge.entity.User}.
 */
@RestController
@RequestMapping("/api")
public class UserController {


	private final UserService userservice;

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userservice) {
		this.userservice = userservice;
	}

	/**
	 * {@code GET  /user/getBalance/{id}} : get the balance of user.
	 *
	 * @param id the id of the user to retrieve.
	 * @return the balance with status {@code 200 (OK)}, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/users/getBalance/{id}")
	public Double getBalance(@PathVariable Long id) {
		try {
			log.debug("REST request to get balance of user: {}", id);
			return userservice.getUserBalance(id);
		} catch (RuntimeException exc) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, exc.getMessage(), exc);
		}
	}
}
