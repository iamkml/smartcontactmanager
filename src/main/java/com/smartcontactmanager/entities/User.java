package com.smartcontactmanager.entities;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="user_details")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int uId;
	@NotBlank(message = "Name field is required !!")
	@Size(min = 2, max = 20,message = "name should be between 2 - 20 character")
	private String uName;
	
	@Column(unique = true)
	private String uEmail;
	private String uPassword;
	private String uRole;
	private boolean uStatus;
	private String uImageUrl;
	@Column(length = 500)
	private String uAbout;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user",orphanRemoval = true)
	private List<Contact> contacts=new ArrayList<Contact>();
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getuEmail() {
		return uEmail;
	}
	public void setuEmail(String uEmail) {
		this.uEmail = uEmail;
	}
	
	public String getuPassword() {
		return uPassword;
	}
	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}
	public String getuRole() {
		return uRole;
	}
	public void setuRole(String uRole) {
		this.uRole = uRole;
	}
	public boolean isuStatus() {
		return uStatus;
	}
	public void setuStatus(boolean uStatus) {
		this.uStatus = uStatus;
	}
	public String getuImageUrl() {
		return uImageUrl;
	}
	public void setuImageUrl(String uImageUrl) {
		this.uImageUrl = uImageUrl;
	}
	public String getuAbout() {
		return uAbout;
	}
	public void setuAbout(String uAbout) {
		this.uAbout = uAbout;
	}
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	@Override
	public String toString() {
		return "User [uId=" + uId + ", uName=" + uName + ", uEmail=" + uEmail + ", uPassword=" + uPassword + ", uRole="
				+ uRole + ", uStatus=" + uStatus + ", uImageUrl=" + uImageUrl + ", uAbout=" + uAbout + ", contacts="
				+ contacts + "]";
	}
	
	
	
}
