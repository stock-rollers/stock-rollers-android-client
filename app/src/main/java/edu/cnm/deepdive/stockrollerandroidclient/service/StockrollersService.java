package edu.cnm.deepdive.stockrollerandroidclient.service;

import edu.cnm.deepdive.stockrollerandroidclient.BuildConfig;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.model.pojo.HistoryResponse;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Class to query our Server Application with HTTP
 */
public interface StockrollersService {

  /**
   * Uses a Single Instance
   * @return single instance of StockrollersService
   */
  static StockrollersService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * Querys the server for a stock object
   * @param token Authorization header
   * @param symbol Stock ticker
   * @return Single Stock object
   */
  @GET("stocks/{symbol}")
  Single<Stock> getStock(@Header("Authorization") String token, @Path("symbol") String symbol);

  /**
   * Gets A list of History objects for the specified ticker
   * @param token Authorization header
   * @param symbol Stock ticker
   * @return Flowable List of History Objects
   */
  @GET("history/{symbol}")
  Flowable<ArrayList<HistoryResponse>> getHistoryForStock(@Header("Authorization") String token, @Path("symbol") String symbol);

  /**
   * Querys the server for a random Stock
   * @param token Authorization Header
   * @return Single Stock
   */
  @GET("stocks/random")
  Single<Stock> getRandom(@Header("Authorization") String token);

  /**
   * Updates the stock on the server and gets back the updated data.
   * @param token Authorization Header
   * @param symbol Stock ticker
   * @return
   */
  @GET("stocks/update/{symbol}")
  Single<Stock> updateStock(@Header("Authorization") String token, @Path("symbol") String symbol);

  /**
   * Holds the Instance of the StockRollersService class.
   * One and only one should  be  present.
   */
  class InstanceHolder {

    private static final StockrollersService INSTANCE;

    static {
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.BODY);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .baseUrl(BuildConfig.BASE_URL)
          .client(client)
          .build();
      INSTANCE = retrofit.create(StockrollersService.class);
    }
  }
}
