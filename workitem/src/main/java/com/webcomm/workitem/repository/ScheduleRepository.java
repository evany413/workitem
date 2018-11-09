package com.webcomm.workitem.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webcomm.workitem.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	public Integer countBySkdDateBetweenAndIsDayOffEquals(Date startdate, Date lastDate, Integer isDayOff);

	public List<Schedule> findBySkdDateBetweenOrderBySkdDate(Date startDate, Date endDate);
}
