package com.rikenmaharjan.y2yc.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.activities.Main2Activity;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by wangdayuan on 4/7/18.
 */

public class ViewLotteryResultFragment extends Fragment {

    private TextView date1;
    private TextView date2;
    private TextView longTermLottery;
    private TextView eBedLottery;
    public SessionManager session;
    String id;
    String name;


    public ViewLotteryResultFragment(){}

    @Override
    public void onResume() {
        super.onResume();

        session = new SessionManager(getContext());

        session.checkLogin();




        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // email
        id = user.get(SessionManager.KEY_ID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_view_lottery_result, container, false);
        date1 = (TextView) view.findViewById(R.id.date1);
        date2 = (TextView) view.findViewById(R.id.date2);
        longTermLottery = (TextView) view.findViewById(R.id.longTermLottery);
        eBedLottery = (TextView) view.findViewById(R.id.eBedLottery);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        date1.setText(currentDateTimeString);
        date2.setText(currentDateTimeString);
        //new MyAsyncTaskgetNews2().execute("hello", "hello", "hello");

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://y2y.herokuapp.com/lottery";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request sucessful", response );
                try{
                    JSONObject apiResult = new JSONObject(response);
                    view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    eBedLottery.setText(apiResult.getString("e-bed"));
                    longTermLottery.setText(apiResult.getString("Long Term"));
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

    public class MyAsyncTaskgetNews2 extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub

            //String json =  "{\"username\": \"sampada\",\"password\":\"password123\"}";
            //String json =  "{\"username\": \""+mUSerNameEt.getText().toString()+"\",\"password\":\""+mUSerPasswordEt.getText().toString()+"\"}";
            HttpURLConnection urlConnection = null;
            //response = new String();
            String response = new String();



            try {

                URL url = new URL("https://y2y.herokuapp.com/lottery");

                //URL url = new URL("http://155.41.34.62:3000/login"); //computerlab ip



                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //urlConnection.setRequestProperty("Content-Type","application/json");
                /*
                if (json != null) {
                    //set the content length of the body
                    urlConnection.setRequestProperty("Content-length", json.getBytes().length + "");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setUseCaches(false);

                    //send the json as body of the request
                    OutputStream outputStream = urlConnection.getOutputStream();
                    outputStream.write(json.getBytes("UTF-8"));
                    outputStream.close();
                }

                */

                //urlConnection.setConnectTimeout(7000);
                urlConnection.connect();
                int status = urlConnection.getResponseCode();
                Log.i("HTTP Client", "HTTP status code : " + status);
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        bufferedReader.close();
                        Log.i("HTTP Client", "Received String : " + sb.toString());
                        response = sb.toString();
                        Log.i("Json",(String)(sb.getClass().getName()));
                        //return received string
                        return sb.toString();




                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                //end connection
                urlConnection.disconnect();
            }





            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {
                //display response data


            } catch (Exception ex) {
            }


        }
        protected void onPostExecute(String  result2) {

            //if (result2 == null) checks this

            JSONObject reader = null;
            String ebed = null;
            String longTerm = null;
            try {
                reader = new JSONObject(result2);
                ebed = reader.getString("e-bed");
                longTerm = reader.getString("Long Term");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //creating session
            //session.createLoginSession(mUSerNameEt.getText().toString(), id);




            //result2 = result2.substring(0, result2.length() - 1); //removing the null char

            //Log.i("isValid", isvalid);
            Log.i("e-bad",ebed);


            /*
            if (isvalid.equals("invalid")) {

                Log.i("isValid", "Invalid");
                Toast.makeText(getContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();


            } else if (isvalid.equals("Network Error")) {
                Log.i("isValid", "Network Error");
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();

            } else {

                Log.i("isValid", id);
                Intent i = (new Intent(getActivity(), Main2Activity.class));
                i.putExtra("id", id);
                startActivity(i);
            }
            */

        }

    }




}
