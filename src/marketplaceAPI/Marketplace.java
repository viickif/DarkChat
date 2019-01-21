package marketplaceAPI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Marketplace {

    private Set<Product> allProducts;

    /**
     * Initializes a new Marketplace
     * @param products to add to Marketplace
     */
    public Marketplace(List<Product> products){
        allProducts=new HashSet<>();
        allProducts.addAll(products);

    }

    /**
     * Initializes a new Marketplace
     * @param products to add to Marketplace
     */
    public Marketplace(Set<Product> products){
        allProducts=new HashSet<>();
        allProducts.addAll(products);

    }

    /**
     * Initializes a new empty Marketplace
     *
     */
    public Marketplace(){
        allProducts=new HashSet<>();
    }

    /**
     * fetches all products in the marketplace that have
     * inventory count > 0
     * @return List of products in marketplace with inventory
     * count > 0
     */
    public List<Product> fetchAllProducts(){
        List<Product> nonEmptyProducts=new ArrayList<>();

        for(Product product:allProducts){
            if(!product.inventoryIsEmpty()){
                nonEmptyProducts.add(product);
            }
        }

        return nonEmptyProducts;
    }

    /**
     * fetches the product with the specified name
     * if it exists
     * @param name of the product to fetch. Such product
     *             must exist in the marketplace
     * @return the product matching the specified name
     */
    public Product fetchOneProduct(String name) {
        for(Product product:allProducts) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Determines if a product with the specified name exists in
     * the marketplace
     * @param name of the product to search for
     * @return true if a product matching the specified name exists
     * in the marketplace and false otherwise
     */
    public boolean containsProduct(String name){
        for(Product product:allProducts){
            if(product.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new product into the marketplace if no product
     * with the same name currently exists in the markletplace
     * @param newProduct to add to the marketplace. No product
     *                   with the same name must currently exists
     *                   in the markletplace
     */
    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * Removes an existing product from the marketplace.
     * @param name of product to remove. A product
     *                   with the same name must currently exists
     *                   in the markletplace.
     */
    public void removeProduct(String name){
        for(Product product:allProducts){
            if(product.getName().equals(name)){
                allProducts.remove(product);
            }
        }
    }


    /**
     * Determines if a product in the marketplace has
     * available inventory
     * @param name of product to check inventory of. It must
     *             exist in the marketplacr
     * @return true if the product inventory > 0 and false otherwise
     */
    public boolean inventoryIsEmpty(String name){
        for(Product product:allProducts){
            if(product.getName().equals(name)){
                return product.inventoryIsEmpty();
            }
        }
        return false;
    }

    /**Purchases a product in the marketplace
     * @param name of product to purchase.
     * @return true if the product inventory was not empty
     * and the product was purchased and false otherwise
     *
     */
    public boolean purchaseProduct(String name){
        for(Product product:allProducts){
            if(product.getName().equals(name)&& !product.inventoryIsEmpty()){
                product.purchaseProduct();
                return true;
            }
        }
        return false;
    }

}
