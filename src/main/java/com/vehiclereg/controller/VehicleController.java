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
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;
    private final PersonRepository personRepository;

    public VehicleController(VehicleRepository vehicleRepository, PersonRepository personRepository) {
        this.vehicleRepository = vehicleRepository;
        this.personRepository = personRepository;
    }

    @GetMapping
    public String vehicles(Model model) {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        
        // Create a map of regnum -> owner for easy lookup in template
        java.util.Map<String, Person> vehicleOwners = new java.util.HashMap<>();
        for (Vehicle vehicle : vehicles) {
            personRepository.findByRegnumber(vehicle.getRegnum())
                .ifPresent(person -> vehicleOwners.put(vehicle.getRegnum(), person));
        }
        
        model.addAttribute("pageTitle", "Vehicle Management - CRUD");
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("vehicleOwners", vehicleOwners);
        return "vehicles/index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("pageTitle", "Add New Vehicle");
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("action", "Add");
        return "vehicles/form";
    }

    @PostMapping("/add")
    public String addVehicle(@Valid @ModelAttribute Vehicle vehicle, BindingResult result, 
                           RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("pageTitle", "Add New Vehicle");
        model.addAttribute("action", "Add");
        
        System.out.println("=== Add Vehicle POST request ===");
        System.out.println("Vehicle data: " + vehicle.getRegnum() + ", " + vehicle.getBrand() + ", " + vehicle.getColor());
        System.out.println("Has errors: " + result.hasErrors());
        
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("error", "Please fix the validation errors below.");
            return "vehicles/form";
        }

        if (vehicleRepository.existsById(vehicle.getRegnum())) {
            System.out.println("Registration number already exists: " + vehicle.getRegnum());
            model.addAttribute("error", "Registration number already exists!");
            return "vehicles/form";
        }

        try {
            Vehicle savedVehicle = vehicleRepository.save(vehicle);
            System.out.println("Vehicle saved successfully: " + savedVehicle.getRegnum());
            redirectAttributes.addFlashAttribute("success", "Vehicle added successfully!");
            return "redirect:/vehicles";
        } catch (Exception e) {
            System.err.println("Error saving vehicle: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while saving the vehicle: " + e.getMessage());
            return "vehicles/form";
        }
    }

    @GetMapping("/edit/{regnum}")
    public String editForm(@PathVariable String regnum, Model model) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(regnum);
        if (vehicle.isEmpty()) {
            return "redirect:/vehicles";
        }
        
        model.addAttribute("pageTitle", "Edit Vehicle");
        model.addAttribute("vehicle", vehicle.get());
        model.addAttribute("action", "Edit");
        return "vehicles/form";
    }

    @PostMapping("/edit/{regnum}")
    public String editVehicle(@PathVariable String regnum, @Valid @ModelAttribute Vehicle vehicle, 
                            BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        model.addAttribute("pageTitle", "Edit Vehicle");
        model.addAttribute("action", "Edit");
        
        System.out.println("=== Edit Vehicle POST request ===");
        System.out.println("Vehicle data: " + vehicle.getRegnum() + ", " + vehicle.getBrand() + ", " + vehicle.getColor());
        
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            model.addAttribute("error", "Please fix the validation errors below.");
            return "vehicles/form";
        }

        if (!vehicleRepository.existsById(regnum)) {
            return "redirect:/vehicles";
        }

        try {
            vehicle.setRegnum(regnum);
            vehicleRepository.save(vehicle);
            System.out.println("Vehicle updated successfully: " + regnum);
            redirectAttributes.addFlashAttribute("success", "Vehicle updated successfully!");
            return "redirect:/vehicles";
        } catch (Exception e) {
            System.err.println("Error updating vehicle: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while updating the vehicle: " + e.getMessage());
            return "vehicles/form";
        }
    }

    @PostMapping("/delete/{regnum}")
    public String deleteVehicle(@PathVariable String regnum, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== Delete Vehicle POST request ===");
            System.out.println("Registration number: " + regnum);
            
            if (vehicleRepository.existsById(regnum)) {
                // First, check if any person owns this vehicle
                Optional<Person> owner = personRepository.findByRegnumber(regnum);
                if (owner.isPresent()) {
                    System.out.println("Found owner for vehicle: " + owner.get().getName());
                    // The relationship is managed by JPA via the join column
                    // We need to delete the Person's reference first or delete fails
                    // Since we want to keep the person, we cannot use cascade
                    // Instead, we should prevent deletion
                    redirectAttributes.addFlashAttribute("error", 
                        "Cannot delete vehicle " + regnum + " because it is owned by " + owner.get().getName() + 
                        ". Please delete the person first or change their registration number.");
                    return "redirect:/vehicles";
                }
                
                vehicleRepository.deleteById(regnum);
                System.out.println("Vehicle deleted successfully: " + regnum);
                redirectAttributes.addFlashAttribute("success", "Vehicle deleted successfully!");
            } else {
                System.out.println("Vehicle not found: " + regnum);
                redirectAttributes.addFlashAttribute("error", "Vehicle not found!");
            }
        } catch (Exception e) {
            System.err.println("Error deleting vehicle: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", 
                "An error occurred while deleting the vehicle: " + e.getMessage());
        }
        
        return "redirect:/vehicles";
    }

    @GetMapping("/view/{regnum}")
    public String viewVehicle(@PathVariable String regnum, Model model) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(regnum);
        if (vehicle.isEmpty()) {
            return "redirect:/vehicles";
        }
        
        // Find the owner by matching registration number
        Optional<Person> owner = personRepository.findByRegnumber(regnum);
        
        model.addAttribute("pageTitle", "View Vehicle");
        model.addAttribute("vehicle", vehicle.get());
        model.addAttribute("owner", owner.orElse(null));
        return "vehicles/view";
    }
}
