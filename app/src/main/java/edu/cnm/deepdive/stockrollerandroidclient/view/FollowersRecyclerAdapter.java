package edu.cnm.deepdive.stockrollerandroidclient.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.view.FollowersRecyclerAdapter.ProfileHolder;
import java.util.ArrayList;
import java.util.List;


public class FollowersRecyclerAdapter extends RecyclerView.Adapter<ProfileHolder> {

  private List<String> names;
  private List<String> followers;

    public FollowersRecyclerAdapter() {
    this.names = new ArrayList<String>();
      names.add("Evan Baxter");
      names.add("Michael Scott");
      names.add("James Halpert");
      names.add("Jim Carrey");
      names.add("Bill Gates");
      names.add("Dwight Shrute");
      names.add("Spongebob");
      names.add("Nick Bennett");
      names.add("Elvis ?:");
      names.add("Warren Buffet");
      this.followers = new ArrayList<String>();
      followers.add("39");
      followers.add("98");
      followers.add("268");
      followers.add("1,022");
      followers.add("1,206,614");
      followers.add("-23");
      followers.add("3442");
      followers.add("869,427");
      followers.add("1,337");
      followers.add("1,477,608");
    }

  @NonNull
  @Override
  public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_profile, parent, false);
    return new ProfileHolder(view);
  }


  @Override
  public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
   String name = names.get(position);
    holder.bind(position, name, followers.get(position));
  }

  @Override
  public int getItemCount() {
    return names.size();
  }

  public class ProfileHolder extends RecyclerView.ViewHolder {

    private final View view;
    public TextView name;
    private TextView number;

    public ProfileHolder(@NonNull View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.name);
      number = itemView.findViewById(R.id.number_of_followers);
      view = itemView;
    }

    private void bind(int position, String name1, String follower) {
      name.setText(name1);
      number.setText("followers " + follower);
//      if (clickListener != null) {
//        view.setOnClickListener((v) -> clickListener.onClick(v, position, stock));
//      }
//      if (contextListener != null) {
//        view.setOnCreateContextMenuListener((menu, v, menuInfo) ->
//            contextListener.onLongPress(menu, position, stock));
//      }
    }
  }
}
