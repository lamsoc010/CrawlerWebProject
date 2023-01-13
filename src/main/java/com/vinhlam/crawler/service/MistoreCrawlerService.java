package com.vinhlam.crawler.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinhlam.crawler.entity.Company;
import com.vinhlam.crawler.entity.Product;
import com.vinhlam.crawler.entity.enumShare.EnumCategory;
import com.vinhlam.crawler.repository.CompanyRepository;

@Service
public class MistoreCrawlerService {

	@Autowired
	private CompanyRepository companyRepository;
	
	Company company;
	
	@Autowired
	public void MistoreCrawlerService() {
		company = new Company();
		company.setName("Mistore");
		company.setUrlRoot("https://hn.mistore.com.vn");
		company.setUrlAtti("/collections/dien-thoai-xiaomi");
		company.setStatus(1);
		company.setDescription("Cửa hàng nhỏ");
	}
	
	public void crawlerProductInMistore() {
		String url = "https://hn.mistore.com.vn/collections/dien-thoai-xiaomi";
        try {
            Document document = Jsoup.connect(company.getUrlRoot().concat(company.getUrlAtti())).get();
            Elements productElements = document.select(".product-block");
            List<Product> listProduct = new ArrayList<>();
            for (int i = 0; i < productElements.size(); i++) {
            	Product product = new Product();
            	String name = productElements.get(i).select(".pro-name > a").text();
            	String price = productElements.get(i).select(".pro-price > span").text().split(" ")[0];
            	String image = productElements.get(i).select("img.img-loop").attr("src");
            	String category = EnumCategory.XIAOMI.getCategory();
            	String link = productElements.get(i).select(".pro-name > a").attr("href");
            	

//            	CrawlerProductDetail to get list Ram, Color, Memory
            	crawlerProductDetail(company.getUrlRoot().concat(link), product);
            	
            	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
                Date date = new Date();  
            	product.setDateCrawler(formatter.format(date));

            	product.setName(name);
            	product.setPrice(price);
            	product.setImages(image);
            	product.setCategory(category);
            	product.setLink(company.getUrlRoot().concat(link));
            	
            	listProduct.add(product);
            	System.out.println(name);
            	
//            	Mỗi lần lấy ba nhiêu sản phẩm
            	if(listProduct.size() == 10) {
            		break;
            	}
            }
            company.setProducts(listProduct);
            
            companyRepository.insertProductToCompany(company);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
//	Crawler Product Detail to get list Color, ram, memory
	public void crawlerProductDetail(String link, Product product) throws IOException {
//    	Crawler cấp độ 2 để lấy size, memory, ram
    	Document documentDetail = Jsoup.connect(link).get();
    	Elements productElementsDetail = documentDetail.select(".product_content_right");
    	
//    	Khai báo
    	List<String> listColor = new ArrayList<>();
    	List<String> listRam = new ArrayList<>();
    	List<String> listMemory = new ArrayList<>();
    	
//    	Tạo đa luồng
    	CompletableFuture<Void> futureColor = CompletableFuture.runAsync(() -> {
            System.out.println("Get Color");
            try {
            	//Lấy ra danh sách màu của product
            	String headerSwatchColor = productElementsDetail.select("#variant-swatch-0 .header-swatch").text();
            	
            	//Nếu có màu sắc thì lấy
            	if(headerSwatchColor.contains("Màu sắc")) {
            		Elements elementListColor = productElementsDetail.select("#variant-swatch-0 .select-swap > div");            		
            		
            		for (int j = 0; j < elementListColor.size(); j++) {
            			Element div = elementListColor.get(j);
            			String color = div.attr("data-value");
            			listColor.add(color);
            		}
            	}
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            
        });
    	
    	CompletableFuture<Void> futureRamAndMemory = CompletableFuture.runAsync(() -> {
            System.out.println("Get Ram And Memory");
            try {
            	//Lấy ra danh sách ram và memory
            	String headerSwatchRam = productElementsDetail.select("#variant-swatch-1 .header-swatch").text();
            	
            	//Nếu có bộ nhớ thì lấy
            	if(headerSwatchRam.contains("Bộ nhớ")) {
            		Elements elementListRam = productElementsDetail.select("#variant-swatch-1 .select-swap > div");            		
                	
                	for (int j = 0; j < elementListRam.size(); j++) {
                		Element div = elementListRam.get(j);
                		System.out.println(div.attr("data-value"));
                		String ram = div.attr("data-value").split("/")[0].split(" ")[1];
                		String memory = div.attr("data-value").split("/")[1].trim();
                		listRam.add(ram);
                		listMemory.add(memory);
                	}
            	}
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            
        });
    	
    	
    	System.out.println("Kết hợp");
        CompletableFuture<Void> anyOfFuture = CompletableFuture.allOf(futureColor, futureRamAndMemory);
        
        CompletableFuture<String> resultAllOfFuture = anyOfFuture.thenApply(v -> {
        	return "Hoàn thành";
        }).handle((res, ex) -> {
        	if (ex != null) {
                System.out.println("Oops! We have an exception - " + ex.getMessage());
                return "Unknown!";
            }
            return "Thông báo sau khi hoàn thành: " + res ;
        });

    	

    	
    	product.setRam(listRam);
    	product.setMemory(listMemory);
    	product.setColor(listColor);
	}
}
