package fellowship.mealmaestro.models;



public class FoodModel {
    private String name;

    private int quantity;

    private String unit;

    public FoodModel(){
        this.name = "";
        this.quantity = 0;
    }

    public FoodModel(String name, int quantity, String unit){
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName(){
        return this.name;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public String getUnit(){
        return this.unit;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }
}
