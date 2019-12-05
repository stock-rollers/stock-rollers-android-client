package edu.cnm.deepdive.stockrollerandroidclient.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.controller.ProfileFragment;
import edu.cnm.deepdive.stockrollerandroidclient.view.FollowersRecyclerAdapter.FollowersHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple RecyclerAdapter to display Followers for the User
 */
public class FollowersRecyclerAdapter extends RecyclerView.Adapter<FollowersHolder> {

  private List<String> names;
  private List<String> followers;

  /**
   * In the future you will specify your own list to show
   */
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
      followers.add("3,442");
      followers.add("1,206,614");
      followers.add("98");
      followers.add("1,022");
      followers.add("-234");
      followers.add("869,427");
      followers.add("1,337");
      followers.add("1,477,608");
      followers.add("268");
    }

  /**
   * Defualt onCreateViewHolder that inflates the list_profile xml file
   * @param parent of the adapter
   * @param viewType for the adapter
   * @return the FollowerHolder view
   */
  @NonNull
  @Override
  public FollowersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_profile, parent, false);
    return new FollowersHolder(view);
  }

  /**
   * Binds each list item to a follower and displays that followers information
   * @param holder of the Adapter
   * @param position current position  in the list of elements
   */
  @Override
  public void onBindViewHolder(@NonNull FollowersHolder holder, int position) {
   String name = names.get(position);
   if(position % 2 == 0) {
     holder.itemView.setBackgroundResource(R.color.lightAccent);
   } else {
     holder.itemView.setBackgroundResource(R.color.colorAccent);
   }
    holder.bind(position, name, followers.get(position));
  }

  /**
   * @return size of the list the adapter is working with
   */
  @Override
  public int getItemCount() {
    return names.size();
  }

  /**
   * Simple RecyclerView.ViewHolder for our adapter class
   */
  public class FollowersHolder extends RecyclerView.ViewHolder {

    private final View view;
    public TextView name;
    private TextView number;

    /**
     * Connects the TextViews to xml items
     * @param itemView of the adapter
     */
    public FollowersHolder(@NonNull View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.name);
      number = itemView.findViewById(R.id.number_of_followers);
      view = itemView;
    }

    private void bind(int position, String name1, String follower) {
      name.setText(name1);
      number.setText(follower);
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
