package project.Appointment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.Appointment.dto.CreateUserDto;
import project.Appointment.entity.Doctor;
import project.Appointment.entity.Users;
import project.Appointment.enums.Role;
import project.Appointment.repository.DoctorRepository;
import project.Appointment.service.DoctorService;
import project.Appointment.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {


    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/getDoctorById/{doctorId}")
    public Doctor findDoctorBbyId(@PathVariable(name="doctorId") Long doctorId) throws Exception {
       Doctor doctor= doctorService.findByDoctorId(doctorId);
       if(doctor==null) throw new Exception("Doctor Not Found");
       return doctor;
    }

    @GetMapping("/getDoctorByUserEmail")
    public Doctor findDoctorBYEmail(@PathVariable(name = "email") String email ){

        return doctorService.findByUserEmail(email);
    }


    @GetMapping("/doctor_details/{userId}")
    public String findAllDoctors(Model model, @PathVariable Long userId)  {
        Users user= userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Doctor> doctors = doctorService.findAllDoctors();
     model.addAttribute("doctors", doctors);
        model.addAttribute("navbar", "navbarAdmin");
     return "DoctorLists";
    }


    @DeleteMapping("/deleteDoctorById/{doctorId}")
    public boolean deleteDoctorById(@PathVariable(name = "doctorId") long doctorId) throws Exception {
        Doctor doctor= doctorService.deleteDoctorByID(doctorId);
        if(doctor!=null){
            return true;
        }
        return false;
    }


    @PutMapping("/UpdateDoctorById/{doctorId}")
    public Doctor updateDoctorById(@PathVariable(name = "doctorId") long doctorId, @RequestBody Doctor doctor) throws Exception {
        Doctor oldDoctor=doctorService.findByDoctorId(doctorId);
        if(oldDoctor==null) throw  new Exception("doctor not found");

        return doctorService.updateDoctorById(oldDoctor,doctor);

}

    @GetMapping("/updateYourProfile/{userId}")
    public String showDoctorUpdate(Model model , @PathVariable Long userId){
        Users user= userService.findUserById(userId);
        Doctor doctor = doctorService.getDoctorByUser(user);
           model.addAttribute("doctor", doctor);
          model.addAttribute("userId", userId);
        return "UpdateDoctor";
       }

       @PostMapping("/updateYourProfile/{userId}")
         public String getDoctorUpdate(@PathVariable Long userId, @ModelAttribute Doctor doctor){
           doctorService.updateDoctorProfile(userId, doctor);
           return "redirect:/doctor/profile/{userId}";
       }



    @GetMapping("/profile/{userId}")
    public String showProfile(Model model , @PathVariable Long userId){
        Users user= userService.findUserById(userId);
        Doctor doctor = doctorService.getDoctorByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "Doctorprofile";

    }


    }

