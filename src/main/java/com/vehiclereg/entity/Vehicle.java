package com.vehiclereg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    
    @Id
    @Column(length = 6)
    private String regnum;
    
    @NotBlank(message = "Brand is required")
    @Column(length = 20, nullable = false)
    private String brand;
    
    @NotBlank(message = "Color is required")
    @Column(length = 20, nullable = false)
    private String color;
    
    // Note: Owner relationship removed to avoid bidirectional mapping issues
    // Person owns vehicle via matching regnumber, but no JPA relationship
    
    // Constructors
    public Vehicle() {}
    
    public Vehicle(String regnum, String brand, String color) {
        this.regnum = regnum;
        this.brand = brand;
        this.color = color;
    }
    
    // Getters and Setters
    public String getRegnum() { return regnum; }
    public void setRegnum(String regnum) { this.regnum = regnum; }
    
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
