package co.istad.service;

import co.istad.dao.DoctorDao;
import co.istad.dao.DoctorDaoImpl;
import co.istad.model.Doctor;

import java.util.List;
import java.util.Optional;

public class DoctorService {

    private final DoctorDao dao = new DoctorDaoImpl();
    // ────────────────────────────────────────────────
    // CREATE
    // ────────────────────────────────────────────────

    public void create(Doctor doctor) {
        int rows = dao.insert(doctor);
        if (rows == 0) {
            System.out.println("⚠️ Doctor NOT created (possibly duplicate email).");
        }
    }

    // ────────────────────────────────────────────────
    // Update
    // ────────────────────────────────────────────────

    public boolean update(Doctor doctor) {

        if (doctor.getDoctorId() == null) return false;

        if (dao.findById(doctor.getDoctorId()).isEmpty()) return false;

        int rows = dao.update(doctor.getDoctorId(), doctor);

        if (rows == 0) {
            System.out.println("⚠️ Update failed (possibly duplicate email).");
            return false;
        }

        return true;
    }

    // ────────────────────────────────────────────────
    // DELETE
    // ────────────────────────────────────────────────

    public void delete(Integer doctorId) {
        dao.softDelete(doctorId);
    }

    // ────────────────────────────────────────────────
    // READ
    // ────────────────────────────────────────────────

    public List<Doctor> listPaginated(int page, int size) {
        return dao.findAllPaginated(page, size);
    }

    public Optional<Doctor> findById(int id) {
        return dao.findById(id);
    }

    // ────────────────────────────────────────────────
    // SEARCH
    // ────────────────────────────────────────────────

    public List<Doctor> search(String keyword) {
        return dao.searchByNameOrSpecialization(keyword);
    }

    // ────────────────────────────────────────────────
    // FIND BY EMAIL (NEW)
    // ────────────────────────────────────────────────

    public Doctor findByEmail(String email) {
        return dao.findByEmail(email); // Make sure this method exists in your DAO
    }

}
