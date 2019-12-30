package com.example.mathproof;

import java.io.Serializable;
import java.util.List;

/**
 * Serializes problems array
 *
 * Transfer is a wrapper class used to serialize problems into an array list so that they can
 * be easily transferred using a bundle.
 *
 * @author Fabrizio Botalla, Alberto Contreras, Jerry Lane, and Adam Tipton
 */
public class Transfer implements Serializable {

  //serialized list object variable
  private List<Integer[]> listObj;

  //auto-load the listObj with problem data
  public Transfer(List<Integer[]> listObj) {
    this.listObj = listObj;
  }

  //currently unused, but available for future development
  public void setArray(List<Integer[]> problems) {
    this.listObj = problems;
  }

  //getter to transfer problem data after bundle is deserialized
  public List<Integer[]> getArray() {
    return this.listObj;
  }
}