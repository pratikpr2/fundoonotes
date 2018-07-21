package com.bridgelabz.fundoonotes.notes.model;

import java.util.Date;

public class ViewNoteDto {

	private String title;
	private String body;
	private Date createdAt;
	private Date lastModified;
	private Date Reminder;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public Date getReminder() {
		return Reminder;
	}
	public void setReminder(Date reminder) {
		Reminder = reminder;
	}
	
	
	
}
