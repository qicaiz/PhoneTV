package qc.com.phonetv;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SatteliteLiveFragment extends Fragment {

    private static final String TAG = "SatteliteLiveFragment";
    private List<Channel> mChannels;
    private ListView mChannelsListView;
    private View mRootView;
    private ProgressBar mProgressBar;
    private LiveAdapter mAdapter;

    public SatteliteLiveFragment() {
        Log.d(TAG, "SatteliteLiveFragment: run");
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannels = new ArrayList<>();
        Log.d(TAG, "onCreate: run");
        mAdapter = new LiveAdapter(getContext(),mChannels);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: run");
        if(mRootView==null){
            Log.d(TAG, "onCreateView: rootview==null run");
            mRootView = inflater.inflate(R.layout.fragment_sattelite_live,container,false);
            mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressbar);
            mProgressBar.setVisibility(View.VISIBLE);
            mChannelsListView = (ListView) mRootView.findViewById(R.id.channel_list);
            mChannelsListView.setAdapter(mAdapter);
            //网络请求卫视channel信息
            String tableName = "Channel";
            BmobQuery<Channel> channelBmobQuery = new BmobQuery<>(tableName);
            channelBmobQuery.findObjects(new FindListener<Channel>() {
                @Override
                public void done(List<Channel> list, BmobException e) {
                    Log.d(TAG, "done: query sattlelite channel run");
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mChannels.clear();
                    for(Channel channel:list){
                        if(channel.getType().equals(ChannelType.SATTELITE_CHANNEL)){
                            mChannels.add(channel);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }


            });
            mChannelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(),PlayActivity.class);
                    intent.putExtra("CHANNEL",mChannels.get(position));
                    startActivity(intent);
                }
            });
        } else {
            Log.d(TAG, "onCreateView: rootview !=null run");
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if(parent!=null){
                parent.removeView(mRootView);
            }
        }

        return mRootView;
    }

}
