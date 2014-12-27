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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import webviewbrowser.ConfirmationDialog;
import webviewbrowser.InformationDialog;
import webviewbrowser.Settings;
import webviewbrowser.common.Option;

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
  private CheckBox chkPhaseName;
  @FXML
  private CheckBox chkPercentofFull;
  @FXML
  private CheckBox chkAzimuth;
  @FXML
  private CheckBox chkAltitude;
  @FXML
  private CheckBox chkAgePercent;
  @FXML
  private CheckBox chkAgeDays;
  @FXML
  private CheckBox chkRiseTime;
  @FXML
  private CheckBox chkSetTime;
  @FXML
  private CheckBox chkSetLocation;
  @FXML
  private CheckBox chkMoonSign;
  @FXML
  private Button btnCancel;
  @FXML
  private Button btnGenerate;
  @FXML
  private RadioButton rbtnDaily;
  @FXML
  private RadioButton rbtnHourly;
  @FXML
  private ComboBox<Option> cmbTimeOfDay;
  @FXML
  private DatePicker startDate;
  @FXML
  private Button btnToday;
  @FXML
  private TextField txtNumberDays;
  @FXML
  private ComboBox<Option> cmbDateFormat;
  @FXML
  private ComboBox<Option> cmbTimeFormat;
  @FXML
  private CheckBox chkWeekday;
  @FXML
  private CheckBox chkGPS;
  @FXML
  private CheckBox chkDaylight;
  @FXML
  private CheckBox chkRiseLocation;
  private Stage stage;

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
    initializeComponents();
    initializeValues();

  }

  @FXML
  private void apply(ActionEvent event) {
    String columns = chkDate.isSelected() ? ",Date" : "";
    columns += chkTime.isSelected() ? ",Time" : "";
    columns += chkWeekday.isSelected() ? ",Weekdays" : "";
    columns += chkDaylight.isSelected() ? ",Daylight Saving Time" : "";
    columns += chkTimezone.isSelected() ? ",Timezone" : "";
    columns += chkGPS.isSelected() ? ",Latitude/Longitude" : "";
    columns += chkPhaseName.isSelected() ? ",Phase Name" : "";
    columns += chkPercentofFull.isSelected() ? ",Percent of Full" : "";
    columns += chkAgePercent.isSelected() ? ",Age (Percent)" : "";
    columns += chkAgeDays.isSelected() ? ",Age (Days)" : "";
    columns += chkAzimuth.isSelected() ? ",Azimuth" : "";
    columns += chkAltitude.isSelected() ? ",Altitude" : "";
    columns += chkRiseTime.isSelected() && !chkRiseTime.isDisabled() ? ",Rise Time" : "";
    columns += chkSetTime.isSelected() && !chkSetTime.isDisabled() ? ",Set Time" : "";
    columns += chkSetLocation.isSelected() && !chkSetLocation.isDisabled() ? ",Set Location" : "";
    columns += chkRiseLocation.isSelected() && !chkRiseLocation.isDisabled() ? ",Rise Location" : "";
    columns += chkMoonSign.isSelected() ? ",Moon Sign" : "";

    String dateFormat = cmbDateFormat.getValue() == null ? settings.getProgrammSettings("dt_format").isEmpty() ? "mm/dd/yyyy" : settings.getProgrammSettings("dt_format") : cmbDateFormat.getValue().getData();
    String timeFormat = cmbTimeFormat.getValue() == null ? settings.getProgrammSettings("clock").isEmpty() ? "12hr" : settings.getProgrammSettings("clock") : cmbTimeFormat.getValue().getData();
    String timOfDay = cmbTimeOfDay.getValue() == null ? "0" : cmbTimeOfDay.getValue().getData();
    String interval = rbtnHourly.isSelected() ? "hourly" : "daily";
    String date = getDate();
    if (date.isEmpty()) {
      new InformationDialog(stage, "Please select valid Date");
      return;
    }
    String numberOfResults = txtNumberDays.getText().isEmpty() ? "10" : txtNumberDays.getText();
    Integer number = Integer.parseInt(numberOfResults);
    if (number > 4000) {
      new InformationDialog(stage, "The maximumber number of results is only 4000");
      return;
    }
    if (columns.isEmpty()) {
      new InformationDialog(stage, "Please select at least one column");
      return;
    }
    if (settings.getProgrammSettings("default_location").isEmpty()) {
      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          settings.showConfigDialog("");
        }
      };
      ConfirmationDialog confirmationDialog = new ConfirmationDialog(stage, "You have no default location set. Would you like to create a new location?", runnable);
    } else {
      browserController.setInterval(columns, dateFormat, timeFormat, timOfDay, date, numberOfResults, interval);
    }

    settings.setProgramSettings("hayStack", columns);
    settings.setProgramSettings("start_date", getDate());
    settings.setProgramSettings("column_date_format", dateFormat);
    settings.setProgramSettings("column_time_format", timeFormat);
    settings.setProgramSettings("column_time_day", timOfDay);
    settings.storeProgramSettings();
    settings.loadProgramSettings();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public Date getFirstDate() {

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1582);
    calendar.set(Calendar.MONTH, 9);
    calendar.set(Calendar.DATE, 12);
    return calendar.getTime();
  }

  public String getDate() {
    try {
      Calendar calendar = Calendar.getInstance();
      int year = calendar.get(Calendar.YEAR);
      int month = calendar.get(Calendar.MONTH);
      int day = calendar.get(Calendar.DATE);
      LocalDate lcDate = LocalDate.of(year, month + 1, day);

      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
      String dateStr = startDate.getValue() == null ? lcDate.toString() : startDate.getValue().toString();
      Date date2 = originalFormat.parse(dateStr);
      String newDate = newformat.format(date2);
      Date date = getFirstDate();
      if (date2.before(date)) {
        return "";
      }
      return newDate;
    } catch (ParseException ex) {
      Logger.getLogger(ColumnChooserFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return "";
  }

  private void initializeValues() {
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkDate), chkDate);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkTime), chkTime);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkWeekday), chkWeekday);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkDaylight), chkDaylight);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkTimezone), chkTimezone);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkGPS), chkGPS);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkPhaseName), chkPhaseName);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkPercentofFull), chkPercentofFull);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkAgePercent), chkAgePercent);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkAgeDays), chkAgeDays);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkAzimuth), chkAzimuth);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkAltitude), chkAltitude);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkRiseTime), chkRiseTime);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkRiseLocation), chkRiseLocation);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkSetTime), chkSetTime);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkSetLocation), chkSetLocation);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkSetLocation), chkSetLocation);
    setCheckBox(showColumn(settings.getProgrammSettings("hayStack"), chkMoonSign), chkMoonSign);

    setDate(settings.getProgrammSettings("start_date"), startDate);
    setComboBox(settings.getProgrammSettings("column_time_format"), cmbTimeFormat);
    setComboBox(settings.getProgrammSettings("column_date_format"), cmbDateFormat);
    setComboBox(settings.getProgrammSettings("column_time_day"), cmbTimeOfDay);
  }

  public void setComboBox(String data, ComboBox comboBox) {
    ObservableList<Option> list1 = comboBox.getItems();
    for (Option option : list1) {
      if (option.getData().equals(data)) {
        comboBox.setValue(option);
      }
    }
  }

  private void setDate(String programmSettings, DatePicker datePicker) {
    datePicker.setValue(settings.getDate(programmSettings));
  }

  private boolean showColumn(String hayStack, CheckBox chkbox) {
    return showColumn(hayStack, chkbox.getText());
  }

  private boolean showColumn(String hayStack, String value) {
    if (hayStack.equalsIgnoreCase("All")) {
      return true;
    }
    if (hayStack.indexOf(value) == -1) {
      return false;
    }
    return true;
  }

  private void setCheckBox(boolean programmSettings, CheckBox chkbox) {
    chkbox.setSelected(programmSettings);
  }

  @FXML
  private void cancel(ActionEvent event) {
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  private void initializeComponents() {
    cmbTimeOfDay.getItems().add(new Option("-1", "Use Target Time"));
    cmbTimeOfDay.getItems().add(new Option("0", "12 AM - Midnight (0:00)"));
    cmbTimeOfDay.getItems().add(new Option("1", "1 AM (1:00)"));
    cmbTimeOfDay.getItems().add(new Option("2", "2 AM (2:00)"));
    cmbTimeOfDay.getItems().add(new Option("3", "3 AM (3:00)"));
    cmbTimeOfDay.getItems().add(new Option("4", "4 AM (4:00)"));
    cmbTimeOfDay.getItems().add(new Option("5", "5 AM (5:00)"));
    cmbTimeOfDay.getItems().add(new Option("6", "6 AM (6:00)"));
    cmbTimeOfDay.getItems().add(new Option("7", "7 AM (7:00)"));
    cmbTimeOfDay.getItems().add(new Option("8", "8 AM (8:00)"));
    cmbTimeOfDay.getItems().add(new Option("9", "9 AM (9:00)"));
    cmbTimeOfDay.getItems().add(new Option("10", "10 AM (10:00)"));
    cmbTimeOfDay.getItems().add(new Option("11", "11 AM (11:00)"));
    cmbTimeOfDay.getItems().add(new Option("12", "12 PM - Noon (12:00)"));
    cmbTimeOfDay.getItems().add(new Option("13", "1 PM (13:00)"));
    cmbTimeOfDay.getItems().add(new Option("14", "2 PM (14:00)"));
    cmbTimeOfDay.getItems().add(new Option("15", "3 PM (15:00)"));
    cmbTimeOfDay.getItems().add(new Option("16", "4 PM (16:00)"));
    cmbTimeOfDay.getItems().add(new Option("17", "5 PM (17:00)"));
    cmbTimeOfDay.getItems().add(new Option("18", "6 PM (18:00)"));
    cmbTimeOfDay.getItems().add(new Option("19", "7 PM (19:00)"));
    cmbTimeOfDay.getItems().add(new Option("20", "8 PM (20:00)"));
    cmbTimeOfDay.getItems().add(new Option("21", "9 PM (21:00)"));
    cmbTimeOfDay.getItems().add(new Option("22", "10 PM (22:00)"));
    cmbTimeOfDay.getItems().add(new Option("23", "11 PM (23:00)"));

    cmbTimeFormat.getItems().add(new Option("12hr", "12-hour clock (AM/PM)"));
    cmbTimeFormat.getItems().add(new Option("24hr", "24-hour clock"));

    cmbDateFormat.getItems().add(new Option("mm/dd/yyyy", "mm/dd/yyyy"));
    cmbDateFormat.getItems().add(new Option("dd/mm/yyyy", "dd/mm/yyyy"));
    ToggleGroup group = new ToggleGroup();
    rbtnDaily.setToggleGroup(group);
    rbtnHourly.setToggleGroup(group);
    chkRiseLocation.disableProperty().bind(rbtnHourly.selectedProperty());
    chkRiseTime.disableProperty().bind(rbtnHourly.selectedProperty());
    chkSetLocation.disableProperty().bind(rbtnHourly.selectedProperty());
    chkSetTime.disableProperty().bind(rbtnHourly.selectedProperty());
    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
        if (group.getSelectedToggle().equals(rbtnDaily)) {
          Calendar calendar = Calendar.getInstance();
          String day = calendar.get(Calendar.DATE) + "";
          ObservableList<Option> list1 = cmbTimeOfDay.getItems();
          for (Option option : list1) {
            if (option.getData().equals(day)) {
              cmbTimeOfDay.setValue(option);
            }
          }
        }
      }
    });
    rbtnDaily.setSelected(true);
  }

  @FXML
  private void getNow(ActionEvent event) {
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DATE);
    LocalDate lcDate = LocalDate.of(year, month + 1, day);
    startDate.setValue(lcDate);
  }

}
