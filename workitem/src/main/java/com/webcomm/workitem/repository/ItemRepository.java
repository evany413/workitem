package com.webcomm.workitem.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webcomm.workitem.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	public Item findFirstByOrderByCreateDateDesc();

	public Item findFirstByOrderByCreateDateAsc();
	
}
