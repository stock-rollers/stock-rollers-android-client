package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.view.ProfileRecyclerAdapter;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;

public class ProfileFragment extends Fragment {

  private MainViewModel viewModel;
  private RecyclerView recyclerView;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    recyclerView = view.findViewById(R.id.following);

    ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter();

    recyclerView.setAdapter(adapter);

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void observeViewModel(MainViewModel viewModel) {
    viewModel.getStock().observe(this, new Observer<Stock>() {
      @Override
      public void onChanged(Stock stock) {
        //adapter.updateSkiResorts(skiResorts);
      }
    });
  }
}
