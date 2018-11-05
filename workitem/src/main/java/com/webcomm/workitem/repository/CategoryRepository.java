package com.webcomm.workitem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webcomm.workitem.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category getFirstByDescription(String s);

}
