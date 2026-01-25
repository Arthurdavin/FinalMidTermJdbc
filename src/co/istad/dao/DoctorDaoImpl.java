package co.istad.dao;

import co.istad.config.DbConfig;
import co.istad.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDaoImpl implements DoctorDao {

    private final Connection conn;

    public DoctorDaoImpl() {
        conn = DbConfig.getInstance();
    }

    @Override
    public List<Doctor> findAllPaginated(int page, int size) {

        List<Doctor> list = new ArrayList<>();

        String sql = """
        SELECT * FROM doctors
        WHERE is_deleted = false
        ORDER BY doctor_id DESC
        LIMIT ? OFFSET ?
    """;

        int offset = (page - 1) * size;
        if (offset < 0) offset = 0;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, offset);

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
    public int insert(Doctor doctor) {
        try {
            final String SQL = """
                    INSERT INTO doctors
                    (full_name, specialization, phone, email, room_number, working_days, working_hours)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, doctor.getFullName());
            pstmt.setString(2, doctor.getSpecialization());
            pstmt.setString(3, doctor.getPhone());
            pstmt.setString(4, doctor.getEmail());
            pstmt.setString(5, doctor.getRoomNumber());
            pstmt.setString(6, doctor.getWorkingDays());
            pstmt.setString(7, doctor.getWorkingHours());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int update(int id, Doctor doctor) {

        final String SQL = """
                    UPDATE doctors SET
                        full_name = COALESCE(?, full_name),
                        specialization = COALESCE(?, specialization),
                        phone = COALESCE(?, phone),
                        email = COALESCE(?, email),
                        room_number = COALESCE(?, room_number),
                        working_days = COALESCE(?, working_days),
                        working_hours = COALESCE(?, working_hours),
                        updated_at = CURRENT_TIMESTAMP
                    WHERE doctor_id = ? AND is_deleted = FALSE
                """;

        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, doctor.getFullName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhone());
            stmt.setString(4, doctor.getEmail());
            stmt.setString(5, doctor.getRoomNumber());
            stmt.setString(6, doctor.getWorkingDays());
            stmt.setString(7, doctor.getWorkingHours());
            stmt.setInt(8, id);

            return stmt.executeUpdate(); // returns affected rows

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Doctor> findById(int id) {
        try {
            final String SQL = """
                    SELECT *
                    FROM doctors
                    WHERE doctor_id = ? AND is_deleted = FALSE
                    """;

            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctor.setFullName(rs.getString("full_name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setPhone(rs.getString("phone"));
                doctor.setEmail(rs.getString("email"));
                doctor.setRoomNumber(rs.getString("room_number"));
                doctor.setWorkingDays(rs.getString("working_days"));
                doctor.setWorkingHours(rs.getString("working_hours"));

                return Optional.of(doctor);
            }

            return Optional.empty();

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            throw new RuntimeException(e);
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

    @Override
    public Doctor findByEmail(String email) {
        String sql = "SELECT * FROM doctors WHERE email = ? AND is_deleted = false";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getInt("doctor_id"));
                d.setFullName(rs.getString("full_name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setPhone(rs.getString("phone"));
                d.setEmail(rs.getString("email"));
                d.setRoomNumber(rs.getString("room_number"));
                d.setWorkingDays(rs.getString("working_days"));
                d.setWorkingHours(rs.getString("working_hours"));
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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