//package co.istad.view;
//
//import org.nocrala.tools.texttablefmt.BorderStyle;
//import org.nocrala.tools.texttablefmt.CellStyle;
//import org.nocrala.tools.texttablefmt.Table;
//
//public class View {
//    public static void printAppMenu() {
//        CellStyle cellStyle = new CellStyle(CellStyle.HorizontalAlign.center);
//        Table table = new Table(1, BorderStyle.UNICODE_DOUBLE_BOX);
//        table.setColumnWidth(0, 50, 100);
//        table.addCell("Application Menu", cellStyle);
//        table.addCell("1) List Products 2) Search  3) Add Product 4) Update Product", cellStyle);
//        table.addCell("5) Delete Product 0) Exit", cellStyle);
//        print(table.render(), true);
//    }
//
//    public static void showSuccessMsg(String prefix) {
//        System.out.println(prefix + " successfully");
//    }
//
//    public static void print(String text, boolean isNewLine) {
//        if (isNewLine)
//            System.out.println(text);
//        else
//            System.out.print(text);
//    }
//
//    public static void printHeader(String text) {
//        Table table = new Table(1,
//                BorderStyle.UNICODE_ROUND_BOX_WIDE);
//        table.addCell(text);
//        print(table.render(), true);
//    }
//
////    public static void table(List<Product> products) {
////
////        CellStyle centerStyle = new CellStyle(CellStyle.HorizontalAlign.center);
////        Table table = new Table(6, BorderStyle.UNICODE_DOUBLE_BOX);
////
////        // Set column widths
////        table.setColumnWidth(0, 10, 20); // ID
////        table.setColumnWidth(1, 10, 20); // Code
////        table.setColumnWidth(2, 15, 30); // Name
////        table.setColumnWidth(3, 10, 20); // Price
////        table.setColumnWidth(4, 5, 10);  // Qty
////        table.setColumnWidth(5, 10, 20); // Is_Deleted
////
////        // Header
////        table.addCell("ID", centerStyle);
////        table.addCell("Code", centerStyle);
////        table.addCell("Name", centerStyle);
////        table.addCell("Price", centerStyle);
////        table.addCell("Qty", centerStyle);
////        table.addCell("Is_Deleted", centerStyle);
////
////        // Data
////        for (Product p : products) {
////            table.addCell(String.valueOf(p.getId()), centerStyle);
////            table.addCell(String.valueOf(p.getCode()), centerStyle);
////            table.addCell(String.valueOf(p.getName()), centerStyle);
////            table.addCell(String.valueOf(p.getPrice()), centerStyle);
////            table.addCell(String.valueOf(p.getQty()), centerStyle);
////            table.addCell(String.valueOf(p.getDeleted()), centerStyle);
////        }
//
////        View.print(table.render(), true);
////    }
//}
package co.istad.view;

import co.istad.model.Appointment;
import co.istad.model.Doctor;
import co.istad.service.AppointmentService;
import co.istad.service.DoctorService;
import co.istad.util.InputUtil;

import java.util.List;

public class View {

    private final DoctorService doctorService = new DoctorService();
    private final AppointmentService appointmentService = new AppointmentService();

