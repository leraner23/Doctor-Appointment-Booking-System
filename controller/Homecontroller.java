package project.Appointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.Appointment.DTOmapping.UserMapper;
import project.Appointment.entity.Doctor;
import project.Appointment.repository.UserRepository;
import project.Appointment.service.AppointmentService;
import project.Appointment.service.DoctorService;
import project.Appointment.service.PatientService;
import project.Appointment.service.UserService;

import java.util.List;

@Controller
public class Homecontroller {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Doctor> doctors = doctorService.findAllDoctors();
        model.addAttribute("doctors", doctors);
        model.addAttribute("healthadvice", "HealthAdvices");
        return "index";
    }

}
