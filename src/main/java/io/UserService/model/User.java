package io.UserService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "tblUser")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {
	
	@Id
	@Column(name = "userid",nullable = false,unique = true)
	private Integer userid;
	@Column(name= "username",nullable = false,unique = true, length = 100)
	private String username;
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public User(Integer userid, String username) {
		this.userid = userid;
		this.username = username;
	}
	public User() {
		
	}
	@Override
	public String toString() {
		return "User [userid=" + userid + ", username=" + username + "]";
	}

}
