package fellowship.mealmaestro.services.webscraping;

import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;
import fellowship.mealmaestro.models.mongo.VisitedLinkModel;
import fellowship.mealmaestro.repositories.mongo.ToVisitLinkRepository;
import fellowship.mealmaestro.repositories.mongo.VisitedLinkRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WoolworthsScraper {

    private final ToVisitLinkRepository toVisitLinkRepository;
    private final VisitedLinkRepository visitedLinkRepository;
    private final LinkService linkService;

    private static final Logger logger = LoggerFactory.getLogger(WoolworthsScraper.class);

    public WoolworthsScraper(ToVisitLinkRepository toVisitLinkRepository, VisitedLinkRepository visitedLinkRepository,
                             LinkService linkService) {
        this.toVisitLinkRepository = toVisitLinkRepository;
        this.visitedLinkRepository = visitedLinkRepository;
        this.linkService = linkService;
    }

    public void scrape() {
        Optional<ToVisitLinkModel> toVisitLink = linkService.getNextWoolworthsLink();

        if (toVisitLink.isPresent()) {
            handleLink(toVisitLink.get());
        } else {
            logger.info("No links to visit for Woolworths...");
        }
    }

    public void handleLink(ToVisitLinkModel toVisitLink) {
        String type = toVisitLink.getType();
        if ("category".equals(type)) {
            handleCategoryLink(toVisitLink);
        } else if ("product".equals(type)) {
            handleProductLink(toVisitLink);
        }
    }

    public void handleCategoryLink(ToVisitLinkModel link) {
        try {
            Document categoryPage = Jsoup.connect(link.getLink()).get();
            scrapeCategoryPage(categoryPage);
        } catch (IOException e) {
            logger.error("Error fetching category page: ", e);
        }
    }

    public void handleProductLink(ToVisitLinkModel link) {
        try {
            Document productPage = Jsoup.connect(link.getLink()).get();
            FoodModelM food = scrapeProductPage(productPage);
            // Save the food object to the database (adapt this to your needs)
        } catch (IOException e) {
            logger.error("Error fetching product page: ", e);
        }
    }

    public void scrapeCategoryPage(Document categoryPage) {
        Elements productCards = categoryPage.select(".product-card");
        for (Element card : productCards) {
            String productLink = card.select(".product--view").attr("abs:href");
            toVisitLinkRepository.save(new ToVisitLinkModel(productLink, "product", "Woolworths"));
        }
    }

    public FoodModelM scrapeProductPage(Document productPage) {
        FoodModelM food = new FoodModelM();
        String name = productPage.select(".product-card__name a").text();
        String price = productPage.select(".price").text();
        String barcode = productPage.select("input[name=catalogRefIds]").val();
        String weight = extractWeight(name);
        food.setName(name);
        food.setPrice(price);
        food.setBarcode(barcode);
        food.setAmount(weight);
        return food;
    }

    public String extractWeight(String name) {
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\s*(g|kg|ml|l)");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "Weight not found";
        }
    }
}
