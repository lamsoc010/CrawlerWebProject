package com.vinhlam.crawler.entity.enumShare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public enum EnumCategory {
	IPHONE, 
	SAMSUNG, 
	SONY, 
	NOKIA, 
	LG, 
	XIAOMI("Xiaomi"), 
	VIVO, 
	OPPO;
	
	public String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
}
