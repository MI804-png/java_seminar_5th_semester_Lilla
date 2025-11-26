package com.vehiclereg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "phones")
public class Phone {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Person ID is required")
    @Column(nullable = false)
    private Long personid;
    
    @NotBlank(message = "Phone number is required")
    @Column(length = 20, nullable = false)
    private String number;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personid", insertable = false, updatable = false)
    private Person person;
    
    // Constructors
    public Phone() {}
    
    public Phone(Long personid, String number) {
        this.personid = personid;
        this.number = number;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPersonid() { return personid; }
    public void setPersonid(Long personid) { this.personid = personid; }
    
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    
    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }
}
