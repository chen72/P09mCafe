package sg.edu.rp.webservices.c302_p09_mcafe;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private String id;
    private String category_id;
    private String item_description;
    private double item_price;

    public MenuItem(String id, String category_id, String item_description, double item_price) {
        this.id = id;
        this.category_id = category_id;
        this.item_description = item_description;
        this.item_price = item_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    @Override
    public String toString() {
        return item_description;
    }
}
