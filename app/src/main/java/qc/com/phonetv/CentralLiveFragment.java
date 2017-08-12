package qc.com.phonetv;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
                mChannelListViw.setAdapter(new CCTVListAdapter());
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

    class CCTVListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mChannels.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView==null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_live_item,null);
                holder = new ViewHolder();
                holder.mmChannelLogo = (ImageView) convertView.findViewById(R.id.ivew_channel_logo);
                holder.mmChannelName = (TextView) convertView.findViewById(R.id.txt_channel_name);
                holder.mmChannelCurrentProgram = (TextView) convertView.findViewById(R.id.txt_channel_current_program);
                holder.mmChannelNextProgram = (TextView) convertView.findViewById(R.id.txt_channel_next_program);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Channel channel = mChannels.get(position);
            Picasso.with(getContext()).load(channel.getPosterAddress()).into(holder.mmChannelLogo);
//            holder.mmChannelLogo.setImageResource(R.drawable.cctv_1);
            holder.mmChannelName.setText(channel.getName());
            return convertView;
        }
    }

    class ViewHolder{
        public ImageView mmChannelLogo;
        public TextView mmChannelName;
        public TextView mmChannelCurrentProgram;
        public TextView mmChannelNextProgram;
    }

}
