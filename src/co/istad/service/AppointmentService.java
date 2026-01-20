package co.istad.service;

import co.istad.dao.AppointmentDao;
import co.istad.dao.AppointmentDaoImpl;
import co.istad.model.Appointment;
import co.istad.util.TelegramNotifier;

import java.util.List;

public class AppointmentService {

    private final AppointmentDao dao = new AppointmentDaoImpl();

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

    public void update(Appointment appointment) {
        dao.update(appointment);
        String msg = """
                ✏️ Appointment Updated
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

    public void delete(Integer appointmentId) {
        dao.softDelete(appointmentId);
    }

    public List<Appointment> listPaginated(int page, int size) {
        return dao.findAllPaginated(page, size);
    }

    public int getTotalCount() {
        return dao.countActiveAppointments();
    }

    public List<Appointment> searchByPhone(String phone) {
        return dao.searchByPatientPhone(phone);
    }
}