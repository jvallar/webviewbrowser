/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import webviewbrowser.Settings;
import webviewbrowser.common.Type;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class ConfirmationFXMLController implements Initializable {

  @FXML
  private Label lblMessage;
  @FXML
  private Button btnLater;
  @FXML
  private Button btnOK;

  private Type type;
  private Settings settings;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  @FXML
  private void later(ActionEvent event) {
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  @FXML
  private void restart(ActionEvent event) {
    if (type.equals(Type.SUCCESS)) {
      System.exit(0);
    } else {
      settings.showActivationDialog();
    }
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  public void setType(Type type) {
    this.type = type;

  }

  public void setMessage(String message) {
    lblMessage.setText(message);
  }

  void setSettings(Settings settings) {
    this.settings = settings;
  }
}
