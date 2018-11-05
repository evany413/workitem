package com.webcomm.workitem.util;

import java.util.List;

import com.webcomm.workitem.model.Item;

public class CommonUtil {

	public static Integer getAllWorkHour(List<Item> list) {
		Integer total = 0;
		for (Item item : list) {
			total += item.getWorkTime();
		}
		return total;
	}

}
