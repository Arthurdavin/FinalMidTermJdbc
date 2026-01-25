package co.istad.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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


}