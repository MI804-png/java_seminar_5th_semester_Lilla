package com.vehiclereg.repository;

import com.vehiclereg.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    
    List<Phone> findByPersonid(Long personid);
    
    List<Phone> findByNumberContaining(String number);
    
    @Query("SELECT COUNT(p) FROM Phone p WHERE p.personid = ?1")
    Long countPhonesByPersonId(Long personid);
    
    void deleteByPersonid(Long personid);
}
