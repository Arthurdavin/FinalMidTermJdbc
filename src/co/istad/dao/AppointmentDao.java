package co.istad.dao;

import co.istad.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {
    void insert(Appointment appointment);
    Optional<Appointment> findById(Integer appointmentId);
    void update(Appointment appointment);
    void softDelete(Integer appointmentId);
    List<Appointment> findAllPaginated(int page, int size);
    List<Appointment> searchByPatientPhone(String phone);
//    boolean existsByDoctorAndTime(int doctorId, LocalDate date, LocalTime time);
    List<Appointment> findByDoctorAndDate(int doctorId, LocalDate date);
}