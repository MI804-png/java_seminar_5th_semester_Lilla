package com.vehiclereg.controller;

import com.vehiclereg.repository.VehicleRepository;
import com.vehiclereg.repository.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/chart")
public class ChartController {

    private final VehicleRepository vehicleRepository;
    private final PersonRepository personRepository;

    public ChartController(VehicleRepository vehicleRepository, PersonRepository personRepository) {
        this.vehicleRepository = vehicleRepository;
        this.personRepository = personRepository;
    }

    @GetMapping
    public String chart(Model model) {
        // Get vehicle brand statistics
        List<Object[]> brandStats = vehicleRepository.countVehiclesByBrand();
        Map<String, Long> brandData = new HashMap<>();
        for (Object[] stat : brandStats) {
            brandData.put((String) stat[0], (Long) stat[1]);
        }
        System.out.println("Brand data: " + brandData);

        // Get vehicle color statistics
        List<Object[]> colorStats = vehicleRepository.countVehiclesByColor();
        Map<String, Long> colorData = new HashMap<>();
        for (Object[] stat : colorStats) {
            colorData.put((String) stat[0], (Long) stat[1]);
        }
        System.out.println("Color data: " + colorData);
        
        model.addAttribute("pageTitle", "Statistics & Charts");
        model.addAttribute("brandData", brandData);
        model.addAttribute("colorData", colorData);
        model.addAttribute("totalPersons", personRepository.count());
        model.addAttribute("totalVehicles", vehicleRepository.count());

        return "chart/index";
    }
}
