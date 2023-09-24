package fellowship.mealmaestro.models.mongo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class findBarcodeRequest {
    String barcode;
    String store;

    public findBarcodeRequest() {
    }

    public findBarcodeRequest(String barcode, String store) {
        this.barcode = barcode;
        this.store = store;
    }
}
