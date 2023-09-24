package fellowship.mealmaestro.services.webscraping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.time.Instant; 


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import fellowship.mealmaestro.models.mongo.ToVisitLinkModel;
import fellowship.mealmaestro.repositories.mongo.ToVisitLinkRepository;
import jakarta.annotation.PostConstruct;
@Service
public class WebscrapeService {

    private final WoolworthsScraper woolworthsScraper;
    private final TaskScheduler taskScheduler;
    private static final Logger logger = LoggerFactory.getLogger(WebscrapeService.class);
    private ScheduledFuture<?> woolworthsScrapingTask;
    private final ToVisitLinkRepository toVisitLinkRepository;  


    public WebscrapeService(TaskScheduler taskScheduler, WoolworthsScraper woolworthsScraper , ToVisitLinkRepository toVisitLinkRepository) {
        this.woolworthsScraper = woolworthsScraper;
        this.taskScheduler = taskScheduler;
        this.toVisitLinkRepository = toVisitLinkRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("WebscrapeService init");
        woolworthsScraper.populateInternalLinksFromSitemaps();  // Populate internal links from sitemaps
        startWoolworthsScraping();
    }

    private void startWoolworthsScraping() {
        logger.info("Starting Woolworths scraping task");
        Duration interval = Duration.ofSeconds(24); 
    
        woolworthsScrapingTask = taskScheduler.scheduleWithFixedDelay(() -> {
            Optional<ToVisitLinkModel> toVisitLinkOptional = toVisitLinkRepository.findFirstByStore("Woolworths");
            if (toVisitLinkOptional.isPresent()) {
                String categoryUrl = toVisitLinkOptional.get().getLink();
                woolworthsScraper.scrapeWoolworths(categoryUrl);
                
                // Remove the processed link from ToVisitLinkRepository
                toVisitLinkRepository.delete(toVisitLinkOptional.get());
            }
        }, Instant.now(), interval);
    
        logger.info("Scheduled Woolworths scraping task to start immediately.");
    }
    
}
