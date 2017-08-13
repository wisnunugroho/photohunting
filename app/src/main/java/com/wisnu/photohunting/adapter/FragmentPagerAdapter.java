package com.wisnu.photohunting.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wahyu Adi S on 29/11/2015.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private boolean        iconOption   = true;
    private List<Fragment> fragmentList = new ArrayList<>();

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private Fragment mCurrentFragment;

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (mCurrentFragment != null) getCurrentFragment();
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return iconOption ? "" : fragmentList.get(position).getArguments().getString("Title");
    }

    @Override
    public int getCount() {
        return fragmentList != null ? fragmentList.size() : 0;
    }

    public void setIcon(boolean option) {
        this.iconOption = option;
    }

    public void addFragment(Fragment fragment, String title) {
        Bundle arguments = new Bundle();
        arguments.putString("Title", title);
        fragment.setArguments(arguments);
        fragmentList.add(fragment);
    }
}
