package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import model.*;
import java.util.Optional;

/**
 * Creates a controller for the main screen
 * @author
 * John Gutierrez
 */
public class MainTabController implements Initializable {

    //Part
    @FXML
    private TextField partSearchTextField;

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableColumn<Part, Integer> partTableIdColumn;

    @FXML
    private TableColumn<Part, String> partTableNameColumn;

    @FXML
    private TableColumn<Part, Integer> partTableInventoryColumn;

    @FXML
    private TableColumn<Part, Double> partTablePriceColumn;

    //Product
    @FXML
    private TextField productSearchTextField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> productTableIdColumn;

    @FXML
    private TableColumn<Product, String> productTableNameColumn;

    @FXML
    private TableColumn<Product, Integer> productTableInventoryColumn;

    @FXML
    private TableColumn<Product, Double> productTablePriceColumn;

    Stage stage;
    Parent scene;
    private Part selectedPart;
    public static Part modifyPart;
    public static Product modifyProduct;

    /**
     * Starts controller and Loads both tables with any data that has been added or modified
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        partTable.setItems(Inventory.getAllParts());
        productTable.setItems(Inventory.getAllProducts());

        //Loads data into the part table
        partTableIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partTableInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partTablePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Loads data into the product table
        productTableIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productTableInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productTablePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    //Part
    /**
     * Search bar that searches for partial name or id within the part table
     *
     * @param event Part Table search action.
     */
    @FXML
    void onActionSearchPart(ActionEvent event) {
        String lookupSearch = partSearchTextField.getText();
        ObservableList<Part> lookupPartFound = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        try{
            for (Part part : allParts) {
                if (part.getName().contains(lookupSearch)) {
                    lookupPartFound.add(part);
                }
                else if(String.valueOf(part.getId()).contains(lookupSearch)){
                    lookupPartFound.add(part);
                }
            }

            partTable.setItems(lookupPartFound);

            if (lookupPartFound.size() == 0) {
                dialogBox(2);
            }
        }
        catch(Exception exception){
            dialogBox(7);
        }
    }

    /**
     * Starts the Add Part form
     *
     * @param event Part table Add action.
     */
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartTab.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * Starts the Modify Part form. if an item is selected it will proceed to the Modify Part Form, if no item was
     * selected it will display a dialog stating that nothing was selected
     *
     * @param event Part table modify action.
     */
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {

        modifyPart = partTable.getSelectionModel().getSelectedItem();

        if (modifyPart != null) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/ModifyPartTab.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            dialogBox(3);
        }
    }

    /**
     * Item is deleted if user selects it from the part table and will give a confirmation if you are sure you want to
     * delete, if you decide to cancel it will state that nothing was deleted.
     *
     * @param event Part table delete action.
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {

        selectedPart = partTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("Alert");
            alertConfirm.setHeaderText(null);
            alertConfirm.setContentText("Are you sure you want to delete?");
            Optional<ButtonType> result = alertConfirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
            else if(result.get() == ButtonType.CANCEL){
                dialogBox(6);
            }
        }
        else {
            dialogBox(3);
        }
    }
    //Product
    /**
     * Search bar that searches for partial name or id within the part table
     *
     * @param event Product table search action.
     */
    @FXML
    void onActionSearchProduct(ActionEvent event) {
        String lookupSearch = productSearchTextField.getText();
        ObservableList<Product> lookupProductFound = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();


        try {
            for (Product product : allProducts) {
                if (product.getName().contains(lookupSearch)) {
                    lookupProductFound.add(product);
                }
                else if(String.valueOf(product.getId()).contains(lookupSearch)){
                    lookupProductFound.add(product);
                }
            }
            productTable.setItems(lookupProductFound);
            if (lookupProductFound.size() == 0) {
                dialogBox(1);
            }
        }
        catch(NumberFormatException exception) {
            dialogBox(7);
        }
    }
    /**
     * Starts the Add product form
     *
     * @param event Add product action.
     */
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProductTab.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * Starts the Modify Product form. if an item is selected it will proceed to the Modify product Form, if no item was
     * selected it will display a dialog stating that nothing was selected
     *
     * @param event Product modify action.
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {

        modifyProduct = productTable.getSelectionModel().getSelectedItem();

        if (modifyProduct != null) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/ModifyProductTab.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            dialogBox(4);
        }
    }

    /**
     * Item is deleted if user selects it from the Product table and will give a confirmation if you are sure you want to
     * delete, if you decide to cancel it will state that nothing was deleted.
     *
     * @param event Product table delete action.
     */

    @FXML
    void onActionDeleteProduct(ActionEvent event) {

        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("Alert");
            alertConfirm.setHeaderText(null);
            alertConfirm.setContentText("Are you sure you want to delete?");
            Optional<ButtonType> result = alertConfirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                ObservableList<Part> asscParts = selectedProduct.getAllAssociatedParts();

                if (asscParts.size() >= 1) {
                    dialogBox(5);
                }
                else {
                    Inventory.deleteProduct(selectedProduct);
                }
            }
            else if(result.get() == ButtonType.CANCEL){
                dialogBox(6);
            }
        }
        else {
            dialogBox(4);
        }
    }


    /**
     * Exits the Inventory Management System App
     *
     * @param event Exit action.
     */
    @FXML
    void onActionExit(ActionEvent event) {
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Alert");
        alertConfirm.setHeaderText(null);
        alertConfirm.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alertConfirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else if(result.get() == ButtonType.CANCEL){
            dialogBox(8);
        }
    }
    /**
     * Displays dialog when method is called using the id.
     *
     * @param dialogType use to identify which dialog to call
     */
    private void dialogBox(int dialogType) {
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        if (dialogType == 1) {
            alertInfo.setTitle("Information");
            alertInfo.setHeaderText(null);
            alertInfo.setHeaderText("Unable to find Product");
            alertInfo.showAndWait();
        }
        else if (dialogType == 2) {
            alertInfo.setTitle("Information");
            alertInfo.setHeaderText(null);
            alertInfo.setHeaderText("Unable to find Part.");
            alertInfo.showAndWait();
        }
        else if (dialogType == 3) {
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setHeaderText("No part was selected.");
            alertError.showAndWait();
        }
        else if (dialogType == 4) {
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setHeaderText("No product was selected.");
            alertError.showAndWait();
        }
        else if (dialogType == 5) {
            alertError.setTitle("Error");
            alertError.setHeaderText("Some Parts are still associated to this product");
            alertError.setContentText("Please remove all associated parts before deleting a product.");
            alertError.showAndWait();
        }
        else if (dialogType == 6) {
            alertInfo.setTitle("Information");
            alertInfo.setHeaderText(null);
            alertInfo.setContentText("item was not deleted.");
            alertInfo.showAndWait();
        }
        else if (dialogType == 7) {
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("Search has incorrect Values.");
            alertError.showAndWait();
        }
        else if (dialogType == 8) {
            alertError.setTitle("Information");
            alertError.setHeaderText(null);
            alertError.setContentText("Program did not Exit.");
            alertError.showAndWait();
        }
    }
}
