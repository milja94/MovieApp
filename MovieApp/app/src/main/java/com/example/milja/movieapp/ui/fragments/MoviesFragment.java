package com.example.milja.movieapp.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.milja.movieapp.R;

public class MoviesFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager viewPager;
    private TextView selectedTab, popularMoviesTextView, topRatedMoviesTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        viewPager = view.findViewById(R.id.view_pager);
        popularMoviesTextView = view.findViewById(R.id.popular_movies_text_view);
        topRatedMoviesTextView = view.findViewById(R.id.top_rated_text_view);
        selectedTab=topRatedMoviesTextView;
        ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(onPageChangedListener);
        viewPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View v) {
        int position = -1;
        switch (v.getId()) {
            case R.id.top_rated_text_view:
                position = 0;
                break;
            case R.id.popular_movies_text_view:
                position = 2;
                break;


        }
        viewPager.setCurrentItem(position);
    }

    private ViewPager.OnPageChangeListener onPageChangedListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (selectedTab != null)
                selectedTab.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

            switch(position){
                case 0:
                    selectedTab=topRatedMoviesTextView;
                    break;
                case 1:
                    selectedTab=popularMoviesTextView;
                    break;

            }
            selectedTab.setTextColor(Color.WHITE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TopRatedFragment();
                case 1:
                    return new PopularMoviesFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }


}
