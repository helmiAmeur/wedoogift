package com.wedoogift.challenge.repository;

import com.wedoogift.challenge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * Spring Data  repository for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	Optional<User> findByAccount_Id (long id) ;
}
