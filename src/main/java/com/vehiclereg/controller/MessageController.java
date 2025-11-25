package com.vehiclereg.controller;

import com.vehiclereg.entity.ContactMessage;
import com.vehiclereg.repository.ContactMessageRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/messages")
@PreAuthorize("hasRole('REGISTERED') or hasRole('ADMIN')")
public class MessageController {

    private final ContactMessageRepository contactMessageRepository;

    public MessageController(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    @GetMapping
    public String messages(Model model) {
        List<ContactMessage> messages = contactMessageRepository.findAllByOrderBySentAtDesc();
        
        model.addAttribute("pageTitle", "Messages");
        model.addAttribute("messages", messages);
        model.addAttribute("totalMessages", messages.size());
        
        return "messages/index";
    }
}
