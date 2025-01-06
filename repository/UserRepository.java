package project.Appointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.Appointment.entity.Users;
import project.Appointment.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Users findUserByEmail(String email);
    Users findByEmailAndPassword(String email, String password);

    Users findByCookieId(String cookieID);

    boolean existsByCookieId(String id);

    @Query("SELECT COUNT(u) FROM Users u WHERE u.role = 'DOCTOR'")
    long countTotalDoctor();

    @Query("SELECT COUNT(u) FROM Users u WHERE u.role = 'PATIENTS'")
    long countTotalPatient();

    Boolean existsByRole(Role role);
}