    public void run() {
        while (true) {
            System.out.println("\n=== Clinic Management System ===");
            System.out.println("1. Doctors");
            System.out.println("2. Appointments");
            System.out.println("0. Exit");
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
            System.out.println("\n--- Doctor Management ---");
            System.out.println("1. List doctors (paginated)");
            System.out.println("2. Add new doctor");
            System.out.println("3. Update doctor");
            System.out.println("4. Soft delete doctor");
            System.out.println("5. Search doctor");
            System.out.println("0. Back");
            int choice = InputUtil.readInt("Choose: ");

            switch (choice) {
                case 1 -> listDoctors();
                case 2 -> addDoctor();
                case 3 -> updateDoctor();
                case 4 -> deleteDoctor();
                case 5 -> searchDoctors();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listDoctors() {
        int page = 0;
        final int size = 10;
        while (true) {
            List<Doctor> doctors = doctorService.listPaginated(page, size);
            if (doctors.isEmpty()) {
                System.out.println("No doctors found.");
                return;
            }

            System.out.println("\nDoctor List (page " + (page + 1) + ")");
            System.out.println("ID    Full Name                 Specialization       Phone          Room");
            System.out.println("───────────────────────────────────────────────────────────────────────");
            doctors.forEach(System.out::println);

            if (!InputUtil.readConfirm("Next page?")) break;
            page++;
        }
    }

    private void addDoctor() {
        Doctor d = new Doctor();
        d.setFullName(InputUtil.readNonEmpty("Full name          : "));
        d.setSpecialization(InputUtil.readLine("Specialization     : "));
        d.setPhone(InputUtil.readLine("Phone              : "));
        d.setEmail(InputUtil.readLine("Email              : "));
        d.setRoomNumber(InputUtil.readLine("Room number        : "));
        d.setWorkingDays(InputUtil.readLine("Working days       : "));
        d.setWorkingHours(InputUtil.readLine("Working hours      : "));

        doctorService.create(d);
        System.out.println("Doctor created successfully. ID = " + d.getDoctorId());
    }

    private void updateDoctor() {
        int id = InputUtil.readInt("Enter doctor ID to update: ");
        // Note: real app should check existence first
        Doctor d = new Doctor();
        d.setDoctorId(id);

        System.out.println("Leave blank to keep current value");
        String name = InputUtil.readLine("New full name: ");
        if (!name.isEmpty()) d.setFullName(name);

        String spec = InputUtil.readLine("New specialization: ");
        if (!spec.isEmpty()) d.setSpecialization(spec);

        String phone = InputUtil.readLine("New phone: ");
        if (!phone.isEmpty()) d.setPhone(phone);

        String email = InputUtil.readLine("New email: ");
        if (!email.isEmpty()) d.setEmail(email);

        String room = InputUtil.readLine("New room: ");
        if (!room.isEmpty()) d.setRoomNumber(room);

        String days = InputUtil.readLine("New working days: ");
        if (!days.isEmpty()) d.setWorkingDays(days);

        String hours = InputUtil.readLine("New working hours: ");
        if (!hours.isEmpty()) d.setWorkingHours(hours);

        doctorService.update(d);
        System.out.println("Doctor updated.");
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
        System.out.println("\nSearch Results:");
        System.out.println("ID    Full Name                 Specialization       Phone          Room");
        System.out.println("───────────────────────────────────────────────────────────────────────");
        results.forEach(System.out::println);
    }

    // ────────────────────────────────────────────────
    // APPOINTMENTS
    // ────────────────────────────────────────────────

    private void appointmentMenu() {
        while (true) {
            System.out.println("\n--- Appointment Management ---");
            System.out.println("1. List appointments (paginated)");
            System.out.println("2. Create appointment");
            System.out.println("3. Update appointment");
            System.out.println("4. Soft delete appointment");
            System.out.println("5. Search by patient phone");
            System.out.println("0. Back");
            int choice = InputUtil.readInt("Choose: ");

            switch (choice) {
                case 1 -> listAppointments();
                case 2 -> createAppointment();
                case 3 -> updateAppointment();
                case 4 -> deleteAppointment();
                case 5 -> searchAppointments();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listAppointments() {
        int page = 0;
        final int size = 10;
        while (true) {
            List<Appointment> apps = appointmentService.listPaginated(page, size);
            if (apps.isEmpty()) {
                System.out.println("No appointments found.");
                return;
            }

            System.out.println("\nAppointment List (page " + (page + 1) + ")");
            System.out.println("ID    DrID  Patient Name         Gender     Phone           Date       Time");
            System.out.println("─────────────────────────────────────────────────────────────────────────────");
            apps.forEach(System.out::println);

            if (!InputUtil.readConfirm("Next page?")) break;
            page++;
        }
    }

    private void createAppointment() {
        Appointment a = new Appointment();
        a.setDoctorId(InputUtil.readInt("Doctor ID              : "));
        a.setPatientName(InputUtil.readNonEmpty("Patient name           : "));
        a.setPatientGender(InputUtil.readLine("Gender (M/F/Other)     : "));
        a.setPatientPhone(InputUtil.readLine("Patient phone          : "));
        a.setAppointmentDate(InputUtil.readDate("Appointment date"));
        a.setAppointmentTime(InputUtil.readTime("Appointment time"));

        appointmentService.create(a);
        System.out.println("Appointment created successfully. ID = " + a.getAppointmentId());
    }

    private void updateAppointment() {
        int id = InputUtil.readInt("Enter appointment ID to update: ");
        Appointment a = new Appointment();
        a.setAppointmentId(id);

        System.out.println("Leave blank to keep current value");
        int doctorId = InputUtil.readInt("New doctor ID (0 = keep): ");
        if (doctorId != 0) a.setDoctorId(doctorId);

        String name = InputUtil.readLine("New patient name: ");
        if (!name.isEmpty()) a.setPatientName(name);

        String gender = InputUtil.readLine("New gender: ");
        if (!gender.isEmpty()) a.setPatientGender(gender);

        String phone = InputUtil.readLine("New phone: ");
        if (!phone.isEmpty()) a.setPatientPhone(phone);

        // Date & time update is optional – skipped for simplicity

        appointmentService.update(a);
        System.out.println("Appointment updated.");
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
        System.out.println("\nSearch Results:");
        System.out.println("ID    DrID  Patient Name         Gender     Phone           Date       Time");
        System.out.println("─────────────────────────────────────────────────────────────────────────────");
        results.forEach(System.out::println);
    }
}
