package com.webcomm.workitem.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.model.PccDeveloper;
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

	@Override
	public List<Category> findAll(long id) {
		return repo.findByPccDeveloper_PkPccDeveloper(id);
	}

	@Override
	public List<Category> findAllWithoutDayOff(long id) {
		List<Category> list = repo.findByPccDeveloper_PkPccDeveloper(id);
		for (Iterator<Category> iter = list.listIterator(); iter.hasNext();) {
			Category c = iter.next();
			if ("休假事項".equals(c.getDescription())) {
				iter.remove();
			}
		}
		return list;
	}

	@Override
	public Category findDayOff(long id) {
		String s = "休假事項";
		return repo.getFirstByDescriptionAndPccDeveloper_PkPccDeveloper(s, id);
	}

	/* 新增休假事項 */
	@Override
	public Category addDayOff(PccDeveloper user) {
		Category category = findDayOff(user.getPkPccDeveloper());
		if (null == category) {
			category = new Category();
			category.setDescription("休假事項");
			category.setPccDeveloper(user);
			category = repo.save(category);
		}
		return category;
	}

}
