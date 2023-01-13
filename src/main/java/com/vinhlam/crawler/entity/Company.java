package com.vinhlam.crawler.entity;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "company")
public class Company {
	@BsonProperty("_id")
	@BsonId
	private ObjectId id;
	private String urlRoot;
	private String urlAtti;
	private String name;
	private String description;
	private int status;
	private List<Product> products;
}
