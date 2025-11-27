package com.vehiclereg.controller;

import com.vehiclereg.entity.Person;
import com.vehiclereg.entity.Vehicle;
import com.vehiclereg.entity.Phone;
import com.vehiclereg.repository.PersonRepository;
import com.vehiclereg.repository.VehicleRepository;
import com.vehiclereg.repository.PhoneRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/database")
public class DatabaseController {

    private final PersonRepository personRepository;
    private final VehicleRepository vehicleRepository;
    private final PhoneRepository phoneRepository;

    public DatabaseController(PersonRepository personRepository, VehicleRepository vehicleRepository, PhoneRepository phoneRepository) {
        this.personRepository = personRepository;
        this.vehicleRepository = vehicleRepository;
        this.phoneRepository = phoneRepository;
    }

    @GetMapping
    public String database(Model model) {
        List<Person> persons = personRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Phone> phones = phoneRepository.findAll();

        // Create maps for looking up vehicles and owners
        java.util.Map<String, Vehicle> personVehicles = new java.util.HashMap<>();
        java.util.Map<String, Person> vehicleOwners = new java.util.HashMap<>();
        
        for (Person person : persons) {
            vehicleRepository.findById(person.getRegnumber())
                .ifPresent(vehicle -> personVehicles.put(person.getRegnumber(), vehicle));
        }
        
        for (Vehicle vehicle : vehicles) {
            personRepository.findByRegnumber(vehicle.getRegnum())
                .ifPresent(person -> vehicleOwners.put(vehicle.getRegnum(), person));
        }

        model.addAttribute("pageTitle", "Database");
        model.addAttribute("persons", persons);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("phones", phones);
        model.addAttribute("personVehicles", personVehicles);
        model.addAttribute("vehicleOwners", vehicleOwners);
        model.addAttribute("totalPersons", persons.size());
        model.addAttribute("totalVehicles", vehicles.size());
        model.addAttribute("totalPhones", phones.size());

        return "database/index";
    }
}
