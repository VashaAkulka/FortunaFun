package com.example.roulet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomProfileAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;

    public CustomProfileAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return DataBase.countHistory();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.custom_profile_list, null);
        return DataBase.setCustomAdapterHistory(i, view);
    }
}
