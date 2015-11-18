package com.cd.crashdata;

public class Contacts {
	
	private int id;
	private String respondent;
	private String number;
	
	public Contacts(){}
	
	public Contacts(int id, String respondent, String number){
		this.id = id;
		this.respondent = respondent;
		this.number = number;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setRespondent(String respondent){
		this.respondent = respondent;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getRespondent(){
		return this.respondent;
	}
	
	public String getNumber(){
		return this.number;
	}

}
