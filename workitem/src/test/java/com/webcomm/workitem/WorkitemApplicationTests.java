package com.webcomm.workitem;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcomm.workitem.repository.ScheduleRepository;
import com.webcomm.workitem.service.ItemService;
import com.webcomm.workitem.service.ScheduleService;
import com.webcomm.workitem.util.ScheduleReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkitemApplicationTests {
//	@Autowired
//	EmpRepository rep;

	@Autowired
	ItemService itemService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ScheduleRepository scheduleRepository;
	@Autowired
	ScheduleReader sr;;

	@Test
	public void contextLoads() throws JsonProcessingException {

		Date lastDate = itemService.getLastDate();
		Date startDate = itemService.getStartDate();
		String[] cool = { "sfafhdfhjfmjh", "srthrsth" };
//		System.out.println(startDate);
//		System.out.println(lastDate);

		System.out.println(scheduleService.getWorkDayCount(startDate, lastDate));
		describe(scheduleRepository.findAll());

	}

	public void describe(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String s = mapper.writeValueAsString(obj);
			System.out.println(s);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
