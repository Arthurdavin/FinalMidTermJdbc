package co.istad.view;

import co.istad.model.Appointment;
import co.istad.model.Doctor;
import co.istad.service.AppointmentService;
import co.istad.service.DoctorService;
import co.istad.util.InputUtil;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class View {


    public static void print(String text, boolean isNewLine) {
        if (isNewLine)
            System.out.println(text);
        else
            System.out.print(text);
    }

    public static void printHeader(String text) {
        Table table = new Table(1,
                BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.addCell(text);
        print(table.render(), true);
    }

    public static void printAppMenu() {
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(1, BorderStyle.UNICODE_DOUBLE_BOX);
        table.setColumnWidth(0, 30, 60);
        table.addCell("Clinic Management System", cellStyle);
        table.addCell("1. Doctors", cellStyle);
        table.addCell("2. Appointments", cellStyle);
        table.addCell("0. Exit", cellStyle);
        print(table.render(), true);
    }

    public static void printMenuAppointment() {
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(1, BorderStyle.UNICODE_DOUBLE_BOX);
        table.setColumnWidth(0, 50, 100);
        table.addCell("Appointment Menu", cellStyle);
        table.addCell("1. List appointments (paginated) 2. Create appointment  3. Update appointment", cellStyle);
        table.addCell("4. Soft delete appointment 5. Search by patient phone 0. Back", cellStyle);
        print(table.render(), true);
    }

    public static void printMenuDoctor() {
        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(1, BorderStyle.UNICODE_DOUBLE_BOX);
        table.setColumnWidth(0, 50, 100);
        table.addCell("Doctor Menu", cellStyle);
        table.addCell("1. List doctors (paginated) 2. Add new doctor  3. Update doctor", cellStyle);
        table.addCell("4. Soft delete doctor 5. Search doctor 0. Back", cellStyle);
        print(table.render(), true);
    }

    public static void printDoctorTable(List<Doctor> doctors) {

        CellStyle center = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(8, BorderStyle.UNICODE_DOUBLE_BOX);

        table.setColumnWidth(0, 5, 10);   // ID
        table.setColumnWidth(1, 20, 30);  // Name
        table.setColumnWidth(2, 15, 25);  // Specialization
        table.setColumnWidth(3, 12, 20);  // Phone
        table.setColumnWidth(4, 10, 15);  // Room
        table.setColumnWidth(5, 20, 30);  // WorkingDays
        table.setColumnWidth(6, 20, 30);  // Workhours
        table.setColumnWidth(7, 10, 15);  // Deleted

        // Header
        table.addCell("ID", center);
        table.addCell("Full Name", center);
        table.addCell("Specialization", center);
        table.addCell("Phone", center);
        table.addCell("Room", center);
        table.addCell("WorkingDays", center);
        table.addCell("Workhours", center);
        table.addCell("Deleted", center);

        // Data
        for (Doctor d : doctors) {
            table.addCell(String.valueOf(d.getDoctorId()), center);
            table.addCell(d.getFullName(), center);
            table.addCell(d.getSpecialization(), center);
            table.addCell(d.getPhone(), center);
            table.addCell(d.getRoomNumber(), center);
            table.addCell(String.valueOf(d.getWorkingDays()), center);
            table.addCell(String.valueOf(d.getWorkingHours()), center);
            table.addCell(String.valueOf(d.isDeleted()), center);
        }

        print(table.render(), true);
    }

    public static void printAppointmentTable(List<Appointment> apps) {

        CellStyle center = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(8, BorderStyle.UNICODE_DOUBLE_BOX);

        table.setColumnWidth(0, 5, 10);    // ID
        table.setColumnWidth(1, 5, 10);    // Doctor ID
        table.setColumnWidth(2, 20, 30);   // Patient Name
        table.setColumnWidth(3, 8, 12);    // Gender
        table.setColumnWidth(4, 12, 20);   // Phone
        table.setColumnWidth(5, 10, 15);   // Date
        table.setColumnWidth(6, 8, 12);    // Time
        table.setColumnWidth(7, 8, 12);    // IsDelete

        // Header
        table.addCell("ID", center);
        table.addCell("DrID", center);
        table.addCell("Patient Name", center);
        table.addCell("Gender", center);
        table.addCell("Phone", center);
        table.addCell("Date", center);
        table.addCell("Time", center);
        table.addCell("Delete", center);

        // Data
        for (Appointment a : apps) {
            table.addCell(String.valueOf(a.getAppointmentId()), center);
            table.addCell(String.valueOf(a.getDoctorId()), center);
            table.addCell(a.getPatientName(), center);
            table.addCell(a.getPatientGender(), center);
            table.addCell(a.getPatientPhone(), center);
            table.addCell(String.valueOf(a.getAppointmentDate()), center);
            table.addCell(String.valueOf(a.getAppointmentTime()), center);
            table.addCell(String.valueOf(a.isDeleted()), center);
        }

        print(table.render(), true);
    }

    private final DoctorService doctorService = new DoctorService();
    private final AppointmentService appointmentService = new AppointmentService();

    public void run() {
        while (true) {
            printAppMenu();
            int choice = InputUtil.readInt("Choose: ");

            switch (choice) {
                case 1 -> doctorMenu();
                case 2 -> appointmentMenu();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void doctorMenu() {
        while (true) {
            printMenuDoctor();
            int choice = InputUtil.readInt("Choose: ");

            switch (choice) {
                case 1 -> listDoctors();
                case 2 -> addDoctor();
                case 3 -> updateDoctor();
                case 4 -> deleteDoctor();
                case 5 -> searchDoctors();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listDoctors() {
        int page = 1;
        final int size = 5;

        while (true) {
            List<Doctor> doctors = doctorService.listPaginated(page, size);

            if (doctors.isEmpty()) {
                System.out.println("No doctors found.");
                if (page > 1) page--; // go back if over last page
                continue;
            }

            printHeader("Doctor List (Page " + page + ")");
            printDoctorTable(doctors);

            System.out.println("""
                    
                        [N] Next page
                        [P] Previous page
                        [B] Back to menu
                    """);

            String choice = InputUtil.readLine("Choose: ").toUpperCase();

            switch (choice) {
                case "N" -> page++;
                case "P" -> {
                    if (page > 1) page--;
                    else System.out.println("Already at first page.");
                }
                case "B" -> {
                    return; // Exit pagination loop
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addDoctor() {
        Doctor d = new Doctor();
        d.setFullName(InputUtil.readNonEmpty("Full name          : "));
        d.setSpecialization(InputUtil.readLine("Specialization     : "));
        d.setPhone(InputUtil.readLine("Phone              : "));
        d.setEmail(InputUtil.readEmail("Email              : "));
        d.setRoomNumber(InputUtil.readLine("Room number        : "));
        d.setWorkingDays(InputUtil.readLine("Working days       : "));
        d.setWorkingHours(InputUtil.readLine("Working hours      : "));

        doctorService.create(d);
        System.out.println("Doctor created successfully. ID = " + d.getDoctorId());
    }

    private void updateDoctor() {
        int id = InputUtil.readInt("Enter doctor ID to update: ");

        // Fetch current doctor
        Doctor existingDoctor = doctorService.findById(id).orElse(null);
        if (existingDoctor == null) {
            System.out.println("Doctor ID not found.");
            return;
        }

        System.out.println("Leave blank to keep current value");

        // Full Name
        String name = InputUtil.readLine("New full name: ");
        if (!name.isEmpty()) existingDoctor.setFullName(name);

        // Specialization
        String spec = InputUtil.readLine("New specialization: ");
        if (!spec.isEmpty()) existingDoctor.setSpecialization(spec);

        // Phone
        String phone = InputUtil.readLine("New phone: ");
        if (!phone.isEmpty()) existingDoctor.setPhone(phone);

        // Email with uniqueness check
        String email = InputUtil.readEmail("New email: ");
        if (!email.isEmpty()) {
            Doctor doctorByEmail = doctorService.findByEmail(email);
            if (doctorByEmail != null && !doctorByEmail.getDoctorId().equals(id)) {
                System.out.println("Email already exists. Update aborted.");
                return;
            }
            existingDoctor.setEmail(email);
        }

        // Room Number
        String room = InputUtil.readLine("New room: ");
        if (!room.isEmpty()) existingDoctor.setRoomNumber(room);

        // Working Days
        String days = InputUtil.readLine("New working days: ");
        if (!days.isEmpty()) existingDoctor.setWorkingDays(days);

        // Working Hours
        String hours = InputUtil.readLine("New working hours: ");
        if (!hours.isEmpty()) existingDoctor.setWorkingHours(hours);

        boolean updated = doctorService.update(existingDoctor);

        if (updated) {
            System.out.println("Doctor updated successfully.");
        } else {
            System.out.println("Update failed.");
        }
    }


    private void deleteDoctor() {
        int id = InputUtil.readInt("Enter doctor ID to delete: ");
        if (InputUtil.readConfirm("Confirm soft delete?")) {
            doctorService.delete(id);
            System.out.println("Doctor soft deleted.");
        }
    }

    private void searchDoctors() {
        String keyword = InputUtil.readLine("Search (name or specialization): ");
        List<Doctor> results = doctorService.search(keyword);

        if (results.isEmpty()) {
            System.out.println("No matching doctors found.");
            return;
        }

        printHeader("Doctor Search Results");
        printDoctorTable(results);
    }

    // ────────────────────────────────────────────────
    // APPOINTMENTS
    // ────────────────────────────────────────────────

    private void appointmentMenu() {
        while (true) {
            printMenuAppointment();
            int choice = InputUtil.readInt("Choose: ");

            switch (choice) {
                case 1 -> listAppointments();
                case 2 -> createAppointment();
                case 3 -> updateAppointment();
                case 4 -> deleteAppointment();
                case 5 -> searchAppointments();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listAppointments() {
        int page = 1;
        final int size = 5;

        while (true) {
            List<Appointment> apps = appointmentService.listPaginated(page, size);

            if (apps.isEmpty()) {
                System.out.println("No appointments found.");
                if (page > 1) page--; // go back if over last page
                continue;
            }

            printHeader("Appointment List (Page " + page + ")");
            printAppointmentTable(apps);

            System.out.println("""
            
            [N] Next page
            [P] Previous page
            [B] Back to menu
        """);

            String choice = InputUtil.readLine("Choose: ").toUpperCase();

            switch (choice) {
                case "N" -> page++;
                case "P" -> {
                    if (page > 1) page--;
                    else System.out.println("Already at first page.");
                }
                case "B" -> {
                    return; // Exit pagination loop
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

//    private void createAppointment() {
//        int doctorId = InputUtil.readInt("Doctor ID: ");
//        Doctor doctor = doctorService.findById(doctorId).orElse(null);
//
//        if (doctor == null) {
//            System.out.println("Doctor not found.");
//            return;
//        }
//
//        // 👉 Show doctor schedule
//        System.out.println("Doctor Working Days : " + doctor.getWorkingDays());
//        System.out.println("Doctor Working Hours: " + doctor.getWorkingHours());
//
//        // Duration
//        int duration;
//        while (true) {
//            duration = InputUtil.readInt("Appointment duration (30 or 60 minutes): ");
//            if (duration == 30 || duration == 60) break;
//            System.out.println("Invalid duration.");
//        }
//
//        LocalDate date;
//        LocalTime time;
//
//        while (true) {
//            date = InputUtil.readDate("Appointment date (yyyy-MM-dd): ");
//
//            if (!isWorkingDay(doctor, date)) {
//                System.out.println("Doctor does not work on this day.");
//                continue;
//            }
//
//            String timeInput = InputUtil.readLine("Appointment time (HH:mm): ");
//            try {
//                time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm"));
//            } catch (Exception e) {
//                System.out.println("Invalid time format. Example: 09:30");
//                continue;
//            }
//
//            if (!isWithinWorkingHours(doctor, time, duration)) {
//                System.out.println("Outside working hours: " + doctor.getWorkingHours());
//                continue;
//            }
//
//            if (date.isBefore(LocalDate.now()) ||
//                    (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
//                System.out.println("Appointment must be in the future.");
//                continue;
//            }
//
//            if (appointmentService.existsByDoctorAndTime(doctorId, date, time, duration)) {
//                System.out.println("Doctor already booked at this time.");
//                continue;
//            }
//
//            break;
//        }
//
//        Appointment a = new Appointment();
//        a.setDoctorId(doctorId);
//        a.setPatientName(InputUtil.readName("Patient name: "));
//        a.setPatientGender(InputUtil.readLine("Gender: "));
//        a.setPatientPhone(InputUtil.readLine("Phone: "));
//        a.setAppointmentDate(date);
//        a.setAppointmentTime(time);
//        a.setDurationMinutes(duration);
//
//        appointmentService.create(a);
//        System.out.println("✅ Appointment created successfully.");
//    }

    private void createAppointment() {
        int doctorId = InputUtil.readInt("Doctor ID: ");
        Doctor doctor = doctorService.findById(doctorId).orElse(null);

        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        // Show doctor schedule
        System.out.println("Doctor Working Days : " + doctor.getWorkingDays());
        System.out.println("Doctor Working Hours: " + doctor.getWorkingHours());

        // Appointment duration
        int duration;
        while (true) {
            duration = InputUtil.readInt("Appointment duration (30 or 60 minutes): ");
            if (duration == 30 || duration == 60) break;
            System.out.println("Invalid duration. Must be 30 or 60 minutes.");
        }

        LocalDate date;
        LocalTime time;

        while (true) {
            // Date input
            date = InputUtil.readDate("Appointment date (yyyy-MM-dd): ");

            // Check working day
            if (!isWorkingDay(doctor, date)) {
                System.out.println("Doctor does not work on this day. Available: " + doctor.getWorkingDays());
                continue;
            }

            // Time input
            String timeInput = InputUtil.readLine("Appointment time (HH:mm): ");
            try {
                time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (Exception e) {
                System.out.println("Invalid time format. Example: 09:30");
                continue;
            }

            // Check working hours
            if (!isWithinWorkingHours(doctor, time, duration)) {
                System.out.println("Outside working hours: " + doctor.getWorkingHours());
                continue;
            }

            // Check future appointment
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            if (date.isBefore(today) || (date.equals(today) && time.isBefore(now))) {
                System.out.println("Appointment must be in the future.");
                continue;
            }

            // Double booking check
            if (appointmentService.existsByDoctorAndTime(doctorId, date, time, duration)) {
                System.out.println("Doctor already has an appointment at this time.");
                continue;
            }

            break; // All validations passed
        }

        // Create appointment
        Appointment a = new Appointment();
        a.setDoctorId(doctorId);
        a.setPatientName(InputUtil.readNonEmpty("Patient name: "));
        a.setPatientGender(InputUtil.readLine("Gender (M/F/Other): "));
        a.setPatientPhone(InputUtil.readLine("Patient phone: "));
        a.setAppointmentDate(date);
        a.setAppointmentTime(time);
        a.setDurationMinutes(duration);

        appointmentService.create(a);
        System.out.println("✅ Appointment created successfully. ID = " + a.getAppointmentId());
    }


//    private void updateAppointment() {
//
//        int id = InputUtil.readInt("Enter appointment ID to update: ");
//        Appointment a = appointmentService.findById(id).orElse(null);
//
//        if (a == null) {
//            System.out.println("Appointment not found.");
//            return;
//        }
//
//        System.out.println("\nLeave blank to keep current value");
//        System.out.println("Current Doctor ID : " + a.getDoctorId());
//        System.out.println("Current Date      : " + a.getAppointmentDate());
//        System.out.println("Current Time      : " + a.getAppointmentTime());
//        System.out.println("Current Duration  : " + a.getDurationMinutes() + " mins");
//
//        // ─────────────────────────────────────────────
//        // Doctor
//        // ─────────────────────────────────────────────
//        String doctorInput = InputUtil.readLine("New Doctor ID (leave blank to keep): ");
//        int doctorId = doctorInput.isEmpty()
//                ? a.getDoctorId()
//                : Integer.parseInt(doctorInput);
//
//        Doctor doctor = doctorService.findById(doctorId).orElse(null);
//        if (doctor == null) {
//            System.out.println("Doctor not found.");
//            return;
//        }
//
//        System.out.println("Doctor Working Days : " + doctor.getWorkingDays());
//        System.out.println("Doctor Working Hours: " + doctor.getWorkingHours());
//
//        // ─────────────────────────────────────────────
//        // Duration
//        // ─────────────────────────────────────────────
//        String durationInput = InputUtil.readLine(
//                "New duration (30 or 60, leave blank to keep): "
//        );
//
//        int duration = durationInput.isEmpty()
//                ? a.getDurationMinutes()
//                : Integer.parseInt(durationInput);
//
//        if (duration != 30 && duration != 60) {
//            System.out.println("Invalid duration. Keeping previous value.");
//            duration = a.getDurationMinutes();
//        }
//
//        // ─────────────────────────────────────────────
//        // Patient Name (LETTERS ONLY)
//        // ─────────────────────────────────────────────
//        String name = InputUtil.readLine("New patient name (leave blank to keep): ");
//        if (!name.isEmpty()) {
//            if (!name.matches("[a-zA-Z ]+")) {
//                System.out.println("Invalid name. Letters and spaces only.");
//                return;
//            }
//            a.setPatientName(name);
//        }
//
//        // Gender
//        String gender = InputUtil.readLine("New gender (leave blank to keep): ");
//        if (!gender.isEmpty()) {
//            a.setPatientGender(gender);
//        }
//
//        // Phone
//        String phone = InputUtil.readLine("New phone (leave blank to keep): ");
//        if (!phone.isEmpty()) {
//            a.setPatientPhone(phone);
//        }
//
//        LocalDate date;
//        LocalTime time;
//
//        // ─────────────────────────────────────────────
//        // Date & Time Validation Loop
//        // ─────────────────────────────────────────────
//        while (true) {
//
//            // DATE
//            String dateInput = InputUtil.readLine(
//                    "New appointment DATE (yyyy-MM-dd, leave blank to keep): "
//            );
//
//            if (dateInput.isEmpty()) {
//                date = a.getAppointmentDate();
//            } else {
//                try {
//                    date = LocalDate.parse(dateInput);
//                } catch (Exception e) {
//                    System.out.println("Invalid date format.");
//                    continue;
//                }
//            }
//
//            // TIME
//            String timeInput = InputUtil.readLine(
//                    "New appointment TIME (HH:mm, leave blank to keep): "
//            );
//
//            if (timeInput.isEmpty()) {
//                time = a.getAppointmentTime();
//            } else {
//                try {
//                    time = LocalTime.parse(
//                            timeInput,
//                            DateTimeFormatter.ofPattern("HH:mm")
//                    );
//                } catch (Exception e) {
//                    System.out.println("Invalid time format. Example: 08:30");
//                    continue;
//                }
//            }
//
//            // Working day check
//            if (!isWorkingDay(doctor, date)) {
//                System.out.println(
//                        "Doctor does not work on this day. Available: "
//                                + doctor.getWorkingDays()
//                );
//                continue;
//            }
//
//            // Working hour check
//            if (!isWithinWorkingHours(doctor, time, duration)) {
//                System.out.println(
//                        "Outside working hours: " + doctor.getWorkingHours()
//                );
//                continue;
//            }
//
//            // Future check
//            LocalDate today = LocalDate.now();
//            LocalTime now = LocalTime.now();
//
//            if (date.isBefore(today)
//                    || (date.equals(today) && time.isBefore(now))) {
//                System.out.println("Appointment must be in the future.");
//                continue;
//            }
//
//            // Double booking (ignore current appointment)
//            if ((doctorId != a.getDoctorId()
//                    || !date.equals(a.getAppointmentDate())
//                    || !time.equals(a.getAppointmentTime()))
//                    && appointmentService.existsByDoctorAndTime(
//                    doctorId, date, time, duration
//            )) {
//                System.out.println("Doctor already booked at this time.");
//                continue;
//            }
//
//            break;
//        }

        // ─────────────────────────────────────────────
        // Apply updates
        // ─────────────────────────────────────────────
//        a.setDoctorId(doctorId);
//        a.setAppointmentDate(date);
//        a.setAppointmentTime(time);
//        a.setDurationMinutes(duration);
//
//        appointmentService.update(a);
//        System.out.println("✅ Appointment updated successfully.");
//    }

    private void updateAppointment() {
        int id = InputUtil.readInt("Enter appointment ID to update: ");
        Appointment a = appointmentService.findById(id).orElse(null);

        if (a == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.println("Leave blank to keep current value");
        System.out.println("Current Doctor ID: " + a.getDoctorId());
        System.out.println("Current Date     : " + a.getAppointmentDate());
        System.out.println("Current Time     : " + a.getAppointmentTime());
        System.out.println("Current Duration : " + a.getDurationMinutes() + " mins");

        // Doctor input
        String doctorInput = InputUtil.readLine("New Doctor ID: ");
        int doctorId = doctorInput.isEmpty() ? a.getDoctorId() : Integer.parseInt(doctorInput);

        Doctor doctor = doctorService.findById(doctorId).orElse(null);
        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        // Show doctor schedule
        System.out.println("Doctor Working Days : " + doctor.getWorkingDays());
        System.out.println("Doctor Working Hours: " + doctor.getWorkingHours());

        // Duration input
        String durationInput = InputUtil.readLine("New duration (30 or 60): ");
        int duration = durationInput.isEmpty() ? a.getDurationMinutes() : Integer.parseInt(durationInput);
        if (duration != 30 && duration != 60) duration = a.getDurationMinutes();

        // Patient info
        String name = InputUtil.readLine("New patient name (leave blank to keep): ");
        if (!name.isEmpty()) a.setPatientName(name);

        String gender = InputUtil.readLine("New gender (leave blank to keep): ");
        if (!gender.isEmpty()) a.setPatientGender(gender);

        String phone = InputUtil.readLine("New phone (leave blank to keep): ");
        if (!phone.isEmpty()) a.setPatientPhone(phone);

        LocalDate date;
        LocalTime time;

        while (true) {
            // Date input
            String dateInput = InputUtil.readLine("New DATE (yyyy-MM-dd, leave blank to keep): ");
            date = dateInput.isEmpty() ? a.getAppointmentDate() : LocalDate.parse(dateInput);

            // Time input
            String timeInput = InputUtil.readLine("New TIME (HH:mm, leave blank to keep): ");
            if (timeInput.isEmpty()) {
                time = a.getAppointmentTime();
            } else {
                try {
                    time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm"));
                } catch (Exception e) {
                    System.out.println("Invalid time format. Example: 08:30");
                    continue;
                }
            }

            // Validate working day
            if (!isWorkingDay(doctor, date)) {
                System.out.println("Doctor does not work on this day. Available: " + doctor.getWorkingDays());
                continue;
            }

            // Validate working hours
            if (!isWithinWorkingHours(doctor, time, duration)) {
                System.out.println("Outside working hours: " + doctor.getWorkingHours());
                continue;
            }

            // Future appointment check
            if (date.isBefore(LocalDate.now()) ||
                    (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
                System.out.println("Appointment must be in the future.");
                continue;
            }

            // Double booking check (ignore current appointment)
            if ((doctorId != a.getDoctorId() ||
                    !date.equals(a.getAppointmentDate()) ||
                    !time.equals(a.getAppointmentTime())) &&
                    appointmentService.existsByDoctorAndTime(doctorId, date, time, duration)) {
                System.out.println("Doctor already booked at this time.");
                continue;
            }

            break; // All validations passed
        }

        // Apply updates
        a.setDoctorId(doctorId);
        a.setAppointmentDate(date);
        a.setAppointmentTime(time);
        a.setDurationMinutes(duration);

        appointmentService.update(a);
        System.out.println("✅ Appointment updated successfully.");
    }


    // ──────────────── Helper Methods ────────────────

//    private boolean isWorkingDay(Doctor doctor, LocalDate date) {
//
//        String workingDays = doctor.getWorkingDays().toLowerCase(); // "mon-tue"
//        DayOfWeek day = date.getDayOfWeek(); // MONDAY
//
//        return switch (day) {
//            case MONDAY    -> workingDays.contains("mon");
//            case TUESDAY   -> workingDays.contains("tue");
//            case WEDNESDAY -> workingDays.contains("wed");
//            case THURSDAY  -> workingDays.contains("thu");
//            case FRIDAY    -> workingDays.contains("fri");
//            case SATURDAY  -> workingDays.contains("sat");
//            case SUNDAY    -> workingDays.contains("sun");
//        };
//    }

    private boolean isWorkingDay(Doctor doctor, LocalDate date) {
        String workDays = doctor.getWorkingDays().toLowerCase(); // e.g., "mon-fri"
        String day = date.getDayOfWeek().toString().substring(0, 3).toLowerCase(); // "mon", "tue", etc.

        if (workDays.contains("-")) {
            // Handle range like "mon-fri"
            String[] parts = workDays.split("-");
            List<String> allDays = List.of("mon","tue","wed","thu","fri","sat","sun");
            int start = allDays.indexOf(parts[0]);
            int end = allDays.indexOf(parts[1]);
            List<String> rangeDays = allDays.subList(start, end + 1);
            return rangeDays.contains(day);
        } else {
            // Comma-separated days
            String[] daysArray = workDays.split(",");
            return Arrays.asList(daysArray).contains(day);
        }
    }


    private boolean isWithinWorkingHours(Doctor doctor, LocalTime time, int durationMinutes) {
        try {
            String hours = doctor.getWorkingHours(); // e.g., "08:00-16:00"
            if (!hours.contains("-")) return false;

            String[] parts = hours.split("-");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm"); // allow 7:00 or 08:00

            LocalTime start = LocalTime.parse(parts[0].trim(), formatter);
            LocalTime end = LocalTime.parse(parts[1].trim(), formatter);

            LocalTime appointmentEnd = time.plusMinutes(durationMinutes);

            return !time.isBefore(start) && !appointmentEnd.isAfter(end);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid working hours format for doctor: " + doctor.getWorkingHours());
            return false;
        }
    }

    private void deleteAppointment() {
        int id = InputUtil.readInt("Enter appointment ID to delete: ");
        if (InputUtil.readConfirm("Confirm soft delete?")) {
            appointmentService.delete(id);
            System.out.println("Appointment soft deleted.");
        }
    }

    private void searchAppointments() {
        String phone = InputUtil.readLine("Patient phone (partial match): ");
        List<Appointment> results = appointmentService.searchByPhone(phone);

        if (results.isEmpty()) {
            System.out.println("No matching appointments.");
            return;
        }

        printHeader("Appointment Search Results");
        printAppointmentTable(results);
    }

    private List<LocalTime> getAvailableTimeSlots(Doctor doctor, int durationMinutes, LocalDate date) {
        List<LocalTime> slots = new ArrayList<>();

        String[] workingHoursParts = doctor.getWorkingHours().split("-"); // e.g., "7:00-16:00"
        if (workingHoursParts.length != 2) return slots;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm"); // allow single-digit hour
        LocalTime start;
        LocalTime end;

        try {
            start = LocalTime.parse(workingHoursParts[0].trim(), formatter);
            end = LocalTime.parse(workingHoursParts[1].trim(), formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Doctor working hours format invalid: " + doctor.getWorkingHours());
            return slots;
        }

        LocalTime slot = start;
        while (slot.plusMinutes(durationMinutes).isBefore(end.plusSeconds(1))) {
            // Only add if not already booked
            if (!appointmentService.existsByDoctorAndTime(doctor.getDoctorId(), date, slot, durationMinutes)) {
                slots.add(slot);
            }
            slot = slot.plusMinutes(30); // next possible slot
        }

        return slots;
    }

}

