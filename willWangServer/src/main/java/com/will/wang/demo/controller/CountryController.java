package com.will.wang.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.will.wang.demo.model.Country;
import com.will.wang.demo.service.CountryService;

@RestController
public class CountryController {

	@Autowired
	private CountryService countryService;
	
	@RequestMapping("/getCountryList")
	public List<Country> getCountryList() {
		return countryService.getCountryList();
	}
	
}
