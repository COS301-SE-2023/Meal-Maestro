package fellowship.mealmaestro.services.webscraping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class WebscrapeService {

    private final CheckersScraper checkersScraper;
    private final TaskScheduler taskScheduler;

    private ScheduledFuture<?> checkersScrapingTask;

    public WebscrapeService(CheckersScraper checkersScraper, TaskScheduler taskScheduler) {
        this.checkersScraper = checkersScraper;
        this.taskScheduler = taskScheduler;
    }

    @PostConstruct
    public void init() {
        startScraping();
    }

    private LocalDateTime getStartTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next6AM = now.withHour(6).withMinute(0).withSecond(0);

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

        // schedule task to start at 6am and use a 10s fixedDelay
        Duration tenSeconds = Duration.ofSeconds(10);
        checkersScrapingTask = taskScheduler.scheduleWithFixedDelay(() -> {
            checkersScraper.scrape();
        }, startTime.toInstant(ZoneOffset.ofHours(2)), tenSeconds);

        // schedule task to stop at 10:40am
        LocalDateTime stopTime = getStopTime();
        taskScheduler.schedule(() -> {
            stopScraping();
        }, stopTime.toInstant(ZoneOffset.ofHours(2)));
    }

    private void stopScraping() {
        if (checkersScrapingTask != null) {
            checkersScrapingTask.cancel(false);
        }

        // schedule tasks for next day
        startScraping();
    }

}
