package fellowship.mealmaestro.services.webscraping;

import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;
import fellowship.mealmaestro.models.mongo.VisitedLinkModel;
import fellowship.mealmaestro.repositories.mongo.ToVisitLinkRepository;
import fellowship.mealmaestro.repositories.mongo.VisitedLinkRepository;
import fellowship.mealmaestro.services.BarcodeService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CheckersScraper {
    private final ToVisitLinkRepository toVisitLinkRepository;

    private final VisitedLinkRepository visitedLinkRepository;

    private final LinkService linkService;

    private final BarcodeService barcodeService;

    private static final Logger logger = LoggerFactory.getLogger(CheckersScraper.class);

    public CheckersScraper(ToVisitLinkRepository toVisitLinkRepository, VisitedLinkRepository visitedLinkRepository,
            LinkService linkService, BarcodeService barcodeService) {
        this.toVisitLinkRepository = toVisitLinkRepository;
        this.visitedLinkRepository = visitedLinkRepository;
        this.linkService = linkService;
        this.barcodeService = barcodeService;
    }

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
        Optional<ToVisitLinkModel> toVisitLink = linkService.getNextCheckersLink();

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
        logger.info("Handling Category Link: " + link.getLink());
        Optional<VisitedLinkModel> visited = visitedLinkRepository.findById(link.getLink());

        // if link has been visited and it has been less than 1 month since last visit,
        // skip
        if (visited.isPresent() && visited.get().getLastVisited().plusMonths(1).isAfter(LocalDate.now())) {
            toVisitLinkRepository.deleteById(link.getLink());
            logger.info("Skipping " + link.getLink() + ", already visited...");
            return;
        }

        try {
            // if link starts with /, add domain
            Document doc;
            if (link.getLink().startsWith("/")) {
                doc = Jsoup.connect("http://www.checkers.co.za" + link.getLink()).userAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 Edg/116.0.1938.76")
                        .header("Accept",
                                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .get();
            } else {
                doc = Jsoup.connect(link.getLink()).userAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36 Edg/116.0.1938.76")
                        .header("Accept",
                                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .get();
            }
            logger.info("Connecting to " + doc.title() + "...");
            if (doc.title().contains("Captcha")) {
                logger.info("######################################");
                logger.info("Error encountered captcha, skipping...");
                logger.info("######################################");
                return;
            }

            // Get product links
            Elements productPageLinks = doc.select("h3.item-product__name > a");

            if (productPageLinks == null) {
                logger.info("Skipping " + link.getLink() + ", no product links...");
                return;
            }

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
            logger.info("Saving " + link.getLink() + " to visited links...");

            // Remove link from ToVisitLinks
            toVisitLinkRepository.deleteById(link.getLink());

            logger.info("Visited " + link.getLink());
        } catch (IOException e) {
            logger.info("Error visiting " + link.getLink() + ", skipping...");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleProductLink(ToVisitLinkModel link) {
        // Visit product page and get product info

        // Check if link has been visited
        logger.info("Handling Product Link: " + link.getLink());
        Optional<VisitedLinkModel> visited = visitedLinkRepository.findById(link.getLink());

        if (visited.isPresent() && visited.get().getLastVisited().plusMonths(1).isAfter(LocalDate.now())) {
            toVisitLinkRepository.deleteById(link.getLink());
            logger.info("Skipping " + link.getLink() + ", already visited...");
            return;
        }

        try {
            // Visit product page
            // if link starts with /, add domain
            Document doc;
            if (link.getLink().startsWith("/")) {
                doc = Jsoup.connect("http://www.checkers.co.za" + link.getLink()).userAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36")
                        .header("Accept",
                                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .get();
            } else {
                doc = Jsoup.connect(link.getLink()).userAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36")
                        .header("Accept",
                                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .get();
            }
            logger.info("Connecting to " + doc.title() + "...");
            // Get product info
            FoodModelM food = new FoodModelM();

            // product name
            Element productNameEl = doc.selectFirst("h1.pdp__name");
            if (productNameEl == null) {
                logger.info("Skipping " + link.getLink() + ", no product name...");
                return;
            }
            String productName = productNameEl.text();
            if (productName == null || productName.isEmpty()) {
                logger.info("Skipping " + link.getLink() + ", no product name...");
                return;
            }
            System.out.println("Product name: " + productName);
            food.setName(productName);

            // product price
            Element productPriceEl = doc.selectFirst("div.special-price__price");
            if (productPriceEl == null) {
                logger.info("No product price");
                food.setPrice(-1.0);
            } else {

                String productPrice = productPriceEl.text();
                if (productPrice == null || productPrice.isEmpty()) {
                    food.setPrice(-1.0);
                }
                System.out.println("Product price: " + productPrice);
                food.setPrice(productPrice);
            }

            // product details
            Elements productDetails = doc.select("table.pdp__product-information > tbody > tr");

            String barcode = "";
            String quantity = "";

            for (Element productDetail : productDetails) {
                if (productDetail.text().toLowerCase().contains("barcode")) {
                    // select second td
                    Element barcodeEl = productDetail.selectFirst("td:nth-child(2)");
                    if (barcodeEl == null) {
                        logger.info("Skipping " + link.getLink() + ", no barcode...");
                        return;
                    }
                    barcode = barcodeEl.text();
                    System.out.println("Barcode: " + barcode);
                    food.setBarcode(barcode);
                }

                if (productDetail.text().toLowerCase().contains("weight")
                        || productDetail.text().toLowerCase().contains("volume")) {
                    // select second td
                    Element quantityEl = productDetail.selectFirst("td:nth-child(2)");
                    if (quantityEl == null) {
                        logger.info("Skipping " + link.getLink() + ", no quantity...");
                        return;
                    }
                    quantity = quantityEl.text();
                    System.out.println("Quantity: " + quantity);
                    food.setAmount(quantity);
                }
            }

            if (barcode.isEmpty() || food.getBarcode().equals("")) {
                logger.info("Skipping " + link.getLink() + ", no barcode...");
                return;
            }

            // Add food to database
            food.setStore("Checkers");
            barcodeService.addProduct(food);
            logger.info("Saving " + food.getName() + " to database...");

            // Add link to visited links
            visitedLinkRepository.save(new VisitedLinkModel(link.getLink(), "product", "Checkers"));
            logger.info("Saving " + link.getLink() + " to visited links...");

            // Remove link from ToVisitLinks
            toVisitLinkRepository.deleteById(link.getLink());

            logger.info("Visited " + link.getLink());
        } catch (IOException e) {
            logger.info("Error visiting " + link.getLink() + ", skipping...");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void scrape() {
        // Get next link to visit
        ToVisitLinkModel toVisitLink = getNextLink();

        if (toVisitLink == null) {
            System.out.println("No links to visit...");
            return;
        }

        // Handle link
        logger.info("### " + System.currentTimeMillis() + " ###");
        handleLink(toVisitLink);
    }
}
