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

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocalLiveFragment extends Fragment {

    private static final String TAG = "LocalLiveFragment";
    private List<Channel> mChannels;
    private ListView mLocalChannelListView;
    public LocalLiveFragment() {
        Log.d(TAG, "LocalLiveFragment: run");
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannels = new ArrayList<>();
        Log.d(TAG, "onCreate: run");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: run");
        View view = inflater.inflate(R.layout.fragment_local_live,container,false);
        mLocalChannelListView = (ListView) view.findViewById(R.id.list_local_channel);
        //网络请求卫视channel信息
        String tableName = "Channel";
        BmobQuery<Channel> channelBmobQuery = new BmobQuery<>(tableName);
        channelBmobQuery.findObjects(new FindListener<Channel>() {
            @Override
            public void done(List<Channel> list, BmobException e) {
                for(Channel channel:list){
                    if(channel.getType().equals(ChannelType.LOCAL_CHANNEL)){
                        mChannels.add(channel);
                    }
                }
                mLocalChannelListView.setAdapter(new LiveAdapter(getContext(),mChannels));
            }
        });
        mLocalChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),PlayActivity.class);
                intent.putExtra("CHANNEL",mChannels.get(position));
                startActivity(intent);
            }
        });
        return view;
    }

}
