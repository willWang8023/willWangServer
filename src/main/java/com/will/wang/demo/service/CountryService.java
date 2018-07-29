package com.will.wang.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.will.wang.demo.model.Country;

@Service
public interface CountryService {
	
	public List<Country> getCountryList();
	
	

}
