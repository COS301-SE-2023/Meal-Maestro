package fellowship.mealmaestro.models.mongo;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "VisitedLinks")
@Setter
@Getter
public class VisitedLinkModel {
    @Id
    private String link;

    private LocalDate lastVisited;

    private String type;

    public VisitedLinkModel(String link, LocalDate lastVisited, String type) {
        this.link = link;
        this.lastVisited = lastVisited;
        this.type = type;
    }

    public VisitedLinkModel(String link, String type) {
        this.link = link;
        this.type = type;
        this.lastVisited = LocalDate.now();
    }

    public VisitedLinkModel() {
    }

    @Override
    public String toString() {
        return "VisitedLinkModel [lastVisited=" + lastVisited + ", link=" + link +
                "]";
    }
}
