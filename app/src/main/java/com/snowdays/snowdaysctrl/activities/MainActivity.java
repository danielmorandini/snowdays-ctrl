package com.snowdays.snowdaysctrl.activities;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.snowdays.snowdaysctrl.fragments.MainFragment;
import com.snowdays.snowdaysctrl.models.MainCardModel;
import com.snowdays.snowdaysctrl.R;

public class MainActivity extends AppCompatActivity {

    private MainCardModel[] firstDay = new MainCardModel[]{new MainCardModel("Check IN")};
    private MainCardModel[] secondDay = new MainCardModel[]{new MainCardModel("Bus departure"), new MainCardModel("Meal")};
    private MainCardModel[] thirdDay = new MainCardModel[]{new MainCardModel("Breakfast")};
    private MainCardModel[] utilities = new MainCardModel[]{new MainCardModel("Util1")};

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

