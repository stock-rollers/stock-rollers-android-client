package edu.cnm.deepdive.stockrollerandroidclient.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.cnm.deepdive.stockrollerandroidclient.R;
import edu.cnm.deepdive.stockrollerandroidclient.service.GoogleSignInService;

public class MainActivity extends AppCompatActivity {

  private TextView mTextMessage;
  private FragmentManager manager = getSupportFragmentManager();
  private HomeFragment homeFragment;
  private SearchFragment searchFragment;
  private ProfileFragment profileFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setUpUI();
  }

  private void addFragment(Fragment fragment, boolean useStack) {
    String tag = fragment.getClass().getSimpleName();
    if (manager.findFragmentByTag(tag) != null) {
      manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.add(R.id.fragment_container, fragment, tag);
    if (useStack) {
      transaction.addToBackStack(tag);
    }
    transaction.commit();
  }

  private void replaceFragment(Fragment fragment, boolean useStack) {
    String tag = fragment.getClass().getSimpleName();
    if (manager.findFragmentByTag(tag) != null) {
      manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.replace(R.id.fragment_container, fragment, tag);
    if (useStack) {
      transaction.addToBackStack(tag);
    }
    transaction.commit();
  }


  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  private void setUpUI() {
    setContentView(R.layout.activity_main);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    homeFragment = new HomeFragment();
    addFragment(homeFragment, true);
  }


  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.sign_out:
        signOut();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          homeFragment = new HomeFragment();
          replaceFragment(homeFragment, false);
          return true;
        case R.id.navigation_dashboard:
          searchFragment = new SearchFragment();
          replaceFragment(searchFragment, false);
          return true;
        case R.id.navigation_notifications:
          profileFragment = new ProfileFragment();
          replaceFragment(profileFragment, false);
          return true;
      }
      return false;
    }
  };


  private void signOut() {
    GoogleSignInService.getInstance().signOut()
        .addOnCompleteListener((task) -> {
          Intent intent = new Intent(this, LoginActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
        });
  }
}
