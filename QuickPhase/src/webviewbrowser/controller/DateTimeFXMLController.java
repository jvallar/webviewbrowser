/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import java.net.URL;
import java.text.DateFormat;
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
import webviewbrowser.ConfirmationDialog;
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

  private static final String DEFAULT_ = "[DEFAULT] ";
  private static final String CURRENT_ = "[CURRENT] ";
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
    boolean found = false;
    for (JSONObject jSONObject : list) {
      try {
        String datetime = jSONObject.getString("datetime");
        if (datetime.equalsIgnoreCase(settings.getDefaultDatetime())) {
          datetime = DEFAULT_ + datetime;
          lstLocation.getItems().add(datetime);
          lstLocation.getSelectionModel().select(datetime);
          found = true;
        } else {
          lstLocation.getItems().add(datetime);
        }
      } catch (JSONException ex) {
        Logger.getLogger(BrowserFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    String currentdatetime = getCurrentDateTime();
    if (!found) {
      currentdatetime = DEFAULT_ + currentdatetime;
      lstLocation.getItems().add(currentdatetime);
      lstLocation.getSelectionModel().select(currentdatetime);
    } else {
      lstLocation.getItems().add(currentdatetime);
    }
  }

  private String getCurrentDateTime() {
    String listLabel = "";
    try {
      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
      String dateStr = settings.getDate("").toString();
      Date date2 = originalFormat.parse(dateStr);

      SimpleDateFormat labelformat = new SimpleDateFormat("EEEE, dd MMM yyyy");
      String newDatTimeFormat = labelformat.format(date2);
      listLabel = CURRENT_ + newDatTimeFormat + " " + settings.getTime("");
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return listLabel;
  }

  private void initializeValues() {
    setTime(settings.getProgrammSettings("time"), txtTime);
    setDate(settings.getProgrammSettings("date"), date);
    setCheckBox(settings.getDefaultDatetime().equalsIgnoreCase(settings.getProgrammSettings("datetime")) ? "yes" : "no", chkDefaulDateTime);
  }

  private void setCheckBox(String programmSettings, CheckBox chkDefaulDateTime) {
    chkDefaulDateTime.setSelected(programmSettings.equalsIgnoreCase("yes"));
  }

  private void setDate(String programmSettings, DatePicker datePicker) {
    datePicker.setValue(settings.getDate(programmSettings));
  }

  private void setTime(String programmSettings, TextField txtTime) {
    txtTime.setText(formatTime(settings.getTime(programmSettings)));

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

      String newDate = newformat.format(date2);
      String newValue = lstLocation.getSelectionModel().getSelectedItem();
      if (newValue.contains(CURRENT_)) {
        settings.setDefaultDatetime(CURRENT_);
      } else {
        settings.setDefaultDatetime(chkDefaulDateTime.isSelected() ? listLabel : settings.getDefaultDatetime());
        settings.setCurrentDatetime(listLabel);
        settings.setProgramSettings("datetime", listLabel);
        settings.setProgramSettings("date", newDate);
        settings.setProgramSettings("time", formatTime(txtTime.getText()));
      }

      settings.storeProgramSettings();
      settings.loadProgramSettings();
      initializeListView();
      clear();
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private String formatTime(String time) {
    try {
      String clock = settings.getProgrammSettings("clock");
      String saveClock = "24hr";
      DateFormat twentyfourformat = new SimpleDateFormat("HH:mm");
      DateFormat HOURformat = new SimpleDateFormat("HH");
      DateFormat hourformat = new SimpleDateFormat("hha");
      DateFormat twelveFormat = new SimpleDateFormat("hh:mm a");
      if ((time.contains("AM") || time.contains("PM")) && time.length() >= 5) {
        saveClock = "12hr";
      } else if (time.length() <= 2) {
        saveClock = "HH";
      } else if ((time.contains("AM") || time.contains("PM") || time.contains("am") || time.contains("pm")) && time.length() <= 4 && !time.contains(":")) {
        saveClock = "hha";
      }
      
      if (saveClock.equalsIgnoreCase("HH") && clock.equalsIgnoreCase("24hr")) {
        Date date = HOURformat.parse(time);
        return twentyfourformat.format(date);
      } else if (saveClock.equalsIgnoreCase("HH") && clock.equalsIgnoreCase("12hr")) {
        Date date = HOURformat.parse(time);
        return twelveFormat.format(date);
      } else if (saveClock.equalsIgnoreCase("hha") && clock.equalsIgnoreCase("24hr")) {
        Date date = hourformat.parse(time);
        return twentyfourformat.format(date);
      } else if (saveClock.equalsIgnoreCase("hha") && clock.equalsIgnoreCase("12hr")) {
        Date date = hourformat.parse(time);
        return twelveFormat.format(date);
      } else if (clock.equalsIgnoreCase("24hr") && !saveClock.equalsIgnoreCase(clock)) {
        Date date = twelveFormat.parse(time);
        return twentyfourformat.format(date);
      } else if (clock.equalsIgnoreCase("12hr") && !saveClock.equalsIgnoreCase(clock)) {
        Date date = twentyfourformat.parse(time);
        return twelveFormat.format(date);
      }
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return time;
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

      settings.setDefaultDatetime(chkDefaulDateTime.isSelected() ? listLabel : settings.getDefaultDatetime());
      settings.setCurrentDatetime(listLabel);
      settings.setProgramSettings("datetime", listLabel);
      settings.setProgramSettings("date", newDate);
      settings.setProgramSettings("time", formatTime(txtTime.getText()));

      settings.storeProgramSettings();
      settings.loadProgramSettings();
      initializeListView();
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void clear() {
    date.setValue(null);
    txtTime.setText("");
    chkDefaulDateTime.setSelected(false);
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
      String currentDate = getDateString();
      if(currentDate.isEmpty()){      
        new InformationDialog(stage, "Please select valid Date");
        return;
      }
//      
//      SimpleDateFormat labelformat = new SimpleDateFormat("EEEE, dd MMM yyyy hh:mm");
//      Date date = labelformat.parse(currentDate);
//      SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
//      String newDate = newformat.format(date);
//      String time = settings.getCurrentDatetime().split(" ")[4];
//      settings.setProgramSettings("date", newDate);
//      settings.setProgramSettings("time", time);

      settings.tempOject.put("date", currentDate);
      settings.tempOject.put("time", txtTime.getText());
      browserController.newHandleSetDateTime(getDateString(), txtTime.getText());
    } catch (JSONException ex) {
      Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @FXML
  private void cancel(ActionEvent event) {
    browserController.continueRefresh();
    ((Node) (event.getSource())).getScene().getWindow().hide();
  } 

  @FXML
  private void delete(ActionEvent event) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        deleteDateTime();
      }
    };
    ConfirmationDialog confirmationDialog = new ConfirmationDialog(stage, "Are you sure you want to delete this date and time?", runnable);

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
      final String[] dateValues = settings.getCurrentDatetime().split(" ");
      String time = dateValues[4];
      browserController.newHandleSetDateTime(newDate, time);

      String newValue = lstLocation.getSelectionModel().getSelectedItem();
      if (newValue.contains(CURRENT_)) {
        settings.setDefaultDatetime(CURRENT_);
      }
      settings.storeProgramSettings();
      settings.loadProgramSettings();
    } catch (JSONException ex) {
      Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ParseException ex) {
      Logger.getLogger(DateTimeFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public Date getFirstDate(){
    
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, 1582);
      calendar.set(Calendar.MONTH,9);
      calendar.set(Calendar.DATE,12);
      return calendar.getTime();
  }
  
  private String getDateString() {
    try {
      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
      String dateStr = date.getValue().toString();
      Date date2 = originalFormat.parse(dateStr);
      String newDate = newformat.format(date2);
      Date date = getFirstDate();
      if(date2.before(date)){
        return "";
      }
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
        
      if(newDate.isEmpty()){      
        new InformationDialog(stage, "Please select valid Date");
        return;
      }
        browserController.newHandleSetDateTime(newDate, txtTime.getText());
      } catch (JSONException ex) {
        Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      String newValue = lstLocation.getSelectionModel().getSelectedItem();
      newValue = newValue.replace(DEFAULT_, "");
      newValue = newValue.replace(CURRENT_, "");
      settings.setCurrentDatetime(newValue);
    }
  }
}
