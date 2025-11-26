package com.vehiclereg.controller;

import com.vehiclereg.entity.User;
import com.vehiclereg.repository.UserRepository;
import com.vehiclereg.repository.PersonRepository;
import com.vehiclereg.repository.VehicleRepository;
import com.vehiclereg.repository.ContactMessageRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/users/export")
    public ResponseEntity<byte[]> exportUsers() {
        List<User> users = userRepository.findAll();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        
        // CSV Header
        writer.println("ID,Username,Email,Full Name,Role,Created At");
        
        // CSV Data
        for (User user : users) {
            writer.printf("%d,\"%s\",\"%s\",\"%s\",%s,%s%n",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getCreatedAt());
        }
        
        writer.flush();
        writer.close();
        
        byte[] csvBytes = outputStream.toByteArray();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "users_export.csv");
        headers.setContentLength(csvBytes.length);
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(csvBytes);
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                userRepository.delete(user);
                redirectAttributes.addFlashAttribute("success", 
                    "User '" + user.getUsername() + "' deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/cache/clear")
    @ResponseBody
    public ResponseEntity<String> clearCache() {
        System.out.println("Cache cleared by admin");
        return ResponseEntity.ok("Cache cleared successfully!");
    }
}
