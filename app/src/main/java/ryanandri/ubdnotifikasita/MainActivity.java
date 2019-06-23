package ryanandri.ubdnotifikasita;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ryanandri.ubdnotifikasita.adapter.ViewPagerAdapter;
import ryanandri.ubdnotifikasita.fragments.JadwalFragment;
import ryanandri.ubdnotifikasita.fragments.JudulFragment;
import ryanandri.ubdnotifikasita.fragments.ProfileFragment;
import ryanandri.ubdnotifikasita.session.SessionConfig;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigation;

    MenuItem menuItem;
    ProfileFragment profileFragment;
    JudulFragment judulFragment;
    JadwalFragment jadwalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // buat channel untuk oreo ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id_channel = getString(R.string.id_channel);
            String nama_channel = getString(R.string.nama_channel);
            String desc_channel = getString(R.string.deskripsi_channel);

            NotificationChannel channel = new NotificationChannel(id_channel, nama_channel, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(desc_channel);
            NotificationManager notificationMgr = getSystemService(NotificationManager.class);
            notificationMgr.createNotificationChannel(channel);
        }

        SessionConfig sessionConfig = SessionConfig.getInstance(getApplicationContext());
        if (!sessionConfig.IsLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

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
                            case R.id.navigation_judul:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_jadwal:
                                viewPager.setCurrentItem(2);
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
        profileFragment = new ProfileFragment();
        judulFragment = new JudulFragment();
        jadwalFragment = new JadwalFragment();
        viewPagerAdapter.addFragment(profileFragment);
        viewPagerAdapter.addFragment(judulFragment);
        viewPagerAdapter.addFragment(jadwalFragment);
        viewPager.setAdapter(viewPagerAdapter);

        // cegah fragment dari reload untuk mengurangi lag.
        // Nilai di ambil dari jumlah menu.
        viewPager.setOffscreenPageLimit(3);
    }
}
