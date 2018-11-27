package com.webcomm.workitem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webcomm.workitem.model.Category;
import com.webcomm.workitem.model.PccDeveloper;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Category getFirstByDescription(String s);

	public List<Category> findByPccDeveloper(PccDeveloper pccDeveloper);

}
