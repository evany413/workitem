package com.webcomm.workitem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkitemApplicationTests {

	@Test
	public void contextLoads() {

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
