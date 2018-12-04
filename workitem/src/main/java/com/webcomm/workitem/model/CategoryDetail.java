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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "CATEGORY_DETAIL")
public class CategoryDetail implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoryDetailSeq")
	@SequenceGenerator(name = "categoryDetailSeq", sequenceName = "CATEGORY_DETAIL_SEQ")
	@Column(name = "PK_CATEGORY_DETAIL", length = 20)
	private Long pkCategoryDetail;

	/* 分類細項名稱 */
	@NotEmpty(message = "分類細項名稱不能為空")
	@Length(max = 100, message = "分類細項名稱長度不得超過100字")
	@Column(name = "DESCRIPTION", length = 300)
	private String description;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", updatable = false)
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@OneToMany(mappedBy = "categoryDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("createDate DESC")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Item> items;

	@NotNull(message = "請選擇類別")
	@ManyToOne
	@JoinColumn(name = "FK_CATEGORY")
	private Category category;

	/* 使用者 */
	@ManyToOne
	@JoinColumn(name = "FK_PCC_DEVELOPER")
	private PccDeveloper pccDeveloper;

	public Long getPkCategoryDetail() {
		return pkCategoryDetail;
	}

	public void setPkCategoryDetail(Long pkCategoryDetail) {
		this.pkCategoryDetail = pkCategoryDetail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public PccDeveloper getPccDeveloper() {
		return pccDeveloper;
	}

	public void setPccDeveloper(PccDeveloper pccDeveloper) {
		this.pccDeveloper = pccDeveloper;
	}

}
