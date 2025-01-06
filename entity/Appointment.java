package project.Appointment.entity;


import jakarta.persistence.*;
import project.Appointment.enums.Status;
import java.time.LocalDate;


@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointment_id;

    @ManyToOne
    @JoinColumn(name = "doctor_id" , referencedColumnName = "doctorId")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id" , referencedColumnName = "patientId")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "userId")
    private Users user;

    private LocalDate date;
    private String bookedTime;

    private String problem;
    private String review;
    private String results;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    private  String description;

    public Long getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(Long appointment_id) {
        this.appointment_id = appointment_id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(String bookedTime) {
        this.bookedTime = bookedTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
