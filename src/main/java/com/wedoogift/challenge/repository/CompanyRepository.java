package com.wedoogift.challenge.repository;

import com.wedoogift.challenge.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * Spring Data  repository for the Company entity.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from Company a where a.id = :id")
	Optional<Company> findCompanyWithPessimisticLock(Long id);
}
