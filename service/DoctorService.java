package project.Appointment.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.Appointment.entity.Doctor;
import project.Appointment.entity.Users;
import project.Appointment.repository.AppointmentRepository;
import project.Appointment.repository.DoctorRepository;
import project.Appointment.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;
@Autowired
private UserRepository userRepository;

    public Doctor createNewDoctor(Users user) throws Exception {
        if(user==null) throw new Exception("Empty doctor");
        Doctor doctor=new Doctor();
        doctor.setUser(user);
        return doctorRepository.save(doctor);
    }

    public Doctor findByDoctorId(Long doctorId) {
       return doctorRepository.findById(doctorId).orElse(null);

    }

    public Doctor deleteDoctorByID(long doctorId) throws Exception {
        Doctor doctor=findByDoctorId(doctorId);
        if(doctor!=null){
             doctorRepository.deleteById(doctorId);
             return doctor;
        }
      throw new Exception("doctor not found");
    }

    public Doctor updateDoctorById(Doctor oldDoctor, Doctor doctor) {


        oldDoctor.setExperience(doctor.getExperience());
        oldDoctor.setQualification(doctor.getQualification());
        oldDoctor.setSpecialization(doctor.getSpecialization());
        return doctorRepository.save(oldDoctor);
    }

    public List<Doctor> findAllDoctors() {
       return doctorRepository.findAll();
    }

    public Doctor findByUserEmail(String email) {
        return doctorRepository.findByUserEmail(email);
    }


    public Doctor findByUser(Users user) {
        return doctorRepository.findByUser(user);   }

    public Doctor getDoctorByUserId(Long userId) {
        return doctorRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found for userId: " + userId));
    }



    public void updateDoctorProfile(Long userId, Doctor updatedDoctor) {
        Optional<Users> user=userRepository.findById(userId);
        Doctor existingDoctor = getDoctorByUser(user.get());

        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setExperience(updatedDoctor.getExperience());
        existingDoctor.setQualification(updatedDoctor.getQualification());
        doctorRepository.save(existingDoctor);
    }

    public Doctor getDoctorByUser(Users user) {
        return  doctorRepository.findByUser(user);
    }


    public long countTotalDoctorAppointment(Long docterId) {
        return appointmentRepository.countDoctorAppointment(docterId);
    }

    public Long countTodayTotal() {
        return appointmentRepository.countByDateEquals(LocalDate.now());
    }

    public long countTodayTotalApproved(Long docterId) {
        return appointmentRepository.countTodayTotalApprovedDoctor(docterId);
    }

    public long countTotalAppointmentDone(Long docterId) {
        return appointmentRepository.countTotalAppointmentCompletedDoctor(docterId);
    }

    public long countTotalDoctor() {
        return userRepository.countTotalDoctor();
    }
}
