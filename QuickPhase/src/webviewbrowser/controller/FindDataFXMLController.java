/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates 
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import webviewbrowser.Settings;
import webviewbrowser.common.Option;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class FindDataFXMLController implements Initializable {

  @FXML
  private TextField txtNumber;
  private Settings settings;
  private BrowserFXMLController browserController;
  @FXML
  private ComboBox<Option> cmbPhaseName;
  @FXML
  private Button txtCancel;
  @FXML
  private Button btnApply1;
  @FXML
  private ComboBox<Option> cmbMonth;
  @FXML
  private ComboBox<Option> cmbDay;
  @FXML
  private DatePicker date;

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
    String number = txtNumber.getText();
    String month = cmbMonth.getValue() == null ? "0" : cmbMonth.getValue().getData();
    String day = cmbDay.getValue() == null ? "0" : cmbDay.getValue().getData();
    String name = cmbPhaseName.getValue().getData();
    String date = getDate();
    browserController.setFindData(number, month, day, name, date);
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

  private void initializeComboBox() {
    cmbMonth.getItems().add(new Option("0", "January"));
    cmbMonth.getItems().add(new Option("1", "February"));
    cmbMonth.getItems().add(new Option("2", "March"));
    cmbMonth.getItems().add(new Option("3", "April"));
    cmbMonth.getItems().add(new Option("4", "May"));
    cmbMonth.getItems().add(new Option("5", "June"));
    cmbMonth.getItems().add(new Option("6", "July"));
    cmbMonth.getItems().add(new Option("7", "August"));
    cmbMonth.getItems().add(new Option("8", "September"));
    cmbMonth.getItems().add(new Option("9", "October"));
    cmbMonth.getItems().add(new Option("10", "November"));
    cmbMonth.getItems().add(new Option("11", "December"));

    cmbPhaseName.getItems().add(new Option("0", "New Moon"));
    cmbPhaseName.getItems().add(new Option("1", "First Quarter"));
    cmbPhaseName.getItems().add(new Option("2", "Full Moon"));
    cmbPhaseName.getItems().add(new Option("3", "Last Quarter"));
    for (int i = 1; i < 31; i++) {
      cmbDay.getItems().add(new Option(i + "", i + ""));
    }

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
      String dateStr = date.getValue() == null ? lcDate.toString() : date.getValue().toString();
      Date date2 = originalFormat.parse(dateStr);
      String newDate = newformat.format(date2);
      return newDate;
    } catch (ParseException ex) {
      Logger.getLogger(ColumnChooserFXMLController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return "";
  }

  @FXML
  private void cancel(ActionEvent event) {
    ((Node) (event.getSource())).getScene().getWindow().hide();
  }

}
