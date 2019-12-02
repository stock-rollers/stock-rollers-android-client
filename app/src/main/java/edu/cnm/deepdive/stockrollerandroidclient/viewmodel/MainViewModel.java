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
  private MutableLiveData<Stock> stock;
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
    stock = new MutableLiveData<>();
    historyMutableLiveData = new MutableLiveData<>();
    stockSearch = new MutableLiveData<>();
    stocks = new MutableLiveData<>();
    account = new MutableLiveData<>();
    googleSignInService = GoogleSignInService.getInstance();
  }

  public LiveData<String> getStockSearch() {
    return stockSearch;
  }

  public void setStockSearch(String name) {
    stockSearch.setValue(name);
  }

  public LiveData<Stock> getStock() {
    return stock;
  }

  public void setStock(Stock stock) {
    this.stock.postValue(stock);
  }

  public LiveData<List<Stock>> getStocks() {
    return database.getStockDao().getAll();
  }

  public LiveData<List<History>> getHistory() {
    return database.getHistoryDao().getAllByStockId(stock.getValue().getId());
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
                if (!database.getStockDao().getByName(ticker).isPresent()) {
                  database.getStockDao().insert(stock);
                  getHistory(ticker);
                }
              })
      );
  }

  public void getHistory(String ticker) {
    String token = getAuthorizationHeader();
    pending.add(
        service.getHistoryForStock(token, ticker)
        .subscribeOn(Schedulers.from(executor))
        .subscribe((response) -> {
          List<History> histories = response.getHistories();
          for (History history :
              histories) {
            database.getHistoryDao().insert(history);
          }
        })
    );
  }


  private String getAuthorizationHeader() {
    String token = getApplication().getString(R.string.oauth_header, googleSignInService.getAccount().getValue().getIdToken());
    Log.d("OAuth2.0 token", token); // FIXME Remove before shipping.
    return token;
  }
}
