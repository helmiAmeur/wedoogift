package com.wedoogift.challenge.repository;

import com.wedoogift.challenge.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * Spring Data  repository for the Account entity.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from Account a where a.id = :id")
	Optional<Account> findAccountWithPessimisticLock(Long id);
}
