package com.bridgelabz.fundoonotes.notes.model;

import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="labelindex",type="label")
public class Label {

	@Id
	private String labelId;
	private String userId;
	private String lableName;
	
	
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLableName() {
		return lableName;
	}
	public void setLableName(String lableName) {
		this.lableName = lableName;
	}
	
}
