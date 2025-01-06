package project.Appointment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import project.Appointment.entity.Doctor;
import project.Appointment.entity.Patient;
import project.Appointment.entity.Users;
import project.Appointment.service.PatientService;
import project.Appointment.service.UserService;

import java.util.List;


@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private UserService userService;

    @GetMapping("/findPatientById/{patientId}")
    public Patient findPatientById(@PathVariable(name= "patientId") Long patientId){
        return patientService.findPatientById(patientId);
    }




    @GetMapping("/findAllPatients")
    public List<Patient> findAllPatients(@RequestBody Patient patient) throws Exception {

        List<Patient> patients = patientService.findAllPatients();
        if(patients.isEmpty()) throw new Exception("Patients list is empty");
        return patients;


    }

    @GetMapping("/findPatientByUserEmail/{email}")
    public Patient findPatientByEmail(@PathVariable(name = "email") String email){
        return patientService.findPatientByUserEmail(email);
    }


    @DeleteMapping("/deletePatientById/{patientId}")
    public boolean deletePatientById(@PathVariable(name = "patientId") long patientId) throws Exception {
        Patient patient = patientService.deletePatientByID(patientId);
        if(patient!=null){
            return true;
        }
        return false;
    }

    @PutMapping("/UpdatePatientById/{patientId}")
    public Patient updatePatientById(@PathVariable(name = "patientId") long patientId, @RequestBody Patient patient) throws Exception {

        Patient oldPatient=patientService.findPatientById(patientId);

        if(oldPatient==null) throw  new Exception("patient not found");

        return patientService.updatePatientById(oldPatient,patient);
    }


    @GetMapping("/updatePatientForm/{patientId}")
    public String updatePatientForm(@PathVariable("patientId") Long patientId, Model model) {
        Patient patient = patientService.findPatientById(patientId);
        model.addAttribute("patient", patient);
        return "updatePatientForm";
    }

    @GetMapping("/search")
    public Object searchPatients(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email) {
        if (id != null) {
            return patientService.findPatientById(id);
        } else if (email != null) {
            return patientService.findPatientByUserEmail(email);
        } else {
            return patientService.findAllPatients();
        }
    }


    @GetMapping("/patient_details/{userId}")
    public String findAllDoctors(Model model, @PathVariable Long userId)  {
        Users user= userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Patient> patients = patientService.findAllPatients();
        model.addAttribute("patients", patients);
        model.addAttribute("navbar", "navbarAdmin");
        return "PatientDetails";
    }


}
