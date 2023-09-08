package fellowship.mealmaestro.models.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "Foods")
@Getter
@Setter
public class FoodModelM {
    @Id
    private String barcode;

    private String store;

    private String name;

    private double quantity;

    private String unit;

    private double price;

    public FoodModelM(String barcode, String name, double quantity, String unit,
            double price) {
        this.barcode = barcode;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
    }

    public FoodModelM() {
    }

    @Override
    public String toString() {
        return "FoodModelM [barcode=" + barcode + ", name=" + name + ", price=" +
                price + ", quantity=" + quantity
                + ", unit=" + unit + "]";
    }
}
