package webviewbrowser;

/**
 *
 * @author Jameson
 */
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import webviewbrowser.controller.ActivationFXMLController;
import webviewbrowser.controller.BrowserFXMLController;
import webviewbrowser.controller.ColumnChooserFXMLController;
import webviewbrowser.controller.DateFormatFXMLController;
import webviewbrowser.controller.DateTimeFXMLController;
import webviewbrowser.controller.FindDataFXMLController;
import webviewbrowser.controller.LocationFXMLController;
import static webviewbrowser.controller.LocationFXMLController.getLocation;
import webviewbrowser.controller.PrintBrowserFXMLController;
import webviewbrowser.json.JSONArray;
import webviewbrowser.json.JSONException;
import webviewbrowser.json.JSONObject;

/**
 *
 * @author Jameson
 */
public class Settings {

  Label labelFromJavascript;
  public JSONObject obj;
  public JSONObject tempOject = new JSONObject();
  BrowserFXMLController browser;
  private String json;
  private ActivationFXMLController activationFXMLController;
  private DateTimeFXMLController dateTimeFXMLController;
  private LocationFXMLController locationFXMLController;

  String currentLocation = "";
  String currentDatetime = "";
  String defaultLocation = "";
  String defaultDatetime = "";
  String homeLocation = "";
  List<JSONObject> list_location;
  List<JSONObject> list_datetime;
  private DateFormatFXMLController dateFormatFXMLController;
  private PrintBrowserFXMLController printBrowserFXMLController;
  private ColumnChooserFXMLController columnChooserController;
  private FindDataFXMLController findDataFXMLController;

  public Settings(BrowserFXMLController browser) {
    obj = new JSONObject();
    list_location = new ArrayList();
    list_datetime = new ArrayList();
    this.browser = browser;
    loadProgramSettings();
  }

  public void callFromJavascript(String msg) {
    labelFromJavascript.setText("Click from Javascript: " + msg);
  }

  public String getProgrammSettings(String settings) {
    boolean isTemporary = false;
    try {
      isTemporary = tempOject.getBoolean("is_temporary");
      System.err.println("isTemporary = " + isTemporary);
      if (isTemporary) {
        try {
          return tempOject.getString(settings);
        } catch (JSONException ex) {
        }
      }
    } catch (JSONException ex) {

    }

    if (json == null) {
      return "";
    }
    try {
      obj = new JSONObject(json);

      String programSettingsType = getProgramSettingType(settings);
      if (programSettingsType.equalsIgnoreCase("location")) {
        JSONArray arrayLocation = obj.getJSONArray("list_location");

        for (int i = 0; i < arrayLocation.length(); i++) {
          JSONObject object = arrayLocation.getJSONObject(i);
          if (object.getString("location").equalsIgnoreCase(currentLocation)) {
            return object.getString(settings);
          }
        }
        for (int i = 0; i < arrayLocation.length(); i++) {
          JSONObject object = arrayLocation.getJSONObject(i);
          if (object.getString("location").equalsIgnoreCase(homeLocation)) {
            return object.getString(settings);
          }
        }
      } else if (programSettingsType.equalsIgnoreCase("datetime")) {
        JSONArray arrayDateTime = obj.getJSONArray("list_datetime");
        for (int i = 0; i < arrayDateTime.length(); i++) {
          JSONObject object = arrayDateTime.getJSONObject(i);
          if (object.getString("datetime").equalsIgnoreCase(currentDatetime)) {
            return object.getString(settings);
          }
        }
      } else {
        return obj.getString(settings);
      }
    } catch (JSONException ex) {
      System.err.println(ex.getMessage());
      return "";
    }
    return "";
  }

