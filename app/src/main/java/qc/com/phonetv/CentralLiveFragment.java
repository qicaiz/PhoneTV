package qc.com.phonetv;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CentralLiveFragment extends Fragment {
    private static final String TAG = "CentralLiveFragment";
    private Activity mParentActivity;
    private ListView mChannelListViw;
    private List<Channel> mChannels;
    private LiveAdapter mAdapter;
    private View mRootView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public CentralLiveFragment() {
        Log.d(TAG, "CentralLiveFragment: run");
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = activity;
        Log.d(TAG, "onAttach: run");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: run");
        mChannels = new ArrayList<>();
        mAdapter = new LiveAdapter(getContext(),mChannels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: run");
        if(mRootView==null){
            Log.d(TAG, "onCreateView: rootview==null run");
            mRootView = inflater.inflate(R.layout.fragment_central_live,container,false);
            mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swiperefresh);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //clear image cache when refesh
                    for(Channel channel:mChannels){
                        Picasso.with(getContext()).invalidate(channel.getPosterAddress());
                    }
                    mChannels.clear();
                    mAdapter.notifyDataSetChanged();

                    requestChannels();

                }
            });
            mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressbar);
            mProgressBar.setVisibility(View.VISIBLE);
            mChannelListViw = (ListView) mRootView.findViewById(R.id.list_cctv);
            mChannelListViw.setAdapter(mAdapter);
            mChannelListViw.setOnItemClickListener(mChannelClickListener);
            requestChannels();

        } else {
            Log.d(TAG, "onCreateView: rootview !=null run");
            /**
             * 缓存的rootView需要判断是否已经被加过parent，
             * 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
             */
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
        }

        return mRootView;
    }

    private AdapterView.OnItemClickListener mChannelClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //put channel info to Playactivity

            Intent intent = new Intent(getContext(),PlayActivity.class);
            intent.putExtra("CHANNEL",mChannels.get(position));
            startActivity(intent);
        }
    };

    private void requestChannels(){
        //网络请求播放地址
        String tableName = "Channel";
        BmobQuery<Channel> channelBmobQuery = new BmobQuery<>(tableName);
        channelBmobQuery.findObjects(new FindListener<Channel>() {
            @Override
            public void done(List<Channel> list, BmobException e) {
                Log.d(TAG, "Central Channel request done: run");
                mProgressBar.setVisibility(View.INVISIBLE);
                mChannels.clear();
                for(Channel channel:list){
                    if(channel.getType().equals(ChannelType.CENTRAL_CHANNEL)){
                        mChannels.add(channel);
                    }
                }
                //sort channel by id
                List<Channel> tempChannels = new ArrayList<>();
                int size = mChannels.size();
                Log.d(TAG, "done: channel size="+size);
                for(int i=0;i<size;i++){

                    for(int j=i;j<size;j++){

                        int idI = Integer.valueOf(mChannels.get(i).getId());
                        int idJ = Integer.valueOf(mChannels.get(j).getId());
                        if(idJ<idI){
                            Log.d(TAG, "done: channel size j="+j);
                            Channel tempI = mChannels.get(i);
                            Channel tempJ = mChannels.get(j);
                            mChannels.remove(i);
                            mChannels.add(i,tempJ);
                            mChannels.remove(j);
                            mChannels.add(j,tempI);
                        }

                    }

                }
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }
        });
    }



}
