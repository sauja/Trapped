package com.thehrmsinc.trapped;

import android.widget.TextView;

/**
 * Created by Acer on 5/18/2016.
 */
public class ViewHolder {
    TextView text;

    public ViewHolder(TextView text) {
        this.text = text;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }

}
