package com.webcomm.workitem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webcomm.workitem.model.CategoryDetail;

@Repository
public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, Long> {

	public CategoryDetail getFirstByDescription(String s);

}
