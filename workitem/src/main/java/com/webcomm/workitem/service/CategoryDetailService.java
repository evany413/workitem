package com.webcomm.workitem.service;

import java.util.List;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.model.CategoryDetail;
import com.webcomm.workitem.model.PccDeveloper;

public interface CategoryDetailService {

	public List<CategoryDetail> findAll();

	public CategoryDetail getOne(Long pkCategoryDetail);

	public CategoryDetail addOne(CategoryDetail categoryDetail);

	public void delete(CategoryDetail categoryDetail);

	public CategoryDetail update(CategoryDetail categoryDetail);

	public CategoryDetail findDayOff();

	public List<CategoryDetail> findAllWithoutDayOff();

	public void deleteById(long pkCategoryDetail);

	public List<CategoryDetail> findAllWithoutDayOff(Long userId);

	public List<CategoryDetail> findAll(Long userId);

	public CategoryDetail findDayOff(PccDeveloper user);

}
