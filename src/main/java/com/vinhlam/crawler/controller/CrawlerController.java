package com.vinhlam.crawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinhlam.crawler.service.MistoreCrawlerService;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {

	@Autowired
	private MistoreCrawlerService mistore;
	
	@GetMapping("/mistore")
	public void crawlerMistore() {
		mistore.crawlerProductInMistore();
	}
}
