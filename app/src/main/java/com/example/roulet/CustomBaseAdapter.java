package com.example.roulet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

public class CustomBaseAdapter extends BaseAdapter {

    Map<String, Double> values;
    LayoutInflater layoutInflater;

    public CustomBaseAdapter(Context context, Map<String, Double> values) {
        this.values = values;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return values.size();
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
        view = layoutInflater.inflate(R.layout.custom_list_view, null);
        TextView textName = view.findViewById(R.id.custom_text_name);
        TextView textCount = view.findViewById(R.id.custom_text_count);
        TextView textNumber = view.findViewById(R.id.custom_text_number);

        int color = 0;
        switch (i) {
            case 0:
                color = Color.parseColor("#ffd700");
                break;
            case 1:
                color = Color.parseColor("#c0c0c0");
                break;
            case 2:
                color = Color.parseColor("#cd7f32");
        }

        if (color != 0) {
            textName.setTextColor(color);
            textCount.setTextColor(color);
            textNumber.setTextColor(color);
        }

        String name = (String) values.keySet().toArray()[i];
        Double count = values.get(name);

        if (name.equals(LoginActivity.login) && i == 0) {
            if (AchievementActivity.achievementMommy()) AchievementActivity.showMessage(view.getContext());
        }

        textName.setText(name);
        textCount.setText("" + count);
        textNumber.setText("#" + (i + 1));

        return view;
    }
}
