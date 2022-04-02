package com.wedoogift.challenge.repository;

import com.wedoogift.challenge.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

/**
 * Spring Data  repository for the Deposit entity.
 */
@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	List<Deposit> findDepositByExpirationDateBeforeAndAndExpiredFalse(Date date);
}
