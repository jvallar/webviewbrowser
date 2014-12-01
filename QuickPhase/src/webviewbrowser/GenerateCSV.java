/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser;

/**
 *
 * @author Jameson
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GenerateCSV {

  public static void generateCsvFile(Stage primaryStage, String str) {
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);
    File file = fileChooser.showSaveDialog(primaryStage);
    if (file != null) {
      saveFile(str, file);
    }
  }

  private static void saveFile(String str, File file) {
    try {
      FileWriter writer = null;

      writer = new FileWriter(file);
      writer.append(str);
//      writer.append("DisplayName");
//      writer.append(',');
//      writer.append("Age");
//      writer.append('\n');
//      writer.append("MKYONG");
//      writer.append(',');
//      writer.append("26");
//      writer.append('\n');
//      writer.append("YOUR NAME");
//      writer.append(',');
//      writer.append("29");
//      writer.append('\n');
      //generate whatever data you want
      writer.flush();
      writer.close();
    } catch (IOException ex) {
      Logger.getLogger(GenerateCSV.class.getName()).log(Level.SEVERE, null, ex);
    }

  }
}
