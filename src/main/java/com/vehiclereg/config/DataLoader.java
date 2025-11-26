package com.vehiclereg.config;

import com.vehiclereg.entity.*;
import com.vehiclereg.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PersonRepository personRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private PhoneRepository phoneRepository;
    
    @Autowired
    private ContactMessageRepository contactMessageRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadInitialData();
    }
    
    private void loadInitialData() {
        // Load users if not exist
        if (userRepository.count() == 0) {
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@vehiclereg.com");
            admin.setFullName("System Administrator");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            
            // Create test user
            User testUser = new User();
            testUser.setUsername("user");
            testUser.setPassword(passwordEncoder.encode("user123"));
            testUser.setEmail("user@vehiclereg.com");
            testUser.setFullName("Test User");
            testUser.setRole(User.Role.REGISTERED);
            userRepository.save(testUser);
            
            System.out.println("✓ Initial users created: admin/admin123, user/user123");
        }
          // Load vehicles data first (since persons reference vehicles)
        if (vehicleRepository.count() == 0) {
            Vehicle vehicle1 = new Vehicle("ABC123", "Ford", "red");
            Vehicle vehicle2 = new Vehicle("FGH456", "Skoda", "blue");
            Vehicle vehicle3 = new Vehicle("SDF345", "Ford", "white");
            Vehicle vehicle4 = new Vehicle("HJK678", "BMW", "red");
            
            vehicleRepository.save(vehicle1);
            vehicleRepository.save(vehicle2);
            vehicleRepository.save(vehicle3);
            vehicleRepository.save(vehicle4);
            
            System.out.println("✓ Initial vehicles data loaded");
        }
        
        // Load persons data if not exist (after vehicles are loaded)
        if (personRepository.count() == 0) {
            Person person1 = new Person("John Smith", "ABC123", 175);
            Person person2 = new Person("Tom Grey", "FGH456", 168);
            Person person3 = new Person("Viola Grant", "SDF345", 165);
            Person person4 = new Person("Steve Roberts", "HJK678", 160);
            
            personRepository.save(person1);
            personRepository.save(person2);
            personRepository.save(person3);
            personRepository.save(person4);
            
            System.out.println("✓ Initial persons data loaded");
        }
        
        // Load phones data if not exist
        if (phoneRepository.count() == 0) {
            // Get person IDs after saving
            Person john = personRepository.findByRegnumber("ABC123").orElse(null);
            Person tom = personRepository.findByRegnumber("FGH456").orElse(null);
            Person viola = personRepository.findByRegnumber("SDF345").orElse(null);
            
            if (john != null) {
                phoneRepository.save(new Phone(john.getId(), "345678"));
            }
            if (tom != null) {
                phoneRepository.save(new Phone(tom.getId(), "456123"));
                phoneRepository.save(new Phone(tom.getId(), "234678"));
            }
            if (viola != null) {
                phoneRepository.save(new Phone(viola.getId(), "345123"));
                phoneRepository.save(new Phone(viola.getId(), "123789"));
                phoneRepository.save(new Phone(viola.getId(), "345987"));
            }
            
            System.out.println("✓ Initial phones data loaded");
        }
        
        // Load sample contact messages if not exist
        if (contactMessageRepository.count() == 0) {
            ContactMessage msg1 = new ContactMessage();
            msg1.setName("Alice Johnson");
            msg1.setEmail("alice@example.com");
            msg1.setSubject("Question about vehicle registration");
            msg1.setMessage("Hello, I have a question about registering my new vehicle. Could you please help me with the process?");
            contactMessageRepository.save(msg1);
            
            ContactMessage msg2 = new ContactMessage();
            msg2.setName("Bob Wilson");
            msg2.setEmail("bob@example.com");
            msg2.setSubject("Update contact information");
            msg2.setMessage("I need to update my contact information in your system. How can I do this?");
            contactMessageRepository.save(msg2);
            
            ContactMessage msg3 = new ContactMessage();
            msg3.setName("Carol Davis");
            msg3.setEmail("carol@example.com");
            msg3.setSubject("System feedback");
            msg3.setMessage("Great system! Very user-friendly and efficient. Keep up the good work!");
            contactMessageRepository.save(msg3);
            
            System.out.println("✓ Sample contact messages loaded");
        }
        
        System.out.println("=== Data initialization completed ===");
        System.out.println("Admin login: admin/admin123");
        System.out.println("User login: user/user123");
        System.out.println("=====================================");
    }
}
