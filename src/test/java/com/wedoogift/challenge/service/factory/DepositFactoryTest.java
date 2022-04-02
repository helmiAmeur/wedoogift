package com.wedoogift.challenge.service.factory;

import com.wedoogift.challenge.ChallengeApplication;
import com.wedoogift.challenge.entity.Account;
import com.wedoogift.challenge.entity.Deposit;
import com.wedoogift.challenge.helper.DepositType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Integration tests for {com.wedoogift.challenge.service.factory.DepositFactory}.
 */
@SpringBootTest(classes = ChallengeApplication.class)
@Transactional
class DepositFactoryTest {

	private Account account;

	@BeforeEach
	public void init() {
		account = new Account();
	}


	@Test
	@Transactional
	@DisplayName("Test Factory with null param")
	public void testNullParam() {
		DepositFactory depositFactory = new DepositFactory();
		Deposit deposit = depositFactory.getDeposit(null, null, null);
		assertNull(deposit);

	}

	@Test
	@Transactional
	@DisplayName("Test Create Meal deposit with correct expiration date")
	public void testCreateMealDeposit() {
		DepositFactory depositFactory = new DepositFactory();
		Deposit deposit = depositFactory.getDeposit(DepositType.MEAL, 100.0, account);
		assertNotNull(deposit);
		assertEquals(deposit.getAmount(), 100.0);
		assertEquals(deposit.getAccount(), account);
		assertEquals(deposit.getExpirationDate(), calculMealExpirationDate(deposit.getCreationDate()));
		assertFalse(deposit.isExpired());

	}

	@Test
	@Transactional
	@DisplayName("Test Create Gift deposit with correct expiration date ")
	public void testCreateGiftDeposit() {
		DepositFactory depositFactory = new DepositFactory();
		Deposit deposit = depositFactory.getDeposit(DepositType.GIFT, 100.0, account);
		assertNotNull(deposit);
		assertEquals(deposit.getAmount(), 100.0);
		assertEquals(deposit.getAccount(), account);
		assertEquals(deposit.getExpirationDate(), calculGiftExpirationDate(deposit.getCreationDate()));
		assertFalse(deposit.isExpired());

	}

	private Date calculGiftExpirationDate(Date creationDate) {
		Instant instant = creationDate.toInstant();
		Instant expiration = instant.plus(365, ChronoUnit.DAYS);
		return Date.from(expiration);
	}

	private Date calculMealExpirationDate(Date creationDate) {
		Instant mealExpiration = creationDate.toInstant();
		ZonedDateTime utc = mealExpiration.atZone(ZoneId.systemDefault());
		ZonedDateTime zonedDateTime = utc.withMonth(Month.MARCH.getValue())
				.withDayOfMonth(1)
				.plusYears(1)
				.minus(1, ChronoUnit.DAYS);
		return Date.from(zonedDateTime.toInstant());
	}


}
