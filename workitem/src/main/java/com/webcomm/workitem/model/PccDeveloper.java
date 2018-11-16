package com.webcomm.workitem.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "PCC_DEVELOPER")
public class PccDeveloper implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pccDeveloperSeq")
	@SequenceGenerator(name = "pccDeveloperSeq", sequenceName = "PCC_DEVELOPER_SEQ")
	@Column(name = "PK_PCC_DEVELOPER", length = 20)
	private Long pkPccDeveloper;

	/* 使用者名稱 */
	@NotEmpty(message = "名稱不能為空")
	@Length(max = 15, message = "名稱過長")
	@Column(name = "NAME", length = 50)
	private String name;

	/* 電子信箱 */
	@NotEmpty(message = "信箱不能為空")
	@Email(message = "信箱格式錯誤")
	@Column(name = "EMAIL", length = 300)
	private String email;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", updatable = false)
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@OneToMany(mappedBy = "pccDeveloper", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("createDate DESC")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Item> items;

	public Long getPkPccDeveloper() {
		return pkPccDeveloper;
	}

	public void setPkPccDeveloper(Long pkPccDeveloper) {
		this.pkPccDeveloper = pkPccDeveloper;
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

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

}
