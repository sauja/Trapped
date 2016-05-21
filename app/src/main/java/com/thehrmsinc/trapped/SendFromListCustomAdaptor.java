package com.thehrmsinc.trapped;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 5/18/2016.
 */
public class SendFromListCustomAdaptor extends ArrayAdapter {

    public static final int TYPE_FROM = 0;
    public static final int TYPE_SEND = 1;


    private ArrayList<SendFromListRow> objects;

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return objects.get(position).getType();
    }
    public SendFromListCustomAdaptor(Context context, int resource, ArrayList<SendFromListRow> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        SendFromListRow sendFromListRow = objects.get(position);
        int sendFromListRowType = getItemViewType(position);


        if (convertView == null) {

            if (sendFromListRowType == TYPE_FROM) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.from_row, null);
            } else if (sendFromListRowType == TYPE_SEND) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.send_row, null);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.rowTextView);
            viewHolder = new ViewHolder(textView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.getText().setText(sendFromListRow.getText());

        return convertView;
    }

    public void refresh(List<SendFromListRow> items)
    {

        notifyDataSetChanged();
    }
}