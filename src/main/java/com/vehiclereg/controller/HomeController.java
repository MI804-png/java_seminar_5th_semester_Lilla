package com.vehiclereg.controller;

import com.vehiclereg.repository.PersonRepository;
import com.vehiclereg.repository.VehicleRepository;
import com.vehiclereg.repository.PhoneRepository;
import com.vehiclereg.repository.ContactMessageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    private final PersonRepository personRepository;
    private final VehicleRepository vehicleRepository;
    private final PhoneRepository phoneRepository;
    private final ContactMessageRepository contactMessageRepository;

    public HomeController(PersonRepository personRepository, VehicleRepository vehicleRepository,
                         PhoneRepository phoneRepository, ContactMessageRepository contactMessageRepository) {
        this.personRepository = personRepository;
        this.vehicleRepository = vehicleRepository;
        this.phoneRepository = phoneRepository;
        this.contactMessageRepository = contactMessageRepository;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model, Authentication authentication) {
        model.addAttribute("pageTitle", "Vehicle Registration System");
        
        // Add statistics data for the homepage
        model.addAttribute("totalPersons", personRepository.count());
        model.addAttribute("totalVehicles", vehicleRepository.count());
        model.addAttribute("totalPhones", phoneRepository.count());
        model.addAttribute("totalMessages", contactMessageRepository.count());
        
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("userRole", authentication.getAuthorities().toString());
        }
        
        return "index";
    }    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("pageTitle", "Test Page");
        
        // Add statistics data for testing
        model.addAttribute("totalPersons", personRepository.count());
        model.addAttribute("totalVehicles", vehicleRepository.count());
        model.addAttribute("totalPhones", phoneRepository.count());
        model.addAttribute("totalMessages", contactMessageRepository.count());
        
        return "index-test";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About Us");
        return "about";
    }
}
