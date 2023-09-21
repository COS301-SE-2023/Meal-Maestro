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
        startScraping();
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

    private void startScraping() {
        LocalDateTime startTime = getStartTime();
        Duration tenSeconds = Duration.ofSeconds(24);
        checkersScrapingTask = taskScheduler.scheduleWithFixedDelay(() -> {
            checkersScraper.scrape();
        }, startTime.toInstant(ZoneOffset.ofHours(2)), tenSeconds);
        logger.info("Scheduled Checkers scraping task to start at {}", startTime);
        
        // schedule task to stop at 10:40am
        LocalDateTime stopTime = getStopTime();
        taskScheduler.schedule(() -> {
            stopScraping();
        }, stopTime.toInstant(ZoneOffset.ofHours(2)));
        logger.info("Scheduled Checkers scraping task to stop at {}", stopTime);
    }

    private void startWoolworthsScraping() {
        LocalDateTime startTime = getStartTime();
        Duration waitInterval = Duration.ofMinutes(1);  // interval 
        woolworthsScrapingTask = taskScheduler.scheduleWithFixedDelay(() -> {
            woolworthsScraper.scrapeWoolworths();
        }, startTime.toInstant(ZoneOffset.ofHours(2)), waitInterval);
        logger.info("Scheduled Woolworths scraping task to start at {}", startTime);
    }

    private void stopScraping() {
        if (checkersScrapingTask != null) {
            checkersScrapingTask.cancel(false);
        }
        logger.info("Stopped Checkers scraping task");
        
        // schedule tasks for next day
        startScraping();
    }

    private void stopWoolworthsScraping() {
        if (woolworthsScrapingTask != null) {
            woolworthsScrapingTask.cancel(false);
        }
        logger.info("Stopped Woolworths scraping task");
    }
}
