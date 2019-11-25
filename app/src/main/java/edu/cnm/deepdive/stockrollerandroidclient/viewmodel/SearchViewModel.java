package edu.cnm.deepdive.stockrollerandroidclient.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;

public class SearchViewModel extends AndroidViewModel {


  private StockRollersDatabase database;
  private MutableLiveData<Long> stockId = new MutableLiveData<>();
//  private LiveData<Stock> stock;

  public SearchViewModel(@NonNull Application application) {
    super(application);
//    database = StockRollersDatabase.getInstance();
//    stock = Transformations.switchMap(stockId, (id) -> database.getStockRepository)
  }
}
