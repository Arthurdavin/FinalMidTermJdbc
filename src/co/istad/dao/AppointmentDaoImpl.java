package co.istad.dao;

import co.istad.config.DbConfig;
import co.istad.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDaoImpl implements AppointmentDao {
    private final Connection conn;
    public AppointmentDaoImpl(){
        conn =DbConfig.getInstance();
    }
    @Override
    public void insert(Appointment app) {
        String sql = """
            INSERT INTO appointments
            (doctor_id, patient_name, patient_gender, patient_phone, appointment_date, appointment_time)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING appointment_id
            """;
        try (Connection conn = DbConfig.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, app.getDoctorId());
            ps.setString(2, app.getPatientName());
            ps.setString(3, app.getPatientGender());
            ps.setString(4, app.getPatientPhone());
            ps.setDate(5, Date.valueOf(app.getAppointmentDate()));
            ps.setTime(6, Time.valueOf(app.getAppointmentTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) app.setAppointmentId(rs.getInt("appointment_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Appointment app) {
        String sql = """
            UPDATE appointments SET
            doctor_id = ?, patient_name = ?, patient_gender = ?,
            patient_phone = ?, appointment_date = ?, appointment_time = ?,
            updated_at = CURRENT_TIMESTAMP
            WHERE appointment_id = ? AND is_deleted = false
            """;
        try (Connection conn = DbConfig.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, app.getDoctorId());
            ps.setString(2, app.getPatientName());
            ps.setString(3, app.getPatientGender());
            ps.setString(4, app.getPatientPhone());
            ps.setDate(5, Date.valueOf(app.getAppointmentDate()));
            ps.setTime(6, Time.valueOf(app.getAppointmentTime()));
            ps.setInt(7, app.getAppointmentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void softDelete(Integer appointmentId) {
        String sql = "UPDATE appointments SET is_deleted = true, updated_at = CURRENT_TIMESTAMP WHERE appointment_id = ?";
        try (Connection conn = DbConfig.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Appointment> findAllPaginated(int page, int size) {
        List<Appointment> list = new ArrayList<>();
        String sql = """
        SELECT * FROM appointments
        WHERE is_deleted = false
        ORDER BY appointment_date DESC, appointment_time DESC
        LIMIT ? OFFSET ?
        """;


        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size); // ✅ FIX pagination

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAppointment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public int countActiveAppointments() {
        String sql = "SELECT COUNT(*) FROM appointments WHERE is_deleted = false";
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
    public List<Appointment> searchByPatientPhone(String phone) {
        List<Appointment> list = new ArrayList<>();
        String sql = """
            SELECT * FROM appointments
            WHERE is_deleted = false AND patient_phone ILIKE ?
            ORDER BY appointment_date DESC, appointment_time DESC
            """;
        try (Connection conn = DbConfig.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + phone + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapAppointment(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setPatientName(rs.getString("patient_name"));
        a.setPatientGender(rs.getString("patient_gender"));
        a.setPatientPhone(rs.getString("patient_phone"));
        a.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        a.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
        a.setDeleted(rs.getBoolean("is_deleted"));
        return a;
    }
}