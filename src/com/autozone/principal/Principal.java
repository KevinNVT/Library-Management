package com.autozone.principal;
import java.util.Scanner;

import com.autozone.dao.BookDAO;
import com.autozone.dao.LoanDAO;
import com.autozone.dao.MemberDAO;
import com.autozone.interactions.BookManager;
import com.autozone.interactions.LoanManager;
import com.autozone.interactions.MemberManager;

public class Principal {
	
	public static void main(String[] args) {
		BookDAO bookDAO = new BookDAO();
		MemberDAO memberDAO = new MemberDAO();
		LoanDAO loanDAO = new LoanDAO();
		
		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		
        while (running) {
            try {
            	// Main menu
                System.out.println("\nLibrary Management System");
                System.out.println("1. Books");
                System.out.println("2. Members");
                System.out.println("3. Loans");
                System.out.println("4. Exit");
                
                // Validates the user enters numbers only
                int choice = 0;
                try {
                	choice = scanner.nextInt();
                } catch (Exception exception) {
                	System.err.println("\nInvalid choice. Please select a valid option.");
                	exception.printStackTrace();
                }
                
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                    	BookManager.manageBooks(scanner, bookDAO);
                        break;
                    case 2:
                        MemberManager.manageMembers(scanner, memberDAO); 
                        break;
                    case 3:
                        LoanManager.manageLoans(scanner, loanDAO);
                        break;
                    case 4:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                        break;
                }
            } catch (Exception exception) {
                System.err.println("An unexpected error has occurred");
                exception.printStackTrace();
            }
        }
        scanner.close();
    }
}

