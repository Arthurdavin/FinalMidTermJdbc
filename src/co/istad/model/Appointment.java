package co.istad.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private Integer appointmentId;
    private Integer doctorId;
    private String patientName;
    private String patientGender;
    private String patientPhone;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private boolean isDeleted;

    // Getters & Setters
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientGender() { return patientGender; }
    public void setPatientGender(String patientGender) { this.patientGender = patientGender; }

    public String getPatientPhone() { return patientPhone; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    @Override
    public String toString() {
        return String.format("%-5d %-8d %-20s %-10s %-15s %s %s",
                appointmentId, doctorId, patientName, patientGender, patientPhone,
                appointmentDate, appointmentTime);
    }
}