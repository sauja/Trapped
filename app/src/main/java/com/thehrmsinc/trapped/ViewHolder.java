package com.thehrmsinc.trapped;

import android.widget.TextView;

/**
 * Created by Acer on 5/18/2016.
 */
public class ViewHolder {
    TextView chatTextView;
    TextView chatSourceView;

    public ViewHolder(TextView chatTextView, TextView chatSourceView) {
        this.chatTextView = chatTextView;
        this.chatSourceView = chatSourceView;
    }

    public TextView getChatTextView() {
        return chatTextView;
    }

    public void setChatTextView(TextView chatTextView) {
        this.chatTextView = chatTextView;
    }

    public TextView getChatSourceView() {
        return chatSourceView;
    }

    public void setChatSourceView(TextView chatSourceView) {
        this.chatSourceView = chatSourceView;
    }
}
