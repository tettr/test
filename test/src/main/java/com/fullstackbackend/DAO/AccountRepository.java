package com.fullstackbackend.DAO;

import com.fullstackbackend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT Account FROM Account WHERE user.id = ?1")
    Optional<Account> findByUser(Integer idddd);
}
