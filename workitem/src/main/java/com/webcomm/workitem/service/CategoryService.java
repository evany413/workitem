package com.webcomm.workitem.service;

import java.util.List;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.model.PccDeveloper;

public interface CategoryService {
	public List<Category> findAll();

	public Category addOne(Category category);

	public Category getOne(Long pkCategory);

	public List<Category> findAllWithoutDayOff();

	public Category findDayOff();

	public void delete(Category category);

	public Category update(Category category);

	public List<Category> findAll(long id);

	public List<Category> findAllWithoutDayOff(long id);

	public Category findDayOff(long id);

	public Category addDayOff(PccDeveloper user);
}
