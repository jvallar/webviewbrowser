/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import webviewbrowser.InformationDialog;
import webviewbrowser.Settings;
import webviewbrowser.json.JSONException;
import webviewbrowser.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class DateTimeFXMLController implements Initializable {

  @FXML
  private ListView<String> lstLocation;
  @FXML
  private DatePicker date;
  @FXML
  private TextField txtTime;
  @FXML
  private Button btnNow;
  @FXML
  private Button btnSave;
  @FXML
  private Button btnApply;
  private BrowserFXMLController browserController;
  private Settings settings;
  private List<JSONObject> list;
  @FXML
  private Button btnDelete;
  @FXML
  private Button btnEdit;
  @FXML
  private Button btnAdd;
  @FXML
  private Button btnClose;
  private boolean edit;
  private Stage stage;
  @FXML
  private CheckBox chkDefaulDateTime;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
//    initializeValues();
    initializeListView();
  }

  public void setBrowserController(BrowserFXMLController browserController) {
    this.browserController = browserController;
  }

  private void initializeListView() {
    list = settings.getDateTimeSettings();
    btnEdit.disableProperty().bind(lstLocation.getSelectionModel().selectedItemProperty().isNull());
    btnDelete.disableProperty().bind(lstLocation.getSelectionModel().selectedItemProperty().isNull());
    btnApply.disableProperty().bind(lstLocation.getSelectionModel().selectedItemProperty().isNull());        

    lstLocation.getItems().clear();
    for (JSONObject jSONObject : list) {
      try {
        String datetime = jSONObject.getString("datetime");
        if (datetime.equalsIgnoreCase(settings.getDefaultDatetime())) {
          datetime = "[DEFAULT] " + datetime;
          lstLocation.getItems().add(datetime);
          lstLocation.getSelectionModel().select(datetime);
        } else {
          lstLocation.getItems().add(datetime);
        }
      } catch (JSONException ex) {
        Logger.getLogger(BrowserFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

  }

  private void initializeValues() {
    setTime(settings.getProgrammSettings("time"), txtTime);
    setDate(settings.getProgrammSettings("date"), date);
    setCheckBox(settings.getCurrentDatetime().equalsIgnoreCase(settings.getProgrammSettings("datetime")) ? "yes" : "no", chkDefaulDateTime);
  }

  private void setCheckBox(String programmSettings, CheckBox chkDefaulDateTime) {
    chkDefaulDateTime.setSelected(programmSettings.equalsIgnoreCase("yes"));
  }

  private void setDate(String programmSettings, DatePicker datePicker) {
    try {
      if (programmSettings.isEmpty()) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        LocalDate lcDate = LocalDate.of(year, month + 1, day);
        datePicker.setValue(lcDate);

      } else {
        SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = newformat.parse(programmSettings);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        LocalDate lcDate = LocalDate.of(year, month + 1, day);
        datePicker.setValue(lcDate);
      }
    } catch (ParseException ex) {
      datePicker.setValue(LocalDate.now());
    }
  }

  private void setTime(String programmSettings, TextField txtTime) {
    if (programmSettings.isEmpty()) {
      Calendar calendar = Calendar.getInstance();
      int hour = calendar.get(Calendar.HOUR_OF_DAY);
      int minute = calendar.get(Calendar.MINUTE);
      txtTime.setText(hour + ":" + minute);
    } else {
      txtTime.setText(programmSettings);
    }
  }

  @FXML
  private void getNowTime(ActionEvent event) {
    setTime("", txtTime);
    setDate("", date);
  }

  @FXML
  private void save(ActionEvent event) {
    if (edit) {
      update();
      edit = false;
    } else {
      add();
    }

  }

  private void update() {
    try {
      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
      String dateStr = date.getValue().toString();
      Date date2 = originalFormat.parse(dateStr);

      SimpleDateFormat labelformat = new SimpleDateFormat("EEEE, dd MMM yyyy");
      String newDatTimeFormat = labelformat.format(date2);
      String listLabel = newDatTimeFormat + " " + txtTime.getText();
      settings.setDefaultDatetime(listLabel);

      String newDate = newformat.format(date2);
      settings.setDefaultDatetime(chkDefaulDateTime.isSelected() ? listLabel : settings.getDefaultDatetime());
      settings.setProgramSettings("date", newDate);
      settings.setProgramSettings("time", txtTime.getText());
      settings.setProgramSettings("datetime", listLabel);

      settings.storeProgramSettings();
      settings.loadProgramSettings();
      initializeListView();
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void add() {
    try {
      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
      String dateStr = date.getValue().toString();
      Date date2 = originalFormat.parse(dateStr);

      SimpleDateFormat labelformat = new SimpleDateFormat("EEEE, dd MMM yyyy");
      String newDatTimeFormat = labelformat.format(date2);
      String listLabel = newDatTimeFormat + " " + txtTime.getText();

      SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
      String newDate = newformat.format(date2);
      settings.setCurrentDatetime(listLabel);
      settings.setProgramSettings("date", newDate);
      settings.setProgramSettings("time", txtTime.getText());
      settings.storeProgramSettings();
      settings.loadProgramSettings();
      initializeListView();
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @FXML
  private void apply(ActionEvent event) {
    try {
      settings.tempOject.put("is_temporary", true);
      applyCommon();
    } catch (JSONException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void applyCommon() {
    try {
      String currentDate = settings.getCurrentDatetime();
      
      SimpleDateFormat labelformat = new SimpleDateFormat("EEEE, dd MMM yyyy hh:mm");
      Date date = labelformat.parse(currentDate);
      SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
      String newDate = newformat.format(date);
      String time = settings.getCurrentDatetime().split(" ")[4];
//      settings.setProgramSettings("date", newDate);
//      settings.setProgramSettings("time", time);

      settings.tempOject.put("date", newDate);
      settings.tempOject.put("time", txtTime.getText());
      browserController.newHandleSetDateTime(getDateString(), txtTime.getText());
    } catch (JSONException ex) {
      Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @FXML
  private void cancel(ActionEvent event) {
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  @FXML
  private void delete(ActionEvent event) {
    deleteDateTime();
  }

  @FXML
  private void edit(ActionEvent event) {
    edit = true;
    initializeValues();
  }

  @FXML
  private void applySave(ActionEvent event) {
    try {
      settings.tempOject.put("is_temporary", false);
      String currentDate = settings.getCurrentDatetime();
      
      SimpleDateFormat labelformat = new SimpleDateFormat("EEEE, dd MMM yyyy hh:mm");
      Date date = labelformat.parse(currentDate);
      SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
      String newDate = newformat.format(date);
      String time = settings.getCurrentDatetime().split(" ")[4];
      browserController.newHandleSetDateTime(newDate, time);
    } catch (JSONException ex) {
      Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private String getDateString() {
    try {
      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
      String dateStr = date.getValue().toString();
      Date date2 = originalFormat.parse(dateStr);
      String newDate = newformat.format(date2);
      return newDate;
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  private void deleteDateTime() {

    boolean done = settings.deleteDateTime();
    if (done) {
      InformationDialog information = new InformationDialog(stage, "Succesfully deleted");
    }
    initializeListView();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  private void click(MouseEvent event) {
    if (event.getClickCount() == 2) {
      try {
        settings.tempOject.put("is_temporary", false);
        settings.storeProgramSettings();
        settings.loadProgramSettings();
        String newDate = getDateString();
        browserController.newHandleSetDateTime(newDate, txtTime.getText());
      } catch (JSONException ex) {
        Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      String newValue = lstLocation.getSelectionModel().getSelectedItem();
      newValue = newValue.replace("[DEFAULT] ", "");
      settings.setCurrentDatetime(newValue);
    }
  }
}
