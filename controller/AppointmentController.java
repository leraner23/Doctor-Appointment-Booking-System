package project.Appointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.Appointment.entity.*;
import project.Appointment.enums.Status;
import project.Appointment.repository.AppointmentRepository;
import project.Appointment.service.*;


import javax.print.Doc;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {


    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;


    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;


//    @GetMapping("/appointments")
//    public String findAllAppointments(Model model){
//        List<Appointment> appointments = appointmentService.getAllAppointments();
//
//        if(appointments == null) {
//            System.out.println("appointments is NULL");
//        }
//
//        model.addAttribute("appointments", appointments);
//        return "dashboard";
//    }

    @GetMapping("/appointment_details/{userId}")
    public String showAppointment(Model model, @PathVariable Long userId) {
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Appointment> todaysAppointments = appointmentService.getTodaysAppointments();
        model.addAttribute("todaysAppointments", todaysAppointments);
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        model.addAttribute("navbar", "navbarAdmin");
        return "AppointmentDetails";
    }


    @GetMapping("/todays_appointments/{userId}")
    public String findAllTodayAppointment(Model model, @PathVariable Long userId) {
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Appointment> todaysAppointments = appointmentService.getTodaysAppointments();
        model.addAttribute("todaysAppointments", todaysAppointments);
        model.addAttribute("navbar", "navbarAdmin");
        return "todayAppointment";
    }

    @GetMapping("/past_appointments/{userId}")
    public String findAllPastAppointment(Model model, @PathVariable Long userId) {
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        List<Appointment> pastsAppointments = appointmentService.getPastAppointments();
        model.addAttribute("pastsAppointments", pastsAppointments);
        model.addAttribute("navbar", "navbarAdmin");
        return "PastAppointments";
    }


    @GetMapping("/doctorAppointment_details/{userId}")
    public String showDoctorAppointment(Model model, @PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        Users user = userService.findUserById(userId);

        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        List<Appointment> DoctorAppointments = appointmentService.findAppointmentsByDoctor(doctor);
        model.addAttribute("DoctorAppointments", DoctorAppointments);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "DoctorSeeAppointment";
    }

    @GetMapping("/pastDoctorAppointment_details/{userId}")
    public String showPastDoctorAppointment(Model model, @PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        List<Appointment> DoctorPastAppointments = appointmentService.findPastAppointmentsByDoctorId(doctor.getDoctorId());
        model.addAttribute("DoctorPastAppointments", DoctorPastAppointments);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "DoctorPastAppointments";
    }

    @GetMapping("/todayDoctor_appointments/{userId}")
    public String findTodayAppointmentDoctor(Model model, @PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("doctor", doctor);
        List<Appointment> todayDoctorAppointments = appointmentService.findTodaysDoctorAppointments(doctor.getDoctorId());
        model.addAttribute("todayDoctorAppointments", todayDoctorAppointments);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        return "DoctorTodayAppointments";
    }

    @GetMapping("/check")
    public String checkAppointments(Model model) {
        List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments();
        List<Appointment> pastAppointments = appointmentService.getPastAppointments();
        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("pastAppointments", pastAppointments);
        return "patient_dashboard";
    }

    @GetMapping("/Book/{userId}")
    public String bookyourappointment(Model model, @PathVariable Long userId) {
        List<Doctor> doctors = doctorService.findAllDoctors();
        Users user = userService.findUserById(userId);
        Patient patient = patientService.getPatientByUser(user);
        model.addAttribute("doctors", doctors);
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patient", patient);
        model.addAttribute("userId", userId);
        return "BookAppointment";
    }

    @PostMapping("/Book/{userId}")
    public String showYourBooking(@ModelAttribute Appointment appointment, Model model,
                                  @PathVariable Long userId, @RequestParam Long doctorId,
                                  @RequestParam String date) {
        appointment.setStatus(Status.PENDING);
        try {
            Users user = userService.findUserById(userId);
            appointment.setUser(user);
            Patient patient = patientService.getPatientByUser(user);
            appointment.setPatient(patient);
//           Long doctorId = appointment.getDoctor().getDoctorId();  // Assuming the doctor is selected in the form
            Doctor doctor = doctorService.findByDoctorId(doctorId);  // Correct method to fetch doctor
            appointment.setDoctor(doctor);
            LocalDate Date = LocalDate.parse(date);
            model.addAttribute("Date", Date);
            appointment.setDate(Date);
            model.addAttribute("doctorId", doctorId);
            model.addAttribute("doctor", doctor);
            Appointment newAppointment = appointmentService.createnewappointment(appointment);
            model.addAttribute("Appointment created");
//            return "redirect:/appointment/bookTime/" + userId + "?doctorId=" + doctorId + "&date=" + Date;
            return "redirect:/user/PatientDashBoard";


        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error creating appointment: " + e.getMessage());
            return "BookAppointment";
        }
    }


    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment == null) {
            redirectAttributes.addFlashAttribute("error", "No appointment found with the provided ID.");
            return "redirect:/user/PatientDashBoard";
        }
        if (appointment.getStatus() == Status.CANCELED) {
            redirectAttributes.addFlashAttribute("error", "This appointment has already been canceled.");
            return "redirect:/user/PatientDashBoard";
        }
        appointment.setStatus(Status.CANCELED);
        appointmentRepository.save(appointment);
        redirectAttributes.addFlashAttribute("message", "Appointment canceled successfully!");
        return "redirect:/user/PatientDashBoard";
    }


    @PostMapping("/delete")
    public String deleteAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment == null) {
            redirectAttributes.addFlashAttribute("error", "No appointment found with the provided ID.");
            return "redirect:/user/PatientDashBoard";
        }
        appointmentService.deleteById(appointment_id);
        redirectAttributes.addFlashAttribute("message", "Appointment approved successfully!");
        return "redirect:/user/PatientDashBoard";
    }

    @PostMapping("/accept/{userId}")
    public String acceptAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes, @PathVariable("userId") Long userId) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment == null) {
            redirectAttributes.addFlashAttribute("message", "No appointment found with the provided ID.");
            return "redirect:/user/DoctorDashboard";
        }
        appointment.setStatus(Status.APPROVED);
        appointmentRepository.save(appointment);
        return "redirect:/appointment/doctorAppointment_details/{userId}";
    }

    @PostMapping("/reject/{userId}")
    public String rejectAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes, @PathVariable("userId") Long userId) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment == null) {
            redirectAttributes.addFlashAttribute("error", "No appointment found with the provided ID.");
            return "redirect:/user/DoctorDashboard";
        }
        appointment.setStatus(Status.REJECTED);
        appointmentRepository.save(appointment);

        return "redirect:/appointment/doctorAppointment_details/{userId}";
    }

    @PostMapping("/complete/{userId}")
    public String completeAppointment(@RequestParam("appointment_id") long appointment_id, RedirectAttributes redirectAttributes, @PathVariable("userId") Long userId) {
        Appointment appointment = appointmentService.findAppointmentByID(appointment_id);
        if (appointment.getStatus() == Status.PENDING) {
            redirectAttributes.addFlashAttribute("error", "No Pending appointment can be completed before approving.");
            return "redirect:/user/DoctorDashboard";
        }
        appointment.setStatus(Status.COMPLETED);
        appointmentRepository.save(appointment);

        return "redirect:/appointment/doctorAppointment_details/{userId}";
    }


