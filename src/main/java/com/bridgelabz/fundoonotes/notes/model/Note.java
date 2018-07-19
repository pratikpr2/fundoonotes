package com.bridgelabz.fundoonotes.notes.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="notes")
public class Note {

	private String noteId;
	private String userId;
	private String title;
	private String body;
	private Date createdAt;
	private Date lastModified;
	private Date Reminder;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
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
	public String getNoteId() {
		return noteId;
	}
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
	
	
	
}
