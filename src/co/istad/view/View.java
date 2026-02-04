package co.istad.view;

import co.istad.model.Appointment;
import co.istad.model.Doctor;
import co.istad.service.AppointmentService;
import co.istad.service.DoctorService;
import co.istad.util.InputUtil;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;

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
        table.addCell("Hospital Appointment System", cellStyle);
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
        Table table = new Table(9, BorderStyle.UNICODE_DOUBLE_BOX); // ✅ 8 → 9

        table.setColumnWidth(0, 5, 10);   // ID
        table.setColumnWidth(1, 20, 30);  // Name
        table.setColumnWidth(2, 15, 25);  // Specialization
        table.setColumnWidth(3, 12, 20);  // Phone
        table.setColumnWidth(4, 25, 30);  // ✅ Email (NEW)
        table.setColumnWidth(5, 10, 15);  // Room
        table.setColumnWidth(6, 20, 30);  // WorkingDays
        table.setColumnWidth(7, 20, 30);  // Workhours
        table.setColumnWidth(8, 10, 15);  // Deleted

        // Header
        table.addCell("ID", center);
        table.addCell("Full Name", center);
        table.addCell("Specialization", center);
        table.addCell("Phone", center);
        table.addCell("Email", center);        // ✅ NEW
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
            table.addCell(d.getEmail(), center);        // ✅ NEW
            table.addCell(d.getRoomNumber(), center);
            table.addCell(String.valueOf(d.getWorkingDays()), center);
            table.addCell(String.valueOf(d.getWorkingHours()), center);
            table.addCell(String.valueOf(d.isDeleted()), center);
        }

        print(table.render(), true);
    }


    public static void printAppointmentTable(List<Appointment> apps) {

        CellStyle center = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(9, BorderStyle.UNICODE_DOUBLE_BOX); // 🔹 changed from 8 → 9

        table.setColumnWidth(0, 5, 10);    // ID
        table.setColumnWidth(1, 5, 10);    // Doctor ID
        table.setColumnWidth(2, 20, 30);   // Patient Name
        table.setColumnWidth(3, 8, 12);    // Gender
        table.setColumnWidth(4, 15, 30);   // Phone
        table.setColumnWidth(5, 15, 20);   // Date
        table.setColumnWidth(6, 8, 12);    // Time
        table.setColumnWidth(7, 10, 15);   // ⏱ Duration Minutes  🔹 NEW
        table.setColumnWidth(8, 8, 12);    // IsDelete

        // Header
        table.addCell("ID", center);
        table.addCell("DrID", center);
        table.addCell("Patient Name", center);
        table.addCell("Gender", center);
        table.addCell("Phone", center);
        table.addCell("Date", center);
        table.addCell("Time", center);
        table.addCell("Duration", center);   // 🔹 NEW
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
            table.addCell(String.valueOf(a.getDurationMinutes()), center); // 🔹 NEW
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

    // List Doctor

//    private void listDoctors() {
//        int page = 1;
//        final int size = 5;
//
//        while (true) {
//            List<Doctor> doctors = doctorService.listPaginated(page, size);
//
//            if (doctors.isEmpty()) {
//                System.out.println("No doctors found.");
//                if (page > 1) page--; // go back if over last page
//                continue;
//            }
//
//            printHeader("Doctor List (Page " + page + ")");
//            printDoctorTable(doctors);
//
//            System.out.println("""
//                        [N] Next page
//                        [P] Previous page
//                        [B] Back to menu
//                    """);
//
//            String choice = InputUtil.readLine("Choose: ").toUpperCase();
//
//            switch (choice) {
//                case "N" -> page++;
//                case "P" -> {
//                    if (page > 1) page--;
//                    else System.out.println("Already at first page.");
//                }
//                case "B" -> {
//                    return; // Exit pagination loop
//                }
//                default -> System.out.println("Invalid choice. Try again.");
//            }
//        }
//    }

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
                    [G] Go to page
                    [B] Back to menu
                    """);

            String choice = InputUtil.readLine("Choose: ").toUpperCase();

            switch (choice) {
                case "N" -> page++;

                case "P" -> {
                    if (page > 1) page--;
                    else System.out.println("Already at first page.");
                }

                case "G" -> {
                    int newPage = InputUtil.readInt("Enter page number: ");
                    if (newPage >= 1) {
                        page = newPage;
                    } else {
                        System.out.println("Page must be 1 or greater.");
                    }
                }

                case "B" -> {
                    return; // Exit pagination loop
                }

                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }


    // Insert Doctor

    private void addDoctor() {
        View.printHeader("Add Doctor");

        Doctor d = new Doctor();

        d.setFullName(InputUtil.readNonEmpty("Full name          : "));
        d.setSpecialization(InputUtil.readNonEmpty("Specialization     : "));
        d.setPhone(InputUtil.readNonEmpty("Phone              : "));

        // --- EMAIL CHECK BEFORE INSERT ---
        String email = InputUtil.readEmail("Email              : ");
        Doctor existing = doctorService.findByEmail(email);

        if (existing != null) {
            System.out.println("❌ Email already exists. Doctor not created.");
            return;
        }

        d.setEmail(email);
        d.setRoomNumber(InputUtil.readNonEmpty("Room number        : "));
        d.setWorkingDays(InputUtil.readWorkingDays("Working days (mon | mon,wed,fri | mon-fri): "));
        d.setWorkingHours(InputUtil.readWorkingHours("Working hours (HH:mm-HH:mm): "));

        doctorService.create(d);
        System.out.println("✅ Doctor created successfully...!");
    }

    // update Doctor

    private void updateDoctor() {
        View.printHeader("Update Doctor");

        int id = InputUtil.readInt("Enter doctor ID to update: ");

        Doctor existingDoctor = doctorService.findById(id).orElse(null);
        if (existingDoctor == null) {
            System.out.println("❌ Doctor ID not found.");
            return;
        }

        System.out.println("Leave blank to keep current value");

        String name = InputUtil.readLine("New full name: ");
        if (!name.isEmpty()) existingDoctor.setFullName(name);

        String spec = InputUtil.readLine("New specialization: ");
        if (!spec.isEmpty()) existingDoctor.setSpecialization(spec);

        String phone = InputUtil.readLine("New phone: ");
        if (!phone.isEmpty()) existingDoctor.setPhone(phone);

        // ====== SAFE EMAIL UPDATE ======
        String email = InputUtil.readLine("New email (leave blank to keep): ").trim();

        if (!email.isEmpty()) {

            if (!InputUtil.isValidEmail(email)) {
                System.out.println("❌ Invalid email format.");
                return;
            }

            Doctor doctorByEmail = doctorService.findByEmail(email);

            if (doctorByEmail != null &&
                    !doctorByEmail.getDoctorId().equals(id)) {
                System.out.println("❌ Email already exists. Update cancelled.");
                return;
            }

            existingDoctor.setEmail(email);
        }

        String room = InputUtil.readLine("New room: ");
        if (!room.isEmpty()) existingDoctor.setRoomNumber(room);

        String days = InputUtil.readLine("New working days: ");
        if (!days.isEmpty()) existingDoctor.setWorkingDays(days);

        String hours = InputUtil.readLine("New working hours: ");
        if (!hours.isEmpty()) existingDoctor.setWorkingHours(hours);

        boolean updated = doctorService.update(existingDoctor);

        if (updated) {
            System.out.println("✅ Doctor updated successfully.");
        } else {
            System.out.println("⚠️ Update failed.");
        }
    }

    // soft Delete Doctor

    private void deleteDoctor() {
        View.printHeader("Delete Doctor");

        int id = InputUtil.readInt("Enter doctor ID to delete: ");

        Optional<Doctor> opt = doctorService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("❌ Doctor not found!");
            return;
        }

        Doctor d = opt.get();

        System.out.println("You are about to delete:");
        System.out.println("Doctor ID   : " + d.getDoctorId());
        System.out.println("Full Name   : " + d.getFullName());
        System.out.println("Speciality  : " + d.getSpecialization());

        if (InputUtil.readConfirm("Confirm soft delete?")) {
            doctorService.delete(id);
            System.out.println("✅ Doctor soft deleted.");
        } else {
            System.out.println("❌ Delete cancelled.");
        }
    }


    // Search Doctor

    private void searchDoctors() {
        View.printHeader("Search Doctor");
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

    // List APPOINTMENT by pagination

    //    private void listAppointments() {
//        int page = 1;
//        final int size = 5;
//
//        while (true) {
//            List<Appointment> apps = appointmentService.listPaginated(page, size);
//
//            if (apps.isEmpty()) {
//                System.out.println("No appointments found.");
//                if (page > 1) page--; // go back if over last page
//                continue;
//            }
//
//            printHeader("Appointment List (Page " + page + ")");
//            printAppointmentTable(apps);
//
//            System.out.println("""
//
//                        [N] Next page
//                        [P] Previous page
//                        [B] Back to menu
//                    """);
//
//            String choice = InputUtil.readLine("Choose: ").toUpperCase();
//
//            switch (choice) {
//                case "N" -> page++;
//                case "P" -> {
//                    if (page > 1) page--;
//                    else System.out.println("Already at first page.");
//                }
//                case "B" -> {
//                    return; // Exit pagination loop
//                }
//                default -> System.out.println("Invalid choice. Try again.");
//            }
//        }
//    }
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
                    [G] Go to page
                    [B] Back to menu
                    """);

            String choice = InputUtil.readLine("Choose: ").toUpperCase();

            switch (choice) {
                case "N" -> page++;

                case "P" -> {
                    if (page > 1) page--;
                    else System.out.println("Already at first page.");
                }

                case "G" -> {
                    int newPage = InputUtil.readInt("Enter page number: ");
                    if (newPage >= 1) {
                        page = newPage;
                    } else {
                        System.out.println("Page must be 1 or greater.");
                    }
                }

                case "B" -> {
                    return; // Exit pagination loop
                }

                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }


    // CREATE APPOINTMENT

//    private void createAppointment() {
//        View.printHeader("Create appointment");
//
//        int doctorId = InputUtil.readInt("Doctor ID: ");
//        Doctor doctor = doctorService.findById(doctorId).orElse(null);
//
//        if (doctor == null) {
//            System.out.println("❌ Doctor not found.");
//            return;
//        }
//
//        System.out.println("Doctor Working Days : " + doctor.getWorkingDays());
//        System.out.println("Doctor Working Hours: " + doctor.getWorkingHours());
//
//        // -------- DURATION (safe) --------
//        int duration;
//        while (true) {
//            duration = InputUtil.readInt("Appointment duration (30 or 60 minutes): ");
//
//            if (duration == 30 || duration == 60) break;
//
//            System.out.println("⚠️ Invalid duration. Must be 30 or 60 minutes.");
//        }
//
//        LocalDate date;
//        LocalTime time;
//
//        while (true) {
//
//            // ---- DATE (safe) ----
//            date = InputUtil.readDate("Appointment date (yyyy-MM-dd): ");
//
//            if (!isWorkingDay(doctor, date)) {
//                System.out.println("❌ Doctor does not work on this day. Available: " + doctor.getWorkingDays());
//                continue;
//            }
//
//            // ---- TIME (safe) ----
//            String timeInput = InputUtil.readLine("Appointment time (HH:mm): ");
//
//            try {
//                time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm"));
//            } catch (Exception e) {
//                System.out.println("⚠️ Invalid time format. Example: 09:30");
//                continue;
//            }
//
//            if (!isWithinWorkingHours(doctor, time, duration)) {
//                System.out.println("❌ Outside working hours: " + doctor.getWorkingHours());
//                continue;
//            }
//
//            // ---- FUTURE CHECK ----
//            if (date.isBefore(LocalDate.now()) ||
//                    (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
//                System.out.println("❌ Appointment must be in the future.");
//                continue;
//            }
//
//            // ---- DOUBLE BOOKING ----
//            if (appointmentService.existsByDoctorAndTime(doctorId, date, time, duration)) {
//                System.out.println("❌ Doctor already has an appointment at this time.");
//                continue;
//            }
//
//            break; // ✅ all validations passed
//        }
//
//        Appointment a = new Appointment();
//        a.setDoctorId(doctorId);
//        a.setPatientName(InputUtil.readNonEmpty("Patient name: "));
//        a.setPatientGender(InputUtil.readNonEmpty("Gender (M/F/Other): "));
//        a.setPatientPhone(InputUtil.readNonEmpty("Patient phone: "));
//        a.setAppointmentDate(date);
//        a.setAppointmentTime(time);
//        a.setDurationMinutes(duration);   // ✅ IMPORTANT
//
//        appointmentService.create(a);
//        System.out.println("✅ Appointment created successfully. ID = " + a.getAppointmentId());
//    }

    private void createAppointment() {
        View.printHeader("Create appointment");

        int doctorId = InputUtil.readInt("Doctor ID: ");
        Doctor doctor = doctorService.findById(doctorId).orElse(null);

        if (doctor == null) {
            System.out.println("❌ Doctor not found.");
            return;
        }

        System.out.println("Doctor Working Days : " + doctor.getWorkingDays());
        System.out.println("Doctor Working Hours: " + doctor.getWorkingHours());

        // -------- DURATION (safe) --------
        int duration;
        while (true) {
            duration = InputUtil.readInt("Appointment duration (30 or 60 minutes): ");

            if (duration == 30 || duration == 60) break;

            System.out.println("⚠️ Invalid duration. Must be 30 or 60 minutes.");
        }

        LocalDate date;
        LocalTime time;

        while (true) {

            // ============ FLEXIBLE DATE INPUT ============
            while (true) {
                String dateInput = InputUtil.readLine(
                        "Appointment date (yyyy-MM-dd) — you can type 2025-2-1: "
                );

                try {
                    DateTimeFormatter dateFormatter =
                            DateTimeFormatter.ofPattern("yyyy-M-d"); // allows 1 or 01

                    date = LocalDate.parse(dateInput, dateFormatter);
                    break; // valid date

                } catch (Exception e) {
                    System.out.println("⚠️ Invalid date. Example: 2025-2-1 or 2025-02-01");
                }
            }

            if (!isWorkingDay(doctor, date)) {
                System.out.println("❌ Doctor does not work on this day. Available: "
                        + doctor.getWorkingDays());
                continue;
            }

            // ============ FLEXIBLE TIME INPUT ============
            while (true) {
                String timeInput =
                        InputUtil.readLine("Appointment time (HH:mm) — you can type 9:30: ");

                try {
                    DateTimeFormatter timeFormatter =
                            DateTimeFormatter.ofPattern("H:mm"); // allows 9:30 or 09:30

                    time = LocalTime.parse(timeInput, timeFormatter);
                    break; // valid time

                } catch (Exception e) {
                    System.out.println("⚠️ Invalid time. Example: 9:30 or 09:30");
                }
            }

            if (!isWithinWorkingHours(doctor, time, duration)) {
                System.out.println("❌ Outside working hours: "
                        + doctor.getWorkingHours());
                continue;
            }

            // ---- FUTURE CHECK ----
            if (date.isBefore(LocalDate.now()) ||
                    (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
                System.out.println("❌ Appointment must be in the future.");
                continue;
            }

            // ---- DOUBLE BOOKING ----
            if (appointmentService.existsByDoctorAndTime(
                    doctorId, date, time, duration)) {
                System.out.println("❌ Doctor already has an appointment at this time.");
                continue;
            }

            break; // ✅ all validations passed
        }

        Appointment a = new Appointment();
        a.setDoctorId(doctorId);
        a.setPatientName(InputUtil.readNonEmpty("Patient name: "));
        a.setPatientGender(InputUtil.readNonEmpty("Gender (M/F/Other): "));
        a.setPatientPhone(InputUtil.readNonEmpty("Patient phone: "));
        a.setAppointmentDate(date);
        a.setAppointmentTime(time);
        a.setDurationMinutes(duration);

        appointmentService.create(a);
        System.out.println("✅ Appointment created successfully. ID = "
                + a.getAppointmentId());
    }


    // Update APPOINTMENT

    private void updateAppointment() {
        View.printHeader("Update appointment");

        int id = InputUtil.readInt("Enter appointment ID to update: ");
        Appointment a = appointmentService.findById(id).orElse(null);

        if (a == null) {
            System.out.println("❌ Appointment not found.");
            return;
        }

        System.out.println("Leave blank to keep current value");
        System.out.println("Current Doctor ID: " + a.getDoctorId());
        System.out.println("Current Date     : " + a.getAppointmentDate());
        System.out.println("Current Time     : " + a.getAppointmentTime());
        System.out.println("Current Duration : " + a.getDurationMinutes() + " mins");

        // ---- Doctor (safe) ----
        String doctorInput = InputUtil.readLine("New Doctor ID: ");
        int doctorId = doctorInput.isEmpty()
                ? a.getDoctorId()
                : Integer.parseInt(doctorInput);

        Doctor doctor = doctorService.findById(doctorId).orElse(null);
        if (doctor == null) {
            System.out.println("❌ Doctor not found.");
            return;
        }

        System.out.println("Doctor Working Days : " + doctor.getWorkingDays());
        System.out.println("Doctor Working Hours: " + doctor.getWorkingHours());

        // ---- Duration (safe) ----
        String durationInput = InputUtil.readLine("New duration (30 or 60): ");
        int duration = durationInput.isEmpty()
                ? a.getDurationMinutes()
                : Integer.parseInt(durationInput);

        if (duration != 30 && duration != 60) {
            duration = a.getDurationMinutes(); // fallback
        }

        // ---- Patient info (safe) ----
        String name = InputUtil.readLine("New patient name (leave blank to keep): ");
        if (!name.isEmpty()) a.setPatientName(name);

        String gender = InputUtil.readLine("New gender (leave blank to keep): ");
        if (!gender.isEmpty()) a.setPatientGender(gender);

        String phone = InputUtil.readLine("New phone (leave blank to keep): ");
        if (!phone.isEmpty()) a.setPatientPhone(phone);

        LocalDate date;
        LocalTime time;

        while (true) {

            String dateInput = InputUtil.readLine("New DATE (yyyy-MM-dd, leave blank to keep): ");
            date = dateInput.isEmpty()
                    ? a.getAppointmentDate()
                    : LocalDate.parse(dateInput);

            String timeInput = InputUtil.readLine("New TIME (HH:mm, leave blank to keep): ");

            if (timeInput.isEmpty()) {
                time = a.getAppointmentTime();
            } else {
                try {
                    time = LocalTime.parse(timeInput, DateTimeFormatter.ofPattern("HH:mm"));
                } catch (Exception e) {
                    System.out.println("⚠️ Invalid time format. Example: 08:30");
                    continue;
                }
            }

            if (!isWorkingDay(doctor, date)) {
                System.out.println("❌ Doctor does not work on this day.");
                continue;
            }

            if (!isWithinWorkingHours(doctor, time, duration)) {
                System.out.println("❌ Outside working hours.");
                continue;
            }

            if (date.isBefore(LocalDate.now()) ||
                    (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
                System.out.println("❌ Appointment must be in the future.");
                continue;
            }

            boolean changed =
                    doctorId != a.getDoctorId() ||
                            !date.equals(a.getAppointmentDate()) ||
                            !time.equals(a.getAppointmentTime());

            if (changed &&
                    appointmentService.existsByDoctorAndTime(doctorId, date, time, duration)) {
                System.out.println("❌ Doctor already booked at this time.");
                continue;
            }

            break;
        }

        a.setDoctorId(doctorId);
        a.setAppointmentDate(date);
        a.setAppointmentTime(time);
        a.setDurationMinutes(duration);

        appointmentService.update(a);
        System.out.println("✅ Appointment updated successfully.");
    }

    // ──────────────── Helper Methods ────────────────

    private boolean isWorkingDay(Doctor doctor, LocalDate date) {
        if (doctor.getWorkingDays() == null) return false;

        String workDays = doctor.getWorkingDays().toLowerCase().trim();
        String day = date.getDayOfWeek().toString().substring(0, 3).toLowerCase();

        List<String> allDays =
                List.of("mon", "tue", "wed", "thu", "fri", "sat", "sun");

        if (workDays.contains("-")) {
            String[] parts = workDays.split("-");
            if (parts.length != 2) return false;

            int start = allDays.indexOf(parts[0].trim());
            int end = allDays.indexOf(parts[1].trim());

            if (start == -1 || end == -1) return false;

            return allDays.subList(start, end + 1).contains(day);
        }

        return Arrays.asList(workDays.split(",")).contains(day);
    }

    private boolean isWithinWorkingHours(Doctor doctor, LocalTime time, int durationMinutes) {

        String hours = doctor.getWorkingHours();
        if (hours == null || !hours.contains("-")) return false;

        try {
            String[] parts = hours.split("-");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

            LocalTime start = LocalTime.parse(parts[0].trim(), formatter);
            LocalTime end = LocalTime.parse(parts[1].trim(), formatter);

            LocalTime appointmentEnd = time.plusMinutes(durationMinutes);

            return !time.isBefore(start) && !appointmentEnd.isAfter(end);

        } catch (Exception e) {
            System.out.println("⚠️ Invalid working hours format: " + hours);
            return false;
        }
    }

    // Soft Delete APPOINTMENT

    private void deleteAppointment() {
        View.printHeader("Delete Appointment");

        int id = InputUtil.readInt("Enter appointment ID to delete: ");

        Optional<Appointment> opt = appointmentService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("❌ Appointment not found!");
            return;
        }

        Appointment app = opt.get();

        System.out.println("You are about to delete:");
        System.out.println("Patient: " + app.getPatientName());
        System.out.println("Date   : " + app.getAppointmentDate());
        System.out.println("Time   : " + app.getAppointmentTime());

        if (InputUtil.readConfirm("Confirm soft delete?")) {
            appointmentService.delete(id);
            System.out.println("✅ Appointment soft deleted.");
        } else {
            System.out.println("❌ Delete cancelled.");
        }
    }


    // Search APPOINTMENT

    private void searchAppointments() {
        View.printHeader("Search Appointments");
        String phone = InputUtil.readLine("Patient phone (partial match): ");
        List<Appointment> results = appointmentService.searchByPhone(phone);

        if (results.isEmpty()) {
            System.out.println("No matching appointments.");
            return;
        }

        printHeader("Appointment Search Results");
        printAppointmentTable(results);
    }

}
