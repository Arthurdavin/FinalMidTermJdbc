package co.istad.dao;

import co.istad.config.DbConfig;
import co.istad.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDaoImpl implements DoctorDao {

    @Override
    public List<Doctor> findAllPaginated(int page, int size) {
        List<Doctor> list = new ArrayList<>();
        String sql = """
        SELECT * FROM doctors
        WHERE is_deleted = false
        ORDER BY doctor_id DESC
        LIMIT ? OFFSET ?
        """;

        Connection conn = DbConfig.getInstance();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapDoctor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public int countActiveDoctors() {
        String sql = "SELECT COUNT(*) FROM doctors WHERE is_deleted = false";
        try (Connection conn = DbConfig.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void insert(Doctor doctor) {
        String sql = """
            INSERT INTO doctors
            (full_name, specialization, phone, email, room_number, working_days, working_hours)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING doctor_id
            """;
        try (Connection conn = DbConfig.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getFullName());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getPhone());
            ps.setString(4, doctor.getEmail());
            ps.setString(5, doctor.getRoomNumber());
            ps.setString(6, doctor.getWorkingDays());
            ps.setString(7, doctor.getWorkingHours());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) doctor.setDoctorId(rs.getInt("doctor_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Doctor doctor) {
        String sql = """
            UPDATE doctors SET
            full_name = ?, specialization = ?, phone = ?, email = ?,
            room_number = ?, working_days = ?, working_hours = ?,
            updated_at = CURRENT_TIMESTAMP
            WHERE doctor_id = ? AND is_deleted = false
            """;
        try (Connection conn = DbConfig.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctor.getFullName());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getPhone());
            ps.setString(4, doctor.getEmail());
            ps.setString(5, doctor.getRoomNumber());
            ps.setString(6, doctor.getWorkingDays());
            ps.setString(7, doctor.getWorkingHours());
            ps.setInt(8, doctor.getDoctorId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void softDelete(Integer doctorId) {
        String sql = "UPDATE doctors SET is_deleted = true, updated_at = CURRENT_TIMESTAMP WHERE doctor_id = ?";
        try (Connection conn = DbConfig.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Doctor> searchByNameOrSpecialization(String keyword) {
        List<Doctor> list = new ArrayList<>();
        String sql = """
            SELECT * FROM doctors
            WHERE is_deleted = false
            AND (full_name ILIKE ? OR specialization ILIKE ?)
            ORDER BY doctor_id DESC
            """;
        try (Connection conn = DbConfig.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapDoctor(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Doctor mapDoctor(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setDoctorId(rs.getInt("doctor_id"));
        d.setFullName(rs.getString("full_name"));
        d.setSpecialization(rs.getString("specialization"));
        d.setPhone(rs.getString("phone"));
        d.setEmail(rs.getString("email"));
        d.setRoomNumber(rs.getString("room_number"));
        d.setWorkingDays(rs.getString("working_days"));
        d.setWorkingHours(rs.getString("working_hours"));
        d.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        Timestamp up = rs.getTimestamp("updated_at");
        d.setUpdatedAt(up != null ? up.toLocalDateTime() : null);
        d.setDeleted(rs.getBoolean("is_deleted"));
        return d;
    }
}