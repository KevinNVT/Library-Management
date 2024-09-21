package com.autozone.models;
import com.autozone.annotations.MembersName;
import com.autozone.annotations.NotEmpty;
import com.autozone.annotations.NotNull;

public class Member {
	private int id;
	@NotNull
	@NotEmpty
	@MembersName
	private String member_name;
	
	public Member(int id, String member_name) {
		this.member_name = member_name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
}
