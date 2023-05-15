package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Starts the Inventory Management System.
 *
 * FUTURE ENHANCEMENT - A future enhancement for the inventory management app is to add a popup preview window, so before you
 * decide to modify a part or product you can view all the details using a popup preview window by selecting the id you wish to modify,
 * it'll also show the parts associated with the product. This enhancement will make it easier and faster to view data.
 *
 * The folder for the JavaDoc files are located within the root folder C482-Software1_JohnG
 *
 * @author
 * John Gutierrez
 */
public class Main extends Application {
    /**
     * Class starts the main screen for the inventory management app
     *
     * @param stage Opens Main Tab
     */
    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainTab.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene(root, 850, 500));
        stage.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
