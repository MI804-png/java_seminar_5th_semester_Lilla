package com.vehiclereg.repository;

import com.vehiclereg.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    
    List<ContactMessage> findAllByOrderBySentAtDesc();
    
    List<ContactMessage> findByEmailOrderBySentAtDesc(String email);
}
