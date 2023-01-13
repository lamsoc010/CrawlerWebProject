package com.vinhlam.crawler.repository;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import com.vinhlam.crawler.entity.Company;

@Repository
public class CompanyRepository  {
	@Autowired
	public MongoDatabase mongoDatabase;
	
	public MongoCollection<Company> companyCollection;
	
	
	@Autowired
	public void CompanyRepository() {
		CodecRegistry cRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
				CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		companyCollection = mongoDatabase.getCollection("company", Company.class).withCodecRegistry(cRegistry);

	}
	
	public void insertProductToCompany(Company company) {
		boolean checkExistCompany = insertIfNotExistCompany(company);
		if(!checkExistCompany) {
			Bson match = Filters.eq("name", company.getName());
			Bson update = new Document("$addToSet", 
					new Document("products", 
							new Document("$each", company.getProducts() ) ) );
//			companyCollection.
			UpdateOptions option = new UpdateOptions().upsert(true);
			companyCollection.updateOne(match, update, option);
		} else {
			insertIfNotExistCompany(company);
		}
	}
	
	public boolean insertIfNotExistCompany(Company company) {
		Bson match = Filters.eq("name", company.getName());
		Bson insert = new Document("$setOnInsert", company);
		FindOneAndUpdateOptions option = new FindOneAndUpdateOptions().upsert(true);
		
		Company result = companyCollection.findOneAndUpdate(match, insert, option);
		
		if(result == null) {
			return true;
		} else {
			return false;
		}
		
	}
}
