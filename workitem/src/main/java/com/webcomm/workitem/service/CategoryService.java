package com.webcomm.workitem.service;

import java.util.List;

import com.webcomm.workitem.model.Category;

public interface CategoryService {
	public List<Category> findAll();

	public Category addOne(Category category);

	public Category getOne(Long pkCategory);

	List<Category> findAllWithoutDayOff();

	Category findDayOff();

	public void delete(Category category);

	public Category update(Category category);
}
