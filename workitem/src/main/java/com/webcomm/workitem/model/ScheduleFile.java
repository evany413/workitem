package com.webcomm.workitem.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SCHEDULE_FILE")
public class ScheduleFile {
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheduleFileSeq")
	@SequenceGenerator(name = "scheduleFileSeq", sequenceName = "SCHEDULE_FILE_SEQ")
	@Column(name = "PK_SCHEDULE_FILE", length = 20)
	private Long pkScheduleFile;

	/* 檔名 */
	@Column(name = "FILE_NAME")
	private String fileName;

	/* 檔案類型 */
	@JsonIgnore
	@Column(name = "FILE_TYPE")
	private String fileType;

	/* 檔案 */
	@JsonIgnore
	@Lob
	@Column(name = "DATA")
	private byte[] data;

	/* 是否匯入schedule */
	@JsonIgnore
	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@JsonIgnore
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@JsonIgnore
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	public ScheduleFile() {
		super();
	}

	public ScheduleFile(String fileName, String fileType, byte[] data) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
	}

	public Long getPkScheduleFile() {
		return pkScheduleFile;
	}

	public void setPkScheduleFile(Long pkScheduleFile) {
		this.pkScheduleFile = pkScheduleFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@JsonIgnore
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	@JsonIgnore
	public String getSize() {
		if (null == this.data) {
			return "no file";
		} else {
			return String.valueOf((this.data.length) / (1024)) + "Kb";
		}
	}

}
