package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The add product controller allows you to enter a product using the text fields provided, as well as adding associated
 * parts to the product after saving it will return to the main screen and show the product you created
 *
 * @author
 * John Gutierrez
 */
public class AddProductTabController implements Initializable {

    private ObservableList<Part> asscParts = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> associatedPartTable;

    @FXML
    private TableColumn<Part, Integer> associatedPartTableIdColumn;

    @FXML
    private TableColumn<Part, String> associatedPartTableNameColumn;

    @FXML
    private TableColumn<Part, Integer> associatedPartTableInventoryColumn;

    @FXML
    private TableColumn<Part, Double> associatedPartTablePriceColumn;

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

    @FXML
    private TextField partSearchTextField;

    @FXML
    private TextField productIdTextField;

    @FXML
    private TextField productNameTextField;

    @FXML
    private TextField productInventoryTextField;

    @FXML
    private TextField productPriceTextField;

    @FXML
    private TextField productMaxTextField;

    @FXML
    private TextField productMinTextField;

    Parent scene;

    private Part selectedPart;

    /**
     * initialize the add product form by filling in the data from the part table in the main tab
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        partTable.setItems(Inventory.getAllParts());
        partTableIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partTableInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partTablePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartTableIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartTableInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartTablePriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Search bar that searches for partial name or id within the part table
     *
     * @param event Part search action.
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
        catch(NumberFormatException exception) {
            dialogBox(8);
        }
    }

    /**
     * Adds selected part to be associated with the current product in the add product form.
     *
     * @param event Add associated part action.
     */
    @FXML
    void onActionAddAsscPart(ActionEvent event) {
        //add private Part selectedPart; as local variable
        selectedPart = partTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            asscParts.add(selectedPart);
            associatedPartTable.setItems(asscParts);
        }
        else {
            dialogBox(5);
        }
    }
    /**
     * Removes selected part that is associated with the current product in the add product form.
     *
     * @param event Remove associated part action.
     */
    @FXML
    void onActionRemoveAsscPart(ActionEvent event) {

        selectedPart = associatedPartTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirm.setTitle("Alert");
            alertConfirm.setContentText(null);
            alertConfirm.setContentText("Do you want to remove the selected part?");
            Optional<ButtonType> result = alertConfirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                asscParts.remove(selectedPart);
                associatedPartTable.setItems(asscParts);
            }
        }
        else {
            dialogBox(5);
        }
    }
    /**
     * Once all the text fields are filled out it will check to make sure no errors are found and then applied to the
     * Main Tab. If errors were found it will display a dialog box to show what error occurred.
     *
     * @param event Save action.
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {

        try {
            int id = 0;
            String name = productNameTextField.getText();
            Double price = Double.parseDouble(productPriceTextField.getText());
            int stock = Integer.parseInt(productInventoryTextField.getText());
            int min = Integer.parseInt(productMinTextField.getText());
            int max = Integer.parseInt(productMaxTextField.getText());

            if (name.isEmpty()) {
                dialogBox(6);
            }
            else if (!(Integer.class.isInstance(Integer.parseInt(productInventoryTextField.getText())))) {
                dialogBox(1);
            }
            else if (!(Double.class.isInstance(Double.parseDouble(productPriceTextField.getText())))) {
                dialogBox(1);
            }
            else if (Integer.parseInt(productMaxTextField.getText()) < Integer.parseInt(productMinTextField.getText())) {
                dialogBox(3);
            }
            else if (Integer.parseInt(productInventoryTextField.getText()) < Integer.parseInt(productMinTextField.getText())) {
                dialogBox(4);
            }
            else if(Integer.parseInt(productInventoryTextField.getText()) > Integer.parseInt(productMaxTextField.getText())){
                dialogBox(4);
            }
            else {
                Product newProduct = new Product(id, name, price, stock, min, max);

                for (Part part : asscParts) {
                    newProduct.addAssociatedPart(part);
                }

                newProduct.setId((int) (Math.random() * 100));
                Inventory.addProduct(newProduct);

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainTab.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (Exception exception){
            dialogBox(1);
        }
    }

    /**
     * Displays a dialog box confirming your action and Starts the Main Tab. If action was cancelled it will display
     * you cancelled.
     *
     * @param event Cancel action.
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {

        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Alert");
        alertConfirm.setHeaderText(null);
        alertConfirm.setContentText("Are you sure? This will cancel all fields and return to the Main Screen.");
        Optional<ButtonType> result = alertConfirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainTab.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else if(result.get() == ButtonType.CANCEL){
            dialogBox(7);
        }
    }


    /**
     * Displays dialog when method is called using the id.
     *
     * @param dialogType use to identify which dialog to call
     */
    private void dialogBox(int dialogType) {

        Alert alertError = new Alert(Alert.AlertType.ERROR);
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

        if (dialogType == 1) {
            alertError.setTitle("Error");
            alertError.setHeaderText("Unable to add Product");
            alertError.setContentText("Invalid values in text fields please revise.");
            alertError.showAndWait();
        }
        else if (dialogType == 2) {
            alertInfo.setTitle("Information");
            alertInfo.setHeaderText(null);
            alertInfo.setHeaderText("Unable to find Part");
            alertInfo.showAndWait();
        }
        else if (dialogType == 3) {
            alertError.setTitle("Error");
            alertError.setHeaderText("Unable to add Min");
            alertError.setContentText("Min value cannot be greater than Max value.");
            alertError.showAndWait();
        }
        else if (dialogType == 4) {
            alertError.setTitle("Error");
            alertError.setHeaderText("Unable to add Inventory");
            alertError.setContentText("Inventory must be between Min and Max values.");
            alertError.showAndWait();
        }
        else if (dialogType == 5) {
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setHeaderText("No part was selected.");
            alertError.showAndWait();
        }
        else if (dialogType == 6) {
            alertError.setTitle("Error");
            alertError.setHeaderText("Unable to add Name");
            alertError.setContentText("Cannot leave Name field blank.");
            alertError.showAndWait();
        }
        else if (dialogType == 7) {
            alertInfo.setTitle("Information");
            alertInfo.setHeaderText(null);
            alertInfo.setContentText("Fields were not canceled.");
            alertInfo.showAndWait();
        }
        else if (dialogType == 8) {
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("Search has incorrect Values.");
            alertError.showAndWait();
        }
    }
}
