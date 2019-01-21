package marketplaceAPI;

/**
 * Product contains and sets information regarding a product
 */
public class Product {
    private String name;
    private double price;
    private int inventory;

    /**Initializes a new product
     *
     * @param name of product
     * @param price of product. Must have a non-negative value
     * @param inventory count of product. Must have a non-negative
     *                  value
     */
    public Product(String name, double price, int inventory){
        this.name=name;
        this.price=price;
        this.inventory=inventory;
    }

    /**
     * Purchases one product
     * @return true if the inventory was not empty so the
     * product was purchased successfully and false otherwise
     */
    public boolean purchaseProduct(){
        if(!inventoryIsEmpty()) {
            inventory--;
            return true;
        } else{
            return false;
        }
    }

    /**
     * Determines whether or not product inventory is empty
     * @return true if the inventory count < 1 and false
     * otherwise
     */
    public boolean inventoryIsEmpty(){
        return inventory<1;
    }

    /**gets the inventory of the product
     *
     * @return inventory count of the product;
     */
    public int getInventory(){
        return inventory;
    }

    /**gets the name of the product
     * @return the name of the product
     */
    public String getName(){
        return name;
    }

    /**
     * gets the price of the product
     * @return the pricr of the product
     */
    public double getPrice(){
        return price;
    }

    /**adds to the product inventory
     * @param num of products to add to the
     *            inventory. Must have value > 0
     */
    public void addInventory(int num){
        inventory+=num;
    }

    /**
     * Sets a new price for the product
     * @param price of the product. Must be a
     *              non-negative value.
     */
    public void changePrice(double price){
        this.price=price;
    }

    /**
     * Sets a new name for the product
     * @param name of the product.
     */
    public void changePrice(String name){
        this.name=name;
    }

}
