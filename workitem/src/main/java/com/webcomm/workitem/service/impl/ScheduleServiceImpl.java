package com.webcomm.workitem.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.repository.ScheduleRepository;
import com.webcomm.workitem.service.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	ScheduleRepository repo;

	@Override
	public Integer getWorkDayCount(Date startdate, Date lastDate) {
		return repo.countBySkdDateBetweenAndIsDayOffEquals(startdate, lastDate, 0);
	}

}
