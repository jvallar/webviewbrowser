/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import webviewbrowser.Settings;
import webviewbrowser.common.Option;
import webviewbrowser.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class DateFormatFXMLController implements Initializable {

  private Settings settings;
  private BrowserFXMLController browserController;
  @FXML
  private ComboBox<Option> cmbDateFormat;
  @FXML
  private ComboBox<Option> cmbTimeFormat;
  @FXML
  private ComboBox<Option> cmdCalendarsTime;
  @FXML
  private ComboBox<Option> cmbRefreshDayView;
  @FXML
  private CheckBox chkSouthernHemisphere;
  @FXML
  private Button btnSaveNew;
  private List<JSONObject> list;
  @FXML
  private Button btnCancel;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
    initializeComboBox();
    initializeValues();
  }

  public void setBrowserController(BrowserFXMLController browserController) {
    this.browserController = browserController;
  }

  private void initializeComboBox() {
    cmbRefreshDayView.getItems().add(new Option("1", "Every second"));
    cmbRefreshDayView.getItems().add(new Option("5", "Every 5 seconds"));
    cmbRefreshDayView.getItems().add(new Option("30", "Every 30 seconds"));
    cmbRefreshDayView.getItems().add(new Option("-1", "Don't refresh"));

    cmdCalendarsTime.getItems().add(new Option("-1", "Use Target Time"));
    cmdCalendarsTime.getItems().add(new Option("0", "12 AM - Midnight (0:00)"));
    cmdCalendarsTime.getItems().add(new Option("1", "1 AM (1:00)"));
    cmdCalendarsTime.getItems().add(new Option("2", "selected>2 AM (2:00)"));
    cmdCalendarsTime.getItems().add(new Option("3", "3 AM (3:00)"));
    cmdCalendarsTime.getItems().add(new Option("4", "4 AM (4:00)"));
    cmdCalendarsTime.getItems().add(new Option("5", "5 AM (5:00)"));
    cmdCalendarsTime.getItems().add(new Option("6", "6 AM (6:00)"));
    cmdCalendarsTime.getItems().add(new Option("7", "7 AM (7:00)"));
    cmdCalendarsTime.getItems().add(new Option("8", "8 AM (8:00)"));
    cmdCalendarsTime.getItems().add(new Option("9", "9 AM (9:00)"));
    cmdCalendarsTime.getItems().add(new Option("10", "10 AM (10:00)"));
    cmdCalendarsTime.getItems().add(new Option("11", "11 AM (11:00)"));
    cmdCalendarsTime.getItems().add(new Option("12", "12 PM - Noon (12:00)"));
    cmdCalendarsTime.getItems().add(new Option("13", "1 PM (13:00)"));
    cmdCalendarsTime.getItems().add(new Option("14", "2 PM (14:00)"));
    cmdCalendarsTime.getItems().add(new Option("15", "3 PM (15:00)"));
    cmdCalendarsTime.getItems().add(new Option("16", "4 PM (16:00)"));
    cmdCalendarsTime.getItems().add(new Option("17", "5 PM (17:00)"));
    cmdCalendarsTime.getItems().add(new Option("18", "6 PM (18:00)"));
    cmdCalendarsTime.getItems().add(new Option("19", "7 PM (19:00)"));
    cmdCalendarsTime.getItems().add(new Option("20", "8 PM (20:00)"));
    cmdCalendarsTime.getItems().add(new Option("21", "9 PM (21:00)"));
    cmdCalendarsTime.getItems().add(new Option("22", "10 PM (22:00)"));
    cmdCalendarsTime.getItems().add(new Option("23", "11 PM (23:00)"));

    cmbTimeFormat.getItems().add(new Option("12hr", "12-hour clock (AM/PM)"));
    cmbTimeFormat.getItems().add(new Option("24hr", "24-hour clock"));

    cmbDateFormat.getItems().add(new Option("mm/dd/yyyy", "mm/dd/yyyy"));
    cmbDateFormat.getItems().add(new Option("dd/mm/yyyy", "dd/mm/yyyy"));
  }


  private void setCheckBox(String programmSettings, CheckBox chkApplyDayLightSaving) {
    chkApplyDayLightSaving.setSelected(programmSettings.equalsIgnoreCase("yes"));
  }

  private void setTextField(String programmSettings, TextField txtField) {
    txtField.setText(programmSettings);
  }

  public void setComboBox(String data, ComboBox comboBox) {
    ObservableList<Option> list1 = comboBox.getItems();
    for (Option option : list1) {
      if (option.getData().equals(data)) {
        comboBox.setValue(option);
      }
    }
  }

  private void initializeValues() {

    setComboBox(settings.getProgrammSettings("dt_format"), cmbDateFormat);
    setComboBox(settings.getProgrammSettings("cal_time"), cmdCalendarsTime);
    setComboBox(settings.getProgrammSettings("refresh"), cmbRefreshDayView);
    setComboBox(settings.getProgrammSettings("s_hemisphere"), cmbRefreshDayView);
    setComboBox(settings.getProgrammSettings("clock"), cmbTimeFormat);

    setCheckBox(settings.getProgrammSettings("s_hemisphere"), chkSouthernHemisphere);

  }

  @FXML
  private void save(ActionEvent event) {
    settings.setProgramSettings("dt_format", cmbDateFormat.getValue().getData());
    settings.setProgramSettings("cal_time", cmdCalendarsTime.getValue().getData());
    settings.setProgramSettings("refresh", cmbRefreshDayView.getValue().getData());
    settings.setProgramSettings("s_hemisphere", chkSouthernHemisphere.isSelected() ? "yes" : "no");
    settings.setProgramSettings("clock", cmbTimeFormat.getValue().getData());
    settings.storeProgramSettings();
    settings.loadProgramSettings();
  }

  @FXML
  private void cancel(ActionEvent event) {    
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }
}
