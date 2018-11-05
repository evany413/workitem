package com.webcomm.workitem.model;

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
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "SCHEDULE")
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheduleSeq")
	@SequenceGenerator(name = "scheduleSeq", sequenceName = "SCHEDULE_SEQ")
	@Column(name = "PK_SCHEDULE", length = 20)
	private Long pkSchedule;

	/* 西元日期 */
	@Column(name = "SKD_DATE")
	private Date skdDate;

	/* 星期 */
	@Column(name = "SKD_WEEK_DAY", length = 9)
	private String skdWeekDay;

	/* 是否放假 */
	@Column(name = "IS_DAY_OFF")
	private Integer isDayOff;

	/* 備註 */
	@Column(name = "NOTE", length = 150)
	private String note;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	public Long getPkSchedule() {
		return pkSchedule;
	}

	public void setPkSchedule(Long pkSchedule) {
		this.pkSchedule = pkSchedule;
	}

	public Date getSkdDate() {
		return skdDate;
	}

	public void setSkdDate(Date skdDate) {
		this.skdDate = skdDate;
	}

	public String getSkdWeekDay() {
		return skdWeekDay;
	}

	public void setSkdWeekDay(String skdWeekDay) {
		this.skdWeekDay = skdWeekDay;
	}

	public Integer getIsDayOff() {
		return isDayOff;
	}

	public void setIsDayOff(Integer isDayOff) {
		this.isDayOff = isDayOff;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
