package com.wedoogift.challenge.service.impl;

import com.wedoogift.challenge.ChallengeApplication;
import com.wedoogift.challenge.entity.Account;
import com.wedoogift.challenge.entity.Company;
import com.wedoogift.challenge.entity.Deposit;
import com.wedoogift.challenge.exception.AccountNotFoundException;
import com.wedoogift.challenge.exception.BalanceExeption;
import com.wedoogift.challenge.exception.CompanyNotFoundException;
import com.wedoogift.challenge.exception.RequiredParamException;
import com.wedoogift.challenge.helper.DepositRequest;
import com.wedoogift.challenge.helper.DepositType;
import com.wedoogift.challenge.repository.AccountRepository;
import com.wedoogift.challenge.repository.CompanyRepository;
import com.wedoogift.challenge.service.DepositService;
import com.wedoogift.challenge.service.factory.DepositFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link com.wedoogift.challenge.service.DepositService}.
 */
@SpringBootTest(classes = ChallengeApplication.class)
@Transactional
class DepositServiceImplTest {

	@Autowired
	private DepositService depositService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private AccountRepository accountRepository;

	private DepositRequest depositRequest;
	private Company company;
	private Account account;

	@BeforeEach
	public void init() {
		createRequest();
		createCompany();
		createAccountWitthDeposits();
	}


	@Test
	@Transactional
	@DisplayName("Test Exception whene request is null")
	public void testExceptionNullRequest() {

		RequiredParamException thrown = Assertions.assertThrows(RequiredParamException.class, () -> {
			depositService.distributeDeposit(null);
		});
		Assertions.assertEquals("Absent mandatory param(s): accountid,companyId,type or ammount ", thrown.getMessage());
	}

	@Test
	@Transactional
	@DisplayName("Test Exception whene a field in the request is null")
	public void testExceptionNullField() {
		depositRequest.setType(null);
		RequiredParamException thrown = Assertions.assertThrows(RequiredParamException.class, () -> {
			depositService.distributeDeposit(depositRequest);
		});
		Assertions.assertEquals("Absent mandatory param(s): accountid,companyId,type or ammount ", thrown.getMessage());
	}

	@Test
	@Transactional
	@DisplayName("Test Exception whene Company not found")
	public void testExceptionNotFoundCompany() {
		companyRepository.saveAndFlush(company);
		depositRequest.setCompanyId(company.getId() + 5);
		CompanyNotFoundException thrown = Assertions.assertThrows(CompanyNotFoundException.class, () -> {
			depositService.distributeDeposit(depositRequest);
		});
		Assertions.assertEquals("Company could not be found! company id : " + (company.getId() + 5), thrown.getMessage());
	}

	@Test
	@Transactional
	@DisplayName("Test Exception whene Account not found")
	public void testExceptionNotFoundAccount() {
		companyRepository.saveAndFlush(company);
		accountRepository.saveAndFlush(account);
		depositRequest.setCompanyId(company.getId());
		depositRequest.setAccountId(account.getId() + 5);
		AccountNotFoundException thrown = Assertions.assertThrows(AccountNotFoundException.class, () -> {
			depositService.distributeDeposit(depositRequest);
		});
		Assertions.assertEquals("Account could not be found! ", thrown.getMessage());
	}

	@Test
	@Transactional
	@DisplayName("Test Exception whene the company does not have as much balance")
	public void testExceptionNotHaveBalance() {
		companyRepository.saveAndFlush(company);
		accountRepository.saveAndFlush(account);
		depositRequest.setCompanyId(company.getId());
		depositRequest.setAccountId(account.getId());
		depositRequest.setAmount(5001.0);
		BalanceExeption thrown = Assertions.assertThrows(BalanceExeption.class, () -> {
			depositService.distributeDeposit(depositRequest);
		});
		Assertions.assertEquals("the company does not have as much balance to carry out this operation", thrown.getMessage());
	}

	@Test
	@Transactional
	@DisplayName("Test when company distribute gift deposit")
	public void testGiftDeposit() {
		companyRepository.saveAndFlush(company);
		accountRepository.saveAndFlush(account);
		depositRequest.setCompanyId(company.getId());
		depositRequest.setAccountId(account.getId());
		depositRequest.setAmount(500.0);
		depositService.distributeDeposit(depositRequest);
		assertEquals(company.getBalance(), 4500.0);
		assertEquals(account.getDeposits().size(), 3);
		assertFalse(account.getDeposits().get(2).isExpired());
		assertEquals(account.getDeposits().get(2).getAmount(), 500.0);
		assertEquals(account.getDeposits().get(2).getExpirationDate(), calculeGiftExpirationDate(account.getDeposits().get(2).getCreationDate()));
	}

	@Test
	@Transactional
	@DisplayName("Test when company distribute meal deposit")
	public void testMealDeposit() {
		companyRepository.saveAndFlush(company);
		accountRepository.saveAndFlush(account);
		depositRequest.setCompanyId(company.getId());
		depositRequest.setAccountId(account.getId());
		depositRequest.setAmount(500.0);
		depositRequest.setType(DepositType.MEAL);
		depositService.distributeDeposit(depositRequest);
		assertEquals(company.getBalance(), 4500.0);
		assertEquals(account.getDeposits().size(), 3);
		assertFalse(account.getDeposits().get(2).isExpired());
		assertEquals(account.getDeposits().get(2).getAmount(), 500.0);
		assertEquals(account.getDeposits().get(2).getExpirationDate(), calculeMealExpirationDate(account.getDeposits().get(2).getCreationDate()));
	}


	private void createRequest() {
		depositRequest = new DepositRequest();
		depositRequest.setAmount(100.0);
		depositRequest.setAccountId(1L);
		depositRequest.setCompanyId(1L);
		depositRequest.setType(DepositType.GIFT);
	}

	private void createCompany() {
		company = new Company();
		company.setBalance(5000.0);
		company.setName("testCompant");
	}

	private void createAccountWitthDeposits() {
		account = new Account();
		List<Deposit> listdeposit = new ArrayList<>();
		DepositFactory depositFactory = new DepositFactory();
		Deposit giftDeposit = depositFactory.getDeposit(DepositType.GIFT, 100.0, account);
		Deposit mealDeposit = depositFactory.getDeposit(DepositType.MEAL, 50.0, account);
		listdeposit.add(mealDeposit);
		listdeposit.add(giftDeposit);
		account.setDeposits(listdeposit);
	}

	private Date calculeMealExpirationDate(Date creationDate) {
		Instant mealExpiration = creationDate.toInstant();
		ZonedDateTime utc = mealExpiration.atZone(ZoneId.systemDefault());
		ZonedDateTime zonedDateTime = utc.withMonth(Month.MARCH.getValue())
				.withDayOfMonth(1)
				.plusYears(1)
				.minus(1, ChronoUnit.DAYS);
		return Date.from(zonedDateTime.toInstant());
	}

	private Date calculeGiftExpirationDate(Date creationDate) {
		Instant instant = creationDate.toInstant();
		Instant expiration = instant.plus(365, ChronoUnit.DAYS);
		return Date.from(expiration);
	}
}
