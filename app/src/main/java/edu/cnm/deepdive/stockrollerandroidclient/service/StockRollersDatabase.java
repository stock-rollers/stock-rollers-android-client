package edu.cnm.deepdive.stockrollerandroidclient.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.stockrollerandroidclient.model.dao.HistoryDao;
import edu.cnm.deepdive.stockrollerandroidclient.model.dao.StockDao;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.History;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import java.time.LocalDate;

/**
 * Simple Database class to handle saving, querying, and deleting items from
 */
@Database(
  entities = {Stock.class, History.class},
  version = 1, exportSchema = true
)
@TypeConverters(StockRollersDatabase.Converters.class)
public abstract class StockRollersDatabase extends RoomDatabase {

  /**
   * Constructor currently does nothing
   */
  protected StockRollersDatabase() {
  }

  private static Application applicationContext;

  /**
   * Give the database an application
   * @param applicationContext
   */
  public static void setApplicationContext(Application applicationContext) {
    StockRollersDatabase.applicationContext = applicationContext;
  }

  /**
   * Returns a Single Instancce of the database
   * @return
   */
  public static StockRollersDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  /**
   * gets the HistoryDao
   * @return
   */
  public abstract HistoryDao getHistoryDao();

  /**
   * Gets the StockDao
   * @return
   */
  public abstract StockDao getStockDao();

  private static class InstanceHolder {

    private static final StockRollersDatabase INSTANCE;

    static {
      INSTANCE =
          Room.databaseBuilder(applicationContext, StockRollersDatabase.class, "stockrollers_db")
              .build();
    }
  }

  /**
   * Type converters for date and string object
   */
  public static class Converters {

    @TypeConverter
    public String LocalDateToString(LocalDate date) {
      return date.toString();
    }

    @TypeConverter
    public LocalDate stringToLocalDate(String date) {
      return LocalDate.parse(date);
    }
  }
}
