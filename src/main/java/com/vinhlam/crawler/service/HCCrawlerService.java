package com.vinhlam.crawler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinhlam.crawler.entity.Company;
import com.vinhlam.crawler.repository.CompanyRepository;

@Service
public class HCCrawlerService {

	@Autowired
	private CompanyRepository companyRepository;
	
	Company company;
	
	@Autowired
	public void MistoreCrawlerService() {
		company = new Company();
		company.setName("Hc");
//		company.setLink("https://hc.com.vn/");
		company.setStatus(1);
		company.setDescription("Cửa hàng nhỏ");
	}
}
