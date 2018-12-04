package com.webcomm.workitem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webcomm.workitem.model.CategoryDetail;
import com.webcomm.workitem.model.PccDeveloper;

@Repository
public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, Long> {

	public CategoryDetail getFirstByDescription(String s);

	public List<CategoryDetail> findByPccDeveloper_PkPccDeveloper(long userId);

	public CategoryDetail getFirstByDescriptionAndPccDeveloper(String s, PccDeveloper user);

}
