package com.webcomm.workitem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.Emp;
import com.webcomm.workitem.repository.EmpRepository;
import com.webcomm.workitem.service.EmpService;

@Service
public class EmpServiceImpl implements EmpService {

	@Autowired
	EmpRepository repo;

	@Override
	public List<Emp> findAll() {
		return repo.findAll();
	}

	@Override
	public Emp getOne(Long pkEmp) {
		return repo.getOne(pkEmp);
	}

	@Override
	public Emp addOne(Emp emp) {
		return repo.save(emp);
	}

	@Override
	public void delete(Emp emp) {
		repo.delete(emp);
	}

	@Override
	public Emp update(Emp emp) {
		return repo.save(emp);
	}

}
