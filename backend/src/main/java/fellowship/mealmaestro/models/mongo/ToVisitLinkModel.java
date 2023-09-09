package fellowship.mealmaestro.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "ToVisitLinks")
@Setter
@Getter
public class ToVisitLinkModel {
    @Id
    private String link;

    private String type;

    public ToVisitLinkModel(String link, String type) {
        this.link = link;
        this.type = type;
    }

    public ToVisitLinkModel() {
    }
}
