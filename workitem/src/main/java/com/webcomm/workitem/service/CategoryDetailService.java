package com.webcomm.workitem.service;

import java.util.List;

import com.webcomm.workitem.model.CategoryDetail;

public interface CategoryDetailService {

	public List<CategoryDetail> findAll();

	public CategoryDetail getOne(Long pkCategoryDetail);

	public CategoryDetail addOne(CategoryDetail categoryDetail);

	public void delete(CategoryDetail categoryDetail);

	public CategoryDetail update(CategoryDetail categoryDetail);

	public CategoryDetail findDayOff();

}
