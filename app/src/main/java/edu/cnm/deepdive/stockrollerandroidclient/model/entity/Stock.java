package edu.cnm.deepdive.stockrollerandroidclient.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.LinkedList;

@Entity
public class Stock {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "stock_id")
  private Long id;

  @ColumnInfo(name = "followed_stocks")
  private LinkedList<Stock> followedStocks;

  @ColumnInfo(name = "plot_path")
  private String plotPath;

}