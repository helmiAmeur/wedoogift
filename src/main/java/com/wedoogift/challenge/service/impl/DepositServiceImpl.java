package com.wedoogift.challenge.service.impl;

import com.wedoogift.challenge.entity.Company;
import com.wedoogift.challenge.exception.AccountNotFoundException;
import com.wedoogift.challenge.exception.BalanceExeption;
import com.wedoogift.challenge.exception.CompanyNotFoundException;
import com.wedoogift.challenge.exception.RequiredParamException;
import com.wedoogift.challenge.exception.UserNotFoundException;
import com.wedoogift.challenge.helper.DepositRequest;
import com.wedoogift.challenge.repository.AccountRepository;
import com.wedoogift.challenge.repository.CompanyRepository;
import com.wedoogift.challenge.repository.DepositRepository;
import com.wedoogift.challenge.repository.UserRepository;
import com.wedoogift.challenge.service.DepositService;
import com.wedoogift.challenge.service.factory.DepositFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Service Implementation for managing {@link com.wedoogift.challenge.entity.Deposit}.
 */
@Service
public class DepositServiceImpl implements DepositService {

	private final CompanyRepository companyRepository;
	private final AccountRepository accountRepository;
	private final DepositRepository depositRepository;
	private final UserRepository userRepository;

	private final Logger log = LoggerFactory.getLogger(DepositServiceImpl.class);

	public DepositServiceImpl(CompanyRepository companyRepository, AccountRepository accountRepository, DepositRepository depositRepository, UserRepository userRepository) {
		this.companyRepository = companyRepository;
		this.accountRepository = accountRepository;
		this.depositRepository = depositRepository;
		this.userRepository = userRepository;
	}


	@Override
	@Transactional
	public void distributeDeposit(DepositRequest depositRequest) {
		if (depositRequest == null) {
			throw new RequiredParamException();
		}
		//check if all request param not null
		if (Stream.of(depositRequest.getAmount(),
				depositRequest.getCompanyId(),
				depositRequest.getAccountId(),
				depositRequest.getType()).anyMatch(Objects::isNull)) {
			throw new RequiredParamException();
		}

		companyRepository.findCompanyWithPessimisticLock(depositRequest.getCompanyId()).ifPresentOrElse(company -> {
			accountRepository.findAccountWithPessimisticLock(depositRequest.getAccountId()).ifPresentOrElse(account -> {
				if (checkCompanyBalance(company, depositRequest.getAmount())) {
					DepositFactory depositFactory = new DepositFactory();
					depositFactory.getDeposit(depositRequest.getType(), depositRequest.getAmount(), account);
					company.setBalance(company.getBalance() - depositRequest.getAmount());
				} else {
					throw new BalanceExeption();
				}

			}, () -> {
				throw new AccountNotFoundException();
			});
		}, () -> {
			throw new CompanyNotFoundException(depositRequest.getCompanyId());
		});

	}

	/**
	 * old Deposit should be automatically Expired after Expired date.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	@Transactional
	public void removeOldAuditEvents() {
		log.debug("run scheduled");
		depositRepository
				.findDepositByExpirationDateBeforeAndAndExpiredFalse(new Date())
				.forEach(deposit -> {
					deposit.setExpired(true);
					if (deposit.getAccount() != null) {
						userRepository.findByAccount_Id(deposit.getAccount().getId()).ifPresentOrElse(user -> {
							if (user.getCompany() != null) {
								user.getCompany().setBalance(user.getCompany().getBalance() + deposit.getAmount());
								log.debug("expired Deposit with id {}", deposit.getId());
							} else {
								throw new CompanyNotFoundException();
							}

						}, () -> {
							throw new UserNotFoundException();
						});
					} else {
						throw new AccountNotFoundException();
					}
				});
	}

	private boolean checkCompanyBalance(Company company, Double amount) {
		return company.getBalance() > amount;
	}


}
