package com.autozone.models;

import com.autozone.annotations.Isbn;
import com.autozone.annotations.NotEmpty;
import com.autozone.annotations.NotNull;

public class Book {

	private int id;
	@NotNull
	@NotEmpty
	private String title;
	@NotNull
	@NotEmpty
	private String author;
	@Isbn
	private String isbn;
	private boolean available = true;
	
	public Book(String title, String author, String isbn, boolean available) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.available = available;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
