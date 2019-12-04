package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.model.entity.Stock;
import edu.cnm.deepdive.stockrollerandroidclient.view.FollowersRecyclerAdapter;
import edu.cnm.deepdive.stockrollerandroidclient.view.FollowingRecyclerAdapter;
import edu.cnm.deepdive.stockrollerandroidclient.viewmodel.MainViewModel;

public class ProfileFragment extends Fragment {

  private MainViewModel viewModel;
  private RecyclerView recyclerView;
  private TextView followers;
  private TextView following;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    View view = inflater.inflate(R.layout.fragment_profile, container, false);
    recyclerView = view.findViewById(R.id.follow);

    FollowersRecyclerAdapter adapter = new FollowersRecyclerAdapter();

    recyclerView.setAdapter(adapter);

   followers = view.findViewById(R.id.followers);
    following = view.findViewById(R.id.following);

    followers.setBackgroundResource(R.color.lightAccent);

    OnClickListener listener = new OnClickListener() {
      @Override
      public void onClick(View view) {
       recyclerView.setAdapter(adapter);
       followers.setBackgroundResource(R.color.lightAccent);
       following.setBackgroundColor(Color.TRANSPARENT);
       view.invalidate();
      }
    };
    OnClickListener listener1 = new OnClickListener() {
      @Override
      public void onClick(View view) {
        FollowingRecyclerAdapter adapter1 = new FollowingRecyclerAdapter();
        recyclerView.setAdapter(adapter1);
        following.setBackgroundResource(R.color.lightAccent);
        followers.setBackgroundColor(Color.TRANSPARENT);
        view.invalidate();
      }
    };

    followers.setOnClickListener(listener);
    following.setOnClickListener(listener1);

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