//    private Long reviewingAppointmentId = null;
//    @PostMapping("/appointment/start-review")
//    public String startReview(@RequestParam("appointment_id") Long appointmentId, Model model) {
//        reviewingAppointmentId = appointmentId;
//
//        model.addAttribute("reviewingAppointmentId", reviewingAppointmentId);
//
//        return "redirect:/user/PatientDashBoard";
//    }
//
//    @PostMapping("/appointment/review")
//    public String submitReview(@RequestParam("appointment_id") Long appointmentId,
//                               @RequestParam("reviewText") String reviewText) {
//        Appointment appointment = appointmentService.findAppointmentByID(appointmentId);
//        appointment.setReview(reviewText);
//        appointmentRepository.save(appointment);
//        reviewingAppointmentId = null;
//        return "redirect:/user/PatientDashBoard";
//    }


    @GetMapping("/appointments/today")
    public String getTodaysAppointments(Model model) {
        List<Appointment> todaysAppointments = appointmentService.getTodaysAppointments();
        model.addAttribute("appointments", todaysAppointments);
        return "AppointmentDetails";
    }
}

//    @GetMapping("/bookTime/{userId}")
//    public String showAppointmentForm(@PathVariable Long userId, Model model,
//                                      @RequestParam Long doctorId,
//                                      @RequestParam String date) {
//
//            Users user = userService.findUserById(userId);
//            LocalDate Date = LocalDate.parse(date);
//            Doctor doctor = doctorService.findByDoctorId(doctorId);
//
//            model.addAttribute("user", user);
//            model.addAttribute("doctor", doctor);
//            model.addAttribute("Date", Date);
//
//                return  "BookingTimeForm";
//
//    }
//
//    @PostMapping("/bookTime/{userId}")
//    public String getrAppointmentForm(@PathVariable Long userId, Model model,
//                                      @RequestParam Long doctorId,
//                                      @RequestParam String date ) {
//
//        Doctor doctor = doctorService.findByDoctorId(doctorId);
//        if (doctor == null) {
//            throw new RuntimeException("Doctor not found for ID: " + doctorId);
//        }
//
//        Appointment appointment = new Appointment();
//
//        appointment.setBookedTime(timeSlotId);
//
//        // Save the appointment (assuming you have an AppointmentService)
//        appointmentService.save(appointment);
//
//        // Add appointment to model for confirmation or further use
//        model.addAttribute("appointment", appointment);
//        return  "BookingTimeForm";
//
//    }
//}


