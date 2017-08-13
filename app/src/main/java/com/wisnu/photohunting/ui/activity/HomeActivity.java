package com.wisnu.photohunting.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.wisnu.photohunting.R;
import com.wisnu.photohunting.adapter.FragmentPagerAdapter;
import com.wisnu.photohunting.ui.fragment.CameraFragment;
import com.wisnu.photohunting.ui.fragment.CategoryFragment;
import com.wisnu.photohunting.ui.fragment.MapFragment;
import com.wisnu.photohunting.ui.fragment.PhotoFeedFragment;
import com.wisnu.photohunting.ui.fragment.UserFragment;
import com.wisnu.photohunting.view.PhotoViewPager;

public class HomeActivity extends AppCompatActivity {
    public static PhotoViewPager viewPager;

    Toolbar              toolbar;
    FragmentPagerAdapter fragmentPagerAdapter;
    TabLayout            tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Photo Feeds");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(PhotoFeedFragment.newInstance(), "Photo Feeds");
        fragmentPagerAdapter.addFragment(CategoryFragment.newInstance(), "Explore Location");
        fragmentPagerAdapter.addFragment(CameraFragment.newInstance(), "Camera");
        fragmentPagerAdapter.addFragment(MapFragment.newInstance(), "Map Location");
        fragmentPagerAdapter.addFragment(UserFragment.newInstance(), "User Profile");
        //fragmentPagerAdapter.addFragment(new FavouriteFragment(), "Favourite");

        viewPager = (PhotoViewPager) findViewById(R.id.support_view_pager);
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(fragmentPagerAdapter);
        setOnViewPagerListener();

        tabLayout = (TabLayout) findViewById(R.id.support_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        int[] tabIcon = new int[]{
                R.drawable.ic_home,
                R.drawable.ic_search,
                R.drawable.ic_camera,
                R.drawable.ic_map,
                R.drawable.ic_user};

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcon[i]);
        }
    }

    private void setOnViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = fragmentPagerAdapter.getItem(position).getArguments().getString("Title");
                toolbar.setTitle(title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
