package com.webcomm.workitem.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webcomm.workitem.model.PccDeveloper;
import com.webcomm.workitem.repository.PccDeveloperRepository;
import com.webcomm.workitem.service.PccDeveloperService;

@Service
public class PccDeveloperServiceImpl implements PccDeveloperService {

	@Autowired
	PccDeveloperRepository repo;

	@Override
	public List<PccDeveloper> findAll() {
		return repo.findAll();
	}

	@Override
	public PccDeveloper getOne(Long pkPccDeveloper) {
		return repo.getOne(pkPccDeveloper);
	}

	@Override
	public PccDeveloper addOne(PccDeveloper pccDeveloper) {
		return repo.save(pccDeveloper);
	}

	@Override
	public void delete(PccDeveloper pccDeveloper) {
		repo.delete(pccDeveloper);
	}

	@Override
	public PccDeveloper update(PccDeveloper pccDeveloper) {
		return repo.save(pccDeveloper);
	}

}
