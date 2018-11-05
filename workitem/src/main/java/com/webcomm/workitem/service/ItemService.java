package com.webcomm.workitem.service;

import java.util.Date;
import java.util.List;

import com.webcomm.workitem.model.Item;

public interface ItemService {
	public List<Item> findAll();

	public Item addOne(Item item);

	public Item getOne(Long pkItem);

	public Date getLastDate();

	public Date getStartDate();

	public Integer getWorkHours();

	public void delete(Item item);

	public Item update(Item item);
}
