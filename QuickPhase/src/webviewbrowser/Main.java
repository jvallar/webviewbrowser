package webviewbrowser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import webviewbrowser.controller.BrowserFXMLController;

/**
 *
 * @author Jameson Vallar
 */
public class Main extends Application {

  public static Stage primaryStage;
  private Scene scene;
  public  BrowserFXMLController browserFXMLController;

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      this.primaryStage = primaryStage;
      FXMLLoader fxmlLoader = new FXMLLoader();
      Parent browser = fxmlLoader.load(getClass().getResource("controller/fxml/BrowserFXML.fxml").openStream());
      browserFXMLController = (BrowserFXMLController) fxmlLoader.getController();
      scene = new Scene(browser, 1024, 640);
      
      scene.getStylesheets().add("webviewbrowser/controller/fxml/test.css");
      primaryStage.getIcons().add(new Image("webviewbrowser/globe.png"));
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
