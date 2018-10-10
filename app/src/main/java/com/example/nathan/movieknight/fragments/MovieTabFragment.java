package com.example.nathan.movieknight.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.activities.MovieListActivity;

public class MovieTabFragment extends Fragment {



    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3;
    MovieListActivity movieListActivity;
    @SuppressLint("ValidFragment")
    public MovieTabFragment(MovieListActivity ma){
        super();
        movieListActivity = ma;




    }
    public MovieTabFragment(){
        super();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.tab_layout,null);
        MovieKnightAppli application = (MovieKnightAppli)movieListActivity.getApplication();
        application.setCurrentContext(inflater.getContext());
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager(), movieListActivity));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {
        MovieListActivity movieListActivity;
        public MyAdapter(FragmentManager fm, MovieListActivity ma) {
            super(fm);
            movieListActivity = ma;
        }

        /**
         * Return fragment with respect to Position .
         */
/*

 */
        @Override
        public Fragment getItem(int position)
        {

            switch (position){
                case 0 : return new InTheatersFragment(movieListActivity);
                case 1 : return new ComingSoonFragment(movieListActivity);
                case 2 : return new TopRatedFragment(movieListActivity);
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "In Theaters";
                case 1 :
                    return "Coming Soon";
                case 2 :
                    return "Top Rated";
            }
            return null;
        }
    }

}