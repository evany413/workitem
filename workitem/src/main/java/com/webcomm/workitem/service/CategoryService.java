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

	public List<Category> findAll(PccDeveloper pccDeveloper);

	public List<Category> findAllWithoutDayOff(PccDeveloper pccDeveloper);

	public Category findDayOff(PccDeveloper pccDeveloper);
}
