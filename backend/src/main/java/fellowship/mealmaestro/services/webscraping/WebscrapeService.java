package fellowship.mealmaestro.services.webscraping;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WebscrapeService {

    private volatile boolean isScrapingAllowed = true;

    @Scheduled(cron = "0 0 6 * * ?")
    public void startScrape() {
        System.out.println("Scraping started...");
    }

    @Scheduled(cron = "0 42 10 * * ?")
    public void stopScraping() {
        System.out.println("Scraping stopped...");
    }
}
