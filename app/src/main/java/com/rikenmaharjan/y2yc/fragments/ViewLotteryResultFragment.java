package com.rikenmaharjan.y2yc.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rikenmaharjan.y2yc.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by wangdayuan on 4/7/18.
 */

public class ViewLotteryResultFragment extends Fragment {

    private TextView date1;
    private TextView date2;

    public ViewLotteryResultFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_view_lottery_result, container, false);
        date1 = (TextView) view.findViewById(R.id.date1);
        date2 = (TextView) view.findViewById(R.id.date2);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        date1.setText(currentDateTimeString);
        date2.setText(currentDateTimeString);

        return view;

    }
}
