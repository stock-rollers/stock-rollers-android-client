package edu.cnm.deepdive.stockrollerandroidclient.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.stockrollerandroidclient.R;
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
  private final MutableLiveData<GoogleSignInAccount> account;


  public MainViewModel(@NonNull Application application) {
    super(application);
    executor = Executors.newSingleThreadExecutor();
    database = StockRollersDatabase.getInstance();
    service = StockrollersService.getInstance();
    stockMutableLiveData = new MutableLiveData<>();
    historyMutableLiveData = new MutableLiveData<>();
    account = new MutableLiveData<>();
  }

  public LiveData<Stock> getStocks() {
    return stockMutableLiveData;
  }

  public LiveData<History> getHistory() {
    return historyMutableLiveData;
  }

  public void setAccount(GoogleSignInAccount account) {
    this.account.setValue(account);
  }

  public void getStock(String ticker) {
    String token = getAuthorizationHeader(account.getValue());
    pending.add(
      service.getStock(token, ticker)
          .subscribeOn(Schedulers.from(executor))
          .subscribe((stock) -> {
            stockMutableLiveData.postValue(stock);
        })
    );
  }


  private String getAuthorizationHeader(GoogleSignInAccount account) {
    String token = getApplication().getString(R.string.oauth_header, account.getIdToken());
    Log.d("OAuth2.0 token", token); // FIXME Remove before shipping.
    return token;
  }

}
