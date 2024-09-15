package com.autozone.models;

import java.sql.Date;

import com.autozone.annotations.NotNull;

public class Loan {

	private int id;
	@NotNull
	private int book_id;
	@NotNull
	private int member_id;
	@NotNull
	private Date loan_date;
	private Date return_date;
	private boolean returned = false;
	
	public Loan(int book_id, int member_id, Date loan_date, Date return_date, boolean returned) {
		this.book_id = book_id;
		this.member_id = member_id;
		this.loan_date = loan_date;
		this.return_date = return_date;
		this.returned = returned;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public Date getLoan_date() {
		return loan_date;
	}

	public void setLoan_date(Date loan_date) {
		this.loan_date = loan_date;
	}

	public Date getReturn_date() {
		return return_date;
	}

	public void setReturn_date(Date return_date) {
		this.return_date = return_date;
	}

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(boolean returned) {
		this.returned = returned;
	}
}
