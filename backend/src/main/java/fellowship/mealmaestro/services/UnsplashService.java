package fellowship.mealmaestro.services;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.cdimascio.dotenv.Dotenv;

public class UnsplashService {
    private static final String UNSPLASH_URL = "https://api.unsplash.com/search/photos";

    private final static String API_KEY;

    private final WebClient webClient;

    public UnsplashService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    static {
        String apiKey;
        Dotenv dotenv;
        if (System.getenv("UNSPLASH_API_KEY") != null) {
            apiKey = System.getenv("UNSPLASH_API_KEY");
        } else {
            try {
                dotenv = Dotenv.load();
                apiKey = dotenv.get("UNSPLASH_API_KEY");
            } catch (Exception e) {
                dotenv = Dotenv.configure()
                        .ignoreIfMissing()
                        .load();
                apiKey = "No API Key Found";
            }
        }
        API_KEY = apiKey;
    }

    public String searchPhotos(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + API_KEY);

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(UNSPLASH_URL)
                        .queryParam("query", query)
                        .queryParam("per_page", 1)
                        .build())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
