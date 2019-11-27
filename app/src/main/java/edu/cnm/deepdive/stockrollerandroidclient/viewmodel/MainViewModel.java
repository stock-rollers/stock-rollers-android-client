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
import java.util.ArrayList;
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
  private MutableLiveData<List<Stock>> stocks;
  private MutableLiveData<Stock> searchResult;

  public MainViewModel(@NonNull Application application) {
    super(application);
    executor = Executors.newSingleThreadExecutor();
    database = StockRollersDatabase.getInstance();
    service = StockrollersService.getInstance();
    stockMutableLiveData = new MutableLiveData<>();
    historyMutableLiveData = new MutableLiveData<>();
    stockSearch = new MutableLiveData<>();
    stocks = new MutableLiveData<>();
    refresh();
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

  public LiveData<Stock> getStock() {
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

  public LiveData<List<Stock>> getStocks() {
    return stocks;
  }

  public void refresh() {
    Stock stock = new Stock("name", "hundred", "551");
    Stock stock1 = new Stock("name1", "hundred1", "550");
    Stock stock2 = new Stock("name2", "hundred2", "559");
    Stock stock3 = new Stock("name3", "hundred3", "558");
    Stock stock4 = new Stock("name4", "hundred4", "557");
    Stock stock5 = new Stock("name5", "hundred5", "556");
    Stock stock6 = new Stock("name6", "hundred6", "555");

    ArrayList<Stock> stockArrayList = new ArrayList<>();

    stockArrayList.add(stock);
    stockArrayList.add(stock1);
    stockArrayList.add(stock2);
    stockArrayList.add(stock3);
    stockArrayList.add(stock4);
    stockArrayList.add(stock5);
    stockArrayList.add(stock6);

    stocks.setValue(stockArrayList);

  }

}
