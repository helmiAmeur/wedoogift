package com.wedoogift.challenge.service.impl;

import com.wedoogift.challenge.ChallengeApplication;
import com.wedoogift.challenge.entity.Account;
import com.wedoogift.challenge.entity.Deposit;
import com.wedoogift.challenge.entity.GiftDeposit;
import com.wedoogift.challenge.entity.MealDeposit;
import com.wedoogift.challenge.entity.User;
import com.wedoogift.challenge.exception.AccountNotFoundException;
import com.wedoogift.challenge.exception.UserNotFoundException;
import com.wedoogift.challenge.repository.UserRepository;
import com.wedoogift.challenge.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link com.wedoogift.challenge.service.UserService}.
 */
@SpringBootTest(classes = ChallengeApplication.class)
@Transactional
class UserServiceImplTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	private User user;

	@BeforeEach
	public void init() {

		user = new User();
		user.setName("userTest");
		Account account = new Account();
		List<Deposit> deposits = new ArrayList<>();
		GiftDeposit giftDeposit = new GiftDeposit();
		giftDeposit.setAmount(100);
		giftDeposit.setExpired(false);
		deposits.add(giftDeposit);
		GiftDeposit expiredGiftDeposit = new GiftDeposit();
		expiredGiftDeposit.setAmount(50);
		expiredGiftDeposit.setExpired(true);
		deposits.add(expiredGiftDeposit);
		MealDeposit mealDeposits = new MealDeposit();
		mealDeposits.setAmount(50);
		mealDeposits.setExpired(false);
		deposits.add(mealDeposits);
		account.setDeposits(deposits);
		user.setAccount(account);
	}

	@Test
	@Transactional
	@DisplayName("Test whene we dont found User in database")
	public void testExpectedNotFoundUserException() {
		userRepository.saveAndFlush(user);
		UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class, () -> {
			double balance = userService.getUserBalance(user.getId() + 5);
		});
		Assertions.assertEquals("User could not be found! with id : " + (user.getId() + 5), thrown.getMessage());
	}

	@Test
	@Transactional
	@DisplayName("Test whene user exist but he  don't have account yet")
	public void testExpectedNotFoundAccountException() {
		user.setAccount(null);
		userRepository.saveAndFlush(user);
		AccountNotFoundException thrown = Assertions.assertThrows(AccountNotFoundException.class, () -> {
			double balance = userService.getUserBalance(user.getId());
		});
		Assertions.assertEquals("User don't have account yet! with id : " + user.getId(), thrown.getMessage());
	}


	@Test
	@Transactional
	@DisplayName("Test get the correct Balance of user (sum of unexpired deposits) ")
	public void testGetBalance() {
		userRepository.saveAndFlush(user);
		double balance = userService.getUserBalance(user.getId());
		assertThat(balance).isEqualTo(150.0);
	}

	@Test
	@Transactional
	@DisplayName("Test get 0 Balance of user without Deposits")
	public void testUserWithoutDeposits() {
		user.getAccount().setDeposits(null);
		userRepository.saveAndFlush(user);
		double balance = userService.getUserBalance(user.getId());
		assertThat(balance).isEqualTo(0.0);
	}

}
