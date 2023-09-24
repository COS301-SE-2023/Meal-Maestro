package fellowship.mealmaestro.models.mongo;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "VisitedLinks")
@Setter
@Getter
public class VisitedLinkModel {
    private String link;

    private LocalDate lastVisited;

    public VisitedLinkModel(String link, LocalDate lastVisited) {
        this.link = link;
        this.lastVisited = lastVisited;
    }

    public VisitedLinkModel() {
    }

    @Override
    public String toString() {
        return "VisitedLinkModel [lastVisited=" + lastVisited + ", link=" + link + "]";
    }
}
