package com.banking.service;

import java.util.List;

import com.banking.entities.Country;

public interface CountryService {
	
	Country saveCountry(Country country);
	Country updateCountry(Integer countryId, Country country);
	List<Country> getAllCountry();
	Country getCountryById(Integer countryId);
	int deleteCountry(Integer countryId);

}
