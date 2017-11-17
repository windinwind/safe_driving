package lazydroid.safedriving;

/**
 * Created by helen on 2017-11-16.
 */

public class ProductInfo {
    private String product_name;
    private int product_cost;

    public ProductInfo(String name, int cost){
        product_name = name;
        product_cost = cost;
    }

    public String getProductName(){
        return product_name;
    }

    public int getProductCost(){
        return product_cost;
    }
}
