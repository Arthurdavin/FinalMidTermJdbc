package co.istad.dao;

import co.istad.config.DbConfig;
import co.istad.model.Appointment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDaoImpl implements AppointmentDao {

    private final Connection conn;

    public AppointmentDaoImpl() {
        conn = DbConfig.getInstance();
    }

    @Override
    public void insert(Appointment app) {
        String sql = """
                INSERT INTO appointments
                (doctor_id, patient_name, patient_gender, patient_phone, appointment_date, appointment_time, duration_minutes)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING appointment_id
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, app.getDoctorId());
            ps.setString(2, app.getPatientName());
            ps.setString(3, app.getPatientGender());
            ps.setString(4, app.getPatientPhone());
            ps.setDate(5, Date.valueOf(app.getAppointmentDate()));
            ps.setTime(6, Time.valueOf(app.getAppointmentTime()));
            ps.setInt(7, app.getDurationMinutes());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) app.setAppointmentId(rs.getInt("appointment_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Appointment> findById(Integer id) {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ? AND is_deleted = false";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void update(Appointment app) {
        String sql = """
                UPDATE appointments SET
                    doctor_id = ?,
                    patient_name = ?,
                    patient_gender = ?,
                    patient_phone = ?,
                    appointment_date = ?,
                    appointment_time = ?,
                    duration_minutes = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE appointment_id = ? AND is_deleted = false
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, app.getDoctorId());
            ps.setString(2, app.getPatientName());
            ps.setString(3, app.getPatientGender());
            ps.setString(4, app.getPatientPhone());
            ps.setDate(5, Date.valueOf(app.getAppointmentDate()));
            ps.setTime(6, Time.valueOf(app.getAppointmentTime()));
            ps.setInt(7, app.getDurationMinutes());
            ps.setInt(8, app.getAppointmentId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void softDelete(Integer appointmentId) {
        String sql = "UPDATE appointments SET is_deleted = true, updated_at = CURRENT_TIMESTAMP WHERE appointment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

        int offset = (page - 1) * size;
        if (offset < 0) offset = 0;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, size);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Appointment> searchByPatientPhone(String phone) {
        List<Appointment> list = new ArrayList<>();
        String sql = """
                SELECT * FROM appointments
                WHERE is_deleted = false
                AND patient_phone ILIKE ?
                ORDER BY appointment_date DESC, appointment_time DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + phone + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Appointment> findByDoctorAndDate(int doctorId, LocalDate date) {
        List<Appointment> list = new ArrayList<>();
        String sql = """
                SELECT * FROM appointments
                WHERE doctor_id = ? AND appointment_date = ? AND is_deleted = false
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, Date.valueOf(date));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment a = mapAppointment(rs);
                list.add(a);
            }
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
        a.setDurationMinutes(rs.getInt("duration_minutes")); // 🔴 important fix
        a.setDeleted(rs.getBoolean("is_deleted"));
        return a;
    }
}
