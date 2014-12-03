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
import javafx.scene.control.CheckBox;
import webviewbrowser.Settings;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class ColumnChooserFXMLController implements Initializable {

  private Settings settings;
  private BrowserFXMLController browserController;
  @FXML
  private CheckBox chkDate;
  @FXML
  private CheckBox chkTime;
  @FXML
  private CheckBox chkTimezone;
  @FXML
  private CheckBox chkLatitude;
  @FXML
  private CheckBox chkLongitude;
  @FXML
  private CheckBox chkPhaseName;
  @FXML
  private CheckBox chkPercentofFull;
  @FXML
  private CheckBox chkAge;
  @FXML
  private CheckBox chkAzimuth;
  @FXML
  private CheckBox chkAltitude;
  @FXML
  private CheckBox chkRise;
  @FXML
  private CheckBox chkSet;
  @FXML
  private Button btnApply;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  public void setBrowserController(BrowserFXMLController browser) {
    this.browserController = browser;
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
    initializeValues();
  }

  @FXML
  private void apply(ActionEvent event) {
    String value = chkDate.isSelected() ? ",Date" : "";
    value += chkTime.isSelected() ? ",Time" : "";
    value += chkTimezone.isSelected() ? ",Timezone" : "";
    value += chkLatitude.isSelected() ? ",Latitude" : "";
    value += chkLongitude.isSelected() ? ",Longitude" : "";
    value += chkPhaseName.isSelected() ? ",Phase Name" : "";
    value += chkPercentofFull.isSelected() ? ",Percent of Full" : "";
    value += chkAge.isSelected() ? ",Age" : "";
    value += chkAzimuth.isSelected() ? ",Azimuth" : "";
    value += chkAltitude.isSelected() ? ",Altitude" : "";
    value += chkRise.isSelected() ? ",Rise" : "";
    value += chkSet.isSelected() ? ",Set" : "";
    browserController.setInterval(value);
    settings.setProgramSettings("hayStack", value);
    settings.storeProgramSettings();
    settings.loadProgramSettings();
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  private void initializeValues() {
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkDate), chkDate);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkTime), chkTime);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkTimezone), chkTimezone);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkLatitude), chkLatitude);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkLongitude), chkLongitude);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkPhaseName), chkPhaseName);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkPercentofFull), chkPercentofFull);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkAge), chkAge);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkAzimuth), chkAzimuth);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkAltitude), chkAltitude);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkRise), chkRise);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkSet), chkSet);

  }

  private boolean showColumn(String hayStack, CheckBox chkbox) {
    if (hayStack.equalsIgnoreCase("All")) {
      return true;
    }
    if (hayStack.indexOf(chkbox.getText()) == -1) {
      return false;
    }
    return true;
  }

  private void setCheckBox(boolean programmSettings, CheckBox chkbox) {
    chkbox.setSelected(programmSettings);
  }
}
