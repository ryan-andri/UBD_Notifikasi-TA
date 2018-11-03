package ryanandri.ubdnotifikasita;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import ryanandri.ubdnotifikasita.fragments.DosenFragment;
import ryanandri.ubdnotifikasita.fragments.HomeFragment;
import ryanandri.ubdnotifikasita.fragments.JadwalFragment;
import ryanandri.ubdnotifikasita.fragments.NotifikasiFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigation;

    MenuItem menuItem;
    HomeFragment homeFragment;
    DosenFragment dosenFragment;
    JadwalFragment jadwalFragment;
    NotifikasiFragment notifikasiFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_dosen:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_jadwal:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.navigation_notifikasi:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return false;
                    }
                }
        );

        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        //
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (menuItem != null) {
                            menuItem.setChecked(false);
                        } else {
                            navigation.getMenu().getItem(0).setChecked(false);
                        }
                        navigation.getMenu().getItem(position).setChecked(true);
                        menuItem = navigation.getMenu().getItem(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        //
                    }
                }
        );

        loadviewpager(viewPager);
    }

    public void loadviewpager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        dosenFragment = new DosenFragment();
        jadwalFragment = new JadwalFragment();
        notifikasiFragment =  new NotifikasiFragment();
        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(dosenFragment);
        viewPagerAdapter.addFragment(jadwalFragment);
        viewPagerAdapter.addFragment(notifikasiFragment);
        viewPager.setAdapter(viewPagerAdapter);
    }
}
