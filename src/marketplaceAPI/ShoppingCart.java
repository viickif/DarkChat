package marketplaceAPI;

import java.util.*;

public class ShoppingCart {

    private Marketplace shop;
    private Map<Product, Integer> cart;
    private double total=0.0;

    /**initializes a new shopping cart
     * @param shop to shop in
     */
    public ShoppingCart(Marketplace shop){
        this.shop=shop;
        this.cart=new HashMap<>();
    }

    /**
     * Adds products from the shop to the cart if the product
     * inventory is not empty and the product exists in the shop.
     * If num > inventory, the available inventory count of the product
     * will be added to the cart.
     * @param productName of the product to add to the cart
     * @param num of the specified product to add to the cart.
     *            Must have value > 0.
     */
    public void addToCart(String productName,int num){
        int inventory;
        Integer inCartNum;
        double price;

        if(shop.containsProduct(productName)){
            Product product=shop.fetchOneProduct(productName);
            inventory=product.getInventory();
            price=product.getPrice();

            //make sure there is enough inventory
            if(num>inventory){
                num=inventory;
            }

            //update total
            total+=num*price;

            //update cart
            if((inCartNum=cart.get(product))!=null){
               cart.put(product,inCartNum+num);
            }else{
                cart.put(product,num);
            }
        }
    }

    /**
     * removes products from cart if such product is in the cart
     * @param productName of the product to remove from the cart.
     *                    Product must be in the cart.
     * @param num of the product to remove from the cart. Must be <=
     *            the number of such product that is currently
     *            in the cart
     */
    public void removeFromCart(String productName, int num){
        Product product=shop.fetchOneProduct(productName);
        int prevCount;
        double price;

        //product might have been removed on the marketplace end
        if(product!=null) {
            prevCount = cart.get(product);
            price = product.getPrice();
            cart.put(product, prevCount - num);
            total -= num * price;
        }
    }

    /**
     * Gets a list of the names of all products in the cart
     * @return a list of the names of all products in the cart
     */
    public List<String> getAllCartItems(){
        List<String> cartItems=new ArrayList<>();

        for (Map.Entry<Product, Integer> pair : cart.entrySet()) {
            cartItems.add(pair.getKey().getName());
        }

        return cartItems;

    }

    /**
     * Counts the number of a product in the cart
     * @param name of product to get count of. Product must be in the cart.
     * @return number of the product that is in the cart
     */
    public int getCartItemCount(String name){
        return cart.get(shop.fetchOneProduct(name));
    }

    /**
     * Checks out and purchases all the products in the cart
     * @return the total amount of money spent
     * @throws IllegalStateException if there is no longer enough
     * inventory of a product or the product has been removed by
     * the shop owner.
     */
    public double checkout() throws IllegalStateException {
        //checks that all items in the cart are still available in the shop
        //and that inventory is still available
        if(allItemsAvailable()){
            for (Map.Entry<Product, Integer> pair : cart.entrySet()) {
                checkoutProduct(pair.getKey(),pair.getValue());
            }

            return total;
        }

        throw new IllegalStateException();
    }

    private void checkoutProduct(Product product, int num){
        for(int i=0;i<num;i++){
            product.purchase();
        }
    }

    private boolean allItemsAvailable(){
        int num;
        Product product;
        for (Map.Entry<Product, Integer> pair : cart.entrySet()) {
            num=pair.getValue();
            product=pair.getKey();

            if(num>product.getInventory()||!shop.containsProduct(product.getName())){
                return false;
            }
        }
        return true;
    }


}
