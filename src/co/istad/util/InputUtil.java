package co.istad.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class InputUtil {

    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readNonEmpty(String prompt) {
        while (true) {
            String s = readLine(prompt);
            if (!s.isEmpty()) return s;
            System.out.println("This field cannot be empty.");
        }
    }

    public static LocalDate readDate(String prompt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            String s = readLine(prompt + " (yyyy-MM-dd): ");
            try {
                return LocalDate.parse(s, fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
            }
        }
    }

    public static LocalTime readTime(String prompt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        while (true) {
            String s = readLine(prompt + " (HH:mm): ");
            try {
                return LocalTime.parse(s, fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format.");
            }
        }
    }

    public static boolean readConfirm(String prompt) {
        while (true) {
            String ans = readLine(prompt + " (y/n): ").trim().toLowerCase();
            if (ans.equals("y") || ans.equals("yes")) return true;
            if (ans.equals("n") || ans.equals("no")) return false;
            System.out.println("Please answer y or n.");
        }
    }

    public static String readEmail(String prompt) {
        while (true) {
            String email = readLine(prompt);
            if (email.isEmpty()) {
                System.out.println("Email cannot be empty.");
                continue;
            }
            // Simple regex for basic email validation
            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return email;
            }
            System.out.println("Invalid email format. Example: example@gmail.com");
        }
    }

    public static String readName(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Name cannot be empty.");
                continue;
            }

            if (!input.matches("[a-zA-Z ]+")) {
                System.out.println("Name must contain letters only (no numbers or symbols).");
                continue;
            }

            return input;
        }
    }

    public static String readWorkingDays(String prompt) {
        List<String> validDays = List.of(
                "mon", "tue", "wed", "thu", "fri", "sat", "sun"
        );

        while (true) {
            String input = readLine(prompt)
                    .toLowerCase()
                    .replaceAll("\\s+", ""); // remove spaces

            if (input.isEmpty()) {
                System.out.println("Working days cannot be empty.");
                continue;
            }

            // ───────── RANGE FORMAT (mon-fri)
            if (input.contains("-")) {
                String[] parts = input.split("-");
                if (parts.length != 2) {
                    System.out.println("Invalid range format. Example: mon-fri");
                    continue;
                }

                String start = parts[0];
                String end = parts[1];

                if (!validDays.contains(start) || !validDays.contains(end)) {
                    System.out.println("Invalid day name. Use: mon,tue,wed,thu,fri,sat,sun");
                    continue;
                }

                int startIndex = validDays.indexOf(start);
                int endIndex = validDays.indexOf(end);

                if (startIndex > endIndex) {
                    System.out.println("Invalid range order. Example: mon-fri");
                    continue;
                }

                return input; // ✅ valid range
            }

            // ───────── LIST FORMAT (mon,wed,fri)
            if (input.contains(",")) {
                String[] parts = input.split(",");

                boolean valid = true;
                for (String p : parts) {
                    if (!validDays.contains(p)) {
                        valid = false;
                        break;
                    }
                }

                if (!valid) {
                    System.out.println("Invalid day name in list.");
                    System.out.println("Example: mon,wed,fri");
                    continue;
                }

                return input; // ✅ valid list
            }

            // ───────── SINGLE DAY (mon)
            if (validDays.contains(input)) {
                return input; // ✅ valid single day
            }

            // ───────── INVALID
            System.out.println("Invalid working days format.");
            System.out.println("Allowed formats:");
            System.out.println("  mon");
            System.out.println("  mon,wed,fri");
            System.out.println("  mon-fri");
        }
    }
    public static String readWorkingHours(String prompt) {
        while (true) {
            String input = readLine(prompt).trim();

            if (input.isEmpty()) {
                System.out.println("Working hours cannot be empty.");
                continue;
            }

            // Format: HH:mm-HH:mm
            if (!input.matches("^\\d{2}:\\d{2}-\\d{2}:\\d{2}$")) {
                System.out.println("Invalid format. Example: 08:00-16:00");
                continue;
            }

            try {
                String[] parts = input.split("-");
                LocalTime start = LocalTime.parse(parts[0]);
                LocalTime end = LocalTime.parse(parts[1]);

                if (!start.isBefore(end)) {
                    System.out.println("Start time must be before end time.");
                    continue;
                }

                return input; // ✅ valid
            } catch (Exception e) {
                System.out.println("Invalid time value.");
            }
        }
    }

//    public static String readNonEmpty(String prompt) {
//        while (true) {
//            String input = readLine(prompt);
//            if (!input.trim().isEmpty()) {
//                return input.trim();
//            }
//            System.out.println("This field cannot be empty.");
//        }
//    }


}