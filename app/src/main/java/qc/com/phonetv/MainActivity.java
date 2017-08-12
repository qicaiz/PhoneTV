package qc.com.phonetv;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Fragment mLiveParentFragment;
    private Fragment mVodFragment;
    private Fragment mMyFragment;
    private FragmentManager mFragmentMgr;
    private FragmentTransaction mFragmentTransaction;
    private int mCurrentFragment = R.id.navigation_live;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_live:
                    if(mCurrentFragment!=R.id.navigation_live){
                        fragmentTransaction.replace(R.id.content,mLiveParentFragment,null);
                        fragmentTransaction.commit();
                        mCurrentFragment = R.id.navigation_live;
                    }

                    return true;
                case R.id.navigation_vod:
                    if(mCurrentFragment!=R.id.navigation_vod){
                        fragmentTransaction.replace(R.id.content,mVodFragment,null);
                        fragmentTransaction.commit();
                        mCurrentFragment = R.id.navigation_vod;
                    }
                    return true;
                case R.id.navigation_my:
                    if(mCurrentFragment!=R.id.navigation_my){
                        fragmentTransaction.replace(R.id.content,mMyFragment,null);
                        fragmentTransaction.commit();
                        mCurrentFragment = R.id.navigation_my;
                    }
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: run");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化bmob
        Bmob.initialize(this, "0c9a831dee331ed2af0bff4975bca3e1");

        mLiveParentFragment = new LiveParentFragment();
        mVodFragment = new VodFragment();
        mMyFragment= new MyFragment();
        mFragmentMgr = getSupportFragmentManager();
        mFragmentTransaction = mFragmentMgr.beginTransaction();
        mFragmentTransaction.add(R.id.content,mLiveParentFragment,"LiveParentFragment");
        mFragmentTransaction.commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Log.d(TAG, "onCreate: exit");
    }

}
