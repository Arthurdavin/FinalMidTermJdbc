package co.istad.dao;

import co.istad.model.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorDao {
    List<Doctor> findAllPaginated(int page, int size);
    int insert(Doctor doctor);
    int update(int id,Doctor doctor);
    Optional<Doctor> findById(int id);
    void softDelete(Integer doctorId);
    List<Doctor> searchByNameOrSpecialization(String keyword);
    Doctor findByEmail(String email);
}