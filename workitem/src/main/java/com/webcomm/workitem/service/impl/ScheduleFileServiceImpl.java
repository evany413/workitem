package com.webcomm.workitem.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.webcomm.workitem.model.ScheduleFile;
import com.webcomm.workitem.repository.ScheduleFileRepository;
import com.webcomm.workitem.service.ScheduleFileService;

@Service
public class ScheduleFileServiceImpl implements ScheduleFileService {

	@Autowired
	ScheduleFileRepository repo;

	@Override
	public List<ScheduleFile> findAll() {
		return repo.findAll();
	}

	@Override
	public ScheduleFile storeFile(MultipartFile file) throws Exception {
		if (null == file || file.getSize() == 0) {
			throw new Exception("file is empty");
		}
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
			}
			ScheduleFile dbFile = new ScheduleFile(fileName, file.getContentType(), file.getBytes());
			System.out.println("----儲存資料中----");
			System.out.println("資料名稱： " + fileName);
			System.out.println("資料類型： " + file.getContentType());
			System.out.println("資料大小： " + file.getSize());
			return repo.save(dbFile);
		} catch (IOException e) {
			throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
		}

	}

	@Override
	public ScheduleFile getFile(Long fileId) {
		ScheduleFile dbFile = repo.getOne(fileId);
		if (null == dbFile) {
			throw new RuntimeException("File not found with id " + fileId);
		} else {
			return dbFile;
		}
	}

	@Override
	public void deleteFile(Long fileId) {
		repo.deleteById(fileId);
	}

}
