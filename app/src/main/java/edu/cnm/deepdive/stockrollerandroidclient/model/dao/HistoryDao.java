package edu.cnm.deepdive.stockrollerandroidclient.model.dao;

import androidx.room.Dao;
import androidx.room.Query;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Dao
public interface HistoryDao {

  @Query("SELECT * FROM history WHERE date = :date AND stock_id = :stockId")
  Optional<History> getHistoryByDateAndStock(LocalDate date, Long stockId);

  @Query("SELECT * FROM history WHERE stock_id = :stockId")
  List<History> getHistoryByStock(Long stockId);

}