  public void setProgramSettings(String settings, String value) {
    try {
      String programSettingsType = getProgramSettingType(settings);
      if (programSettingsType.equalsIgnoreCase("location")) {
        boolean foundLocation = false;
        for (JSONObject jSONObject : list_location) {
          if (jSONObject.getString("location").equalsIgnoreCase(currentLocation)) {
            jSONObject.put(settings, value);
            foundLocation = true;
          }
        }
        if (!foundLocation) {
          JSONObject jsonObject1 = new JSONObject();
          jsonObject1.put("location", currentLocation);
          jsonObject1.put(settings, value);
          list_location.add(jsonObject1);
        }
      } else if (programSettingsType.equalsIgnoreCase("datetime")) {
        boolean foundDateTime = false;
        for (JSONObject jSONObject : list_datetime) {
          if (jSONObject.getString("datetime").equalsIgnoreCase(currentDatetime)) {
            jSONObject.put(settings, value);
            foundDateTime = true;
          }
        }
        if (!foundDateTime) {
          JSONObject jsonObject1 = new JSONObject();
          jsonObject1.put("datetime", currentDatetime);
          jsonObject1.put(settings, value);
          list_datetime.add(jsonObject1);
        }
      } else {
        obj.put(settings, value);
      }
    } catch (JSONException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void loadProgramSettings() {
    loadProgramSettings(currentLocation, currentDatetime);
  }

  public void loadProgramSettings(String location, String datetime) {

    FileReader reader = null;
    try {
      File file = new File("settings.nosemaj");
      reader = new FileReader(file);
      char[] a = new char[2048];
      reader.read(a); // reads the content to the array
      json = "";
      for (char c : a) {
        json += c;
      }
      reader.close();

      obj = new JSONObject(json);

      String saveDatetime = obj.getString("default_datetime");

      currentDatetime = datetime.isEmpty() ? saveDatetime : datetime;
      defaultDatetime = saveDatetime;

      JSONArray arrayDatetime = obj.getJSONArray("list_datetime");
      list_datetime.clear();
      for (int i = 0; i < arrayDatetime.length(); i++) {
        JSONObject object = arrayDatetime.getJSONObject(i);
        list_datetime.add(object);
      }

      JSONArray arrayLocation = obj.getJSONArray("list_location");
      list_location.clear();

      String saveLocation = obj.getString("default_location");
      homeLocation = obj.getString("home_location");
      currentLocation = location.isEmpty() ? saveLocation.isEmpty() ? homeLocation : saveLocation : location;
      defaultLocation = saveLocation;
      for (int i = 0; i < arrayLocation.length(); i++) {
        JSONObject object = arrayLocation.getJSONObject(i);
        list_location.add(object);
      }

    } catch (FileNotFoundException ex) {
//      Logger.getLogger(ProgramSettings.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException | JSONException ex) {
//      Logger.getLogger(ProgramSettings.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void storeProgramSettings() {
    try {
      obj.put("list_location", list_location);
      obj.put("default_location", defaultLocation);
      obj.put("default_datetime", defaultDatetime);
      obj.put("home_location", homeLocation);
      obj.put("list_datetime", list_datetime);
      File file = new File("settings.nosemaj");
      file.createNewFile();
      FileWriter writer = new FileWriter(file);
      writer.write(obj.toString());
      writer.flush();
      writer.close();
      loadProgramSettings();
    } catch (IOException ex) {
    } catch (JSONException ex) {
    }
  }

  public List<JSONObject> getLocationSettings() {
    return list_location;
  }

  public List<JSONObject> getDateTimeSettings() {
    return list_datetime;
  }

  public void writeMessage(String s) {
    System.err.println(" message= " + s);
  }

  public static void storeActivationCode(JSONObject json) {
    try {
      File file = new File("activation.nosemaj");
      file.createNewFile();
      FileWriter writer = new FileWriter(file);
      writer.write(json.toString());
      writer.flush();
      writer.close();
    } catch (IOException ex) {

    }
  }

  public boolean isActivated() {
    FileReader reader = null;
    try {
      File file = new File("activation.nosemaj");
      reader = new FileReader(file);
      char[] a = new char[1024];
      reader.read(a); // reads the content to the array
      String json = "";
      for (char c : a) {
        json += c;
      }
      reader.close();
      JSONObject readerjson = new JSONObject(json);
      return readerjson.getInt("success") == 1;
    } catch (FileNotFoundException ex) {
//      Logger.getLogger(ProgramSettings.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException | JSONException ex) {
//      Logger.getLogger(ProgramSettings.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  public int isTrial() {
    FileReader reader = null;
    try {
      File file = new File("activation.nosemaj");
      reader = new FileReader(file);
      char[] a = new char[1024];
      reader.read(a); // reads the content to the array
      String json = "";
      for (char c : a) {
        json += c;
      }
      reader.close();
      JSONObject readerjson = new JSONObject(json);
      return readerjson.getInt("date");
    } catch (FileNotFoundException ex) {
//      Logger.getLogger(ProgramSettings.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException | JSONException ex) {
//      Logger.getLogger(ProgramSettings.class.getName()).log(Level.SEVERE, null, ex);
    }
    return 0;
  }

  public void showActivationDialog() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      Parent myActivation = fxmlLoader.load(getClass().getResource("controller/fxml/ActivationFXML.fxml").openStream());
      activationFXMLController = (ActivationFXMLController) fxmlLoader.getController();
      activationFXMLController.setBrowserController(browser);
      activationFXMLController.setSettings(this);
      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(Main.primaryStage);
      stage.setTitle("Activation");
      stage.setScene(new Scene(myActivation, 400, 320));
      stage.show();
    } catch (IOException ex) {
    }
  }

  public void addOptions(String tzd, String tzh) {
//    locationFXMLController.addOption(tzd, tzh);
//    locationFXMLController.setComboBox(getProgrammSettings("tz"), locationFXMLController.cmbTimeZone);
  }

  public String getDefaultLocation() {
    return defaultLocation;
  }

  public void setDefaultLocation(String defaultLocation) {
    this.defaultLocation = defaultLocation;
  }

  public String getDefaultDatetime() {
    return defaultDatetime;
  }

  public void setDefaultDatetime(String defaultDatetime) {
    this.defaultDatetime = defaultDatetime;
  }

  public String getCurrentLocation() {
    return currentLocation;
  }

  public void setCurrentLocation(String currentLocation) {
    this.currentLocation = currentLocation;
  }

  public String getCurrentDatetime() {
    return currentDatetime;
  }

  public void setCurrentDatetime(String currentDatetime) {
    this.currentDatetime = currentDatetime;
  }

  public String getHomeLocation() {
    return homeLocation;
  }

  public void setHomeLocation(String homeLocation) {
    this.homeLocation = homeLocation;
  }

  public void callDefaultBrowser(String urlString) {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(new URI(urlString));
      } catch (URISyntaxException ex) {
      } catch (IOException ex) {
      }
    }
  }

