package edu.cnm.deepdive.stockrollerandroidclient;

import android.app.Application;
import edu.cnm.deepdive.stockrollerandroidclient.service.GoogleSignInService;

public class StockrollersApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setApplicationContext(this);
  }

}
