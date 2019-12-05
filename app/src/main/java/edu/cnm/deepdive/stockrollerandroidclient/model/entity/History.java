package edu.cnm.deepdive.stockrollerandroidclient.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import com.facebook.stetho.json.annotation.JsonValue;
import java.time.LocalDate;

@Entity(
    foreignKeys = {
        @ForeignKey(
            entity = Stock.class,
            childColumns = "stock_id",
            parentColumns = "stock_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class History {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "history_id")
  private Long id;

  @ColumnInfo(name = "stock_id", index = true)
  private Long stockId;

  @ColumnInfo(name = "date")
  private LocalDate date;

  @ColumnInfo(name = "open")
  private float open;

  @ColumnInfo(name = "close")
  private float close;

  @ColumnInfo(name = "high")
  private float high;

  @ColumnInfo(name = "low")
  private float low;

  @ColumnInfo(name = "volume")
  private Long volume;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Long getStockId() {
    return stockId;
  }

  public void setStockId(Long stockId) {
    this.stockId = stockId;
  }

  public float getOpen() {
    return open;
  }

  public void setOpen(float open) {
    this.open = open;
  }

  public float getClose() {
    return close;
  }

  public void setClose(float close) {
    this.close = close;
  }

  public float getHigh() {
    return high;
  }

  public void setHigh(float high) {
    this.high = high;
  }

  public float getLow() {
    return low;
  }

  public void setLow(float low) {
    this.low = low;
  }

  public Long getVolume() {
    return volume;
  }

  public void setVolume(Long volume) {
    this.volume = volume;
  }
}
