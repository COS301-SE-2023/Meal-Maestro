package fellowship.mealmaestro.services.webscraping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;
import fellowship.mealmaestro.models.mongo.VisitedLinkModel;
import fellowship.mealmaestro.repositories.mongo.ToVisitLinkRepository;
import fellowship.mealmaestro.repositories.mongo.VisitedLinkRepository;
import fellowship.mealmaestro.services.BarcodeService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CheckersScraper {
    @Autowired
    private ToVisitLinkRepository toVisitLinkRepository;

    @Autowired
    private VisitedLinkRepository visitedLinkRepository;

    @Autowired
    private BarcodeService barcodeService;

    public void getLocLinks() {
        // Visit categories sitemap to get all locs
        Optional<VisitedLinkModel> visited = visitedLinkRepository
                .findById("https://www.checkers.co.za/sitemap/medias/Category-checkersZA-0.xml");

        if (visited.isPresent()) {
            System.out.println("Skipping sitemap, already visited");
            return;
        }

        try {
            Document doc = Jsoup.connect("https://www.checkers.co.za/sitemap/medias/Category-checkersZA-0.xml").get();

            Elements links = doc.select("loc");

            // Filter out non-food links
            for (int i = 0; i < links.size(); i++) {
                String link = links.get(i).text();
                if (link.contains("food") || link.contains("Food")) {
                    toVisitLinkRepository.save(new ToVisitLinkModel(link));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrape() {

        // Visit categories sitemap to get all locs
        // driver.get("https://www.checkers.co.za/sitemap/medias/Category-checkersZA-0.xml");

        // String domString = driver.getPageSource();

        // Document doc = Jsoup.parse(domString);
        // Elements links = doc.select("loc");

        // Filter out non-food links
        List<String> foodLinks = new ArrayList<String>();
        // for (int i = 0; i < links.size(); i++) {
        // String link = links.get(i).text();
        // if (link.contains("food") || link.contains("Food")) {
        // foodLinks.add(link);
        // }
        // }

        long lastRequestTime = 0;

        foodLinks.add("file:///D:\\Code\\MessingAround\\app\\src\\main\\java\\messingaround\\applep1.html");
        foodLinks.add("file:///D:\\Code\\MessingAround\\app\\src\\main\\java\\messingaround\\milk.html");

        // Visit each food category page and get all product links
        Set<String> visitedLinks = new HashSet<String>();
        List<String> productLinks = new ArrayList<String>();
        List<String> paginationLinks = new ArrayList<String>();
        List<FoodModelM> foodModels = new ArrayList<FoodModelM>();

        for (String link : foodLinks) {
            // Check if link has been visited
            if (visitedLinks.contains(link)) {
                continue;
            }

            long currentTime = System.currentTimeMillis();
            long timeSinceLastRequest = currentTime - lastRequestTime;

            // Wait 10 seconds between requests
            if (timeSinceLastRequest < 10000) {
                try {
                    System.out.println("Waiting " + (10000 - timeSinceLastRequest) + "ms");
                    Thread.sleep(10000 - timeSinceLastRequest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            visitedLinks.add(link);
            driver.get(link);
            System.out.println("Visiting " + link);

            // Wait for page to load
            WebElement element = wait
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("h3.item-product__name > a")));

            // Full products page dom
            String dom = driver.getPageSource();
            Document productPageDoc = Jsoup.connect(link).get();

            // Get product links
            Elements productPageLinks = productPageDoc.select("h3.item-product__name > a");

            for (int i = 0; i < productPageLinks.size(); i++) {
                String productPageLink = productPageLinks.get(i).attr("href");
                productLinks.add(productPageLink);
            }

            // Find pagination links if they exist
            Element paginationBar = productPageDoc.selectFirst("div.pagination-bar.bottom");
            Elements paginationLinksEl = paginationBar.select("a");

            for (Element paginationLink : paginationLinksEl) {
                String paginationLinkHref = paginationLink.attr("href");

                // add to pagination links if they aren't in visitedLinks
                if (!visitedLinks.contains(paginationLinkHref)) {
                    paginationLinks.add(paginationLinkHref);
                }
            }

            // Update last request time
            lastRequestTime = System.currentTimeMillis();
        }

        System.out.println("############################################");
        System.out.println("Main links finished, starting pagination links");
        System.out.println("############################################");

        // Visit each pagination link and get all product links
        for (String link : paginationLinks) {
            // Check if link has been visited
            if (visitedLinks.contains(link)) {
                continue;
            }

            long currentTime = System.currentTimeMillis();
            long timeSinceLastRequest = currentTime - lastRequestTime;

            // Wait 10 seconds between requests
            if (timeSinceLastRequest < 10000) {
                try {
                    System.out.println("Waiting " + (10000 - timeSinceLastRequest) + "ms");
                    Thread.sleep(10000 - timeSinceLastRequest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            visitedLinks.add(link);
            driver.get("file:///D:\\Code\\MessingAround" + link);
            System.out.println("Visiting " + link);

            // Wait for page to load
            WebElement element = wait
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("h3.item-product__name > a")));

            // Full products page dom
            String dom = driver.getPageSource();
            Document productPageDoc = Jsoup.parse(dom);

            // Get product links
            Elements productPageLinks = productPageDoc.select("h3.item-product__name > a");

            for (int i = 0; i < productPageLinks.size(); i++) {
                String productPageLink = productPageLinks.get(i).attr("href");
                productLinks.add(productPageLink);
            }

            // Update last request time
            lastRequestTime = System.currentTimeMillis();
        }

        System.out.println("############################################");
        System.out.println("Finished getting product links");
        System.out.println("############################################");

        // Visit each product page and get product info
        for (String link : productLinks) {
            // Check if link has been visited
            if (visitedLinks.contains(link)) {
                continue;
            }
            long currentTime = System.currentTimeMillis();
            long timeSinceLastRequest = currentTime - lastRequestTime;

            // Wait 10 seconds between requests
            if (timeSinceLastRequest < 10000) {
                try {
                    System.out.println("Waiting " + (10000 - timeSinceLastRequest) + "ms");
                    Thread.sleep(10000 - timeSinceLastRequest);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            visitedLinks.add(link);
            driver.get("file:///D:\\Code\\MessingAround" + link);
            System.out.println("Visiting " + driver.getTitle());

            // Wait for page to load
            WebElement element = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("#accessibletabsnavigation0-1")));

            // Full product page dom
            String dom = driver.getPageSource();
            Document productDoc = Jsoup.parse(dom);

            // Get product info
            FoodModelM food = new FoodModelM();
            // product name
            String productName = productDoc.selectFirst("h1.pdp__name").text();
            System.out.println("Product name: " + productName);
            food.setName(productName);

            // product price
            String productPrice = productDoc.selectFirst("div.special-price__price").text();
            System.out.println("Product price: " + productPrice);
            food.setPrice(productPrice);

            // product details
            Elements productDetails = productDoc.select("table.pdp__product-information > tbody > tr");

            String barcode = "";
            String quantity = "";

            for (Element productDetail : productDetails) {
                if (productDetail.text().toLowerCase().contains("barcode")) {
                    // select second td
                    barcode = productDetail.selectFirst("td:nth-child(2)").text();
                    System.out.println("Barcode: " + barcode);
                    food.setBarcode(barcode);
                }

                if (productDetail.text().toLowerCase().contains("weight")
                        || productDetail.text().toLowerCase().contains("volume")) {
                    // select second td
                    quantity = productDetail.selectFirst("td:nth-child(2)").text();
                    System.out.println("Quantity: " + quantity);
                    food.setAmount(quantity);
                }
            }

            // Add food to list
            foodModels.add(food);

            lastRequestTime = System.currentTimeMillis();
        }

        driver.quit();
    }
}