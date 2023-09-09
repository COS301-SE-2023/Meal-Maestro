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

    public ToVisitLinkModel(String link) {
        this.link = link;
    }

    public ToVisitLinkModel() {
    }
}
