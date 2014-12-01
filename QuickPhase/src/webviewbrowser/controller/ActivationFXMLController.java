/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import webviewbrowser.Main;
import webviewbrowser.Settings;
import static webviewbrowser.common.Type.ERROR;
import static webviewbrowser.common.Type.SUCCESS;
import webviewbrowser.json.JSONException;
import webviewbrowser.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class ActivationFXMLController implements Initializable {

  @FXML
  private Button btnActivate;
  @FXML
  private Hyperlink lnkLicenseNumber;
  @FXML
  private Button btnCancel;
  @FXML
  private TextField txtActivationCode;

  @FXML
  private Button btnLater;
  private Settings settings;
  private BrowserFXMLController browserController;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    txtActivationCode.setPromptText("Enter Activation Code Here");
  }

  public void callDefaultBrowser(String urlString) {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(new URI(urlString));
      } catch (URISyntaxException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @FXML
  private void activate(ActionEvent event) {
    try {
      URL url = new URL("http://localhost/pos-stream/activation.php?activation_code=" + txtActivationCode.getText() + "&version=" + 10);

      System.err.println(url.toString());
      BufferedReader reader = null;

      try {
        reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        for (String line; (line = reader.readLine()) != null;) {
          JSONObject jObject = new JSONObject(line);
          String message = jObject.getString("message");
          Settings.storeActivationCode(jObject);

          FXMLLoader fxmlLoader = new FXMLLoader();
          Parent myActivation = fxmlLoader.load(getClass().getResource("fxml/ConfirmationFXML.fxml").openStream());
          ConfirmationFXMLController confirmationFXMLController = (ConfirmationFXMLController) fxmlLoader.getController();

          confirmationFXMLController.setMessage(message);
          confirmationFXMLController.setSettings(settings);
          if (jObject.getInt("success") == 1) {
            confirmationFXMLController.setType(SUCCESS);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(Main.primaryStage);
            stage.setTitle("Restart");
            stage.setScene(new Scene(myActivation, 270, 100));
            stage.show();

            ((Node) (event.getSource())).getScene().getWindow().hide();
          } else {
            confirmationFXMLController.setType(ERROR);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(Main.primaryStage);
            stage.setTitle("Invalid");
            stage.setScene(new Scene(myActivation, 270, 100));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
          }
        }
      } catch (IOException | JSONException ex) {
        Logger.getLogger(ActivationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (MalformedURLException ex) {
      Logger.getLogger(ActivationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @FXML
  private void openActivationCodePage(ActionEvent event) {
    callDefaultBrowser("http://www.moonconnection.com/go/qps-a");
  }

  @FXML
  private void openAnswerPage(ActionEvent event) {
    callDefaultBrowser("http://www.moonconnection.com/quickphase/user/qph_pro/");
  }

  @FXML
  private void openSupportPage(ActionEvent event) {
    callDefaultBrowser("http://www.moonconnection.com/quickphase/user/qph_pro/");
  }

  @FXML
  private void close(ActionEvent event) {
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  
  public void setSettings(Settings settings) {
    this.settings = settings;
    int trial = settings.isTrial();
    btnLater.setVisible(trial == 0);
  }
  @FXML
  private void later(ActionEvent event) {
    try {
      JSONObject jObject = new JSONObject();
      jObject.put("activation_code", "");
      jObject.put("message", "no");
      Date date = new Date();
      jObject.put("date", date.getDate());
      Settings.storeActivationCode(jObject);
    } catch (JSONException ex) {
      Logger.getLogger(ActivationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  public void setBrowserController(BrowserFXMLController browserController) {
    this.browserController  = browserController;
  }
}
