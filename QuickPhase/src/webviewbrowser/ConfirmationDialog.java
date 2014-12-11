/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Jameson
 */
public class ConfirmationDialog extends Stage {

  private Runnable callback;

  public ConfirmationDialog(final Stage stg, final String text, Runnable callback) {
    this.callback = callback;
    setResizable(false);
    initModality(Modality.APPLICATION_MODAL);
//        initStyle(StageStyle.TRANSPARENT);

    FlowPane buttons = new FlowPane(10, 10);
    buttons.setAlignment(Pos.CENTER);
    Button btnOk = new Button("Yes");
    btnOk.setOnAction(new EventHandler() {
      @Override
      public void handle(Event event) {
        callback.run();
        ConfirmationDialog.this.hide();//.close();
      }
    });
    Button btnCancel = new Button("Cancel");
    btnCancel.setOnAction(new EventHandler() {
      @Override
      public void handle(Event event) {
        ConfirmationDialog.this.hide();//.close();
      }
    });
    buttons.getChildren().addAll(btnOk);
    buttons.getChildren().addAll(btnCancel);
    VBox box = new VBox();
    box.setAlignment(Pos.CENTER);
    box.setSpacing(10);
    box.getChildren().addAll(new Label(text), buttons);
    Scene s = new Scene(box);
    setScene(s);
    show();
  }
}
