package com.webcomm.workitem.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webcomm.workitem.model.PccDeveloper;
import com.webcomm.workitem.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	public Item findFirstByOrderByCreateDateDesc();

	public Item findFirstByOrderByCreateDateAsc();

	public List<Item> findByCreateDateBetweenOrderByCreateDateDesc(Date startDate, Date endDate);

	public List<Item> findByPccDeveloperOrderByCreateDateDesc(PccDeveloper pccDeveloper);

	public Item findFirstByPccDeveloperOrderByCreateDateAsc(PccDeveloper selectedPccDeveloper);

	public Item findFirstByPccDeveloperOrderByCreateDateDesc(PccDeveloper selectedPccDeveloper);

	public List<Item> findByPccDeveloper(PccDeveloper selectedPccDeveloper);

}
