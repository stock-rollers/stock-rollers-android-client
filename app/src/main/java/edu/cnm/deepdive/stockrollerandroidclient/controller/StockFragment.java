package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;

public class StockFragment extends Fragment {

  private MainViewModel viewModel;
  private StockRollersDatabase database;


  private View view;
  private FloatingActionButton fab;
  private TextView name;
  private TextView price;
  private TextView companyName;
  private TextView yearHigh;
  private TextView yearLow;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    database = StockRollersDatabase.getInstance();
    view = inflater.inflate(R.layout.individual_stock, container, false);
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    name = view.findViewById(R.id.stock_name);
    price = view.findViewById(R.id.stock_price);
    companyName = view.findViewById(R.id.company_name);
    yearHigh = view.findViewById(R.id.year_high);
    yearLow = view.findViewById(R.id.year_low);
    fab = view.findViewById(R.id.add);

    viewModel.getStock().observe(this, (stock) -> {
      OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
          new Thread(() -> {
            database.getStockDao().insert(stock);
            viewModel.getHistory(database.getStockDao().getByName(stock.getNasdaqName()).get());
          }).start();
        }
      };
      fab.setOnClickListener(listener);
      name.setText(" (" + stock.getNasdaqName() + ")");
      price.setText("Price: $ " + stock.getPrice().toString());
      companyName.setText(stock.getCompany());
      yearHigh.setText("Year High: " + stock.getFiftyTwoWkHigh().toString());
      yearLow.setText("Year Low: " + stock.getFiftyTwoWkLow().toString());
    });
    return view;
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

}