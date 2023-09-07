package fellowship.mealmaestro.models.mongo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Barcode {
    String barcode;

    public Barcode() {
    }

    public Barcode(String barcode) {
        this.barcode = barcode;
    }
}
