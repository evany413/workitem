package com.webcomm.workitem.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "EMP2")
public class Emp2 implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp2Seq")
	@SequenceGenerator(name = "emp2Seq", sequenceName = "EMP2_SEQ")
	@Column(name = "PK_EMP2", length = 20)
	private Long pkEmp2;

	/* 使用者名稱 */
	@NotEmpty(message = "名稱不能為空")
	@Length(max = 15, message = "名稱過長")
	@Column(name = "NAME", length = 50)
	private String name;

	/* 電子信箱 */
	@NotEmpty(message = "信箱不能為空")
	@Email(message = "請輸入信箱")
	@Column(name = "EMAIL", length = 300)
	private String email;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	public Long getPkEmp2() {
		return pkEmp2;
	}

	public void setPkEmp2(Long pkEmp2) {
		this.pkEmp2 = pkEmp2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
