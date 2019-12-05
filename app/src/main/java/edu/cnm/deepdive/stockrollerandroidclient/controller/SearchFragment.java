package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;
import java.util.List;

public class SearchFragment extends Fragment {

  private MainViewModel viewModel;
  private SearchView stockSearch;
  private StockRollersDatabase database;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    database = StockRollersDatabase.getInstance();
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    setUpSearch(view);
    return view;
  }

  private void setUpSearch(View view) {
    stockSearch = view.findViewById(R.id.search_stock);
    stockSearch.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
        stockSearch.clearFocus();
        viewModel.setStock(null);
        s = s.toUpperCase();
        viewModel.getStock(s);
        Fragment fragment = new StockFragment(true);
        FragmentManager manager = getFragmentManager();
        viewModel.getStock().observe(SearchFragment.this, (stock) -> {
          FragmentTransaction transaction = manager.beginTransaction();
          transaction.replace(R.id.fragment_container, fragment);
          transaction.commit();
        });
        return false;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        return false;
      }
    });
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
