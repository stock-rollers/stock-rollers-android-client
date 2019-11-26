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

  @ColumnInfo(name = "plot_path")
  private String plotPath;

  @ColumnInfo(name = "fifty_two_wk_high")
  private Double fiftyTwoWkHigh;

  @ColumnInfo(name = "fifty_two_wk_low")
  private Double fiftyTwoWkLow;

  @ColumnInfo(name = "price")
  private Double price;

  @ColumnInfo(name = "company")
  private String company;

  @ColumnInfo(name = "nasdaq_name")
  private String nasdaqName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPlotPath() {
    return plotPath;
  }

  public void setPlotPath(String plotPath) {
    this.plotPath = plotPath;
  }

  public Double getFiftyTwoWkHigh() {
    return fiftyTwoWkHigh;
  }

  public void setFiftyTwoWkHigh(Double fiftyTwoWkHigh) {
    this.fiftyTwoWkHigh = fiftyTwoWkHigh;
  }

  public Double getFiftyTwoWkLow() {
    return fiftyTwoWkLow;
  }

  public void setFiftyTwoWkLow(Double fiftyTwoWkLow) {
    this.fiftyTwoWkLow = fiftyTwoWkLow;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getNasdaqName() {
    return nasdaqName;
  }

  public void setNasdaqName(String nasdaqName) {
    this.nasdaqName = nasdaqName;
  }
}