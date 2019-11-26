package edu.cnm.deepdive.stockrollerandroidclient.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockrollersService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private Executor executor;
  private StockRollersDatabase database;
  private CompositeDisposable pending = new CompositeDisposable();
  private StockrollersService service;
  private MutableLiveData<Stock> stockMutableLiveData;
  private MutableLiveData<History> historyMutableLiveData;
  private MutableLiveData<String> stockSearch;

  public MainViewModel(@NonNull Application application) {
    super(application);
    executor = Executors.newSingleThreadExecutor();
    database = StockRollersDatabase.getInstance();
    service = StockrollersService.getInstance();
    stockMutableLiveData = new MutableLiveData<>();
    historyMutableLiveData = new MutableLiveData<>();
    stockSearch = new MutableLiveData<>();
  }

  public void setStockSearch(String name) {

  }

  public LiveData<Stock> getStocks() {
    return stockMutableLiveData;
  }

  public LiveData<History> getHistory() {
    return historyMutableLiveData;
  }

  public void getStock(String ticker) {
    pending.add(
      service.getStock(ticker)
          .subscribeOn(Schedulers.from(executor))
          .subscribe((stock) -> {
            stockMutableLiveData.postValue(stock);
        })
    );
  }


}
