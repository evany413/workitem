package com.webcomm.workitem.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.Item;
import com.webcomm.workitem.repository.ItemRepository;
import com.webcomm.workitem.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ItemRepository repo;

	@Override
	public List<Item> findAll() {
		return repo.findAll(new Sort(Sort.Direction.DESC, "createDate"));
	}

	@Override
	public Item getOne(Long pkItem) {
		return repo.getOne(pkItem);
	}

	@Override
	public Item addOne(Item item) {
		return repo.save(item);
	}

	@Override
	public Date getLastDate() {
		return repo.findFirstByOrderByCreateDateDesc().getCreateDate();
	}

	@Override
	public Date getStartDate() {
		return repo.findFirstByOrderByCreateDateAsc().getCreateDate();
	}

	@Override
	public Integer getWorkHours() {
		Integer total = 0;
		List<Item> list = repo.findAll();
		for (Item item : list) {
			total += item.getWorkTime();
		}
		return total;
	}

	@Override
	public void delete(Item item) {
		repo.delete(item);
	}

	@Override
	public Item update(Item item) {
		return repo.save(item);
	}

}
