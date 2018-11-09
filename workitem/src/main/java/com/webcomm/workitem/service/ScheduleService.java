package com.webcomm.workitem.service;

import java.util.Date;
import java.util.List;

import com.webcomm.workitem.model.Schedule;

public interface ScheduleService {

	public Integer getWorkDayCount(Date startdate, Date lastDate);

	public List<Schedule> getByYear(Integer year);

}
