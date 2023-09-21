package fellowship.mealmaestro.services.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.mongo.FoodModelM;
import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;
import fellowship.mealmaestro.models.mongo.VisitedLinkModel;
import fellowship.mealmaestro.repositories.mongo.ToVisitLinkRepository;
import fellowship.mealmaestro.repositories.mongo.VisitedLinkRepository;
import fellowship.mealmaestro.services.BarcodeService;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WoolworthsScraper {
    private final ToVisitLinkRepository toVisitLinkRepository;
    private final VisitedLinkRepository visitedLinkRepository;
    private final LinkService linkService;
    private final BarcodeService barcodeService;
    private static final Logger logger = LoggerFactory.getLogger(WoolworthsScraper.class);

    public WoolworthsScraper(ToVisitLinkRepository toVisitLinkRepository,
                             VisitedLinkRepository visitedLinkRepository,
                             LinkService linkService, BarcodeService barcodeService) {
        this.toVisitLinkRepository = toVisitLinkRepository;
        this.visitedLinkRepository = visitedLinkRepository;
        this.linkService = linkService;
        this.barcodeService = barcodeService;
    }

    public void scrapeWoolworths() {
        Optional<ToVisitLinkModel> toVisitLinkOptional = linkService.getNextWoolworthsLink();

        if (toVisitLinkOptional.isPresent()) {
            ToVisitLinkModel toVisitLink = toVisitLinkOptional.get();
            String categoryUrl = toVisitLink.getLink();

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

                    String name = card.select(".product-card__name a").text();
                    food.setName(name);

                    String price = card.select(".price").text();
                    food.setPrice(Double.parseDouble(price.replaceAll("[^\\d.]", "")));

                    String productLink = card.select(".product--view").attr("abs:href");

                    Document productPage = Jsoup.connect(productLink).get();
                    String barcode = productPage.select("input[name=catalogRefIds]").val();
                    food.setBarcode(barcode);

                    String weight = extractWeight(name);
                    food.setAmount(weight);

                    barcodeService.addProduct(food);

                    logger.info("Scraped product: " + food.toString());
                }

                visitedLinkRepository.save(new VisitedLinkModel(categoryUrl, "category", "Woolworths"));

            } catch (IOException e) {
                logger.error("Error fetching category page: ", e);
            }
        } else {
            logger.info("No more links to visit.");
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
