package project.Appointment.entity;

import jakarta.persistence.*;

@Entity
public class Patient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long patientId;


    @OneToOne
    @JoinColumn(name= "user_id", referencedColumnName = "userId")
    private Users user;

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }



}
