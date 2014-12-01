/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import webviewbrowser.Settings;
import webviewbrowser.common.Option;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class IntervalFXMLController implements Initializable {

  @FXML
  private TextField txtNumber;
  @FXML
  private ComboBox<Option> cmbUnit;
  private Settings settings;
  private BrowserFXMLController browserController;
  @FXML
  private Button btnApply;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
    initializeComboBox();
  }

  public void setBrowserController(BrowserFXMLController browserController) {
    this.browserController = browserController;
  }

  @FXML
  private void apply(ActionEvent event) {
    BigDecimal number = new BigDecimal(txtNumber.getText());
    BigDecimal rate = new BigDecimal(cmbUnit.getValue().getData());
    BigDecimal value = number.multiply(rate);
    browserController.setInterval(value);
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  private void initializeComboBox() {
    cmbUnit.getItems().add(new Option("1", "day"));
    cmbUnit.getItems().add(new Option("0.0416666666666667", "hour"));
    cmbUnit.getItems().add(new Option("0.0006944444444444444", "minute"));
    cmbUnit.getItems().add(new Option("0.000011574074074074074074074074074074", "second"));

  }

}
