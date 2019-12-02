package edu.cnm.deepdive.stockrollerandroidclient.model.pojo;

import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import java.util.ArrayList;
import java.util.List;

public class HistoryResponse {

  ArrayList<History> histories;

  public ArrayList<History> getHistories() {
    return histories;
  }

  public void setHistories(
      ArrayList<History> histories) {
    this.histories = histories;
  }
}
