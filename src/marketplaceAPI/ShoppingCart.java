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
     *
     * @return
     */
    public List<String> getAllCartItems(){


    }

    /**
     *
     * @param name
     * @return
     */
    public int getCartItemCount(String name){


    }

    /**
     *
     * @param name
     * @return
     */
    public boolean containsProduct(String name){

    }

    /**
     *
     */
    public void checkout(){

    }


}
