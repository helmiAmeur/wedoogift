package com.wedoogift.challenge.helper;

import com.wedoogift.challenge.entity.Account;
import com.wedoogift.challenge.entity.Company;
import com.wedoogift.challenge.entity.Deposit;
import com.wedoogift.challenge.entity.User;
import com.wedoogift.challenge.repository.UserRepository;
import com.wedoogift.challenge.service.factory.DepositFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Profile({"dev"})
public class CreateFakeData implements CommandLineRunner {

	private final UserRepository userRepository;

	public CreateFakeData(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	private Company company;
	private Account account;
	private User user;

	@Override
	public void run(String... args) throws Exception {
		createCompany();
		createAccount();
		createUser();
	}

	private void createUser() {
		user = new User();
		user.setName("test User");
		user.setCompany(company);
		user.setAccount(account);
		userRepository.saveAndFlush(user);
	}

	private void createAccount() {
		account = new Account();
		account.setCreationDate(new Date());

		List<Deposit> depositList = new ArrayList<>();

		DepositFactory depositFactory = new DepositFactory();

		Deposit giftDeoposit = depositFactory.getDeposit(DepositType.GIFT, 50.0, account);
		Deposit mealDeposit = depositFactory.getDeposit(DepositType.MEAL, 50.0, account);
		depositList.add(giftDeoposit);
		depositList.add(mealDeposit);
		account.addDeposit(mealDeposit);
		account.addDeposit(giftDeoposit);

	}

	private void createCompany() {
		company = new Company();
		company.setName("wedoogift");
		company.setBalance(1000);
	}
}
