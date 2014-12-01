/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webviewbrowser.common;

/**
 *
 * @author Jameson
 */
public class Option {

  String data;
  String description;

  public Option(String data, String description) {
    this.data = data;
    this.description = description;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return description;
  }

}
