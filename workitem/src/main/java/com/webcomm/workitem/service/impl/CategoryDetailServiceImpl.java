package com.webcomm.workitem.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.model.CategoryDetail;
import com.webcomm.workitem.repository.CategoryDetailRepository;
import com.webcomm.workitem.service.CategoryDetailService;

@Service
public class CategoryDetailServiceImpl implements CategoryDetailService {

	@Autowired
	CategoryDetailRepository repo;

	@Override
	public List<CategoryDetail> findAll() {
		return repo.findAll();
	}

	@Override
	public CategoryDetail getOne(Long pkCategoryDetail) {
		return repo.getOne(pkCategoryDetail);
	}

	@Override
	public CategoryDetail addOne(CategoryDetail categoryDetail) {
		return repo.save(categoryDetail);
	}

	@Override
	public void delete(CategoryDetail categoryDetail) {
		repo.delete(categoryDetail);
	}

	@Override
	public CategoryDetail update(CategoryDetail categoryDetail) {
		return repo.save(categoryDetail);
	}

	@Override
	public CategoryDetail findDayOff() {
		String s = "休假事項";
		return repo.getFirstByDescription(s);
	}

}
