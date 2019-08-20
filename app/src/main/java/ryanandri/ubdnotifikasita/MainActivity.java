package ryanandri.ubdnotifikasita;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import ryanandri.ubdnotifikasita.adapter.ViewPagerAdapter;
import ryanandri.ubdnotifikasita.fragments.JadwalFragment;
import ryanandri.ubdnotifikasita.fragments.NilaiFragment;
import ryanandri.ubdnotifikasita.fragments.NotifikasiFragment;
import ryanandri.ubdnotifikasita.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigation;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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
                }
        );

        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

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
                    public void onPageScrollStateChanged(int state) { }
                }
        );

        loadviewpager();
    }

    public void loadviewpager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        ProfileFragment profileFragment = new ProfileFragment();
        JadwalFragment jadwalFragment = new JadwalFragment();
        NilaiFragment nilaiFragment = new NilaiFragment();
        NotifikasiFragment notifikasiFragment = new NotifikasiFragment();

        viewPagerAdapter.addFragment(profileFragment); // 0
        viewPagerAdapter.addFragment(jadwalFragment);  // 1
        viewPagerAdapter.addFragment(nilaiFragment); // 2
        viewPagerAdapter.addFragment(notifikasiFragment); // 3
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(viewPagerAdapter);
    }
}
