package edu.cnm.deepdive.stockrollerandroidclient.view;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter.StockHolder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockHolder>{

  private List<Stock> stocks;
  private Stock stockEntity;
  private OnClickListener clickListener;
  private OnContextListener contextListener;

  public StockRecyclerAdapter(List<Stock> stocks,  OnClickListener clickListener, OnContextListener contextListener) {
    this.clickListener = clickListener;
    this.contextListener = contextListener;
    this.stocks = new LinkedList<>(stocks);
  }

  public void addStockToView(Stock stock) {
    stocks.add(stocks.size(), stock);
    notifyItemChanged(stocks.size());
  }



  public void updateStocks(List<Stock> stocks) {
    this.stocks.clear();
    this.stocks = stocks;
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

  @FunctionalInterface
  public interface OnClickListener {

    void onClick(View view, int position, Stock stock);

  }

  @FunctionalInterface
  public interface OnContextListener {

    void onLongPress(Menu menu, int position, Stock stock);

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
      if (clickListener != null) {
        view.setOnClickListener((v) -> clickListener.onClick(v, position, stock));
      }
      if (contextListener != null) {
        view.setOnCreateContextMenuListener((menu, v, menuInfo) ->
            contextListener.onLongPress(menu, position, stock));
      }
    }
  }
}
