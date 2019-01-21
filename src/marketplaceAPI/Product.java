package marketplaceAPI;

public class Product {
    private String name;
    private int price;
    private int inventory;

    public Product(String name, int price, int inventory){
        this.name=name;
        this.price=price;
        this.inventory=inventory;
    }

    public void purchaseProduct(){
        inventory--;
    }

    public boolean inventoryIsEmpty(){
        return inventory==0;
    }

    public String getName(){
        return name;
    }

    public int getPrice(){
        return price;
    }

}
