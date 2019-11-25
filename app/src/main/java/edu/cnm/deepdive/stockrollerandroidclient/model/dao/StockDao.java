package edu.cnm.deepdive.stockrollerandroidclient.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import java.util.LinkedList;
import java.util.List;

@Dao
public interface StockDao {

  @Insert
  LinkedList<Stock> FollowedStocks();

  @Insert
  long insert(Stock stock);

  @Insert
  long insert(String plotPath);

  @Query("SELECT * FROM Stock WHERE stock_id = :followedStock ORDER BY followed_stocks ASC")
  LiveData<List<Stock>> getFollowedStocks(String followedStock);

  @Query("SELECT * FROM Stock WHERE stock_id = :stock ORDER BY plot_path ASC")
  String getPlotPathbyStock(Stock stock);

  @Update
  int update(Stock stock);

  @Delete
  int delete(Stock stock);
}
