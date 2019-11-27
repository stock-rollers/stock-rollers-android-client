package edu.cnm.deepdive.stockrollerandroidclient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockrollersService;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter.StockHolder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockHolder>{

  private List<Stock> stocks;
  private Stock stockEntity;

  public StockRecyclerAdapter(List<Stock> stocks) {
    this.stocks = new LinkedList<>(stocks);
  }

  public void addStockToView(Stock stock) {
    stocks.add(0, stock);
    notifyItemChanged(0);
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

    public StockHolder(@NonNull View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.stock_name);
      price = itemView.findViewById(R.id.stock_price);
      view = itemView;
    }

    private void bind(int position, Stock stock) {
      name.setText(stock.getNasdaqName());
//      price.setText(stock.getPrice().toString());
    }
  }
}
