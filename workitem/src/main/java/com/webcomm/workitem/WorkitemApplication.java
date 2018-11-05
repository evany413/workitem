package com.webcomm.workitem;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.webcomm.workitem.util.ScheduleReader;

@SpringBootApplication
public class WorkitemApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(WorkitemApplication.class, args);
		try {
			context.getBean(ScheduleReader.class).readSkdFile();
		} catch (BeansException | IOException e) {
			e.printStackTrace();
		}
	}
}
