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
import com.autozone.models.Member;
import com.autozone.utils.Validator;

public class MemberDAO {

	public void addMember(Member member) throws SQLException, IllegalArgumentException, IllegalAccessException {
		Validator.validate(member);
		
		String sql = "INSERT INTO tbl_members (member_name) VALUES (?)";
		
		//try-with-resources
		try(Connection conn = DatabaseConnection.getInstance().getConnection();
				PreparedStatement psmt = conn.prepareStatement(sql)) {
			
			psmt.setString(1, member.getMember_name());
			//psmt.setString(2, member.getMember_id());
			
			psmt.executeUpdate();
		}	
	}	
	
	public void updateMember(Member member, int id) throws SQLException {
	    String sql = "UPDATE tbl_members SET member_name = ? WHERE id = ?";

	    // try-with-resources
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, member.getMember_name());
	        pstmt.setInt(2, id);

	        pstmt.executeUpdate();
	    }
	}
	
	public void deleteMember(int id) throws SQLException, IllegalArgumentException, IllegalAccessException {
	    // Validates ID is greater than 0
	    if (id <= 0) {
	        throw new IllegalArgumentException("ID must be greater than 0.");
	    }

	    // Verifies the member exists
	    MemberDAO memberDAO = new MemberDAO();
	    Member member = memberDAO.findMemberById(id);
	    if (member == null) {
	        throw new IllegalArgumentException("Member with ID " + id + " does not exist.");
	    }

	    // Deletes the member
	    String sql = "DELETE FROM tbl_members WHERE id = ?";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement psmt = conn.prepareStatement(sql)) {

	        psmt.setInt(1, id);
	        int affectedRows = psmt.executeUpdate();
	        
	        // Validates a member has been eliminated
	        if (affectedRows == 0) {
	            throw new SQLException("Failed to delete member. No rows affected.");
	        }
	    }
	}
	
	public List<Member> findAll() throws SQLException {
	    String sql = "SELECT * FROM tbl_members";
	    List<Member> members = new ArrayList<>();

	    // try-with-resources
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        while (rs.next()) {
	            Member member = new Member(
	                rs.getInt("id"),
	                rs.getString("member_name")
	            );
	            
	            members.add(member);
	        }
	    }
	    return members;
	}
	
	public List<Member> findByName(String member_name) throws SQLException {
	    String sql = "SELECT * FROM tbl_members WHERE member_name LIKE ?";
	    List<Member> members = new ArrayList<>();

	    // try-with-resources
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        pstmt.setString(1, "%" + member_name + "%");
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Member member = new Member(
	                    rs.getInt("id"),
	                    rs.getString("member_name")
	                );
	                
	                members.add(member);
	            }
	        }
	    }
	    return members;
	}
	
	public Member findMemberById(int id) {
	    String query = "SELECT * FROM tbl_members WHERE id = ?";
	    
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        
	        stmt.setInt(1, id);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                Member member = new Member(rs.getInt("id"), rs.getString("member_name"));
	                return member;
	            }
	        }
	    } catch (SQLException exception) {
	    	exception.printStackTrace();
	    }
	    
	    return null;
	}
}
