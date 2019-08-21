package ryanandri.ubdnotifikasita;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ryanandri.ubdnotifikasita.fragments.JadwalFragment;
import ryanandri.ubdnotifikasita.fragments.NilaiFragment;
import ryanandri.ubdnotifikasita.fragments.NotifikasiFragment;
import ryanandri.ubdnotifikasita.fragments.BerandaFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigation;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // buat channel untuk oreo ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String id_channel = getString(R.string.id_channel);
            String nama_channel = getString(R.string.nama_channel);
            String desc_channel = getString(R.string.deskripsi_channel);

            long[] vibrate = {0, 600};
            Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.voila);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            NotificationChannel channel = new NotificationChannel(id_channel,
                    nama_channel, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(desc_channel);
            channel.setSound(uri, audioAttributes);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(vibrate);

            NotificationManager notificationMgr = getSystemService(NotificationManager.class);
            if (notificationMgr != null) {
                notificationMgr.createNotificationChannel(channel);
            }
        }

        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        viewPager = findViewById(R.id.viewpager);

        ViewPagerAdapter viewPagerAdapter =
                new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new BerandaFragment()); // 0
        viewPagerAdapter.addFragment(new JadwalFragment());  // 1
        viewPagerAdapter.addFragment(new NilaiFragment()); // 2
        viewPagerAdapter.addFragment(new NotifikasiFragment()); // 3
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(viewPagerAdapter);

        navigation.setOnNavigationItemSelectedListener(bottomNavListener);
        viewPager.addOnPageChangeListener(viewPagerListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.navigation_jadwal:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.navigation_nilai:
                            viewPager.setCurrentItem(2);
                            break;
                        case R.id.navigation_notifikasi:
                            viewPager.setCurrentItem(3);
                            break;
                    }
                    return false;
                }
    };

    private ViewPager.OnPageChangeListener viewPagerListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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

                }
    };

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}
