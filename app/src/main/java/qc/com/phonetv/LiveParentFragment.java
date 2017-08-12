package qc.com.phonetv;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveParentFragment extends Fragment {

    private List<Fragment> mChildLiveFragmens;
    private String[] mFragmentTitles = {
      "央视",
      "卫视",
      "地方台",
    };
    public LiveParentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mChildLiveFragmens = new ArrayList<>();
        Fragment centralLiveFragment = new CentralLiveFragment();
        Fragment satteliteLiveFragment = new SatteliteLiveFragment();
        Fragment localLiveFragment = new LocalLiveFragment();
        mChildLiveFragmens.add(centralLiveFragment);
        mChildLiveFragmens.add(satteliteLiveFragment);
        mChildLiveFragmens.add(localLiveFragment);
        View view = inflater.inflate(R.layout.fragment_live_parent, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyFragmentAdapter(getChildFragmentManager()));
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    class MyFragmentAdapter extends FragmentPagerAdapter{

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mChildLiveFragmens.get(position);
        }

        @Override
        public int getCount() {
            return mChildLiveFragmens.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles[position];
        }
    }


}
