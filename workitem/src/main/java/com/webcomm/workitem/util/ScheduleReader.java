package com.webcomm.workitem.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webcomm.workitem.model.Schedule;
import com.webcomm.workitem.repository.ScheduleRepository;

@Component
public class ScheduleReader {

	@Autowired
	ScheduleRepository repo;

	ObjectMapper mapper = new ObjectMapper();

	public void readSkdFile() throws IOException {

		File[] files = new ClassPathResource("/schedule/").getFile().listFiles();
		System.out.println(mapper.writeValueAsString(files));

		for (File f : files) {
			saveToTable(f);
		}
	}

	public void saveToTable(File file) {
		BufferedReader br = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String line = "";
		String cvsSplitBy = ",";

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "big5"));
			while ((line = br.readLine()) != null) {
				String[] skd = line.split(cvsSplitBy, -1);// add -1 so the last empty string will not be discarded
				Schedule schedule = new Schedule();
				try {
					schedule.setSkdDate(sdf.parse(skd[0]));// 西元日期
					schedule.setSkdWeekDay(skd[1]);// 星期
					schedule.setIsDayOff(Integer.parseInt(skd[2]));// 是否放假
					schedule.setNote(skd[3]);// 備註
					repo.save(schedule);
				} catch (ParseException e) {
					System.out.println(e.getMessage());
				}
				System.out.println(mapper.writeValueAsString(skd));
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

	}

}
