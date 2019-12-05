package edu.cnm.deepdive.stockrollerandroidclient.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.view.FollowersRecyclerAdapter.FollowersHolder;
import edu.cnm.deepdive.stockrollerandroidclient.view.FollowingRecyclerAdapter.FollowingHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple RecyclerAdapter to display who the User is Following
 */
public class FollowingRecyclerAdapter extends RecyclerView.Adapter<FollowingHolder> {

  private List<String> names;
  private List<String> following;

  /**
   * In the future you will specify your own list to show
   */
  public FollowingRecyclerAdapter() {
    this.names = new ArrayList<String>();
    names.add("Elon Musk");
    names.add("Steve Wozniak");
    names.add("James Halpert");
    names.add("Bill Gates");
    names.add("Morgan Freeman");
    names.add("Warren Buffet");
    names.add("Oprah Winfrey");
    names.add("Spongebob");
    names.add("Nick Bennett");
    names.add("Deep Dive Coding");
    names.add("Squidward");
    names.add("Billy Joel");
    names.add("Bob Dylan");

    this.following = new ArrayList<String>();
    following.add("39,884,687");
    following.add("98");
    following.add("268");
    following.add("1,022");
    following.add("206,614");
    following.add("83");
    following.add("36,942");
    following.add("86,967,487");
    following.add("17");
    following.add("1,477,608");
    following.add("128,623");
    following.add("1,265");
    following.add("74,608");
  }

  /**
   * Defualt onCreateViewHolder that inflates the list_profile xml file
   * @param parent of the adapter
   * @param viewType for the adapter
   * @return the FollowerHolder view
   */
  @NonNull
  @Override
  public FollowingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_profile, parent, false);
    return new FollowingHolder(view);
  }

  /**
   * Binds each list item to a follower and displays that followers information
   * @param holder of the Adapter
   * @param position current position  in the list of elements
   */
  @Override
  public void onBindViewHolder(@NonNull FollowingHolder holder,
      int position) {
    if(position % 2 == 0) {
      holder.itemView.setBackgroundResource(R.color.lightAccent);
    } else {
      holder.itemView.setBackgroundResource(R.color.colorAccent);
    }
    holder.bind(position, names.get(position), following.get(position));
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
  public class FollowingHolder extends RecyclerView.ViewHolder {

    private final View view;
    public TextView name;
    private TextView number;

    /**
     * Connects the TextViews to xml items
     * @param itemView of the adapter
     */
    public FollowingHolder(@NonNull View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.name);
      number = itemView.findViewById(R.id.number_of_followers);
      view = itemView;
    }

    private void bind(int position, String name1, String following) {
      name.setText(name1);
      number.setText(following);
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
