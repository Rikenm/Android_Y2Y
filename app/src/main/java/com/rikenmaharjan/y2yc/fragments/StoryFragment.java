package com.rikenmaharjan.y2yc.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by Riken on 3/24/18.
 */

public class StoryFragment extends BaseFragment {

    private TextView txtBedR;
    private TextView txtLockerR;
    private TextView txtMinor;
    private TextView txtMajor;
    private TextView txtDayR;
    private String id;


    public static StoryFragment newInstance(){
        return new StoryFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story,container,false);

        Bundle bundle = getArguments();
        id = bundle.getString("id");

        txtBedR = (TextView) view.findViewById(R.id.txtBedR);
        txtDayR = (TextView) view.findViewById(R.id.txtDayR);
        txtLockerR = (TextView) view.findViewById(R.id.txtLockerR);
        txtMinor = (TextView) view.findViewById(R.id.txtMinor);
        txtMajor = (TextView) view.findViewById(R.id.txtMajor);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://y2y.herokuapp.com/detailuser/0031D000003OvlSQAS";
        //String url = "https://y2y.herokuapp.com/detailuser/"+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request sucessful", response );
                try{
                    JSONObject apiResult = new JSONObject(response);
                    txtBedR.setText(apiResult.getString("Bed_name"));
                    txtDayR.setText(apiResult.getString("Last_Day_Of_Stay"));
                    txtLockerR.setText(apiResult.getString("Locker"));
                    txtMinor.setText(apiResult.getString("Minor_warning")+" Minor");
                    txtMajor.setText(apiResult.getString("Major_warning")+" Major");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i("request failed", "failed");
            }
        });

        queue.add(stringRequest);
        Log.i("result",queue.toString());

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
