package com.webcomm.workitem.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.Schedule;
import com.webcomm.workitem.repository.ScheduleRepository;
import com.webcomm.workitem.service.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	ScheduleRepository repo;

	@Override
	public Integer getWorkDayCount(Date startdate, Date lastDate) {
		if (null == startdate)
			return 0;
		return repo.countBySkdDateBetweenAndIsDayOffEquals(startdate, lastDate, 0);
	}

	@Override
	public List<Schedule> getByYear(Integer year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date startDate = calendar.getTime(); // xxxx/01/01
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		Date endDate = calendar.getTime(); // xxxx/12/31
		return repo.findBySkdDateBetweenOrderBySkdDate(startDate, endDate);
	}

}
