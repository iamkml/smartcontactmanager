package com.smartcontactmanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="contact_details")
public class Contact {
	@Id()
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	private String cFirstName;
	private String cSecondName;
	private String cWork;
	private String cEmail;
	private String cPhone;
	private String cImage;
	
	@Column(length = 50000)
	private String cDescription;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getcId() {
		return cId;
	}


	public void setcId(int cId) {
		this.cId = cId;
	}


	public String getcFirstName() {
		return cFirstName;
	}


	public void setcFirstName(String cFirstName) {
		this.cFirstName = cFirstName;
	}



	public String getcSecondName() {
		return cSecondName;
	}


	public void setcSecondName(String cSecondName) {
		this.cSecondName = cSecondName;
	}


	public String getcWork() {
		return cWork;
	}


	public void setcWork(String cWork) {
		this.cWork = cWork;
	}


	public String getcEmail() {
		return cEmail;
	}


	public void setcEmail(String cEmail) {
		this.cEmail = cEmail;
	}


	public String getcPhone() {
		return cPhone;
	}


	public void setcPhone(String cPhone) {
		this.cPhone = cPhone;
	}


	public String getcImage() {
		return cImage;
	}


	public void setcImage(String cImage) {
		this.cImage = cImage;
	}


	public String getcDescription() {
		return cDescription;
	}


	public void setcDescription(String cDescription) {
		this.cDescription = cDescription;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	/*
	 * @Override public String toString() { return "Contact [cId=" + cId +
	 * ", cFirstName=" + cFirstName + ", cSecondName=" + cSecondName + ", cWork=" +
	 * cWork + ", cEmail=" + cEmail + ", cPhone=" + cPhone + ", cImage=" + cImage +
	 * ", cDescription=" + cDescription + ", user=" + user + "]"; }
	 */
	
	public boolean equals(Object obj) {
		return this.cId==((Contact)obj).getcId();
	}
	
}
