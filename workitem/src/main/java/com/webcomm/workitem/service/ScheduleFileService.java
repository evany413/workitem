package com.webcomm.workitem.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.webcomm.workitem.model.ScheduleFile;

public interface ScheduleFileService {

	public List<ScheduleFile> findAll();

	public ScheduleFile storeFile(MultipartFile file) throws Exception;

	public ScheduleFile getFile(Long fileId);

	public void deleteFile(Long fileId);

}
