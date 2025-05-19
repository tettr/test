package com.fullstackbackend.DAO;

import com.fullstackbackend.model.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PhoneDataRepository  extends JpaRepository<PhoneData, Integer> {
    @Query("SELECT PhoneData FROM PhoneData WHERE user.id = ?1")
    Optional<PhoneData> findByUser(Integer idddd);
}
