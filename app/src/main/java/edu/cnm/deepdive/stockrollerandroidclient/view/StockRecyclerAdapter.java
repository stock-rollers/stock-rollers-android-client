package edu.cnm.deepdive.stockrollerandroidclient.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter.StockHolder;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple RecyclerAdapter to display who the User is Following
 */
public class StockRecyclerAdapter extends RecyclerView.Adapter<StockHolder>{

  private List<Stock> stocks;
  private Stock stockEntity;
  private OnClickListener clickListener;
  private OnContextListener contextListener;

  /**
   * Sets the Click Listener, the Context Listener, and the List of stocks for the adapter
   * @param stocks list that is to be displayed
   * @param clickListener for each item
   * @param contextListener for each item on long press
   */
  public StockRecyclerAdapter(List<Stock> stocks,  OnClickListener clickListener, OnContextListener contextListener) {
    this.clickListener = clickListener;
    this.contextListener = contextListener;
    this.stocks = new LinkedList<>(stocks);
  }
//
//  public void addStockToView(Stock stock) {
//    stocks.add(stocks.size(), stock);
//    notifyItemChanged(stocks.size());
//  }


//
//  public void updateStocks(List<Stock> stocks) {
//    this.stocks.clear();
//    this.stocks = stocks;
//  }

  /**
   * Defualt onCreateViewHolder that inflates the list_profile xml file
   * @param parent of the adapter
   * @param viewType for the adapter
   * @return the FollowerHolder view
   */
  @NonNull
  @Override
  public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stocks, parent, false);
    return new StockHolder(view);
  }

  /**
   * Binds each list item to a follower and displays that followers information
   * @param holder of the Adapter
   * @param position current position  in the list of elements
   */
  @Override
  public void onBindViewHolder(@NonNull StockHolder holder, int position) {
    Stock stock = stocks.get(position);
    if(position % 2 == 0) {
      holder.itemView.setBackgroundResource(R.color.mediumDark);
    }
    holder.bind(position, stock);
  }

  /**
   * @return size of the list the adapter is working with
   */
  @Override
  public int getItemCount() {
    return stocks.size();
  }

  /**
   * Fuctional interface to setup a click listener for each item
   */
  @FunctionalInterface
  public interface OnClickListener {

    void onClick(View view, int position, Stock stock);

  }

  /**
   * Functional interface to setup a long click listener for each item
   */
  @FunctionalInterface
  public interface OnContextListener {

    void onLongPress(Menu menu, int position, Stock stock);

  }


  /**
   * Simple RecyclerView.ViewHolder for our adapter class
   */
  public class StockHolder extends RecyclerView.ViewHolder {

    private final View view;
    public TextView name;
    private TextView price;
    public TextView companyName;

    /**
     * Connects the TextViews to xml items
     * @param itemView of the adapter
     */
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
        view.setOnClickListener((v) -> {
          view.clearFocus();
          clickListener.onClick(v, position, stock);
        });
      }
      if (contextListener != null) {
        view.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
          view.clearFocus();
          contextListener.onLongPress(menu, position, stock);
        });
      }
    }
  }
}
