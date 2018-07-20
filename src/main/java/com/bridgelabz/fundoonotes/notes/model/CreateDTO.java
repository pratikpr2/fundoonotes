package com.bridgelabz.fundoonotes.notes.model;

public class CreateDTO {

	private String title;
	private String body;
	private String color;
	private String Reminder;
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getReminder() {
		return Reminder;
	}
	public void setReminder(String reminder) {
		Reminder = reminder;
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
	
}
