package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The modify part controller allows you update the part that the user has selected.
 *
 * @author
 * John Gutierrez
 */
public class ModifyPartTabController implements Initializable {

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
    private TextField MachineidAndCompanyNameTextField;

    @FXML
    private TextField partMinTextField;

    Parent scene;

    private Part selectedPart;

    /**
     * action to set InHouse radio button to Machine ID
     *
     * @param event In-house radio action.
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
     * initialize Modify Part Tab text fields by filling in the data provided from the main tab, an if statement is used
     * to check if the inhouse or outsourced radio buttons are pressed.
     *
     * LOGICAL ERROR - I ran into a problem when bringing in the part I wanted to modify the part did not show the correct
     * label for the MachineidAndCompanyNameLabel it applied the default radio button label which is "Machine ID", I first tried setting up
     * a separate function to handle the process, and start the function by calling it in the mainTabController when using
     * the modify part action but i found it easier to start it in initialize and creating an if else statement so when it detects
     * Inhouse Radio button is selected it will set the MachineidAndCompanyNameLabel to "Machine ID" and else which means the
     * radio button is set to outsourced. MachineidAndCompanyNameLabel will be rename the label to "Company Name".
     *
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectedPart = MainTabController.modifyPart;

        if (selectedPart instanceof InHouse) {
            inHouseRadioButton.setSelected(true);
            MachineidAndCompanyNameLabel.setText("Machine ID");
            partIdTextField.setText(String.valueOf(selectedPart.getId()));
            partNameTextField.setText(String.valueOf(selectedPart.getName()));
            partInventoryTextField.setText(String.valueOf(selectedPart.getStock()));
            partPriceTextField.setText(String.valueOf(selectedPart.getPrice()));
            partMaxTextField.setText(String.valueOf(selectedPart.getMax()));
            partMinTextField.setText(String.valueOf(selectedPart.getMin()));
            MachineidAndCompanyNameTextField.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }
        else {
            outsourcedRadioButton.setSelected(true);
            MachineidAndCompanyNameLabel.setText("Company Name");
            partIdTextField.setText(String.valueOf(selectedPart.getId()));
            partNameTextField.setText(String.valueOf(selectedPart.getName()));
            partInventoryTextField.setText(String.valueOf(selectedPart.getStock()));
            partPriceTextField.setText(String.valueOf(selectedPart.getPrice()));
            partMaxTextField.setText(String.valueOf(selectedPart.getMax()));
            partMinTextField.setText(String.valueOf(selectedPart.getMin()));
            MachineidAndCompanyNameTextField.setText(String.valueOf(((Outsourced) selectedPart).getCompanyName()));
        }


    }

    /**
     * Once all the text fields are filled out it will check to make sure no errors are found and then the action will update the
     * selected id and apply it to the Main Tab. If errors were found it will display a dialog box to show what error occurred.
     *
     * @param event Save action.
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {

        try {
            int id = selectedPart.getId();
            String name = partNameTextField.getText();
            Double price = Double.parseDouble(partPriceTextField.getText());
            int stock = Integer.parseInt(partInventoryTextField.getText());
            int min = Integer.parseInt(partMinTextField.getText());
            int max = Integer.parseInt(partMaxTextField.getText());
            boolean formIsValid = false;

            if (name.isEmpty()) {
                dialogBox(6);
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
            else if(Integer.parseInt(partInventoryTextField.getText()) > Integer.parseInt(partMaxTextField.getText())){
                dialogBox(4);
            }
            else {
                if (inHouseRadioButton.isSelected()) {
                    try {
                        int machineId = Integer.parseInt(MachineidAndCompanyNameTextField.getText());
                        InHouse partInHouse = new InHouse(id, name, price, stock, min, max, machineId);
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
                    Inventory.addPart(partOutsourced);
                    formIsValid = true;
                }

                if(formIsValid) {
                    Inventory.deletePart(selectedPart);
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
            dialogBox(5);
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
            alertError.setHeaderText("Unable to modify Part");
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
            alertInfo.setTitle("Information");
            alertInfo.setHeaderText(null);
            alertInfo.setContentText("Fields were not canceled.");
            alertInfo.showAndWait();
        }
        else if (dialogType == 6) {
            alertError.setTitle("Error");
            alertError.setHeaderText("Unable to add Name");
            alertError.setContentText("Cannot leave Name field blank.");
            alertError.showAndWait();
        }
    }
}
