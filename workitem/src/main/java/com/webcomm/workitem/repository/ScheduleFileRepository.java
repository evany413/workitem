package com.webcomm.workitem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webcomm.workitem.model.ScheduleFile;

@Repository
public interface ScheduleFileRepository extends JpaRepository<ScheduleFile, Long> {

	public List<ScheduleFile> findByIsActive(boolean isActive);

}
