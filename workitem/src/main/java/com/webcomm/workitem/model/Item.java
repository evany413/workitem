package com.webcomm.workitem.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "ITEM")
public class Item implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemSeq")
	@SequenceGenerator(name = "itemSeq", sequenceName = "ITEM_SEQ")
	@Column(name = "PK_ITEM", length = 20)
	private Long pkItem;

	@NotNull(message = "請選擇類別")
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "FK_CATEGORY_DETAIL")
	private CategoryDetail categoryDetail;

	/* 使用者 */
	@NotNull(message = "請選擇使用者")
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "FK_PCC_DEVELOPER")
	private PccDeveloper pccDeveloper;

	/* 工作內容 */
	@NotEmpty(message = "工作內容不可為空")
	@Column(name = "Content", length = 300)
	private String content;

	/* 工作時數 */
	@NotNull(message = "工作時數不可為空")
	@Range(min = 0, max = 999, message = "工作時數範圍必須界於0-999")
	@Column(name = "WORK_TIME", length = 100)
	private Integer workTime;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", updatable = false)
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	public Long getPkItem() {
		return pkItem;
	}

	public void setPkItem(Long pkItem) {
		this.pkItem = pkItem;
	}

	public CategoryDetail getCategoryDetail() {
		return categoryDetail;
	}

	public void setCategoryDetail(CategoryDetail categoryDetail) {
		this.categoryDetail = categoryDetail;
	}

	public PccDeveloper getPccDeveloper() {
		return pccDeveloper;
	}

	public void setPccDeveloper(PccDeveloper pccDeveloper) {
		this.pccDeveloper = pccDeveloper;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Integer workTime) {
		this.workTime = workTime;
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
