package com.vehiclereg.controller;

import com.vehiclereg.entity.ContactMessage;
import com.vehiclereg.repository.ContactMessageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final ContactMessageRepository contactMessageRepository;

    public ContactController(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    @GetMapping
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Contact Us");
        model.addAttribute("contactMessage", new ContactMessage());
        return "contact/index";
    }

    @PostMapping
    public String submitContact(@Valid @ModelAttribute("contactMessage") ContactMessage contactMessage, 
                               BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("pageTitle", "Contact Us");
        
        System.out.println("=== Contact POST request ===");
        System.out.println("Has errors: " + result.hasErrors());
        System.out.println("Error count: " + result.getErrorCount());
        
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("error", "Please fix the validation errors below.");
            return "contact/index";
        }

        try {
            contactMessageRepository.save(contactMessage);
            redirectAttributes.addFlashAttribute("success", "Thank you for your message! We will get back to you soon.");
            return "redirect:/contact";
        } catch (Exception e) {
            System.out.println("Contact save error: " + e.getMessage());
            model.addAttribute("error", "An error occurred while sending your message. Please try again.");
            return "contact/index";
        }
    }
}
