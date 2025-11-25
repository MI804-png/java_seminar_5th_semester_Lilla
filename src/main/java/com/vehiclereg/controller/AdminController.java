package com.vehiclereg.controller;

import com.vehiclereg.entity.User;
import com.vehiclereg.repository.UserRepository;
import com.vehiclereg.repository.PersonRepository;
import com.vehiclereg.repository.VehicleRepository;
import com.vehiclereg.repository.ContactMessageRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final VehicleRepository vehicleRepository;
    private final ContactMessageRepository contactMessageRepository;

    public AdminController(UserRepository userRepository, PersonRepository personRepository, 
                          VehicleRepository vehicleRepository, ContactMessageRepository contactMessageRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.vehicleRepository = vehicleRepository;
        this.contactMessageRepository = contactMessageRepository;
    }

    @GetMapping
    public String admin(Model model) {
        List<User> users = userRepository.findAll();
        
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("users", users);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalPersons", personRepository.count());
        model.addAttribute("totalVehicles", vehicleRepository.count());
        model.addAttribute("totalMessages", contactMessageRepository.count());
        
        return "admin/index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userRepository.findAll();
        
        model.addAttribute("pageTitle", "User Management");
        model.addAttribute("users", users);
        
        return "admin/users";
    }
}
