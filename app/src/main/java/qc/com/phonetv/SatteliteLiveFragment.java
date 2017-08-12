package qc.com.phonetv;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SatteliteLiveFragment extends Fragment {

    private List<Channel> mChannels;
    private ListView mChannelsListView;

    public SatteliteLiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannels = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sattelite_live,container,false);
        mChannelsListView = (ListView) view.findViewById(R.id.channel_list);
        //网络请求卫视channel信息
        String tableName = "Channel";
        BmobQuery<Channel> channelBmobQuery = new BmobQuery<>(tableName);
        channelBmobQuery.findObjects(new FindListener<Channel>() {
            @Override
            public void done(List<Channel> list, BmobException e) {
                for(Channel channel:list){
                    if(channel.getType().equals(ChannelType.SATTELITE_CHANNEL)){
                        mChannels.add(channel);
                    }
                }
                mChannelsListView.setAdapter(new SatteliteChannelAdapter());
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
        return view;
    }

    class SatteliteChannelAdapter extends BaseAdapter{

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
            holder.mmChannelName.setText(channel.getName());

            holder.mmChannelLogo.setImageResource(R.mipmap.ic_launcher);

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
