package com.main;

import com.service.UserService;

import com.user.Order;
import com.user.Route;
import com.user.User;
import com.service.JourneyService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static List<User> users = new ArrayList<>();
    private static List<Route> routes = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();
    private static Map<String, Integer> userInvalidLoginAttempt = new HashMap<>();
    private static UserService userService = new UserService(users, userInvalidLoginAttempt);
    private static JourneyService journeyService = new JourneyService(routes, orders);

    public static void main(String[] args) {
        // Pre-populate the services with some data if necessary

              initializeRoutes();
        if (displayCompanyLogo()) {
            showMenuOptions();
        } else {
            System.out.println("Failed to load company logo. Exiting...");
        }
    }
    private static void initializeRoutes() {
        routes.add(new Route(1, "Nellore", "Hyderabad", LocalDate.parse("2024-01-20", DateTimeFormatter.ISO_LOCAL_DATE), 1000, 40));
        routes.add(new Route(2, "Hyderabad", "Goa", LocalDate.parse("2024-01-19", DateTimeFormatter.ISO_LOCAL_DATE), 1500, 40));
        routes.add(new Route(3, "Rajahamundry", "Hyderabad", LocalDate.parse("2024-01-19", DateTimeFormatter.ISO_LOCAL_DATE), 1500, 40));
        // ... add more routes as needed
    }


    private static boolean displayCompanyLogo() {
        try { 
        	FileInputStream reader = new FileInputStream("Text.txt"); {
            int line;
            while ((line=reader.read()) != -1) {
                System.out.print((char)line);
            }
        }
         
        return true; // Logo loaded successfully 
        }
        catch(IOException e) {
            System.out.println("Error reading company logo file: " + e.getMessage());
            return false; // Logo loading failed
        }
    }
    
    private static void showMenuOptions() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean running = true;

        while (running) {
            System.out.println("\nMenu Options:");
            System.out.println("1. New Admin User Registration");
            System.out.println("2. Login");
            System.out.println("3. Plan journey");
            System.out.println("4. Reschedule booking date");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
            case 1:
                userService.registerNewAdmin();
                break;
            case 2:
                userService.login();
                break;
            case 3 : 
            	journeyService.planJourney();
                   break;
            case 4 : 
            	journeyService.reScheduleJourney();
            case 5:
                System.out.println("Exiting...");
                running = false;
                break;
            default:
                System.out.println("Invalid choice. Please enter a correct option.");
                break;
        }
    }
        scanner.close();  // Close the scanner when we're done with it
}

}