  public void showConfigDialog(String id) {
    try {
      if (id.equalsIgnoreCase("id_f1_date_time")) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent myActivation = fxmlLoader.load(getClass().getResource("controller/fxml/DateTimeFXML.fxml").openStream());
        dateTimeFXMLController = (DateTimeFXMLController) fxmlLoader.getController();
        dateTimeFXMLController.setBrowserController(browser);
        dateTimeFXMLController.setSettings(this);
        dateTimeFXMLController.setStage(stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
            browser.continueRefresh();
          }
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(Main.primaryStage);
        stage.setTitle("Date and Time");
        stage.setScene(new Scene(myActivation, 300, 375));
        stage.show();
      } else {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent myActivation = fxmlLoader.load(getClass().getResource("controller/fxml/LocationFXML.fxml").openStream());
        locationFXMLController = (LocationFXMLController) fxmlLoader.getController();
        locationFXMLController.setBrowserController(browser);
        locationFXMLController.setSettings(this);
        locationFXMLController.setStage(stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
            browser.continueRefresh();
          }
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(Main.primaryStage);
        stage.setTitle("Location");
        stage.setScene(new Scene(myActivation, 380, 520));
        stage.show();
      }

    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public void showDateFormatDialog() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader();
      Parent myActivation = fxmlLoader.load(getClass().getResource("controller/fxml/DateFormatFXML.fxml").openStream());
      dateFormatFXMLController = (DateFormatFXMLController) fxmlLoader.getController();
      dateFormatFXMLController.setBrowserController(browser);
      dateFormatFXMLController.setSettings(this);
      Stage stage = new Stage();
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(Main.primaryStage);
      stage.setTitle("Program Configuration");
      stage.setScene(new Scene(myActivation, 365, 345));
      stage.show();
    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void printWebView(String str, String type) {
    browser.setStage(Main.primaryStage);
    browser.setContent(str, type);
  }

  public void exportCSV(String str) {

    str = str.replace("<table width=\"100%\">", "");
    str = str.replace("<tbody>", "");
    str = str.replace("</tr>", "\n");
    str = str.replace("<tr>", "");
    str = str.replace("</tbody>", "");
    str = str.replace("</table>", "");

    str = str.replace("</td><td style=\"border:1px solid #5A5A5B;border-left:0px;\">", ",");
    str = str.replace("</td><td style=\"border:1px solid #5A5A5B;border-left:0px;border-right:0px;\">", ",");
    str = str.replace("<td style=\"border:1px solid #5A5A5B;border-left:0px;\">", "");
    str = str.replace("</td><td style=\"border:1px solid #5A5A5B;border-left:0px;border-top:0px;\">", ",");
    str = str.replace("</td><td style=\"border:1px solid #5A5A5B;border-left:0px;border-top:0px;border-right:0px;\">", ",");
    str = str.replace("<td style=\"border:1px solid #5A5A5B;border-left:0px;border-top:0px;\">", "");
    str = str.replace("</td>", "");

    GenerateCSV.generateCsvFile(Main.primaryStage, str);
  }

  public void showColumnChooserDialog() {
    try {
      Stage stage = new Stage();
      FXMLLoader fxmlLoader = new FXMLLoader();
      Parent myActivation = fxmlLoader.load(getClass().getResource("controller/fxml/ColumnChooserFXML.fxml").openStream());
      columnChooserController = (ColumnChooserFXMLController) fxmlLoader.getController();
      columnChooserController.setBrowserController(browser);
      columnChooserController.setSettings(this);
      columnChooserController.setStage(stage);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(Main.primaryStage);
      stage.setTitle("Interval");
      stage.setScene(new Scene(myActivation, 400, 500));
      stage.show();
    } catch (IOException ex) {
    }
  }

  public void showFindData() {
    try {
      Stage stage = new Stage();
      FXMLLoader fxmlLoader = new FXMLLoader();
      Parent myActivation = fxmlLoader.load(getClass().getResource("controller/fxml/FindDataFXML.fxml").openStream());
      findDataFXMLController = (FindDataFXMLController) fxmlLoader.getController();
      findDataFXMLController.setBrowserController(browser);
      findDataFXMLController.setSettings(this);
      findDataFXMLController.setStage(stage);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(Main.primaryStage);
      stage.setTitle("Find");
      stage.setScene(new Scene(myActivation, 310, 210));
      stage.show();
    } catch (IOException ex) {
    }
  }

  private String getProgramSettingType(String settings) {
    String[] datetime = {"date", "time", "datetime"};
    String[] location = {"tz", "apply_dst", "Lt_d", "Lt_m", "Lt_s", "Lt_dir", "Lg_d", "Lg_m", "Lg_s", "Lg_dir", "location"};
    for (String string : location) {
      if (settings.equalsIgnoreCase(string)) {
        return "location";
      }
    }
    for (String string : datetime) {
      if (settings.equalsIgnoreCase(string)) {
        return "datetime";
      }
    }
    return "";
  }

  public boolean deleteLocation() {
    for (JSONObject jSONObject : list_location) {
      try {
        if (jSONObject.getString("location").equalsIgnoreCase(getCurrentLocation())) {
          list_location.remove(jSONObject);
          storeProgramSettings();
          loadProgramSettings();
          return true;
        }
      } catch (JSONException ex) {
        Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return false;
  }

  public boolean deleteDateTime() {
    for (JSONObject jSONObject : list_datetime) {
      try {
        if (jSONObject.getString("datetime").equalsIgnoreCase(getCurrentDatetime())) {
          list_datetime.remove(jSONObject);
          storeProgramSettings();
          loadProgramSettings();
          return true;
        }
      } catch (JSONException ex) {
        Logger.getLogger(LocationFXMLController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return false;
  }

  public double getHeight() {
    return Main.primaryStage.getHeight();
  }

  public String getTime(String programmSettings) {
    if (programmSettings.isEmpty()) {
      Calendar calendar = Calendar.getInstance();
      int hour = calendar.get(Calendar.HOUR_OF_DAY);
      int minute = calendar.get(Calendar.MINUTE);
      return hour + ":" + minute;
    } else {
      return programmSettings;
    }
  }

  public LocalDate getDate(String programmSettings) {
    try {
      if (programmSettings.isEmpty()) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        LocalDate lcDate = LocalDate.of(year, month + 1, day);
        return lcDate;
      } else {
        SimpleDateFormat newformat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = newformat.parse(programmSettings);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        return LocalDate.of(year, month + 1, day);
      }
    } catch (ParseException ex) {
      return LocalDate.now();
    }
  }
}
