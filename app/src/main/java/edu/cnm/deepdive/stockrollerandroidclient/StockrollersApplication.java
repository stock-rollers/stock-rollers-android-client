package edu.cnm.deepdive.stockrollerandroidclient;

import android.app.Application;
import com.facebook.stetho.Stetho;
import edu.cnm.deepdive.stockrollerandroidclient.service.GoogleSignInService;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;

public class StockrollersApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
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
