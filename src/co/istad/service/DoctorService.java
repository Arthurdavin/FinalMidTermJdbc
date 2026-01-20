package co.istad.service;

import co.istad.dao.DoctorDao;
import co.istad.dao.DoctorDaoImpl;
import co.istad.model.Doctor;

import java.util.List;

public class DoctorService {

    private final DoctorDao dao = new DoctorDaoImpl();

    public List<Doctor> listPaginated(int page, int size) {
        return dao.findAllPaginated(page, size);
    }

    public int getTotalCount() {
        return dao.countActiveDoctors();
    }

    public void create(Doctor doctor) {
        dao.insert(doctor);
    }

    public void update(Doctor doctor) {
        dao.update(doctor);
    }

    public void delete(Integer doctorId) {
        dao.softDelete(doctorId);
    }

    public List<Doctor> search(String keyword) {
        return dao.searchByNameOrSpecialization(keyword);
    }
}