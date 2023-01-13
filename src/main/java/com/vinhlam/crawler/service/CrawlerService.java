//package com.vinhlam.crawler.service;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.springframework.stereotype.Service;
//
//import com.vinhlam.crawler.config.CrawlerProperties;
//
//@Service
//public class CrawlerService {
//	private final CrawlerProperties crawlerProperties;
//    private final Set<String> pagesVisited = new HashSet<>();
//
//    public CrawlerService(CrawlerProperties crawlerProperties) {
//        this.crawlerProperties = crawlerProperties;
//    }
//
//    public void startCrawling() {
//        pagesVisited.add(crawlerProperties.getRootUrl());
//        ExecutorService executor = Executors.newFixedThreadPool(crawlerProperties.getNumThreads());
//
//        executor.execute(() -> searchPage(crawlerProperties.getRootUrl()));
//
//        try {
//            executor.awaitTermination(30, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            executor.shutdown();
//        }
//    }
//
//    private void searchPage(String url) {
//        if(!pagesVisited.contains(url) && pagesVisited.size() < crawlerProperties.getMaxPages()) {
//            Document document;
//            try {
//                document = Jsoup.connect(url).get();
//                Elements links = document.select("a[href]");
//                pagesVisited.add(url);
//                System.out.println("### Searching page " + url);
//                links.forEach(link -> {
//                    String absUrl = link.attr("abs:href");
//                    if(absUrl.startsWith("http")) {
//                        pagesVisited.add(absUrl);
//                        searchPage(absUrl);
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
