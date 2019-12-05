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
import io.reactivex.internal.schedulers.SchedulerWhen;
import io.reactivex.schedulers.Schedulers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Basic View Model class that handles the current stock that the UI is working with
 */
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


  /**
   * Setup of all of the livedata objects aswell as creating the executor and Composite Disposal
   * @param application context to keep viewModels the same across the fragments
   */
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

  /**
   * Get the current stock that the View Model has
   * @return liveData object holding current stock
   */
  public LiveData<Stock> getStock() {
    return stock;
  }

  /**
   * Set the stock that the View Model is handling
   * @param stock that you want the view model hold
   */
  public void setStock(Stock stock) {
    this.stock.postValue(stock);
  }

  /**
   * Gets all of the stocks from the database
   * @return LiveData of a list of all of the stocks
   */
  public LiveData<List<Stock>> getStocks() {
    return database.getStockDao().getAll();
  }

  /**
   * Gets all of the history it has for the stock that the viewModel is holding
   * @return LiveData of a List of Histories for the current stock
   */
  public LiveData<List<History>> getHistory() {
    return database.getHistoryDao().getAllByStockId(stock.getValue().getId());
  }

  /**
   * Sets the account of the user in the viewModel
   * @param account of the user
   */
  public void setAccount(GoogleSignInAccount account) {
    this.account.setValue(account);
  }

  /**
   * Removes a stock from the database
   * @param stock you want to delete
   */
  public void deleteStock(Stock stock) {
    new Thread(() -> database.getStockDao().delete(stock)).start();
  }

  /**
   * Makes an API call if  you do not already have the specified stock in your database.
   * Otherwise it will return the stock from your database.
   * @param ticker
   */
  public void getStock(String ticker) {
    String token = getAuthorizationHeader();
      pending.add(
          service.getStock(token, ticker)
              .subscribeOn(Schedulers.from(executor))
              .subscribe((stock) -> {
                if (!database.getStockDao().getByName(ticker).isPresent()) {
                  setStock(stock);
                  //getHistory(ticker);
                } else {
                  setStock(database.getStockDao().getByName(ticker).get());
                }
              })
      );
  }

  /**
   * Will make another call for a stock to update its values.
   * CURRENTLY NOT WORKING
   * @param stock
   */
  public void updateStock(Stock stock) {
    String token = getAuthorizationHeader();
    pending.add(
        service.updateStock(token, stock.getNasdaqName())
        .subscribeOn(Schedulers.from(executor))
        .subscribe((s) -> {
          stock.setFiftyTwoWkHigh(s.getFiftyTwoWkHigh());
          stock.setFiftyTwoWkLow(s.getFiftyTwoWkLow());
          stock.setPrice(s.getPrice());
          database.getStockDao().update(stock);
        })
    );
  }

  /**
   * Gets the history for the specified stock and saves it in the database.
   * @param stock you want to  get the history for
   */
  public void getHistory(Stock stock) {
    String token = getAuthorizationHeader();
    pending.add(
        service.getHistoryForStock(token, stock.getNasdaqName())
        .subscribeOn(Schedulers.from(executor))
        .subscribe((histories) -> {
          long id = stock.getId();
          for (int i = 0; i < histories.size(); i++) {
            History history = new History();
            history.setStockId(id);
            history.setDate(LocalDate.parse(histories.get(i).getDate()));
            history.setClose(histories.get(i).getClose());
            history.setOpen(histories.get(i).getOpen());
            history.setHigh(histories.get(i).getHigh());
            history.setLow(histories.get(i).getLow());
            history.setVolume(histories.get(i).getVolume());
            database.getHistoryDao().insert(history);
          }
        })
    );
  }

  /**
   * Makes a call to the API for a random stock and sets it as the current stock the  View Model is working with
   */
  public void getRandom() {
    pending.add(
        service.getRandom(getAuthorizationHeader())
        .subscribeOn(Schedulers.from(executor))
        .subscribe((stock) -> setStock(stock))
    );
  }


  private String getAuthorizationHeader() {
    String token = getApplication().getString(R.string.oauth_header, googleSignInService.getAccount().getValue().getIdToken());
    Log.d("OAuth2.0 token", token); // FIXME Remove before shipping.
    return token;
  }
}
