package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;

public class StockFragment extends Fragment {

  private MainViewModel viewModel;
  private LinearLayout linearLayout;
  private StockRollersDatabase database;
  private TextView itemView;

  private View view;
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
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    View view = inflater.inflate(R.layout.individual_stock, container, false);

    return view;
  }

  private void Stockholder() {
    name = itemView.findViewById(R.id.stock_name);
    price = itemView.findViewById(R.id.stock_price);
    companyName = itemView.findViewById(R.id.company_name);
    yearHigh = itemView.findViewById(R.id.year_high);
    yearLow = itemView.findViewById(R.id.year_low);
    view = itemView;

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

}
