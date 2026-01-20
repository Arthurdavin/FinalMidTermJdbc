package co.istad.dao;

import co.istad.model.Doctor;

import java.util.List;

public interface DoctorDao {
    List<Doctor> findAllPaginated(int page, int size);
    int countActiveDoctors();
    void insert(Doctor doctor);
    void update(Doctor doctor);
    void softDelete(Integer doctorId);
    List<Doctor> searchByNameOrSpecialization(String keyword);
}