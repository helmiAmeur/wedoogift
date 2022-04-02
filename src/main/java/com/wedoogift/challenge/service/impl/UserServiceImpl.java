package com.wedoogift.challenge.service.impl;

import com.wedoogift.challenge.entity.Deposit;
import com.wedoogift.challenge.exception.AccountNotFoundException;
import com.wedoogift.challenge.exception.UserNotFoundException;
import com.wedoogift.challenge.repository.UserRepository;
import com.wedoogift.challenge.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Service Implementation for managing {@link com.wedoogift.challenge.entity.User}.
 */

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public double getUserBalance(Long userId) {
		AtomicReference<Double> balance = new AtomicReference<>((double) 0);
		userRepository.findById(userId).ifPresentOrElse(user -> {
			if (user.getAccount() == null) {
				throw new AccountNotFoundException(userId);
			}
			//get the amount sum of all Deposit in the user Account where deposit not expired
			if (!CollectionUtils.isEmpty(user.getAccount().getDeposits())) {
				balance.set(user.getAccount().getDeposits().stream()
						.filter(deposit -> !deposit.isExpired())
						.mapToDouble(Deposit::getAmount)
						.sum());
			}
		}, () -> {
			throw new UserNotFoundException(userId);
		});
		return balance.get();

	}
}
