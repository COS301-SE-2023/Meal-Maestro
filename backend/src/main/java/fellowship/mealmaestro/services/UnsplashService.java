package fellowship.mealmaestro.services;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
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
                .uri(UNSPLASH_URL + "?query=" + query + "&per_page=3&orientation=landscape")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }

    public String fetchPhoto(String query) {
        String response = searchPhotos(query);
        ObjectMapper jsonMapper = new ObjectMapper();
        String photoUrl = "";

        try {
            photoUrl = jsonMapper.readTree(response)
                    .path("results")
                    .get(0)
                    .path("urls")
                    .path("regular")
                    .asText();
        } catch (Exception e) {
            System.out.println("Error fetching photo from Unsplash");
        }

        return photoUrl;
    }
}
