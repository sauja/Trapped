package com.thehrmsinc.trapped;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thehrmsinc.trapped.Database.Messages;

import java.util.ArrayList;

/**
 * Created by sauja on 5/18/2016.
 */
public class SendFromListCustomAdaptor extends ArrayAdapter {

    public static final int TYPE_FROM = 0;
    public static final int TYPE_SEND = 1;

    private ArrayList<Messages> objects;
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).getType();
    }
    public SendFromListCustomAdaptor(Context context, int resource, ArrayList<Messages> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        Messages sendFromListRow = objects.get(position);
        int sendFromListRowType = getItemViewType(position);


        if (convertView == null) {

            if (sendFromListRowType == TYPE_FROM) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.from_row, null);
            } else if (sendFromListRowType == TYPE_SEND) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.send_row, null);
            }

            TextView chatTextView = (TextView) convertView.findViewById(R.id.rowTextView);
            TextView chatSourceView=(TextView) convertView.findViewById(R.id.rowTextName) ;
            viewHolder = new ViewHolder(chatTextView,chatSourceView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.getChatTextView().setText(sendFromListRow.getChatText());
        viewHolder.getChatSourceView().setText(sendFromListRow.getChatSource());
        return convertView;
    }
}