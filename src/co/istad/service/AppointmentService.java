package co.istad.service;

import co.istad.dao.AppointmentDao;
import co.istad.dao.AppointmentDaoImpl;
import co.istad.model.Appointment;
import co.istad.model.Doctor;
import co.istad.util.TelegramNotifier;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentService {

    private final AppointmentDao dao = new AppointmentDaoImpl();

//    private final DoctorService doctorService = new DoctorService();

    public boolean existsByDoctorAndTime(
            int doctorId,
            LocalDate date,
            LocalTime newStart,
            int newDuration
    ) {
        List<Appointment> list = dao.findByDoctorAndDate(doctorId, date);

        LocalTime newEnd = newStart.plusMinutes(newDuration);

        for (Appointment a : list) {
            LocalTime existStart = a.getAppointmentTime();
            LocalTime existEnd   = existStart.plusMinutes(a.getDurationMinutes());

            // 🚨 OVERLAP RULE
            if (newStart.isBefore(existEnd) && newEnd.isAfter(existStart)) {
                return true;
            }
        }
        return false;
    }

    public Optional<Appointment> findById(int id) {
        return dao.findById(id);
    }

    public void create(Appointment appointment) {
        dao.insert(appointment);
        String msg = """
                🆕 New Appointment
                Patient : %s (%s)
                Phone   : %s
                Date    : %s
                Time    : %s
                Doctor ID : %d
                """.formatted(
                appointment.getPatientName(),
                appointment.getPatientGender(),
                appointment.getPatientPhone(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getDoctorId()
        );
        TelegramNotifier.send(msg);
    }

    public void update(Appointment input) {
        Appointment existing = dao.findById(input.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (input.getDoctorId() != null && input.getDoctorId() != 0)
            existing.setDoctorId(input.getDoctorId());
        if (input.getPatientName() != null && !input.getPatientName().isBlank())
            existing.setPatientName(input.getPatientName());
        if (input.getPatientGender() != null && !input.getPatientGender().isBlank())
            existing.setPatientGender(input.getPatientGender());
        if (input.getPatientPhone() != null && !input.getPatientPhone().isBlank())
            existing.setPatientPhone(input.getPatientPhone());
        if (input.getAppointmentDate() != null)
            existing.setAppointmentDate(input.getAppointmentDate());
        if (input.getAppointmentTime() != null)
            existing.setAppointmentTime(input.getAppointmentTime());

        dao.update(existing);

        String msg = """
                ✏️ Appointment Updated
                Patient : %s (%s)
                Phone   : %s
                Date    : %s
                Time    : %s
                Doctor ID : %d
                """.formatted(
                existing.getPatientName(),
                existing.getPatientGender(),
                existing.getPatientPhone(),
                existing.getAppointmentDate(),
                existing.getAppointmentTime(),
                existing.getDoctorId()
        );

        TelegramNotifier.send(msg);
    }

    public void delete(Integer appointmentId) {
        dao.softDelete(appointmentId);
    }

    public List<Appointment> listPaginated(int page, int size) {
        return dao.findAllPaginated(page, size);
    }

    public List<Appointment> searchByPhone(String phone) {
        return dao.searchByPatientPhone(phone);
    }

    // ───────────────────────────────
    // Available time slots generation
    // ───────────────────────────────
//    public List<LocalTime> getAvailableTimeSlots(Doctor doctor, LocalDate date, int durationMinutes) {
//        List<LocalTime> slots = new ArrayList<>();
//
//        String[] parts = doctor.getWorkingHours().split("-");
//        LocalTime start = LocalTime.parse(parts[0]);
//        LocalTime end = LocalTime.parse(parts[1]);
//
//        for (LocalTime time = start; time.plusMinutes(durationMinutes).compareTo(end) <= 0; time = time.plusMinutes(30)) {
//            if (!existsByDoctorAndTime(doctor.getDoctorId(), date, time, durationMinutes)) {
//                slots.add(time);
//            }
//        }
//
//        return slots;
//    }

}