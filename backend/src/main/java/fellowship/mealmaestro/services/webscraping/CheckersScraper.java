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

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
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
    private LinkService linkService;

    @Autowired
    private BarcodeService barcodeService;

    private long lastRequestTime;

    public void getLocLinks() {
        // Visit categories sitemap to get all locs
        lastRequestTime = 0;
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
                    toVisitLinkRepository.save(new ToVisitLinkModel(link, "category", "Checkers"));
                }
            }

            // Add sitemap to visited links
            visitedLinkRepository.save(new VisitedLinkModel(
                    "https://www.checkers.co.za/sitemap/medias/Category-checkersZA-0.xml", "category", "Checkers"));
            // Remove sitemap from ToVisitLinks
            toVisitLinkRepository.deleteById(
                    "https://www.checkers.co.za/sitemap/medias/Category-checkersZA-0.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ToVisitLinkModel getNextLink() {
        // Get next link to visit

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

        Optional<ToVisitLinkModel> toVisitLink = linkService.getNextLink();
        lastRequestTime = System.currentTimeMillis();

        if (toVisitLink.isPresent()) {
            return toVisitLink.get();
        }

        return null;
    }

    public void handleLink(ToVisitLinkModel toVisitLink) {
        // Handle link based on type
        if (toVisitLink.getType().equals("category")) {
            handleCategoryLink(toVisitLink);
        } else if (toVisitLink.getType().equals("product")) {
            handleProductLink(toVisitLink);
        }
    }

    public void handleCategoryLink(ToVisitLinkModel link) {
        // Visit category page and get all product links and pagination links
        Optional<VisitedLinkModel> visited = visitedLinkRepository.findById(link.getLink());

        // if link has been visited and it has been less than 1 month since last visit,
        // skip
        if (visited.isPresent() && visited.get().getLastVisited().plusMonths(1).isAfter(LocalDate.now())) {
            System.out.println("Skipping " + link.getLink() + ", already visited...");
            return;
        }

        try {
            Document doc = Jsoup.connect("https://www.checkers.co.za" + link.getLink()).get();

            // Get product links
            Elements productPageLinks = doc.select("h3.item-product__name > a");

            for (int i = 0; i < productPageLinks.size(); i++) {
                String productPageLink = productPageLinks.get(i).attr("href");

                if (productPageLink != null && !productPageLink.isEmpty()) {
                    toVisitLinkRepository.save(new ToVisitLinkModel(productPageLink, "product", "Checkers"));
                }
            }

            // Get pagination links
            Element paginationBar = doc.selectFirst("div.pagination-bar.bottom");
            if (paginationBar != null) {
                Elements paginationLinksEl = paginationBar.select("a");

                for (Element paginationLink : paginationLinksEl) {
                    String paginationLinkHref = paginationLink.attr("href");

                    // add to ToVisitLinks if they aren't in VisitedLinks
                    if (!visitedLinkRepository.existsById(paginationLinkHref)) {
                        toVisitLinkRepository.save(new ToVisitLinkModel(paginationLinkHref, "category", "Checkers"));
                    }

                }
            }

            // Add link to visited links
            visitedLinkRepository.save(new VisitedLinkModel(link.getLink(), "category", "Checkers"));

            // Remove link from ToVisitLinks
            toVisitLinkRepository.deleteById(link.getLink());

            System.out.println("Visited " + link.getLink());
        } catch (IOException e) {
            System.out.println("Error visiting " + link.getLink() + ", skipping...");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleProductLink(ToVisitLinkModel link) {

    }

    public void scrape() {

        // Visit each product page and get product info
        for (String link : productLinks) {
            // Check if link has been visited
            if (visitedLinks.contains(link)) {
                continue;
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