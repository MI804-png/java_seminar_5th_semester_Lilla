package com.vehiclereg.repository;

import com.vehiclereg.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    Optional<Person> findByRegnumber(String regnumber);
    
    List<Person> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Person p WHERE p.height >= ?1")
    List<Person> findByHeightGreaterThanEqual(Integer height);
    
    @Query("SELECT COUNT(p) FROM Person p")
    Long countAllPersons();
    
    boolean existsByRegnumber(String regnumber);
}
