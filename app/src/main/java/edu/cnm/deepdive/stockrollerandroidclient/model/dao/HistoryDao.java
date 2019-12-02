package edu.cnm.deepdive.stockrollerandroidclient.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Dao
public interface HistoryDao {

  @Insert
  void insert(History history);

  @Query("SELECT * FROM history WHERE stock_id = :stockId")
  LiveData<List<History>> getAllByStockId(Long stockId);

  @Query("SELECT * FROM history WHERE date = :date AND stock_id = :stockId")
  Optional<History> getHistoryByDateAndStock(LocalDate date, Long stockId);

  @Query("SELECT * FROM history WHERE stock_id = :stockId")
  List<History> getHistoryByStock(Long stockId);

  @Delete
  void delete(History... histories);

}
