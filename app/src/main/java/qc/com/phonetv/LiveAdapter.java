package qc.com.phonetv;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by qicaiz on 8/12/2017.
 * 直播列表适配器
 */

public class LiveAdapter extends BaseAdapter {

    private List<Channel> mChannels;
    private Context mContext;
    public LiveAdapter(Context context, List<Channel> channels) {
        mContext = context;
        mChannels = channels;
    }

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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_live_item, null);
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

        if(TextUtils.isEmpty(channel.getPosterAddress())){
            holder.mmChannelLogo.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.with(mContext).load(channel.getPosterAddress()).into(holder.mmChannelLogo);
        }

        return convertView;
    }


    class ViewHolder {
        public ImageView mmChannelLogo;
        public TextView mmChannelName;
        public TextView mmChannelCurrentProgram;
        public TextView mmChannelNextProgram;
    }
}
