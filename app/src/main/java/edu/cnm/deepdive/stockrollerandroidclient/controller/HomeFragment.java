package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HomeFragment extends Fragment {

  private MainViewModel viewModel;
  private RecyclerView recyclerView;
  private StockRollersDatabase database = StockRollersDatabase.getInstance();
  private final Lock lock = new ReentrantLock();
  private ArrayList<Stock> stocks = new ArrayList<>();
  private Stock stockEntity;
  private Stock stock;
  private StockRecyclerAdapter adapter = StockRecyclerAdapter.getInstance();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    recyclerView = view.findViewById(R.id.stock_list);
    viewModel.getStocks().observe(this, (stocks) -> {
      StockRecyclerAdapter newAdapter = new StockRecyclerAdapter(stocks);
      recyclerView.setAdapter(newAdapter);
      recyclerView.getAdapter().notifyDataSetChanged();
    });

//    FloatingActionButton fab = view.findViewById(R.id.fab);
//    fab.setOnClickListener((v) -> {
//
//    });
//    observeViewModel(viewModel);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void observeViewModel(MainViewModel viewModel) {
//    viewModel.getStocks().observe(this, new Observer<List<Stock>>() {
//      @Override
//      public void onChanged(List<Stock> stocks) {
//        adapter.updateStocks(stocks);
//      }
//    });
  }
}
