package fellowship.mealmaestro.models.mongo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void setPrice(String price) {
        // remove R sign
        Pattern pattern = Pattern.compile("(R)([0-9.]+)");
        Matcher matcher = pattern.matcher(price);

        if (matcher.find()) {
            this.price = Double.parseDouble(matcher.group(2));
        }
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAmount(String amount) {
        // Regular expression to find quantity and unit from the amount string
        Pattern pattern = Pattern.compile("([0-9.]+)\\s*([a-zA-Z]+)");
        Matcher matcher = pattern.matcher(amount);
    
        if (matcher.find()) {
            // Group 1 will contain the quantity
            this.quantity = Double.parseDouble(matcher.group(1));
            
            // Group 2 will contain the unit
            this.unit = matcher.group(2);
        }
    }
    
}