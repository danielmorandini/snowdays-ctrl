package com.snowdays.snowdaysctrl.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.snowdays.snowdaysctrl.fragments.MainFragment;
import com.snowdays.snowdaysctrl.models.MainCard;
import com.snowdays.snowdaysctrl.R;
import com.snowdays.snowdaysctrl.utilities.KeyStore;

public class MainActivity extends AppCompatActivity {

    private MainCard[] firstDay = new MainCard[]{new MainCard("Check IN")};
    private MainCard[] secondDay = new MainCard[]{new MainCard("Bus departure"), new MainCard("Meal"), new MainCard("Bus departure"), new MainCard("Meal"), new MainCard("Bus departure"), new MainCard("Meal"), new MainCard("Bus departure"), new MainCard("Meal"), new MainCard("Bus departure"), new MainCard("Meal")};
    private MainCard[] thirdDay = new MainCard[]{new MainCard("Breakfast")};
    private MainCard[] utilities = new MainCard[]{new MainCard("Util1")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        setFragmentWithTabId(R.id.tab_first_day);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        setFragmentWithTabId(item.getItemId());
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // Remove saved token and userId
                KeyStore.clearAll(this);

                // Return to the base activity
                Intent intent = new Intent(this, BaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setFragmentWithTabId(int tabId) {
        MainFragment fragment = new MainFragment();

        switch (tabId) {
            case R.id.tab_first_day:
                fragment.setDatasource(firstDay);
                break;
            case R.id.tab_second_day:
                fragment.setDatasource(secondDay);
                break;
            case R.id.tab_third_day:
                fragment.setDatasource(thirdDay);
                break;
            default:
                fragment.setDatasource(utilities);
        }

        //set current fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }
}

