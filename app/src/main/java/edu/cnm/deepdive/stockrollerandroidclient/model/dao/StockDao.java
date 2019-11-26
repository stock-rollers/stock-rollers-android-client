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
import java.util.Optional;

@Dao
public interface StockDao {

  @Insert
  long insert(Stock stock);

  @Update
  int update(Stock stock);

  @Query("SELECT * FROM stock WHERE nasdaq_name LIKE :nasdaqName ORDER BY nasdaq_name ASC")
  LiveData<List<Stock>> search(String nasdaqName);

  @Delete
  int delete(Stock stock);
}
