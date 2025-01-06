package project.Appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.Appointment.entity.Appointment;
import project.Appointment.entity.Doctor;
import project.Appointment.entity.Patient;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    List<Appointment> findByDateAfter(LocalDate date);
    List<Appointment> findByDateBefore(LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.doctor =:doctor")
    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByPatient(Patient patient);


    List<Appointment> findAppointmentByDate(LocalDate now);

    Long countByDateEquals(LocalDate today);


    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = 'APPROVED' AND a.date = CURRENT_DATE AND a.doctor.id = :doctorId")
    long countTodayTotalApprovedDoctor(@Param("doctorId") Long doctorId);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = 'COMPLETED'")
    long countTotalAppointmentCompletedDoctor(Long doctorId);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId")
    long countDoctorAppointment(Long doctorId);

    List<Appointment> findDoctorAppointmentByDate(LocalDate now);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date < CURRENT_DATE")
    List<Appointment> findByPastDoctorAppointment(@Param("doctorId") Long doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.date = CURRENT_DATE")
    List<Appointment> findByTodayDoctorAppointment(Long doctorId);


}
