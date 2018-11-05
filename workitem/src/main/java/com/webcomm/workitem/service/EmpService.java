package com.webcomm.workitem.service;

import java.util.List;

import com.webcomm.workitem.model.Emp;

public interface EmpService {
	public List<Emp> findAll();

	public Emp addOne(Emp emp);

	public Emp getOne(Long pkEmp);

	public void delete(Emp emp);

	public Emp update(Emp emp);
}
