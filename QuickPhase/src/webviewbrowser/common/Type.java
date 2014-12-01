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
public enum Type {

  ERROR("Enter another code?"),
  SUCCESS("Restart Application?");
  private String message;

  Type(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
