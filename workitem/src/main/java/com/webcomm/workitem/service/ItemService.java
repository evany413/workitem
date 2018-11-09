package com.webcomm.workitem.service;

import java.util.Date;
import java.util.List;

import com.webcomm.workitem.model.PccDeveloper;
import com.webcomm.workitem.model.Item;

public interface ItemService {
	public List<Item> findAll();

	public List<Item> findAllAsc();

	public List<Item> findAllWithOutDayOff();

	public List<Item> findAllByPccDeveloperWithOutDayOff(PccDeveloper pccDeveloper);

	public Item addOne(Item item);

	public Item getOne(Long pkItem);

	public Date getLastDate();

	public Date getStartDate();

	public Integer getWorkHours();

	public void delete(Item item);

	public Item update(Item item);

	public List<Item> findAllBetweenDate(Date startDate, Date endDate);

	public Date getStartDateByPccDeveloper(PccDeveloper selectedPccDeveloper);

	public Date getLastDateByPccDeveloper(PccDeveloper selectedPccDeveloper);

	public Integer getPccDeveloperWorkHours(PccDeveloper selectedPccDeveloper);

}
