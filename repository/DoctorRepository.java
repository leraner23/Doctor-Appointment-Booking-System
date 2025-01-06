package project.Appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.Appointment.entity.Doctor;
import project.Appointment.entity.Users;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {


    Doctor findByUserEmail(String email);

    Optional<Doctor> findByUser_UserId(long userId);

    Doctor findByUser(Users user);




//    long countByDateEquals(LocalDate now);
//
//    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = 'APPROVED' AND a.date = CURRENT_DATE")
//    long countTodayTotalApproved();
//
//    @Query("SELECT COUNT(b)  FROM Appointment b WHERE b.status = 'COMPLETED'")
//    long countTotalAppointmentDone();
}
