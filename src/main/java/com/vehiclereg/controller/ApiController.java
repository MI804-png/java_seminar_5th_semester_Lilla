package com.vehiclereg.controller;

import com.vehiclereg.entity.Person;
import com.vehiclereg.entity.Vehicle;
import com.vehiclereg.repository.PersonRepository;
import com.vehiclereg.repository.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PersonRepository personRepository;
    private final VehicleRepository vehicleRepository;

    public ApiController(PersonRepository personRepository, VehicleRepository vehicleRepository) {
        this.personRepository = personRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // Person API endpoints
    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/persons")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        try {
            if (personRepository.existsByRegnumber(person.getRegnumber())) {
                return ResponseEntity.badRequest().build();
            }
            Person savedPerson = personRepository.save(person);
            return ResponseEntity.ok(savedPerson);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            person.setId(id);
            Person savedPerson = personRepository.save(person);
            return ResponseEntity.ok(savedPerson);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        if (!personRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            personRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Vehicle API endpoints
    @GetMapping("/vehicles")
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/vehicles/{regnum}")
    public ResponseEntity<Vehicle> getVehicleByRegnum(@PathVariable String regnum) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(regnum);
        return vehicle.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            return ResponseEntity.ok(savedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/vehicles/{regnum}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable String regnum, @RequestBody Vehicle vehicle) {
        if (!vehicleRepository.existsById(regnum)) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            vehicle.setRegnum(regnum);
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            return ResponseEntity.ok(savedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/vehicles/{regnum}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String regnum) {
        if (!vehicleRepository.existsById(regnum)) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            vehicleRepository.deleteById(regnum);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Statistics endpoints
    @GetMapping("/stats/vehicles-by-brand")
    public List<Object[]> getVehiclesByBrand() {
        return vehicleRepository.countVehiclesByBrand();
    }

    @GetMapping("/stats/vehicles-by-color")
    public List<Object[]> getVehiclesByColor() {
        return vehicleRepository.countVehiclesByColor();
    }
}
