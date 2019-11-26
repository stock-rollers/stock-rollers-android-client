package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockrollersService;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

  private MainViewModel viewModel;
  private RecyclerView recyclerView;
  private ArrayList<Stock> stocks = new ArrayList<>();
  private StockRecyclerAdapter adapter;
  private Stock stockEntity;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    FloatingActionButton fab = view.findViewById(R.id.fab);
    fab.setOnClickListener((v) -> {

    });

    viewModel.getStocks().observe(this, stock -> {
      stocks.addAll(stocks);
      recyclerView = (RecyclerView) view.findViewById(R.id.stock_list);
      StockRecyclerAdapter adapter = new StockRecyclerAdapter(getContext(), StockrollersService.getInstance(), stockEntity );
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    });
    observeViewModel(viewModel);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void observeViewModel(MainViewModel viewModel) {
    viewModel.getStocks().observe(this, new Observer<Stock>() {
      @Override
      public void onChanged(Stock stock) {
        //adapter.updateSkiResorts(skiResorts);
      }
    });
  }
}
