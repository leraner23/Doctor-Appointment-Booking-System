package project.Appointment.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.Appointment.Cookie.UserIdGenerator;
import project.Appointment.DTOmapping.UserMapper;
import project.Appointment.dto.CreateUserDto;
import project.Appointment.dto.LoginDto;
import project.Appointment.entity.Appointment;
import project.Appointment.entity.Doctor;
import project.Appointment.entity.Patient;
import project.Appointment.entity.Users;
import project.Appointment.enums.Role;
import project.Appointment.repository.UserRepository;
import project.Appointment.service.AppointmentService;
import project.Appointment.service.DoctorService;
import project.Appointment.service.PatientService;
import project.Appointment.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

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

    @Autowired
    private UserIdGenerator sessionManager;



    @GetMapping("/PatientDashBoard")
    public String getPatientDashboardPage(Model model, HttpServletRequest req) {
        Users user = sessionManager.getAuthenticatedUser(req);
        System.out.println(user);
        Patient patient = patientService.getPatientByUser(user);
        List<Appointment> appointments = appointmentService.findAppointmentsByPatient(patient);
        model.addAttribute("appointments", appointments);
        model.addAttribute("user", user);
        model.addAttribute("patient", patient);
        return "PatientDashBoard";
    }

    @GetMapping("/DoctorDashboard")
    public String getDoctorDashboardPage(HttpServletRequest req, Model model) {

        Users user = sessionManager.getAuthenticatedUser(req);
        Doctor doctor = doctorService.getDoctorByUser(user);

        List<Appointment> appointment = appointmentService.findAppointmentsByDoctor(doctor);
        long totalAppointments = doctorService.countTotalDoctorAppointment(doctor.getDoctorId());
        long totalTodayAppointments = doctorService.countTodayTotal();
        long totalTodayApprovedAppointments = doctorService.countTodayTotalApproved(doctor.getDoctorId());
        long totalAppointmentsDone = doctorService.countTotalAppointmentDone(doctor.getDoctorId());
        model.addAttribute("user", user);
        model.addAttribute("Doctornavbar", "DoctorNavbar");
        model.addAttribute("appointment",appointment);
        model.addAttribute("totalAppointments",totalAppointments);
        model.addAttribute("totalTodayAppointments", totalTodayAppointments);
        model.addAttribute("totalTodayApprovedAppointments", totalTodayApprovedAppointments);
        model.addAttribute("totalAppointmentsDone", totalAppointmentsDone);
        return "DoctorDashboard";
    }


    @GetMapping("/AdminDashBoard")
    public String getAdmindashBoardPage(HttpServletRequest req,Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        List<Doctor> doctor = doctorService.findAllDoctors();
        List<Patient> patient = patientService.findAllPatients();
        Users user = sessionManager.getAuthenticatedUser(req);
        long totalPatients = patientService.countTotalPatients();
        long totalDoctors = doctorService.countTotalDoctor();
        long totalAppointments = appointmentService.countTotal();
        Long totalTodayAppointments = appointmentService.countTodayTotal();
        model.addAttribute("totalPatients",totalPatients);
        model.addAttribute("totalDoctors",totalDoctors);
        model.addAttribute("totalAppointments",totalAppointments);
        model.addAttribute("totalPatients",totalPatients);
        model.addAttribute("totalTodayAppointments", totalTodayAppointments);
        model.addAttribute("doctor",doctor);
        model.addAttribute("patient", patient);
        model.addAttribute("user", user);
        model.addAttribute("navbar", "navbarAdmin");
        return "AdminDashBoard";
    }


    @GetMapping("/create_user")
    public String showCreateUserForm(Model model) {
        Boolean isAdminExists = userService.isRoleExists(Role.ADMIN);
        model.addAttribute("isAdminExists",isAdminExists);
        model.addAttribute("user", new Users());
        model.addAttribute("roles", Role.values());
        return "UserRegistration";
    }

    @PostMapping("/create_user")
    public String createNewUser(@ModelAttribute CreateUserDto dto, Model model) {
        try {
            System.out.println(dto.getFullName());
            Users newUser = userService.createNewUser(dto);
            System.out.println(newUser);
            if (newUser.getRole() == (Role.DOCTOR)) {
                System.out.println(newUser.getRole());
                Doctor doctor = doctorService.createNewDoctor(newUser);
                System.out.println(doctor);
                if (doctor == null) throw new Exception("No doctor to add");
            } else if (newUser.getRole() == (Role.PATIENTS)) {
                Patient patient = patientService.createNewPatient(newUser);
                if (patient == null) throw new Exception("No patient to add");
            }
            model.addAttribute("successMessage", "User created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error creating user: " + e.getMessage());
        }
        return "redirect:/";
    }





    @GetMapping("/findUserById/{userId}")
    public String findUserById(@PathVariable(name = "userId") Long userId, Model model) {
        Users user = userService.findUserById(userId);
        model.addAttribute("user", userMapper.mapToDto(user));
        return "user-details";
    }

    @GetMapping("/findUserByEmail/{email}")
    public String findUserByEmail(@PathVariable(name = "email") String email, Model model) {
        Users user = userService.findUserByEmail(email);
        model.addAttribute("user", userMapper.mapToDto(user));
        return "user-details";
    }

    @GetMapping("/findAllUsers")
    public String findAllUsers(Model model) {
        List<Users> users = userService.findAllUsers();
        model.addAttribute("users", userMapper.mapToUserDtos(users));
        return "user-list";
    }

    @DeleteMapping("/deleteUserById/{userId}")
    public String deleteUserById(@PathVariable(name = "userId") long userId, Model model) {
        try {
            userService.deleteUserByID(userId);
            model.addAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "user-list";
    }

    @GetMapping("/updateUserForm/{userId}")
    public String showUpdateUserForm(@PathVariable(name = "userId") long userId, Model model) {
        Users user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "update-user";
    }

    @PostMapping("/updateUserById/{userId}")
    public String updateUserById(@PathVariable(name = "userId") long userId, @ModelAttribute Users user, Model model) {
        try {
            Users oldUser = userService.findUserById(userId);
            if (oldUser == null) throw new Exception("User not found");

            Users updatedUser = userService.updateUserById(oldUser, user);
            model.addAttribute("successMessage", "User updated successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }
        return "update-user";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model,HttpServletRequest request) {
        UserIdGenerator cookieGenerated = new UserIdGenerator();
        String cookieIdByBrowser = cookieGenerated.getYourCookie(request);
        System.out.println("CookieId -> "+cookieIdByBrowser);
        if (cookieIdByBrowser != null) {
            Users userByCookie = userRepository.findByCookieId(cookieIdByBrowser);
            String cookieIdByDb = userByCookie.getCookieId();
            System.out.println("databaseCookie" + cookieIdByDb);
            if (cookieIdByDb != null) {
                return redirectBasedOnRole(userByCookie);
            }
        }
        model.addAttribute("loginRequest", new LoginDto());
        return "Login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginRequest, Model model, HttpServletRequest request, HttpServletResponse response) {
        try {
            String cookieIdByBrowser = sessionManager.getYourCookie(request);
            System.out.println("CookieId -> "+cookieIdByBrowser);
            if (cookieIdByBrowser != null) {
                Users userByCookie = userRepository.findByCookieId(cookieIdByBrowser);
                String cookieIdByDb = userByCookie.getCookieId();
                System.out.println("databaseCookie" + cookieIdByDb);
                if (cookieIdByDb != null) {
                    return redirectBasedOnRole(userByCookie);
                }
            }

            Users user = userService.authenticate(loginRequest);
            if (user == null) {
                throw new RuntimeException("Invalid credentials");
            }
            String newId = sessionManager.setYourCookie(response);
            user.setCookieId(newId);
            System.out.println(newId);
            userRepository.save(user);
            return redirectBasedOnRole(user);

        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "Login";
        }
    }
    private String redirectBasedOnRole(Users user) {
        System.out.println(user.getRole());
        if (user.getRole() == Role.ADMIN) {
            return "redirect:/user/AdminDashBoard";
        } else if (user.getRole() == Role.DOCTOR) {
            return "redirect:/user/DoctorDashboard";
        } else if (user.getRole() == Role.PATIENTS){
            return "redirect:/user/PatientDashBoard";
        }else {
            return "redirect:/";
        }
        }


        @GetMapping("/logout")
    public  String   LogOut(Model model,HttpServletResponse response, HttpServletRequest request){
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie cookie: cookies){
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
            return "redirect:/";

        }






    }

