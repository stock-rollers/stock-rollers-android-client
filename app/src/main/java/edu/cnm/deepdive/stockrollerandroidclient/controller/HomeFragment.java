package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.service.StockRollersDatabase;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter.OnClickListener;
import edu.cnm.deepdive.stockrollerandroidclient.view.StockRecyclerAdapter.OnContextListener;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HomeFragment extends Fragment  implements OnContextListener, OnClickListener {

  private MainViewModel viewModel;
  private RecyclerView recyclerView;
  private Fragment graphFragment;
  private StockRollersDatabase database = StockRollersDatabase.getInstance();
  private final Lock lock = new ReentrantLock();
  private ArrayList<Stock> stocks = new ArrayList<>();
  private Stock stockEntity;
  private Stock stock;
  private StockRecyclerAdapter adapter;
  private FragmentManager fragmentManager;
  private StockFragment stockFragment;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    recyclerView = view.findViewById(R.id.stock_list);
    graphFragment = new HistoryGraphFragment();
    viewModel.getStocks().observe(this, (stocks) -> {
      StockRecyclerAdapter newAdapter = new StockRecyclerAdapter(stocks, this, this);
      recyclerView.setAdapter(newAdapter);
      recyclerView.getAdapter().notifyDataSetChanged();
    });

    ImageView fab = view.findViewById(R.id.fab);
    fab.setOnClickListener((v) -> {
      viewModel.getHistory();

    });
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

  @Override
  public void onClick(View view, int position, Stock stock) {
    viewModel.setStock(stock);
    FragmentManager manager = getFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.replace(R.id.fragment_container, stockFragment);
    transaction.commit();

  }

  @Override
  public void onLongPress(Menu menu, int position, Stock stock) {
    MenuInflater inflater = getActivity().getMenuInflater();
    inflater.inflate(R.menu.stock_context, menu);
    menu.findItem(R.id.history).setOnMenuItemClickListener((item) -> {
      viewModel.setStock(stock);
      FragmentManager manager = getFragmentManager();
      FragmentTransaction transaction = manager.beginTransaction();
      transaction.replace(R.id.fragment_container, graphFragment);
      transaction.commit();
      return true;
    });
    menu.findItem(R.id.update).setOnMenuItemClickListener((item) -> {
      Toast.makeText(getActivity(), "Click", Toast.LENGTH_LONG);
      return true;
    });

    Toast.makeText(getActivity(), "Long Press", Toast.LENGTH_LONG).show();
  }
}
