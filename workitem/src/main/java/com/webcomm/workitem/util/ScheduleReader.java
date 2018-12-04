package com.webcomm.workitem.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcomm.workitem.model.Schedule;
import com.webcomm.workitem.model.ScheduleFile;
import com.webcomm.workitem.repository.ScheduleFileRepository;
import com.webcomm.workitem.repository.ScheduleRepository;

@Component
public class ScheduleReader {

	@Autowired
	ScheduleRepository repo;
	@Autowired
	ScheduleFileRepository fileRepo;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	ObjectMapper mapper = new ObjectMapper();
	
	public void readSkdFile() throws IOException {
		List<ScheduleFile> fileList = fileRepo.findByIsActive(false);
		System.out.println(mapper.writeValueAsString(fileList));

		List<Schedule> scheduleList = new ArrayList<Schedule>();
		scheduleList = saveToSKDList(fileList);
		repo.saveAll(scheduleList);

		for (ScheduleFile f : fileList) {
			f.setActive(true);
			fileRepo.save(f);
		}

	}

	public List<Schedule> saveToSKDList(List<ScheduleFile> fileList) {
		List<Schedule> returnList = new ArrayList<Schedule>();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Schedule schedule;
		try {
			for (ScheduleFile f : fileList) {
				InputStream input = new ByteArrayInputStream(f.getData());
				br = new BufferedReader(new InputStreamReader(input, "big5"));
				while ((line = br.readLine()) != null) {
					String[] skd = line.split(cvsSplitBy, -1);// add -1 so the last empty string will not be discarded
					try {
						schedule = new Schedule();
						schedule.setSkdDate(sdf.parse(skd[0]));// 西元日期
						schedule.setSkdWeekDay(skd[1]);// 星期
						schedule.setIsDayOff(Integer.parseInt(skd[2]));// 是否放假
						schedule.setNote(skd[3]);// 備註
						System.out.println(mapper.writeValueAsString(schedule));
						returnList.add(schedule);
					} catch (ParseException pe) {
						System.out.println(pe.getMessage());
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnList;
	}

}
