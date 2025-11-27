package com.vehiclereg.controller;

import com.vehiclereg.entity.Person;
import com.vehiclereg.entity.Vehicle;
import com.vehiclereg.repository.PersonRepository;
import com.vehiclereg.repository.VehicleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/crud")
public class CrudController {

    private final PersonRepository personRepository;
    private final VehicleRepository vehicleRepository;

    public CrudController(PersonRepository personRepository, VehicleRepository vehicleRepository) {
        this.personRepository = personRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public String crud(Model model) {
        List<Person> persons = personRepository.findAll();
        model.addAttribute("pageTitle", "Person Management - CRUD");
        model.addAttribute("persons", persons);
        return "crud/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("pageTitle", "Add New Person");
        model.addAttribute("person", new Person());
        model.addAttribute("action", "Add");
        return "crud/form";
    }

    @PostMapping("/add")
    public String addPerson(@Valid @ModelAttribute Person person, BindingResult result, 
                           RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("pageTitle", "Add New Person");
        model.addAttribute("action", "Add");
        
        System.out.println("=== Add Person POST request ===");
        System.out.println("Person data: " + person.getName() + ", " + person.getRegnumber() + ", " + person.getHeight());
        System.out.println("Has errors: " + result.hasErrors());
        System.out.println("Error count: " + result.getErrorCount());
        
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("error", "Please fix the validation errors below.");
            return "crud/form";
        }

        if (personRepository.existsByRegnumber(person.getRegnumber())) {
            System.out.println("Registration number already exists: " + person.getRegnumber());
            model.addAttribute("error", "Registration number already exists!");
            return "crud/form";
        }

        try {
            Person savedPerson = personRepository.save(person);
            System.out.println("Person saved successfully with ID: " + savedPerson.getId());
            redirectAttributes.addFlashAttribute("success", "Person added successfully!");
            return "redirect:/crud";
        } catch (Exception e) {
            System.err.println("Error saving person: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while saving the person: " + e.getMessage());
            return "crud/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isEmpty()) {
            return "redirect:/crud";
        }
        
        model.addAttribute("pageTitle", "Edit Person");
        model.addAttribute("person", person.get());
        model.addAttribute("action", "Edit");
        return "crud/form";
    }

    @PostMapping("/edit/{id}")
    public String editPerson(@PathVariable Long id, @Valid @ModelAttribute Person person, 
                            BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("pageTitle", "Edit Person");
        model.addAttribute("action", "Edit");
        
        if (result.hasErrors()) {
            return "crud/form";
        }

        Optional<Person> existingPerson = personRepository.findById(id);
        if (existingPerson.isEmpty()) {
            return "redirect:/crud";
        }

        // Check if regnumber is taken by another person
        Optional<Person> personWithSameRegnum = personRepository.findByRegnumber(person.getRegnumber());
        if (personWithSameRegnum.isPresent() && !personWithSameRegnum.get().getId().equals(id)) {
            model.addAttribute("error", "Registration number already exists!");
            return "crud/form";
        }

        try {
            person.setId(id);
            personRepository.save(person);
            redirectAttributes.addFlashAttribute("success", "Person updated successfully!");
            return "redirect:/crud";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while updating the person.");
            return "crud/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePerson(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            if (personRepository.existsById(id)) {
                personRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Person deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Person not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while deleting the person.");
        }
        
        return "redirect:/crud";
    }

    @GetMapping("/view/{id}")
    public String viewPerson(@PathVariable Long id, Model model) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isEmpty()) {
            return "redirect:/crud";
        }
        
        // Look up vehicle by matching registration number
        Optional<Vehicle> vehicle = vehicleRepository.findById(person.get().getRegnumber());
        
        model.addAttribute("pageTitle", "View Person");
        model.addAttribute("person", person.get());
        model.addAttribute("vehicle", vehicle.orElse(null));
        return "crud/view";
    }
}
