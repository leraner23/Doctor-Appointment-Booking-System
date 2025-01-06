package project.Appointment.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.Appointment.entity.Patient;
import project.Appointment.entity.Users;
import project.Appointment.repository.PatientRepository;
import project.Appointment.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    public Patient createNewPatient(Users user) throws Exception {
        if(user == null) throw new Exception("NO Patient available to create");
        Patient patient = new Patient();
        patient.setUser(user);
        return patientRepository.save(patient);
    }

    public Patient findPatientById(Long patientId) {
       return patientRepository.findById(patientId).orElse(null);
    }


    public List<Patient> findAllPatients() {
       return patientRepository.findAll();
    }

    public Patient findPatientByUserEmail(String email) {
        return patientRepository.findByUserEmail(email);
    }

    public Patient deletePatientByID(long patientId) throws Exception {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        if(patient == null) throw new Exception("Patient not found to delete by this Id");
        patientRepository.deleteById(patientId);
    return patient;
    }

    public Patient updatePatientById(Patient oldPatient, Patient patient) {
        return patientRepository.save(oldPatient);
    }

    public Patient getPatientByUser(Users user) {
        return patientRepository.getPatientByUser(user);
    }

    public long countTotalPatients() {
      return  userRepository.countTotalPatient();
    }
}
