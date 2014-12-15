/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import webviewbrowser.ConfirmationDialog;
import webviewbrowser.InformationDialog;
import webviewbrowser.Settings;
import webviewbrowser.common.Option;
import webviewbrowser.json.JSONException;
import webviewbrowser.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class LocationFXMLController implements Initializable {

  private static final String DEFAULT_ = "[DEFAULT] ";
  private static final String HOME_ = "[HOME] ";

  @FXML
  private ListView<String> lstLocation;
  @FXML
  private CheckBox chkApplyDayLightSaving;
  @FXML
  private TextField txtLatitudeD;
  @FXML
  private TextField txtLatitudeM;
  @FXML
  private TextField txtLatitudeS;
  @FXML
  private ComboBox<Option> cmbLatitudeDir;
  @FXML
  private TextField txtLongitudeD;
  @FXML
  private TextField txtLongitudeM;
  @FXML
  private TextField txtLongitudeS;
  @FXML
  private ComboBox<Option> cmbLongitudeDir;
  @FXML
  public ComboBox<Option> cmbTimeZone;
  @FXML
  private Button btnSave;
  @FXML
  private Button btnApply;
  private Settings settings;
  private BrowserFXMLController browserController;
  private List<JSONObject> list;
  @FXML
  private Button btnDelete;
  @FXML
  private Button btnClose;
  @FXML
  private Button btnEdit;
  @FXML
  private TextField txtLocation;
  private boolean edit = false;
  private Stage stage;
  @FXML
  private CheckBox chkDefaultLocation;
  @FXML
  private CheckBox chkHomeLocation;

  /**
   * InitializebtnDeletetr @FXML private Button btnSave1;
   *
   * @FXML private Button btnApply1;
   * @FXML private Button btnClose; oller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
  }

  @FXML
  private void openFindCoordinatesOnline(ActionEvent event) {
    settings.callDefaultBrowser("http://www.calculatorcat.com/latitude_longitude.phtml");
  }

  @FXML
  private void save(ActionEvent event) {
    boolean success = false;
    if (edit) {
      success = update();
      edit = false;
    } else {
      success = add();
    }
    if (success) {
      InformationDialog information = new InformationDialog(stage, "Succesfully saved");
      information.show();
    }
  }

  private boolean update() {
    if (validateValues()) {
      return false;
    }
    final String apply_dst = chkApplyDayLightSaving.isSelected() ? "yes" : "no";
    final String location = txtLocation.getText();
    final String tz = cmbTimeZone.getValue().getData();
    String Lt_dir = cmbLatitudeDir.getValue() == null ? "" : cmbLatitudeDir.getValue().getData();
    String Lg_dir = cmbLongitudeDir.getValue() == null ? "" : cmbLongitudeDir.getValue().getData();
    final String Lt_d = txtLatitudeD.getText();
    final String Lt_m = txtLatitudeM.getText();
    final String Lt_s = txtLatitudeS.getText();
    final String Lg_d = txtLongitudeD.getText();
    final String Lg_m = txtLongitudeM.getText();
    final String Lg_s = txtLongitudeS.getText();

    BigDecimal latitude_decimal = new BigDecimal(Lt_d);
    BigDecimal latitude_int = new BigDecimal(latitude_decimal.intValue());

    BigDecimal latitude_minute_decimal = latitude_decimal.subtract(latitude_int);
    BigDecimal latitude_minute_int_1 = latitude_minute_decimal.multiply(new BigDecimal(60));
    BigDecimal latitude_minute_int_2 = new BigDecimal(latitude_minute_int_1.intValue());

    BigDecimal latitude_second_decimal = latitude_minute_int_1.subtract(latitude_minute_int_2);
    BigDecimal latitude_second_int_1 = latitude_second_decimal.multiply(new BigDecimal(60));
    BigDecimal latitude_second_int_2 = new BigDecimal(latitude_second_int_1.intValue());

    BigDecimal longitude_decimal = new BigDecimal(Lg_d);
    BigDecimal longitude_int = new BigDecimal(longitude_decimal.intValue());

    BigDecimal longitude_minute_decimal = longitude_decimal.subtract(longitude_int);
    BigDecimal longitude_minute_int_1 = longitude_minute_decimal.multiply(new BigDecimal(60));
    BigDecimal longitude_minute_int_2 = new BigDecimal(longitude_minute_int_1.intValue());

    BigDecimal longitude_second_decimal = longitude_minute_int_1.subtract(longitude_minute_int_2);
    BigDecimal longitude_second_int_1 = longitude_second_decimal.multiply(new BigDecimal(60));
    BigDecimal longitude_second_int_2 = new BigDecimal(longitude_second_int_1.intValue());

    Lt_dir = Lt_dir.isEmpty() ? latitude_decimal.compareTo(BigDecimal.ZERO) > 0 ? "N" : "S" : Lt_dir;
    Lg_dir = Lg_dir.isEmpty() ? longitude_decimal.compareTo(BigDecimal.ZERO) > 0 ? "W" : "E" : Lg_dir;

    settings.setDefaultLocation(chkDefaultLocation.isSelected() ? txtLocation.getText() : settings.getDefaultLocation());
    settings.setProgramSettings("location", txtLocation.getText());
    settings.setProgramSettings("tz", tz);
    settings.setProgramSettings("apply_dst", apply_dst);
    settings.setProgramSettings("Lt_d", latitude_int.toString());
    settings.setProgramSettings("Lt_m", Lt_m.isEmpty() || Lt_d.contains(".") ? latitude_minute_int_2.toString() : Lt_m);
    settings.setProgramSettings("Lt_s", Lt_s.isEmpty() || Lt_d.contains(".") ? latitude_second_int_2.toString() : Lt_s);
    settings.setProgramSettings("Lt_dir", Lt_dir);
    settings.setProgramSettings("Lg_d", longitude_int.toString());
    settings.setProgramSettings("Lg_m", Lg_m.isEmpty() || Lg_d.contains(".") ? longitude_minute_int_2.toString() : Lg_m);
    settings.setProgramSettings("Lg_s", Lg_s.isEmpty() || Lg_d.contains(".") ? longitude_second_int_2.toString() : Lg_s);
    settings.setProgramSettings("Lg_dir", Lg_dir);
    settings.storeProgramSettings();
    settings.loadProgramSettings();
    initializeListView();
    clear();
    return true;
  }

  private boolean add() {
    if (validateValues()) {
      return false;
    }

    final String apply_dst = chkApplyDayLightSaving.isSelected() ? "yes" : "no";
    final String location = txtLocation.getText();
    final String tz = cmbTimeZone.getValue().getData();
    String Lt_dir = cmbLatitudeDir.getValue() == null ? "" : cmbLatitudeDir.getValue().getData();
    String Lg_dir = cmbLongitudeDir.getValue() == null ? "" : cmbLongitudeDir.getValue().getData();
    final String Lt_d = txtLatitudeD.getText();
    final String Lt_m = txtLatitudeM.getText();
    final String Lt_s = txtLatitudeS.getText();
    final String Lg_d = txtLongitudeD.getText();
    final String Lg_m = txtLongitudeM.getText();
    final String Lg_s = txtLongitudeS.getText();

    BigDecimal latitude_decimal = new BigDecimal(Lt_d);
    BigDecimal latitude_int = new BigDecimal(latitude_decimal.abs().intValue());

    BigDecimal latitude_minute_decimal = latitude_decimal.abs().subtract(latitude_int);
    BigDecimal latitude_minute_int_1 = latitude_minute_decimal.multiply(new BigDecimal(60));
    BigDecimal latitude_minute_int_2 = new BigDecimal(latitude_minute_int_1.intValue());

    BigDecimal latitude_second_decimal = latitude_minute_int_1.subtract(latitude_minute_int_2);
    BigDecimal latitude_second_int_1 = latitude_second_decimal.multiply(new BigDecimal(60));
    BigDecimal latitude_second_int_2 = new BigDecimal(latitude_second_int_1.intValue());

    BigDecimal longitude_decimal = new BigDecimal(Lg_d);
    BigDecimal longitude_int = new BigDecimal(longitude_decimal.abs().intValue());

    BigDecimal longitude_minute_decimal = longitude_decimal.abs().subtract(longitude_int);
    BigDecimal longitude_minute_int_1 = longitude_minute_decimal.multiply(new BigDecimal(60));
    BigDecimal longitude_minute_int_2 = new BigDecimal(longitude_minute_int_1.intValue());

    BigDecimal longitude_second_decimal = longitude_minute_int_1.subtract(longitude_minute_int_2);
    BigDecimal longitude_second_int_1 = longitude_second_decimal.multiply(new BigDecimal(60));
    BigDecimal longitude_second_int_2 = new BigDecimal(longitude_second_int_1.intValue());

    Lt_dir = Lt_dir.isEmpty() ? latitude_decimal.compareTo(BigDecimal.ZERO) > 0 ? "N" : "S" : Lt_dir;
    Lg_dir = Lg_dir.isEmpty() ? longitude_decimal.compareTo(BigDecimal.ZERO) > 0 ? "W" : "E" : Lg_dir;
    settings.setDefaultLocation(chkDefaultLocation.isSelected() ? txtLocation.getText() : settings.getDefaultLocation());
    settings.setCurrentLocation(txtLocation.getText());
    settings.setProgramSettings("location", txtLocation.getText());
    settings.setProgramSettings("tz", tz);
    settings.setProgramSettings("apply_dst", apply_dst);
    settings.setProgramSettings("Lt_d", latitude_int.toString());
    settings.setProgramSettings("Lt_m", Lt_m.isEmpty() || Lt_d.contains(".") ? latitude_minute_int_2.toString() : Lt_m);
    settings.setProgramSettings("Lt_s", Lt_s.isEmpty() || Lt_d.contains(".") ? latitude_second_int_2.toString() : Lt_s);
    settings.setProgramSettings("Lt_dir", Lt_dir);
    settings.setProgramSettings("Lg_d", longitude_int.toString());
    settings.setProgramSettings("Lg_m", Lg_m.isEmpty() || Lg_d.contains(".") ? longitude_minute_int_2.toString() : Lg_m);
    settings.setProgramSettings("Lg_s", Lg_s.isEmpty() || Lg_d.contains(".") ? longitude_second_int_2.toString() : Lg_s);
    settings.setProgramSettings("Lg_dir", Lg_dir);
    settings.storeProgramSettings();
    settings.loadProgramSettings();
    initializeListView();
    clear();
    return true;
  }

  private boolean validateValues() {
    if (cmbTimeZone.getValue() == null) {
      new InformationDialog(stage, "Please select timezone");
      return true;
    }
    if (!txtLatitudeD.getText().matches("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$")) {
      new InformationDialog(stage, "Invalid latitude values");
      return true;
    }
    if (!txtLongitudeD.getText().matches("^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")) {
      new InformationDialog(stage, "Invalid longitude values");
      return true;
    }
    return false;
  }

  public static String getLocation(JSONObject jSONObject) {
    String tz = null;
    String Lt_d = null;
    String Lt_m = null;
    String Lt_s = null;
    String Lt_dir = null;
    String Lg_d = null;
    String Lg_m = null;
    String Lg_s = null;
    String Lg_dir = null;
    String location = null;
    try {
      location = jSONObject.getString("location");
      tz = jSONObject.getString("tz");
      Lt_d = jSONObject.getString("Lt_d");
      Lt_m = jSONObject.getString("Lt_m");
      Lt_s = jSONObject.getString("Lt_s");
      Lt_dir = jSONObject.getString("Lt_dir");
      Lg_d = jSONObject.getString("Lg_d");
      Lg_m = jSONObject.getString("Lg_m");
      Lg_s = jSONObject.getString("Lg_s");
      Lg_dir = jSONObject.getString("Lg_dir");
    } catch (JSONException ex) {
      Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return getLocation(location, tz, Lt_d, Lt_m, Lt_s, Lt_dir, Lg_d, Lg_m, Lg_s, Lg_dir);
  }

  public static String getLocation(String location, String tz, String Lt_d, String Lt_m, String Lt_s, String Lt_dir, String Lg_d, String Lg_m, String Lg_s, String Lg_dir) {
    final String DEGREE = "\u00b0";
    if (!location.isEmpty()) {
      location += ", ";
    }
    location += Lt_d + DEGREE + " " + Lt_m + "\" " + Lt_s + "' " + Lt_dir;
    location += ", " + Lg_d + DEGREE + " " + Lg_m + "\" " + Lg_s + "' " + Lg_dir;
    location += ", GMT " + tz;
    return location;
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
    initializeCombo();
    initializeListView();
  }

  public void setBrowserController(BrowserFXMLController browserController) {
    this.browserController = browserController;
  }

  private void initializeCombo() {
    cmbLongitudeDir.getItems().add(new Option("", ""));
    cmbLongitudeDir.getItems().add(new Option("W", "W"));
    cmbLongitudeDir.getItems().add(new Option("E", "E"));

    cmbLatitudeDir.getItems().add(new Option("", ""));
    cmbLatitudeDir.getItems().add(new Option("N", "N"));
    cmbLatitudeDir.getItems().add(new Option("S", "S"));
    String[] aTZh = {"", "-12", "-11", "-10", "-9.5", "-9", "-8.5", "-8", "-7", "-6", "-5", "-4", "-3.5", "-3", "-2", "-1", "0", "1", "2", "3", "3.5", "4", "4.5", "5", "5.5", "6", "6.5", "7", "8", "9", "9.5", "10", "10.5", "11", "11.5", "12", "13"};
    String[] aTZd = {"", "-12:00", "-11:00", "-10:00", "-9:30", "-9:00", "-8:30", "-8:00 (PST)", "-7:00 (MST)", "-6:00 (CST)", "-5:00 (EST)", "-4:00 (AST)", "-3:30", "-3:00", "-2:00", "-1:00", "GMT 0:00", "+1:00", "+2:00", "+3:00", "+3:30", "+4:00", "+4:30", "+5:00", "+5:30", "+6:00", "+6:30", "+7:00", "+8:00", "+9:00", "+9:30", "+10:00", "+10:30", "+11:00", "+11:30", "+12:00", "+13:00"};
    for (int i = 0; i < aTZd.length; i++) {
      addOption(aTZh[i], aTZd[i]);
    }
  }

  public void addOption(String tzd, String tzh) {
    cmbTimeZone.getItems().add(new Option(tzd, tzh));
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

    setComboBox(settings.getProgrammSettings("tz"), cmbTimeZone);
    setComboBox(settings.getProgrammSettings("Lt_dir"), cmbLatitudeDir);
    setComboBox(settings.getProgrammSettings("Lg_dir"), cmbLongitudeDir);

    setCheckBox(settings.getProgrammSettings("apply_dst"), chkApplyDayLightSaving);
    setCheckBox(settings.getDefaultLocation().equalsIgnoreCase(settings.getProgrammSettings("location")) ? "yes" : "no", chkDefaultLocation);

    setTextField(settings.getProgrammSettings("Lt_d"), txtLatitudeD);
    setTextField(settings.getProgrammSettings("Lt_m"), txtLatitudeM);
    setTextField(settings.getProgrammSettings("Lt_s"), txtLatitudeS);

    setTextField(settings.getProgrammSettings("Lg_d"), txtLongitudeD);
    setTextField(settings.getProgrammSettings("Lg_m"), txtLongitudeM);
    setTextField(settings.getProgrammSettings("Lg_s"), txtLongitudeS);
    setTextField(settings.getProgrammSettings("location"), txtLocation);

  }

  private void initializeListView() {
    list = settings.getLocationSettings();
//    lstLocation.getSelectionModel().selectedItemProperty()
//            .addListener(new ChangeListener<String>() {
//              @Override
//              public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                String[] location = newValue.split(",");
//                settings.setCurrentLocation(location[0]);
//              }
//            });
    btnEdit.disableProperty().bind(lstLocation.getSelectionModel().selectedItemProperty().isNull());
    btnDelete.disableProperty().bind(lstLocation.getSelectionModel().selectedItemProperty().isNull());
    btnApply.disableProperty().bind(lstLocation.getSelectionModel().selectedItemProperty().isNull());
    lstLocation.getItems().clear();
    for (JSONObject jSONObject : list) {
      try {
        String location = getLocation(jSONObject);
        if (jSONObject.getString("location").equalsIgnoreCase(settings.getDefaultLocation())) {
          location = DEFAULT_ + location;
          lstLocation.getItems().add(location);
          lstLocation.getSelectionModel().select(location);
        } else {
          lstLocation.getItems().add(location);
        }
      } catch (JSONException ex) {
        Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @FXML
  private void delete(ActionEvent event) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        deleteLocation();
      }
    };
    ConfirmationDialog confirmationDialog = new ConfirmationDialog(stage, "Are you sure you want to delete this location?", runnable);
  }

  @FXML
  private void edit(ActionEvent event) {
    edit = true;
    initializeValues();
  }

  private void deleteLocation() {

    boolean done = settings.deleteLocation();
    if (done) {
      InformationDialog information = new InformationDialog(stage, "Succesfully deleted");
    }
    initializeListView();
  }

  public void applyCommon() {
    try {
      settings.tempOject.put("tz", cmbTimeZone.getValue().getData());
      settings.tempOject.put("apply_dst", chkApplyDayLightSaving.isSelected() ? "yes" : "no");
      settings.tempOject.put("Lt_d", txtLatitudeD.getText());
      settings.tempOject.put("Lt_m", txtLatitudeM.getText());
      settings.tempOject.put("Lt_s", txtLatitudeS.getText());
      settings.tempOject.put("Lt_dir", cmbLatitudeDir.getValue().getData());
      settings.tempOject.put("Lg_d", txtLongitudeD.getText());
      settings.tempOject.put("Lg_m", txtLongitudeM.getText());
      settings.tempOject.put("Lg_s", txtLongitudeS.getText());
      settings.tempOject.put("Lg_dir", cmbLongitudeDir.getValue().getData());
      settings.tempOject.put("location", txtLocation.getText());
      browserController.newSetLoc();
    } catch (JSONException ex) {
      Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @FXML
  private void click(MouseEvent event) {
    if (event.getClickCount() == 2) {
//      initializeValues();
      try {
        settings.tempOject.put("is_temporary", false);
        settings.storeProgramSettings();
        settings.loadProgramSettings();
        browserController.newSetLoc();
      } catch (JSONException ex) {
        Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      String newValue = lstLocation.getSelectionModel().getSelectedItem();
      newValue = newValue.replace(DEFAULT_, "");
      String[] location = newValue.split(",");
      settings.setCurrentLocation(location.length >= 4 ? location[0] : "");
    }
  }

  private void apply(ActionEvent event) {
    try {
      settings.tempOject.put("is_temporary", true);
      applyCommon();
    } catch (JSONException ex) {
      Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @FXML
  private void cancel(ActionEvent event) {
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  @FXML
  private void applySave(ActionEvent event) {
    try {
      settings.tempOject.put("is_temporary", false);
      settings.storeProgramSettings();
      settings.loadProgramSettings();
      browserController.newSetLoc();
    } catch (JSONException ex) {
      Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  private void clear() {
    setComboBox("", cmbTimeZone);
    setComboBox("", cmbLatitudeDir);
    setComboBox("", cmbLongitudeDir);

    setCheckBox("", chkApplyDayLightSaving);
    setCheckBox("no", chkDefaultLocation);

    setTextField("", txtLatitudeD);
    setTextField("", txtLatitudeM);
    setTextField("", txtLatitudeS);

    setTextField("", txtLongitudeD);
    setTextField("", txtLongitudeM);
    setTextField("", txtLongitudeS);
    setTextField("", txtLocation);
  }
}
