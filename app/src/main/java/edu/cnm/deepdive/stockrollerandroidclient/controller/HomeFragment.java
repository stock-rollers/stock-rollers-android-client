package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter.OnClickListener;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter.OnContextListener;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;

public class HomeFragment extends Fragment  implements OnContextListener, OnClickListener {

  private MainViewModel viewModel;
  private RecyclerView recyclerView;
  private Fragment graphFragment;
  private StockFragment stockFragment;
  private ProgressBar progressBar;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    recyclerView = view.findViewById(R.id.stock_list);
    graphFragment = new HistoryGraphFragment();
    progressBar = view.findViewById(R.id.waiting_home);
    progressBar.setVisibility(View.VISIBLE);
    viewModel.getStocks().observe(this, (stocks) -> {
      StockRecyclerAdapter newAdapter = new StockRecyclerAdapter(stocks, this, this);
      recyclerView.setAdapter(newAdapter);
      recyclerView.getAdapter().notifyDataSetChanged();
      progressBar.setVisibility(View.GONE);
    });

    ImageView fab = view.findViewById(R.id.fab);
    fab.setOnClickListener((v) -> {
      viewModel.setStock(null);
      viewModel.getRandom();
      stockFragment = new StockFragment(true);
      FragmentManager manager = getFragmentManager();
      FragmentTransaction transaction = manager.beginTransaction();
      transaction.replace(R.id.fragment_container, stockFragment);
      transaction.commit();
    });
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onClick(View view, int position, Stock stock) {
    viewModel.setStock(stock);
    stockFragment = new StockFragment(false);
    FragmentManager manager = getFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.replace(R.id.fragment_container, stockFragment);
    transaction.commit();
    Toast.makeText(getActivity(), "Click", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onLongPress(Menu menu, int position, Stock stock) {
    MenuInflater inflater = getActivity().getMenuInflater();
    inflater.inflate(R.menu.stock_context, menu);
    menu.findItem(R.id.history).setOnMenuItemClickListener((item) -> {
      viewModel.setStock(stock);
      Fragment historyFragment = new HistoryGraphFragment();
      FragmentManager manager = getFragmentManager();
      FragmentTransaction transaction = manager.beginTransaction();
      transaction.replace(R.id.fragment_container, historyFragment);
      transaction.commit();
      return false;
    });
    menu.findItem(R.id.delete).setOnMenuItemClickListener((item) -> {
      viewModel.deleteStock(stock);
      return false;
    });
    menu.findItem(R.id.update).setOnMenuItemClickListener((item) -> {
      viewModel.updateStock(stock);
      return false;
    });
  }
}
