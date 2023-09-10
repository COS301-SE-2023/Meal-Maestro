package fellowship.mealmaestro.services.webscraping;

import org.springframework.stereotype.Service;

@Service
public class WebscrapeService {

    private final LinkService linkService;

    private final CheckersScraper checkersScraper;

    public WebscrapeService(LinkService linkService, CheckersScraper checkersScraper) {
        this.linkService = linkService;
        this.checkersScraper = checkersScraper;
    }
}
