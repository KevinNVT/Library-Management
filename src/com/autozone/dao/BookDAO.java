package com.autozone.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.autozone.database.DatabaseConnection;
import com.autozone.models.Book;
import com.autozone.utils.Validator;

public class BookDAO {

	public void addBook(Book book) throws SQLException, IllegalArgumentException, IllegalAccessException {
		Validator.validate(book);
		book.setAvailable(true);
		
		String sql = "INSERT INTO tbl_books (title, author, isbn, available) VALUES (?,?,?,?)";
		
		//try-with-resources
		try(Connection conn = DatabaseConnection.getInstance().getConnection();
				PreparedStatement psmt = conn.prepareStatement(sql)) {
			
			psmt.setString(1, book.getTitle());
			psmt.setString(2, book.getAuthor());
			psmt.setString(3, book.getIsbn());
			psmt.setBoolean(4, book.isAvailable());

			psmt.executeUpdate();
		}	
	}
	
	public void updateBook(Book book) throws SQLException, IllegalArgumentException, IllegalAccessException {
		Validator.validate(book);
		
		String sql = "UPDATE tbl_books SET title = ?, author = ?, isbn = ?, available = ? WHERE id = ?";
		
		//try-with-resources
		try(Connection conn = DatabaseConnection.getInstance().getConnection();
				PreparedStatement psmt = conn.prepareStatement(sql)) {
			
			psmt.setString(1, book.getTitle());
			psmt.setString(2, book.getAuthor());
			psmt.setString(3, book.getIsbn());
			psmt.setBoolean(4, book.isAvailable());
			psmt.setInt(5, book.getId());
			
			psmt.executeUpdate();
		}
	}
	
	// I decided to delete books by ISBN instead of ID
	public void deleteBook(String isbn) throws SQLException {
		String sql = "DELETE FROM tbl_books WHERE isbn = ?";

		//try-with-resources
		try(Connection conn = DatabaseConnection.getInstance().getConnection();
				PreparedStatement psmt = conn.prepareStatement(sql)) {
			
			psmt.setString(1, isbn);	
			psmt.executeUpdate();
		}	
	}
	
	public List<Book> findAll() throws SQLException {
		String sql = "SELECT * FROM tbl_books";
		List<Book> books = new ArrayList<Book>();

		//try-with-resources
		try(Connection conn = DatabaseConnection.getInstance().getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
				
			Book book = null;
			
			while(rs.next()) {
				book = new Book(
						rs.getString("title"), 
						rs.getString("author"), 
						rs.getString("isbn"), 
						rs.getBoolean("available")
				);

				book.setId(rs.getInt("id"));
				books.add(book);
			}
		}	
		return books;
	}
	
    public Book findBookById(int id) throws SQLException {
        String sql = "SELECT * FROM tbl_books WHERE id = ?";
        Book book = null;

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    book = new Book(
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("available")
                    );
                    book.setId(rs.getInt("id"));
                } else {
                	System.out.println("\nBook with ID " + id + " not found.");
                }
            }
        }

        return book;
    }

	public List<Book> findByTitle(String title) throws SQLException {
	    String sql = "SELECT * FROM tbl_books WHERE title LIKE ?";
	    /*I instantiated the list without explicitly specifying the type of elements it will contain. 
	    This is a test for educational purposes*/
	    List<Book> books = new ArrayList<>();

	    // try-with-resources
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, "%" + title + "%");
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Book book = new Book(
	                    rs.getString("title"),
	                    rs.getString("author"),
	                    rs.getString("isbn"),
	                    rs.getBoolean("available")
	                );
	                
	                book.setId(rs.getInt("id"));
	                books.add(book);
	            }
	        }
	    }
	    return books;
	}
	
	public List<Book> findByAuthor(String author) throws SQLException {
	    String sql = "SELECT * FROM tbl_books WHERE author LIKE ?";
	    /*I instantiated the list without explicitly specifying the type of elements it will contain. 
	    This is a test for educational purposes*/
	    List<Book> books = new ArrayList<>();

	    // try-with-resources
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, "%" + author + "%");
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Book book = new Book(
	                    rs.getString("title"),
	                    rs.getString("author"),
	                    rs.getString("isbn"),
	                    rs.getBoolean("available")
	                );
	                
	                book.setId(rs.getInt("id"));
	                books.add(book);
	            }
	        }
	    }
	    return books;
	}
	
	public List<Book> findByIsbn(String isbn) throws SQLException {
		
		String sql = "SELECT * FROM tbl_books WHERE isbn = ?";
	    List<Book> books = new ArrayList<>();

	    // try-with-resources
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	    	pstmt.setString(1, isbn);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Book book = new Book(
	                    rs.getString("title"),
	                    rs.getString("author"),
	                    rs.getString("isbn"),
	                    rs.getBoolean("available")
	                );
	                
	                book.setId(rs.getInt("id"));
	                books.add(book);
	            }
	        }
	    }
	    return books;
	}
	
	public void updateLoanStatusTrue(int id) {
	    String updateLoansQuery = "UPDATE tbl_loans SET return_date = ?, returned = ? WHERE book_id = ? AND returned = false";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement psmt = conn.prepareStatement(updateLoansQuery)) {

	        psmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
	        psmt.setBoolean(2, true);
	        psmt.setInt(3, id);

	        int updatedRows = psmt.executeUpdate();

	        if (updatedRows > 0) {
	            System.out.println("\nLoan returned successfully for Book ID: " + id);
	        } else {
	            System.out.println("\nNo active loan found for Book ID: " + id);
	        }
	    } catch (SQLException exception) {
	        System.err.println("\nFailed to update loan status.");
	        exception.printStackTrace();
	    }
	}
	
	public void updateLoanStatusFalse(int id) {
	    String updateLoansQuery = "UPDATE tbl_loans SET return_date = ?, returned = ? WHERE book_id = ? AND returned = false";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement psmt = conn.prepareStatement(updateLoansQuery)) {

	        psmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
	        psmt.setBoolean(2, false);
	        psmt.setInt(3, id);

	        int updatedRows = psmt.executeUpdate();

	        if (updatedRows > 0) {
	            System.out.println("\nLoan returned successfully for Book ID: " + id);
	        } else {
	            System.out.println("\nNo active loan found for Book ID: " + id);
	        }
	    } catch (SQLException exception) {
	        System.err.println("\nFailed to update loan status.");
	        exception.printStackTrace();
	    }
	}
	
	public boolean hasActiveLoan(int bookId) {
	    String query = "SELECT COUNT(*) FROM tbl_loans WHERE book_id = ? AND returned = false";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement psmt = conn.prepareStatement(query)) {

	        psmt.setInt(1, bookId);

	        try (ResultSet rs = psmt.executeQuery()) {
	            if (rs.next()) {
	                int count = rs.getInt(1);
	                return count > 0;
	            }
	        }
	    } catch (SQLException exception) {
	        System.err.println("\nFailed to check active loan status.");
	        exception.printStackTrace();
	    }
	    return false;
	}
}
