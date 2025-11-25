package com.vehiclereg.repository;

import com.vehiclereg.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    
    List<Vehicle> findByBrand(String brand);
    
    List<Vehicle> findByColor(String color);
    
    List<Vehicle> findByBrandAndColor(String brand, String color);
    
    @Query("SELECT v.brand, COUNT(v) FROM Vehicle v GROUP BY v.brand")
    List<Object[]> countVehiclesByBrand();
    
    @Query("SELECT v.color, COUNT(v) FROM Vehicle v GROUP BY v.color")
    List<Object[]> countVehiclesByColor();
}
