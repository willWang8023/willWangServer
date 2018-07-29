package com.will.wang.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.will.wang.demo.dao.CountryMapper;
import com.will.wang.demo.model.Country;
import com.will.wang.demo.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryMapper countryDao;
	
	public List<Country> getCountryList() {
		return countryDao.getCountryList();
	}
	
}
