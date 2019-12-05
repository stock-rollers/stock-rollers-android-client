package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;

public class StockFragment extends Fragment {

  private MainViewModel viewModel;
  private StockRollersDatabase database;


  private View view;
  private ProgressBar progressBar;
  private FloatingActionButton fab;
  private TextView name;
  private TextView price;
  private TextView companyName;
  private TextView yearHigh;
  private TextView yearLow;
  private boolean showButton;
  private ImageView imageView;

  public StockFragment (boolean showButton) {
    this.showButton = showButton;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    database = StockRollersDatabase.getInstance();
    view = inflater.inflate(R.layout.individual_stock, container, false);
    progressBar = view.findViewById(R.id.waiting_stock);
    progressBar.setVisibility(View.VISIBLE);
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    name = view.findViewById(R.id.stock_name);
    price = view.findViewById(R.id.stock_price);
    companyName = view.findViewById(R.id.company_name);
    yearHigh = view.findViewById(R.id.year_high);
    yearLow = view.findViewById(R.id.year_low);
    imageView = view.findViewById(R.id.graph_button);
    fab = view.findViewById(R.id.add);
    if (!showButton) {
      fab.hide();
    }

    OnClickListener listener2 = new OnClickListener() {
      @Override
      public void onClick(View view) {
        Fragment historyFragment = new HistoryGraphFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, historyFragment);
        transaction.commit();
      }
    };

    imageView.setOnClickListener(listener2);
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
      if (stock != null) {
        fab.setOnClickListener(listener);
        name.setText(" (" + stock.getNasdaqName() + ")");
        price.setText("Price: $ " + stock.getPrice().toString());
        companyName.setText(stock.getCompany());
        yearHigh.setText("Year High: " + stock.getFiftyTwoWkHigh().toString());
        yearLow.setText("Year Low: " + stock.getFiftyTwoWkLow().toString());
        progressBar.setVisibility(View.GONE);
      }
    });
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

}
