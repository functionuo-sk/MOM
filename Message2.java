package com.mom_management.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.UpdateTimestamp;

import com.mom_management.ennum.Status;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
@Entity
public class Message2 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String senderName;
	private String receiverName;
	
	private String groupName;

//	@ManyToOne
//	@JoinColumn(name = "sender_id")
////	// @JsonIgnoreProperties({ "username","firstName","lastName",
////	"active", "email", "token", "department", "role",
//////	        "created_By", "createdDate", "updated_By",
////	"updated_Date","sender","sentMessages"})
//	private UserDao senderName;
//	
//	@ManyToOne
//	@JoinColumn(name = "receiver_id")
//	@JsonIgnoreProperties({ "email", "username", "firstName", "lastName",
//	"active", "token", "department", "role",
//	"created_By", "createdDate", "updated_By", "updated_Date" })
//	private UserDao receiverName;
	private String message;
	@UpdateTimestamp
	private LocalDateTime date;
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Message2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Message2(Long id, String senderName, String receiverName, String groupName, String message,
			LocalDateTime date, Status status) {
		super();
		this.id = id;
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.groupName = groupName;
		this.message = message;
		this.date = date;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Message2 [id=" + id + ", senderName=" + senderName + ", receiverName=" + receiverName + ", groupName="
				+ groupName + ", message=" + message + ", date=" + date + ", status=" + status + "]";
	}

	

}
