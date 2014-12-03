/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser.controller;

import com.sun.javafx.print.PrintHelper;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import webviewbrowser.Settings;

/**
 * FXML Controller class
 *
 * @author Jameson
 */
public class BrowserFXMLController implements Initializable {

  @FXML
  private WebView webView;

  private Settings settings;
  private String html = "html/index.html";
  private WebEngine webEngine;
  @FXML
  private WebView webView1;
  private String printHtml;
  private Stage stage;
  private WebEngine webEngine1;
  String html2 = "html/";

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    webEngine = webView.getEngine();
    final URL urlHello = getClass().getResource(html);
    webEngine.load(urlHello.toExternalForm());
    settings = new Settings(BrowserFXMLController.this);
    JSObject window = (JSObject) webEngine.executeScript("window");
    window.setMember("settings", settings);
  }

  public void newHandleSetDateTime(String date, String time) {
    webEngine.executeScript("newHandleSetDateTime('" + date + "','" + time + "')");
  }

  public void newSetLoc() {
    webEngine.executeScript("newSetLoc()");
  }

  public void setInterval(String data) {
    webEngine.executeScript("setInterval('" + data+ "')");
  }

  public void setContent(String str, String type) {

    String url = getClass().getResource(html2).toExternalForm();
    str = str.replace("img src=\"", "img src=\"" + url);
//    str = str.replace("height=\"50\">", "height=\"50\">"+url);
    printHtml = "<html><head>"
            + "<script type=\"text/javascript\">\n"
            + "function rotateAllMoon(degrees)\n"
            + "{\n"
            + " var canvases = document.getElementsByTagName('canvas');\n"
            + "  for(i = 0;i < canvases.length; i++){\n"
            + "   var canvas = canvases[i];\n"
            + "   var base_image = new Image();\n"
            + "   base_image.src = \"" + url + "\"+canvas.innerHTML; \n"
            + "   drawRotated(base_image,degrees,canvas);\n"
            + " }\n"
            + "}"
            + "function drawRotated(image, degrees,  canvas){ \n"
            + "  image.onload = function(){  \n"
            + "   var context = canvas.getContext('2d');\n"
            + "   context.save();\n"
            + "   context.clearRect(0,0,canvas.width,canvas.height);\n"
            + "   context.translate(canvas.width/2,canvas.height/2);\n"
            + "   context.rotate(degrees*Math.PI/180);\n"
            + "   context.drawImage(image,-canvas.width/2,-canvas.height/2, canvas.width, canvas.height);\n"
            + "   context.fillStyle = \"#000000\";\n"
            + "   context.restore();\n"
            + "  };\n"
            + " }"
            + "</script>"
            + "<style type=\"text/css\">"
            + ".helpHeading { font-size:14px; font-weight:bold; color:orange; }\n"
            + ".helpHeading2 { font-size:13px; font-weight:bold; color:#ffffff; }\n"
            + ".helpHeading3 { font-size:12px; font-weight:bold; color:#ffffff; }\n"
            + "\n"
            + "body, td, p { font-family: tahoma,verdana,arial,sans-serif; font-size: 11px; color:#dddddd; }\n"
            + "\n"
            + ".style1 { font-family: tahoma,verdana,arial,sans-serif; font-size: 10px; }\n"
            + ".style2 { font-family: tahoma,verdana,arial,sans-serif; font-size: 11px; }\n"
            + ".style3 { font-family: tahoma,verdana,arial,sans-serif; font-size: 12px; }\n"
            + ".style4 { font-family: tahoma,verdana,arial,sans-serif; font-size: 13px; }\n"
            + "\n"
            + "a:link { color: #8FB7FF; text-decoration:none; }\n"
            + "a:visited { color: #8FB7FF; text-decoration:none; }\n"
            + "a:hover { color: #8FB7FF; text-decoration:none; }\n"
            + "\n"
            + ".mainNav { cursor:hand; color:#8FB7FF; }\n"
            + ".subNav { cursor:hand; font-size:10px; color:#47669E; padding:2px; padding-left:6px; padding-right:6px; }\n"
            + "\n"
            + ".TDLlabel { color:#ffffff; }\n"
            + ".TDLdata { color:orange; font-weight:bold; }\n"
            + ".f1FieldStyle { background:#BAC6DA; color:#000000; font-family:tahoma,arial,verdana,sans-serif; font-size:12px; }\n"
            + "\n"
            + ".f2Label { color:#666666; }\n"
            + ".f2FieldStyle { background:#BAC6DA; color:#000000; font-family:tahoma,arial,verdana,sans-serif; font-size:12px; }\n"
            + "\n"
            + ".f3FieldStyle { background:#ffffff; color:#000000; font-family:tahoma,arial,verdana,sans-serif; font-size:12px; }\n"
            + "\n"
            + ".dayInfoLabel { font-size:11px; color:#D8D7D7; }\n"
            + ".dayInfoData { color:#CFFECD; }\n"
            + "\n"
            + ".calDayInfoLabel { font-size:10px; color:#D8D7D7; }\n"
            + ".calDayInfoData { color:#FDE698; }\n"
            + ".calDayInfoDataB { color:#BBD7FD; }\n"
            + "\n"
            + "a.calDate:link { color: #ffffff; text-decoration: none; }\n"
            + "a.calDate:visited { color: #ffffff; text-decoration: none; }\n"
            + "a.calDate:hover { color: #ffffff; text-decoration: none; }\n"
            + "\n"
            + ".calQtr { color:#BBD7FD; }\n"
            + "a.calQtr:link { color: #ffffff; text-decoration: none; }\n"
            + "a.calQtr:visited { color: #ffffff; text-decoration: none; }\n"
            + "a.calQtr:hover { color: #ffffff; text-decoration: none; }\n"
            + "\n"
            + ".contentPage {\n"
            + "	background-color:#000000;\n"
            + "	background-image:url('bg_s.gif');\n"
            + "	background-attachment:fixed;\n"
            + "}\n"
            + ".contentPage2 {\n"
            + "	background-color:#ffffff;\n"
            + "}\n"
            + "\n"
            + "select.            + \"   font-size:11px;\\n\"\n"
            + "style1 {\n"
            + "   background-color:#294570;\n"
            + "   color:#ffffff;\n"
            + "}\n"
            + "\n"
            + ".inputBtn {\n"
            + "	background:#666666;\n"
            + "	border: 1px solid #ffffff;\n"
            + "	color:#ffffff;\n"
            + "	font-size : 11px;\n"
            + "  	font-family : tahoma,verdana,arial,sans-serif;\n"
            + "  	font-weight: bold;\n"
            + "}\n"
            + ".inputBtn2 {\n"
            + "	background:#666666;\n"
            + "	border: 1px solid #999999;\n"
            + "	color:#000000;\n"
            + "	font-size : 10px;\n"
            + "  	font-family : tahoma,verdana,arial,sans-serif;\n"
            + "}\n"
            + ".pseudoInputBtn1 {\n"
            + "	background:#666666;\n"
            + "	border: 1px solid #ffffff;\n"
            + "	color:#ffffff;\n"
            + "	font-size : 10px;\n"
            + "  	font-family : tahoma,verdana,arial,sans-serif;\n"
            + "  	font-weight: bold;\n"
            + "  	padding-left:5px;\n"
            + "  	padding-right:5px;\n"
            + "  	cursor:hand;\n"
            + "}\n"
            + ".pseudoInputBtn2 {\n"
            + "	height:11px;\n"
            + "	background:#222222;\n"
            + "	border:1px solid #333333;\n"
            + "	color:#999999;\n"
            + "	font-size:9px;\n"
            + "  	font-family:tahoma,verdana,arial,sans-serif;\n"
            + "  	letter-spacing:.1em;\n"
            + "  	padding-left:2px;\n"
            + "  	padding-right:2px;\n"
            + "  	text-align:center;\n"
            + "  	cursor:hand;\n"
            + "}\n"
            + "\n"
            + ".border1 {\n"
            + "	border: 1px solid #9A9999;\n"
            + "}\n"
            + "\n"
            + ".menustyle { position:absolute; left:0; top:0; visibility:hidden; }\n"
            + "\n"
            + ".formfield2 { border: 1px solid #000000; background:#ffffff; color:#000000; margin: 0px; font-family:tahoma,arial,verdana,sans-serif; font-size:11px; }\n"
            + "\n"
            + "select.style2 {\n"
            + "   font-size:11px;\n"
            + "   background-color:#ffffff;\n"
            + "   color:#000000;\n"
            + "}\n"
            + "\n"
            + ".cal1width { width:400px; }\n"
            + ".pwidth { width:100%; }\n"
            + ".palign { align:center; }"
            + "</style>"
            + "</head>"
            + "<body onload=\"rotateAllMoon(30)\" style=\"width: 100%;margin-left: auto; margin-right: auto; margin-top: auto; \" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" text=\"#000000\" link=\"#696D6A\" vlink=\"#696D6A\" alink=\"#FC8A10\" ";
    if (type.equalsIgnoreCase("cal2_screen")) {
      printHtml += " bgcolor=\"#ffffff\"";
      str = str.replace("<table", "<table valign=\"middle\"");
//      str = str.replace("<tr", "<tr bgcolor=\"#000000\"");
    } else if (type.equalsIgnoreCase("cal3_screen")) {
      printHtml += " bgcolor=\"#ffffff\"";
    } else {
      printHtml += "bgcolor=\"#000000\" background=\"" + url + "bg_s.gif\"";
    }
    printHtml += ">"
            + "<table height=\"100%\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
            + "	<tr>\n"
            + "		<td valign=\"middle\" align=\"center\">"
            + str
            + "		</td>\n"
            + "	</tr>\n"
            + "</table>"
            + "</body></html>";

    webEngine1 = webView1.getEngine();
    webEngine1.loadContent(printHtml);
    System.err.println(printHtml);
    print();
  }

  public void print() {
    Printer printer = Printer.getDefaultPrinter();
    PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, 15, 15, 15, 15);
    PrintResolution resolution = PrintHelper.createPrintResolution(1000, 1000);
    PrinterJob job = PrinterJob.createPrinterJob();

    JobSettings jobsetting = job.getJobSettings();
    jobsetting.setPageLayout(pageLayout);
    jobsetting.setPrintResolution(resolution);
    jobsetting.setPrintQuality(PrintQuality.HIGH);

    if (job.showPrintDialog(stage.getOwner()) && job.showPageSetupDialog(stage.getOwner())) {
      boolean success = job.printPage(webView1);
      if (success) {
        job.endJob();
      }
    }
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

}
