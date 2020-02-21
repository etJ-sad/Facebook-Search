package com.example.liuyx.hw9_3;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Liuyx on 4/19/17.
 */
public class FragmentFav extends Fragment {

    public static SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    //Mandatory Constructor
    public FragmentFav() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fav,container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        setFragArray();
        mViewPager = (ViewPager) rootView.findViewById(R.id.fav_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.fav_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.users);
        tabLayout.getTabAt(1).setIcon(R.drawable.pages);
        tabLayout.getTabAt(2).setIcon(R.drawable.events);
        tabLayout.getTabAt(3).setIcon(R.drawable.places);
        tabLayout.getTabAt(4).setIcon(R.drawable.groups);

        return rootView;
    }

    public static void setFragArray() {
        if (mSectionsPagerAdapter == null) {
            return;
        }
        if (mSectionsPagerAdapter.fragments == null) {
            mSectionsPagerAdapter.fragments = new PlaceholderFragment[]{
                    new PlaceholderFragment(),
                    new PlaceholderFragment(),
                    new PlaceholderFragment(),
                    new PlaceholderFragment(),
                    new PlaceholderFragment()
            };
            mSectionsPagerAdapter.fragments[0].setType("user");
            mSectionsPagerAdapter.fragments[1].setType("page");
            mSectionsPagerAdapter.fragments[2].setType("event");
            mSectionsPagerAdapter.fragments[3].setType("place");
            mSectionsPagerAdapter.fragments[4].setType("group");
        }
        mSectionsPagerAdapter.fragments[0].setArray();
        mSectionsPagerAdapter.fragments[1].setArray();
        mSectionsPagerAdapter.fragments[2].setArray();
        mSectionsPagerAdapter.fragments[3].setArray();
        mSectionsPagerAdapter.fragments[4].setArray();
    }
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        //0 user, 1 page, 2 event, 3 place, 4 group
        PlaceholderFragment[] fragments;

        public SectionsPagerAdapter(FragmentManager fm) {

            super(fm);
            fragments = new PlaceholderFragment[]{
                    new PlaceholderFragment(),
                    new PlaceholderFragment(),
                    new PlaceholderFragment(),
                    new PlaceholderFragment(),
                    new PlaceholderFragment()
            };
            fragments[0].setType("user");
            fragments[1].setType("page");
            fragments[2].setType("event");
            fragments[3].setType("place");
            fragments[4].setType("group");
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return fragments[position];
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Users";
                case 1:
                    return "Pages";
                case 2:
                    return "Events";
                case 3:
                    return "Places";
                case 4:
                    return "Groups";
            }
            return null;
        }
    }
}

