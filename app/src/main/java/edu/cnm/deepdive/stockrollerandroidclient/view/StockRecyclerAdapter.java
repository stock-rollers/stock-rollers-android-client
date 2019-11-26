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
import java.util.List;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockHolder>{

  private ArrayList<Stock> stocks = new ArrayList<>();
  private final Context context;
  private final StockrollersService stock;
  private Stock stockEntity;

  public StockRecyclerAdapter(Context context, StockrollersService stock, Stock entityStock) {
    this.context = context;
    this.stock = stock;
    this.stockEntity = stockEntity;
  }

  public void updateStocks(List<Stock> stocks) {
    this.stocks.clear();
    this.stocks.addAll(stocks);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.list_stocks, parent, false);
    return new StockHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull StockHolder holder, int position) {
  TextView name = (TextView) holder.textView.findViewById(R.id.stock_name);
  name.setText(stockEntity.getNasdaqName());

  TextView price = (TextView) holder.textView.findViewById(R.id.stock_price);
  price.setText(stockEntity.getPrice().intValue());
  }

  @Override
  public int getItemCount() {
    return stocks.size();
  }

  public class StockHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    private StockHolder(@NonNull View textView) {
      super(textView);
      this.textView = (TextView) textView;
    }
  }
}
