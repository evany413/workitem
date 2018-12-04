package com.webcomm.workitem.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.model.CategoryDetail;
import com.webcomm.workitem.model.PccDeveloper;
import com.webcomm.workitem.repository.CategoryDetailRepository;
import com.webcomm.workitem.service.CategoryDetailService;
import com.webcomm.workitem.service.CategoryService;

@Service
public class CategoryDetailServiceImpl implements CategoryDetailService {

	@Autowired
	CategoryDetailRepository repo;

	@Autowired
	CategoryService categoryService;

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

	@Override
	public CategoryDetail findDayOff(PccDeveloper user) {
		String s = "休假事項";
		CategoryDetail detail = repo.getFirstByDescriptionAndPccDeveloper(s, user);
		if (detail == null) {
			detail = new CategoryDetail();
			detail.setPccDeveloper(user);
			Category c = categoryService.addDayOff(user);
			System.out.println(c.getPkCategory());
			detail.setCategory(c);
			detail.setDescription(s);
			detail = repo.save(detail);
		}
		return detail;
	}

	@Override
	public List<CategoryDetail> findAllWithoutDayOff() {
		List<CategoryDetail> list = repo.findAll();
		for (Iterator<CategoryDetail> iter = list.listIterator(); iter.hasNext();) {
			CategoryDetail cd = iter.next();
			if ("休假事項".equals(cd.getDescription())) {
				iter.remove();
			}
		}
		return list;
	}

	@Override
	public void deleteById(long pkCategoryDetail) {
		repo.deleteById(pkCategoryDetail);
	}

	@Override
	public List<CategoryDetail> findAllWithoutDayOff(Long userId) {
		List<CategoryDetail> list = repo.findByPccDeveloper_PkPccDeveloper(userId);
		for (Iterator<CategoryDetail> iter = list.listIterator(); iter.hasNext();) {
			CategoryDetail cd = iter.next();
			if ("休假事項".equals(cd.getDescription())) {
				iter.remove();
			}
		}
		return list;
	}

	@Override
	public List<CategoryDetail> findAll(Long userId) {
		List<CategoryDetail> list = repo.findByPccDeveloper_PkPccDeveloper(userId);
		return list;
	}

}
