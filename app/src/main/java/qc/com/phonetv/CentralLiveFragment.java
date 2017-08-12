package qc.com.phonetv;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CentralLiveFragment extends Fragment {

    private Activity mParentActivity;
    private ListView mChannelListViw;
    private List<Channel> mChannels;

    public CentralLiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_central_live,container,false);
        mChannelListViw = (ListView) view.findViewById(R.id.list_cctv);
        //网络请求播放地址
        String tableName = "Channel";
        BmobQuery<Channel> channelBmobQuery = new BmobQuery<>(tableName);
        channelBmobQuery.findObjects(new FindListener<Channel>() {
            @Override
            public void done(List<Channel> list, BmobException e) {
                mChannels = list;
                mChannelListViw.setAdapter(new LiveAdapter(getContext(),mChannels));
            }
        });
        mChannelListViw.setOnItemClickListener(mChannelClickListener);
        return view;
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



}
