package edu.cnm.deepdive.stockrollerandroidclient;

import android.app.Application;
import edu.cnm.deepdive.stockrollerandroidclient.service.GoogleSignInService;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;

public class StockrollersApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    StockRollersDatabase.setApplicationContext(this);
    GoogleSignInService.setApplicationContext(this);
    final StockRollersDatabase database = StockRollersDatabase.getInstance();
    new Thread(() -> {
      database.getHistoryDao().delete();
      database.getStockDao().delete();
      //delete nothing in the database to initialize it
    }).start();
  }

}
