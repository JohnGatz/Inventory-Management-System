package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for Inventory
 *
 *  @author
 *  John Gutierrez
 */
public class Inventory {

    //Part
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     *
     * @param partId
     * @return null
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }

        return null;
    }

    /**
     *
     * @param partName
     * @return lookupPartFound
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> lookupPartFound = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().equals(partName)) {
                lookupPartFound.add(part);
            }
        }

        return lookupPartFound;
    }

    /**
     *
     * @param index
     * @param selectedPart
     */
    public static void updatePart (int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     *
     * @param selectedPart
     * @return boolean
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @return allParts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    //Product
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     *
     * @param productId
     * @return null
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }

        return null;
    }

    /**
     *
     * @param productName
     * @return lookupProductFound
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> lookupProductFound = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().equals(productName)) {
                lookupProductFound.add(product);
            }
        }

        return lookupProductFound;
    }

    /**
     *
     * @param index
     * @param selectedProduct
     */
    public static void updateProduct (int index, Product selectedProduct) {

        allProducts.set(index, selectedProduct);
    }

    /**
     *
     * @param selectedProduct
     * @return boolean
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @return allProducts
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
