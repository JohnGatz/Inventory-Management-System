package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.util.Optional;


/**
 * The add part controller allows you to enter a part using the text fields provided, after saving it will
 * return to the main screen and show the part you created
 * @author
 * John Gutierrez
 */
public class AddPartTabController {

    @FXML
    private Label MachineidAndCompanyNameLabel;

    @FXML
    private RadioButton inHouseRadioButton;

    @FXML
    private RadioButton outsourcedRadioButton;

    @FXML
    private TextField partIdTextField;

    @FXML
    private TextField partNameTextField;

    @FXML
    private TextField partInventoryTextField;

    @FXML
    private TextField partPriceTextField;

    @FXML
    private TextField partMaxTextField;

    @FXML
    private TextField partMinTextField;

    @FXML
    private TextField MachineidAndCompanyNameTextField;


    Parent scene;

    /**
     * action to set inhouse radio button to Machine ID
     *
     * @param event InHouse radio action.
     */
    @FXML
    void inHouseRadioButtonOnAction(ActionEvent event) {

        MachineidAndCompanyNameLabel.setText("Machine ID");
    }

    /**
     * action to set Outsourced radio button to Machine ID
     *
     * @param event Outsourced radio action.
     */
    @FXML
    void outsourcedRadioButtonOnAction(ActionEvent event) {

        MachineidAndCompanyNameLabel.setText("Company Name");
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
            String name = partNameTextField.getText();
            Double price = Double.parseDouble(partPriceTextField.getText());
            int stock = Integer.parseInt(partInventoryTextField.getText());
            int min = Integer.parseInt(partMinTextField.getText());
            int max = Integer.parseInt(partMaxTextField.getText());
            boolean formIsValid = false;

            if (name.isEmpty()) {
                dialogBox(5);
            }
            else if (!(Integer.class.isInstance(Integer.parseInt(partInventoryTextField.getText())))) {
                dialogBox(1);
            }
            else if (!(Double.class.isInstance(Double.parseDouble(partPriceTextField.getText())))) {
                dialogBox(1);
            }
            else if (Integer.parseInt(partMaxTextField.getText()) < Integer.parseInt(partMinTextField.getText())) {
                dialogBox(3);
            }
            else if (Integer.parseInt(partInventoryTextField.getText()) < Integer.parseInt(partMinTextField.getText())){
                dialogBox(4);
            }
            else if (Integer.parseInt(partInventoryTextField.getText()) > Integer.parseInt(partMaxTextField.getText())){
                dialogBox(4);
            }
            else {
                if (inHouseRadioButton.isSelected()) {
                    try {
                        int machineId = Integer.parseInt(MachineidAndCompanyNameTextField.getText());
                        InHouse partInHouse = new InHouse(id, name, price, stock, min, max, machineId);
                        partInHouse.setId((int) (Math.random() * 100));
                        Inventory.addPart(partInHouse);
                        formIsValid = true;
                    }
                    catch (Exception exception) {
                        dialogBox(2);
                    }
                }
                else if (outsourcedRadioButton.isSelected()) {
                    String companyName = MachineidAndCompanyNameTextField.getText();
                    Outsourced partOutsourced = new Outsourced(id, name, price, stock, min, max, companyName);
                    partOutsourced.setId((int) (Math.random() * 100));
                    Inventory.addPart(partOutsourced);
                    formIsValid = true;
                }

                if(formIsValid) {
                    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainTab.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                }
            }
        }
        catch(Exception exception) {
            dialogBox(1);
        }
    }

    /**
     * Displays a dialog box confirming your action and Starts the Main Tab. If action was cancelled it will display
     * you cancelled
     *
     * @param event
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {

        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Alert");
        alertConfirm.setHeaderText(null);
        alertConfirm.setContentText("Are you sure? This will cancel all fields and return to the Main Screen.");
        Optional<ButtonType> result = alertConfirm.showAndWait();

        if(result.get() == ButtonType.OK){
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainTab.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else if(result.get() == ButtonType.CANCEL){
            dialogBox(6);
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
            alertError.setTitle("Error");
            alertError.setHeaderText("Unable to add Part");
            alertError.setContentText("Invalid values in text fields please revise.");
            alertError.showAndWait();
        }
        else if (dialogType == 2) {
            alertError.setTitle("Error");
            alertError.setHeaderText("Unable to add Machine ID");
            alertError.setContentText("Incorrect value in Machine ID must use number.");
            alertError.showAndWait();
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
            alertError.setHeaderText("Unable to add Name");
            alertError.setContentText("Cannot leave Name field blank.");
            alertError.showAndWait();
        }
        else if (dialogType == 6) {
            alertInfo.setTitle("Information");
            alertInfo.setHeaderText(null);
            alertInfo.setContentText("Fields were not canceled.");
            alertInfo.showAndWait();
        }
    }
}
