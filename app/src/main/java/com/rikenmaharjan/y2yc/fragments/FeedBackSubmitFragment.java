package com.rikenmaharjan.y2yc.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by wangdayuan on 4/7/18.
 */

public class FeedBackSubmitFragment extends Fragment {

    private EditText feedBack;
    private Button feedBackSubmit;
    private StoryFragment sf;
    private FragmentManager fm;

    public SessionManager session;
    String id;
    String name;




    public FeedBackSubmitFragment(){}


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



        View view = inflater.inflate(R.layout.fragment_submit_feedback, container, false);

        feedBack = (EditText) view.findViewById(R.id.feedBack);
        feedBackSubmit = (Button) view.findViewById(R.id.feedBackSubmit);

        feedBackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String feedBackText = feedBack.getText().toString();
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    String url = "https://y2y.herokuapp.com/feedback";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", id);
                    jsonBody.put("comment", feedBackText);
                    final String requestBody = jsonBody.toString();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                                Log.i("response",response.toString());
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                    Toast.makeText(getActivity(), "Sent!!", Toast.LENGTH_LONG).show();
                    feedBack.setText("");

                    sf = new StoryFragment();
                    fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
                    ft.replace(R.id.constraintLayout, sf);
                    ft.addToBackStack ("myFrag2");  //why do we do this?
                    ft.commit();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }






            }
        });

        return view;

    }



}
