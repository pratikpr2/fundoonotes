package com.bridgelabz.fundoonotes.notes.model;

public class EditNoteDto {

	private String title;
	private String body;
	private String color;
	private DateDto reminder;
	
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
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public DateDto getReminder() {
		return reminder;
	}
	public void setReminder(DateDto reminder) {
		this.reminder = reminder;
	}
	
}
