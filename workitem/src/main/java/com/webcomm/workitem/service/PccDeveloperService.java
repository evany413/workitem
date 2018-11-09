package com.webcomm.workitem.service;

import java.util.List;

import com.webcomm.workitem.model.PccDeveloper;

public interface PccDeveloperService {
	public List<PccDeveloper> findAll();

	public PccDeveloper addOne(PccDeveloper pccDeveloper);

	public PccDeveloper getOne(Long pkPccDeveloper);

	public void delete(PccDeveloper pccDeveloper);

	public PccDeveloper update(PccDeveloper pccDeveloper);
}
