package com.webcomm.workitem.util;

import java.math.BigDecimal;
import java.util.List;

import com.webcomm.workitem.model.Item;

public class CommonUtil {

	public static BigDecimal getAllWorkHour(List<Item> list) {
		BigDecimal total = new BigDecimal(0.0);
		for (Item item : list) {
			total = total.add(item.getWorkTime());
		}
		return total;
	}

}
