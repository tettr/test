package com.fullstackbackend.DAO;

import com.fullstackbackend.model.EmailData;
import com.fullstackbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailDataRepository  extends JpaRepository<EmailData, Integer> {
    @Query("SELECT EmailData FROM EmailData WHERE user.id = ?1")
    Optional<EmailData> findByUser(Integer idddd);

    @Query("SELECT User FROM EmailData WHERE email = ?1")
    Optional<User> findByEmail(String emailD);
}
