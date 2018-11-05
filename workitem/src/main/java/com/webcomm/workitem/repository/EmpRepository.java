package com.webcomm.workitem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.Emp;

@Repository
public interface EmpRepository extends JpaRepository<Emp, Long> {

}
