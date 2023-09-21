package fellowship.mealmaestro.services.webscraping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class WebscrapeService {

    private final CheckersScraper checkersScraper;
    private final WoolworthsScraper woolworthsScraper;
    private final TaskScheduler taskScheduler;

    private static final Logger logger = LoggerFactory.getLogger(WebscrapeService.class);

    private ScheduledFuture<?> checkersScrapingTask;
    private ScheduledFuture<?> woolworthsScrapingTask;

    public WebscrapeService(CheckersScraper checkersScraper, TaskScheduler taskScheduler, WoolworthsScraper woolworthsScraper) {
        this.checkersScraper = checkersScraper;
        this.woolworthsScraper = woolworthsScraper;
        this.taskScheduler = taskScheduler;
    }

    @PostConstruct
    public void init() {
        System.out.println("WebscrapeService init");
        // Commenting out startScraping() for Checkers
        // startScraping();
        startWoolworthsScraping();
    }

    private LocalDateTime getStartTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next6AM = now.withHour(13).withMinute(55).withSecond(0);

        if (now.isAfter(next6AM) || now.isEqual(next6AM)) {
            return next6AM.plusDays(1);
        } else {
            return next6AM;
        }
    }

    private LocalDateTime getStopTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next1040AM = now.withHour(10).withMinute(40).withSecond(0);

        if (now.isAfter(next1040AM) || now.isEqual(next1040AM)) {
            return next1040AM.plusDays(1);
        } else {
            return next1040AM;
        }
    }

    // Commenting out the Checkers scraping logic
    /*
    private void startScraping() {
        // ... (original logic here)
    }
    */

    private void startWoolworthsScraping() {
        LocalDateTime startTime = getStartTime();
        Duration interval = Duration.ofSeconds(24); // Change this as appropriate
        woolworthsScrapingTask = taskScheduler.scheduleWithFixedDelay(() -> {
            woolworthsScraper.scrapeWoolworths();
        }, startTime.toInstant(ZoneOffset.ofHours(2)), interval);
        logger.info("Scheduled Woolworths scraping task to start at {}", startTime);
    }

    // Commenting out the Checkers scraping logic
    /*
    private void stopScraping() {
        // ... (original logic here)
    }
    */

    private void stopWoolworthsScraping() {
        if (woolworthsScrapingTask != null) {
            woolworthsScrapingTask.cancel(false);
        }
        logger.info("Stopped Woolworths scraping task");
    }
}
