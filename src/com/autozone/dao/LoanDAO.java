package com.autozone.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.autozone.database.DatabaseConnection;
import com.autozone.models.Loan;
import com.autozone.utils.Validator;

public class LoanDAO {

	public void addLoan(Loan loan) throws SQLException, IllegalArgumentException, IllegalAccessException {
		Validator.validate(loan);
		
		String sql = "INSERT INTO tbl_loans (book_id, member_id, loan_date, return_date, returned) VALUES (?,?,?,?,?)";
		
		//try-with-resources
		try(Connection conn = DatabaseConnection.getInstance().getConnection();
				PreparedStatement psmt = conn.prepareStatement(sql)) {
			
			psmt.setInt(1, loan.getBook_id());
			psmt.setInt(2, loan.getMember_id());
			psmt.setDate(3, new java.sql.Date(loan.getLoan_date().getTime()));
			psmt.setNull(4, java.sql.Types.DATE);
			psmt.setBoolean(5, loan.isReturned());
			
			psmt.executeUpdate();
		} catch (SQLException exception) {
			System.err.println("Failed to add loan.");
	        exception.printStackTrace();
		}
	}
	
	public void returnLoan(int member_id, int book_id) throws SQLException {
	    String sql = "UPDATE tbl_loans SET return_date = ?, returned = ? " +
	                 "WHERE member_id = ? AND book_id = ? AND returned = false";
	    
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement psmt = conn.prepareStatement(sql)) {
	        
	        psmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
	        psmt.setBoolean(2, true);
	        psmt.setInt(3, member_id);
	        psmt.setInt(4, book_id);
	        
	        int updatedRows = psmt.executeUpdate();
	        
	        if (updatedRows > 0) {
	            System.out.println("\nLoan returned successfully.");
	            
	            // Updates availability on tbl_books once the first update runs
	            String updateTbl_books = "UPDATE tbl_books SET available = false WHERE id = ?";
	            try (PreparedStatement psmtUpdateBook = conn.prepareStatement(updateTbl_books)) {
	                psmtUpdateBook.setInt(1, book_id);
	                psmtUpdateBook.executeUpdate();
	                
	                System.out.println("\nAvailability updated for Book ID: " + book_id);
	            } catch (SQLException e) {
	                System.err.println("\nFailed to update book availability.");
	                e.printStackTrace();
	            }
	            
	        } else {
	            System.out.println("\nNo active loan found for Member ID: " + member_id + " and Book ID: " + book_id);
	        }
	    } catch (SQLException exception) {
	        System.err.println("\nFailed to return loan.");
	        exception.printStackTrace();
	    }
	}
	
	public List<Loan> history(int member_id) throws SQLException {
	    String sql = "SELECT l.id, l.book_id, b.title, l.loan_date, l.return_date, l.returned "
	            + "FROM tbl_loans l "
	            + "JOIN tbl_books b ON l.book_id = b.id "
	            + "WHERE l.member_id = ?";
	    
	    List<Loan> loans = new ArrayList<>();
	    
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement psmt = conn.prepareStatement(sql)) {
	        
	        psmt.setInt(1, member_id);
	        
	        try (ResultSet rs = psmt.executeQuery()) {
	            while (rs.next()) {
	                Loan loan = new Loan(
	                        rs.getInt("l.book_id"),
	                        member_id,
	                        rs.getDate("l.loan_date"),
	                        rs.getDate("l.return_date"),
	                        rs.getBoolean("l.returned")
	                );
	                loan.setId(rs.getInt("l.id"));
	                loans.add(loan);
	            }
	        }
	    } catch (SQLException exception) {
	        System.err.println("\nFailed to fetch loan history.");
	        exception.printStackTrace();
	        throw exception;
	    }
	    
	    return loans;
	}
	
	public boolean checkAvailableness(int book_id) throws SQLException {
	    String sql = "SELECT COUNT(*) AS count FROM tbl_loans WHERE book_id = ? AND returned = false";
	    
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement psmt = conn.prepareStatement(sql)) {
	        
	        psmt.setInt(1, book_id);
	        
	        try (ResultSet resultSet = psmt.executeQuery()) {
	            if (resultSet.next()) {
	                int count = resultSet.getInt("count");
	                return count == 0; 
	            } else {
	                System.out.println("\nBook with ID " + book_id + " not found in loans.");
	            }
	        }
	    } catch (SQLException exception) {
	        System.err.println("\nError checking availability for book ID: " + book_id);
	        exception.printStackTrace();
	    }
	    return false;
	}	
}
