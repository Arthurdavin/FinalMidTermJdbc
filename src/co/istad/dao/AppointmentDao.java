package co.istad.dao;

import co.istad.model.Appointment;

import java.util.List;

public interface AppointmentDao {
    void insert(Appointment appointment);
    void update(Appointment appointment);
    void softDelete(Integer appointmentId);
    List<Appointment> findAllPaginated(int page, int size);
    int countActiveAppointments();
    List<Appointment> searchByPatientPhone(String phone);
}