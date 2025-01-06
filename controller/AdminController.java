package project.Appointment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.Appointment.entity.Users;
import project.Appointment.service.AppointmentService;
import project.Appointment.service.DoctorService;
import project.Appointment.service.PatientService;
import project.Appointment.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

@GetMapping("/profile/{userId}")
public String showProfile(Model model , @PathVariable Long userId){
    Users user= userService.findUserById(userId);

    model.addAttribute("user", user);

    model.addAttribute("navbar", "navbarAdmin");
    return "AdminProfile";

}




}
