package edu.cnm.deepdive.stockrollerandroidclient.viewmodel;

import android.app.Application;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockrollersService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;
import java.util.List;
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
  private MutableLiveData<List<Stock>> searchResult;

  public MainViewModel(@NonNull Application application) {
    super(application);
    executor = Executors.newSingleThreadExecutor();
    database = StockRollersDatabase.getInstance();
    service = StockrollersService.getInstance();
    stockMutableLiveData = new MutableLiveData<>();
    historyMutableLiveData = new MutableLiveData<>();
    stockSearch = new MutableLiveData<>();
//    searchResult = Transformations.switchMap(stockSearch, (data) -> {
//      if (data == null) {
//        return new MutableLiveData<List<Stock>>(Collections.EMPTY_LIST);
//      } else {
//        return database.getStockDao().search(data);
//      }
//    });
  }

  public LiveData<String> getStockSearch() {
    return stockSearch;
  }

  public void setStockSearch(String name) {
    stockSearch.setValue(name);
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

  private static class SearchData extends MediatorLiveData<Pair<Long, String>> {
    public SearchData(LiveData<Long> stockId, LiveData<String> symbol) {
      addSource(stockId, new Observer<Long>() {
        @Override
        public void onChanged(Long id) {
          setValue(Pair.create(id, symbol.getValue()));
        }
      });
      addSource(symbol, new Observer<String>() {
        @Override
        public void onChanged(String ticker) {
          setValue(Pair.create(stockId.getValue(), ticker));
        }
      });
    }
  }


}
