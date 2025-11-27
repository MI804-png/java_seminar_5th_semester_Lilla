package com.vehiclereg.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name = "persons")
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(length = 50, nullable = false)
    private String name;
    
    @NotBlank(message = "Registration number is required")
    @Column(length = 6, nullable = false, unique = true)
    private String regnumber;
    
    @NotNull(message = "Height is required")
    @Min(value = 100, message = "Height must be at least 100 cm")
    @Column(nullable = false)
    private Integer height;
    
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Phone> phones;
    
    // Note: Vehicle relationship removed to avoid foreign key constraint issues
    // Vehicles are linked by regnumber matching regnum, but no JPA relationship
    
    // Constructors
    public Person() {}
    
    public Person(String name, String regnumber, Integer height) {
        this.name = name;
        this.regnumber = regnumber;
        this.height = height;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getRegnumber() { return regnumber; }
    public void setRegnumber(String regnumber) { this.regnumber = regnumber; }
    
    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }
    
    public List<Phone> getPhones() { return phones; }
    public void setPhones(List<Phone> phones) { this.phones = phones; }
}
