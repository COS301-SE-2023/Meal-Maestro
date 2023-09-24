package fellowship.mealmaestro.services.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;
import fellowship.mealmaestro.models.mongo.VisitedLinkModel;
import fellowship.mealmaestro.repositories.mongo.DynamicFoodMRepository;
import fellowship.mealmaestro.repositories.mongo.ToVisitLinkRepository;
import fellowship.mealmaestro.repositories.mongo.VisitedLinkRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WoolworthsScraper {
    private final ToVisitLinkRepository toVisitLinkRepository;
    private final VisitedLinkRepository visitedLinkRepository;
    private final DynamicFoodMRepository dynamicFoodMRepository;
    private static final Logger logger = LoggerFactory.getLogger(WoolworthsScraper.class);

    public WoolworthsScraper(ToVisitLinkRepository toVisitLinkRepository,
                             VisitedLinkRepository visitedLinkRepository,
                             @Qualifier("dynamicFoodMRepositoryImpl") DynamicFoodMRepository dynamicFoodMRepository) {
        this.toVisitLinkRepository = toVisitLinkRepository;
        this.visitedLinkRepository = visitedLinkRepository;
        this.dynamicFoodMRepository = dynamicFoodMRepository;
    }

    // New method to fetch sitemap links
    public List<String> fetchSitemapLinks(String sitemapUrl) throws IOException {
        List<String> links = new ArrayList<>();
        Document doc = Jsoup.connect(sitemapUrl).get();
        Elements urlElements = doc.select("url");

        for (Element urlElement : urlElements) {
            String link = urlElement.select("loc").text();
            links.add(link);
        }

        return links;
    }

    public void cleanUpToVisitLinks() {
        System.out.println("Cleaning up ToVisitLink collection");
        List<ToVisitLinkModel> toVisitLinks = toVisitLinkRepository.findAll();
        for (ToVisitLinkModel link : toVisitLinks) {
            if (!link.getLink().contains("Food")) {
                toVisitLinkRepository.deleteById(link.getLink());
            }
        }
    }

    
    

    public void populateInternalLinksFromSitemaps() {
        // List of sitemaps to fetch internal URLs from
        List<String> sitemapUrls = Arrays.asList(
            "https://www.woolworths.co.za/images/sitemaps/categorySitemap.xml",
            "https://www.woolworths.co.za/images/sitemaps/categorySitemap2.xml"
        );
    
        // Loop over each sitemap
        for (String sitemapUrl : sitemapUrls) {
            try {
                // Fetch internal URLs from the sitemap
                Document sitemap = Jsoup.connect(sitemapUrl).get();
                Elements urls = sitemap.select("loc");
    
                // Save each internal URL to ToVisitLink if it contains the word 'Food'
                for (Element urlElement : urls) {
                    String internalUrl = urlElement.text();
                    if (internalUrl.contains("Food")) {  // Filtering condition
                        ToVisitLinkModel toVisitLinkModel = new ToVisitLinkModel(internalUrl, "Woolworths");
                        toVisitLinkRepository.save(toVisitLinkModel);
                    }
                }
            } catch (IOException e) {
                logger.error("Error fetching sitemap: ", e);
            }
        }
    }
    

    // Initiator method for scraping
    public void initiateScraping() {
        try {
            List<String> sitemapLinks = fetchSitemapLinks("https://www.woolworths.co.za/images/sitemaps/siteIndex.xml");

            for (String link : sitemapLinks) {
                scrapeWoolworths(link);  // Modified to accept a URL
            }
        } catch (IOException e) {
            logger.error("Error fetching sitemap: ", e);
        }
    }
// Existing scraping method modified to accept a URL
public void scrapeWoolworths(String categoryUrl) {
    logger.info("Woolworths scraping started.");

    Optional<VisitedLinkModel> visitedLinkModelOptional = visitedLinkRepository.findById(categoryUrl);

    if (visitedLinkModelOptional.isPresent()) {
        logger.info("Skipping " + categoryUrl + ", already visited...");
        return;
    }

    try {
        Document categoryPage = Jsoup.connect(categoryUrl).get();
        Elements productCards = categoryPage.select(".product-card");

        for (Element card : productCards) {
            FoodModelM food = new FoodModelM();

            // Set the default quantity to 1
            food.setQuantity(1.0);  // Assuming quantity is a double

            String name = card.select(".product-card__name a").text();
            food.setName(name);

            String price = card.select(".price").text();
            if (!price.isEmpty()) {
                String cleanedPrice = price.replaceAll("[^\\d.]", "");
                int firstDecimalPoint = cleanedPrice.indexOf('.');
                if (firstDecimalPoint != -1) {
                    cleanedPrice = cleanedPrice.substring(0, firstDecimalPoint + 1) +
                                   cleanedPrice.substring(firstDecimalPoint + 1).replace(".", "");
                }
                try {
                    food.setPrice(Double.parseDouble(cleanedPrice));
                } catch (NumberFormatException e) {
                    logger.error("Could not parse price: " + cleanedPrice, e);
                    food.setPrice(0.0);  // or some default value, or log an error
                }
            } else {
                food.setPrice(0.0);  // Or some default value, or log an error
            }

            String productLink = card.select(".product--view").attr("abs:href");

            Document productPage = Jsoup.connect(productLink).get();
            String barcode = productPage.select("input[name=catalogRefIds]").val();
            food.setBarcode(barcode);

            String weight = extractWeight(name);
            food.setAmount(weight);

            // Saving to MongoDB
            food.setStore("Woolworths");
            dynamicFoodMRepository.saveInDynamicCollection(food);

            logger.info("Scraped product: " + food.toString());
        }

        visitedLinkRepository.save(new VisitedLinkModel(categoryUrl, LocalDate.now(), "category", "Woolworths"));

    } catch (IOException e) {
        logger.error("Error fetching category page: ", e);
    }

    logger.info("Woolworths scraping completed.");
}

    
    

    public void populateToVisitLinks() {
        String sitemapUrl = "https://www.woolworths.co.za/images/sitemaps/siteIndex.xml";
        try {
            Document sitemap = Jsoup.connect(sitemapUrl).get();
            Elements urls = sitemap.select("loc");
            for (Element urlElement : urls) {
                String url = urlElement.text();
                ToVisitLinkModel toVisitLinkModel = new ToVisitLinkModel(url, "Woolworths");
                toVisitLinkRepository.save(toVisitLinkModel);
            }
        } catch (IOException e) {
            logger.error("Error fetching sitemap: ", e);
        }
    }
    

    private String extractWeight(String name) {
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(g|kg|ml|l)");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "Weight not found";
        }
    }
}
