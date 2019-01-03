package qc.com.phonetv;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 频道直播父页面，包括CentralLiveFragment,SatteliteLiveFragment,LocalLiveFragment三个fragment
 */
public class LiveParentFragment extends Fragment {
    private static final String TAG = "LiveParentFragment";
    private List<Fragment> mChildLiveFragmens;
    private String[] mFragmentTitles = {
      "央视",
      "卫视",
      "地方台",
    };
    public LiveParentFragment() {
        Log.d(TAG, "LiveParentFragment: run");
        Log.d(TAG, "LiveParentFragment: exit");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: run");
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
        Log.d(TAG, "onCreateView: exit");
        return view;
    }

    class MyFragmentAdapter extends FragmentPagerAdapter{

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
            Log.d(TAG, "MyFragmentAdapter: run");
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: run");
            return mChildLiveFragmens.get(position);
        }

        @Override
        public int getCount() {
            return mChildLiveFragmens.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.d(TAG, "getPageTitle: run");
            return mFragmentTitles[position];
        }
    }


}
