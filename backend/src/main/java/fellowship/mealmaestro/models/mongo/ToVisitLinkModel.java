package fellowship.mealmaestro.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Indexed
    private String store;

    public ToVisitLinkModel(String link, String type, String store) {
        this.link = link;
        this.type = type;
        this.store = store;
    }

     public ToVisitLinkModel(String link, String store) {
        this.link = link;
        this.store = store;
    }

    public ToVisitLinkModel() {
    }
}