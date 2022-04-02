package com.wedoogift.challenge.controller;

import com.wedoogift.challenge.helper.DepositRequest;
import com.wedoogift.challenge.service.DepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * REST controller for managing {@link com.wedoogift.challenge.entity.Deposit}.
 */
@RestController
@RequestMapping("/api")
public class DepositController {
	private final DepositService depositService;

	private final Logger log = LoggerFactory.getLogger(DepositController.class);

	public DepositController(DepositService depositService) {
		this.depositService = depositService;
	}

	/**
	 * {@code POST  /deposits} : Create a new deposit.
	 *
	 * @param request the deposit to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)}  or with status {@code 400 (Bad Request)} if the request has problem.
	 */
	@PostMapping("/deposits")
	public ResponseEntity<Void> distributeDeposit(@RequestBody DepositRequest request) {
		try {
			log.debug("REST request to distribute Deposit : {}", request);
			depositService.distributeDeposit(request);
			return ResponseEntity.ok().build();
		} catch (RuntimeException exc) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
		}

	}
}
