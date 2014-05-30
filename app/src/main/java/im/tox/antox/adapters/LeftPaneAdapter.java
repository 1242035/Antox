package im.tox.antox.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import im.tox.antox.R;
import im.tox.antox.utils.Constants;
import im.tox.antox.utils.LeftPaneItem;
import im.tox.antox.utils.PrettyTimestamp;

/**
 * Created by ollie on 04/03/14.
 */
public class LeftPaneAdapter extends BaseAdapter {

    private ArrayList<LeftPaneItem> mData = new ArrayList<LeftPaneItem>();
    private LayoutInflater mInflater;

    public LeftPaneAdapter(Context context) {
        mInflater = ((Activity) context).getLayoutInflater();
    }

    public void addItem(final LeftPaneItem item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final LeftPaneItem item) {
        mData.add(item);
        // save separator position
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int type = getItem(position).viewType;
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return Constants.TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public LeftPaneItem getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private String SplitKey(String key) {
        return key.substring(0,38) + "\n" + key.substring(38);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case Constants.TYPE_FRIEND_REQUEST:
                    convertView = mInflater.inflate(R.layout.friendrequest_list_item, null);
                    holder.firstText = (TextView)convertView.findViewById(R.id.request_key);
                    holder.secondText = (TextView)convertView.findViewById(R.id.request_message);
                    break;
                case Constants.TYPE_CONTACT:
                    convertView = mInflater.inflate(R.layout.contact_list_item, null);
                    holder.firstText = (TextView)convertView.findViewById(R.id.friend_name);
                    holder.secondText = (TextView)convertView.findViewById(R.id.friend_status);
                    holder.icon = (TextView)convertView.findViewById(R.id.icon);
                    holder.countText = (TextView)convertView.findViewById(R.id.unread_messages_count);
                    holder.timeText = (TextView)convertView.findViewById(R.id.last_message_timestamp);
                    break;
                case Constants.TYPE_HEADER:
                    convertView = mInflater.inflate(R.layout.header_list_item, null);
                    holder.firstText = (TextView)convertView.findViewById(R.id.left_pane_header);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        LeftPaneItem item = mData.get(position);

        if (type == Constants.TYPE_FRIEND_REQUEST) {
            holder.firstText.setText(SplitKey(item.first));
        } else {
            holder.firstText.setText(item.first);
        }
        if (type != Constants.TYPE_HEADER) {
            holder.secondText.setText(item.second);
        }
        if (type == Constants.TYPE_CONTACT) {
            if (item.count > 0) {
                holder.countText.setVisibility(View.VISIBLE);
                holder.countText.setText(Integer.toString(item.count));
            } else {
                holder.countText.setVisibility(View.GONE);
            }
            holder.timeText.setText(PrettyTimestamp.prettyTimestamp(item.timestamp));
            holder.icon.setBackgroundColor(Color.parseColor(iconColor(item.icon)));
        }

        return convertView;
    }

    private String iconColor (int i) {
        String color;
        if (i == 0) {
            color = "#B0B0B0"; //offline
        } else if (i == 1) {
            color = "#5ec245"; //online
        } else if (i == 2) {
            color = "#E5C885"; //away
        } else if (i == 3) {
            color = "#CF4D58"; //busy
        } else {
            color = "#FFFFFF";
        }
        return color;
    }

    private static class ViewHolder {
        public TextView firstText;
        public TextView secondText;
        public TextView icon;
        public TextView countText;
        public TextView timeText;
    }

}


