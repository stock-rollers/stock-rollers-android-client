package edu.cnm.deepdive.stockrollerandroidclient.model.pojo;

import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import java.util.List;

public class HistoryResponse {

  List<History> histories;

  public List<History> getHistories() {
    return histories;
  }

  public void setHistories(
      List<History> histories) {
    this.histories = histories;
  }
}
