package edu.cnm.deepdive.stockrollerandroidclient.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.GoogleSignInService;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockrollersService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private Executor executor;
  private StockRollersDatabase database;
  private CompositeDisposable pending = new CompositeDisposable();
  private StockrollersService service;
  private GoogleSignInService googleSignInService;
  private MutableLiveData<Stock> stockMutableLiveData;
  private MutableLiveData<History> historyMutableLiveData;
  private MutableLiveData<String> stockSearch;
  private MutableLiveData<List<Stock>> stocks;
  private MutableLiveData<Stock> searchResult;
  private final MutableLiveData<GoogleSignInAccount> account;


  public MainViewModel(@NonNull Application application) {
    super(application);
    executor = Executors.newSingleThreadExecutor();
    database = StockRollersDatabase.getInstance();
    service = StockrollersService.getInstance();
    stockMutableLiveData = new MutableLiveData<>();
    historyMutableLiveData = new MutableLiveData<>();
    stockSearch = new MutableLiveData<>();
    stocks = new MutableLiveData<>();
    account = new MutableLiveData<>();
    googleSignInService = GoogleSignInService.getInstance();
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

  public void setAccount(GoogleSignInAccount account) {
    this.account.setValue(account);
  }

  public void getStock(String ticker) {
    String token = getAuthorizationHeader();
    pending.add(
      service.getStock(token, ticker)
          .subscribeOn(Schedulers.from(executor))
          .subscribe((stock) -> {
            stockMutableLiveData.postValue(stock);
            List<Stock> current = stocks.getValue();
            current.add(stock);
            stocks.postValue(current);
            database.getStockDao().insert(stock);
        })
    );
  }


  private String getAuthorizationHeader() {
    String token = getApplication().getString(R.string.oauth_header, googleSignInService.getAccount().getValue().getIdToken());
    Log.d("OAuth2.0 token", token); // FIXME Remove before shipping.
    return token;
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
