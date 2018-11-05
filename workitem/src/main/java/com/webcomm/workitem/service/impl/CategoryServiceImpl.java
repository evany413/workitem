package com.webcomm.workitem.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.repository.CategoryRepository;
import com.webcomm.workitem.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository repo;

	@Override
	public List<Category> findAll() {
		return repo.findAll();
	}

	@Override
	public List<Category> findAllWithoutDayOff() {
		List<Category> list = repo.findAll();
		for (Iterator<Category> iter = list.listIterator(); iter.hasNext();) {
			Category c = iter.next();
			if ("休假事項".equals(c.getDescription())) {
				iter.remove();
			}
		}
		return list;
	}

	@Override
	public Category getOne(Long pkCategory) {
		return repo.getOne(pkCategory);
	}

	@Override
	public Category addOne(Category category) {
		return repo.save(category);
	}

	@Override
	public Category findDayOff() {
		String s = "休假事項";
		return repo.getFirstByDescription(s);
	}

	@Override
	public void delete(Category category) {
		repo.delete(category);
	}

	@Override
	public Category update(Category category) {
		return repo.save(category);
	}

}
