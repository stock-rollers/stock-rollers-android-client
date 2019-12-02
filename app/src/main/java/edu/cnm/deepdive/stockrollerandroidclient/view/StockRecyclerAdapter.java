package edu.cnm.deepdive.stockrollerandroidclient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockrollersService;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter.StockHolder;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockHolder>{

  private List<Stock> stocks;
  private Stock stockEntity;

  public StockRecyclerAdapter(List<Stock> stocks) {
    this.stocks = new LinkedList<>(stocks);
  }

  public static StockRecyclerAdapter getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public void addStockToView(Stock stock) {
    stocks.add(stocks.size(), stock);
    notifyItemChanged(stocks.size());
  }

  public void updateStocks(List<Stock> stocks) {
    this.stocks.clear();
    this.stocks.addAll(stocks);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stocks, parent, false);
    return new StockHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull StockHolder holder, int position) {
    Stock stock = stocks.get(position);
    holder.bind(position, stock);
  }

  @Override
  public int getItemCount() {
    return stocks.size();
  }

  public class StockHolder extends RecyclerView.ViewHolder {

    private final View view;
    public TextView name;
    private TextView price;
    public TextView companyName;

    public StockHolder(@NonNull View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.stock_name);
      price = itemView.findViewById(R.id.stock_price);
      companyName = itemView.findViewById(R.id.company_name);
      view = itemView;
    }

    private void bind(int position, Stock stock) {
      name.setText(stock.getNasdaqName() + ",");
      price.setText(stock.getPrice().toString() + " $");
      companyName.setText(stock.getCompany());
    }
  }

  private static class InstanceHolder {

    private static final StockRecyclerAdapter INSTANCE = new StockRecyclerAdapter(Collections.emptyList());
  }
}
