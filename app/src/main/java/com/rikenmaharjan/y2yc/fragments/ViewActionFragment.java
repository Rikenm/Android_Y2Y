package com.rikenmaharjan.y2yc.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.graphics.Typeface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.rikenmaharjan.y2yc.R;
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
import com.rikenmaharjan.y2yc.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// FOLLOWED THIS TUTORIAL: https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
// And referenced this: http://helloiamandroid.blogspot.com/2012/11/hello-android-developers_29.html

public class ViewActionFragment extends BaseFragment {
    //BaseFragmentActivity calls the following method and create LoginFragment to add to this activity

    //public static ViewActionFragment newInstance(){
    //    return new ViewActionFragment();
    //}


    public SessionManager session;
    static String id;
    String name;

    private
    ExpandableListView actions;
    ExpandableListAdapter adapter;
    List<String> Header;
    HashMap<String, List<String>> Child;
    TextView txtview;

    public  View rootView;
    //public static List<Boolean[]> childCheckbox = new ArrayList<>();
    public static List<String> action_item_ids = new ArrayList<>();
    public static List<String[]> action_item_step_ids = new ArrayList<>();
    public static List<Integer> action_item_num_steps = new ArrayList<>();
    public static EditText reason;
    public static Button save_reason;
    public static HashMap<String, List<String>> completed_steps = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        session = new SessionManager(getContext());

        session.checkLogin();
        //this takes you to login page if you are not logged in



        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // email
        id = user.get(SessionManager.KEY_ID);

       // this makes user that every fragment gets id and name of the user



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_view_action,container,false);
        txtview = rootView.findViewById(R.id.txtView);
        txtview.setText("Here are your ongoing goals and action items. We would love to hear any updates or progress!");
        actions = rootView.findViewById(R.id.actions);
        reason = rootView.findViewById(R.id.action_reason);
        save_reason = rootView.findViewById(R.id.btnreason);
        //reason.setVisibility(View.INVISIBLE);
        //save_reason.setVisibility(View.INVISIBLE);
        prepareListData();
        Log.d("header",Header.toString());
        Log.d("child",Child.toString());


        actions.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //Toast.makeText(getContext(),"Group Clicked " + Header.get(groupPosition), Toast.LENGTH_SHORT).show();;
                return false;
            }
        });

        actions.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getContext(), Header.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        actions.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getContext(),Header.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
            }
        });

        actions.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(getContext(),Header.get(groupPosition) + " : " + Child.get(Header.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return rootView;
    }

    public void prepareListData() {
        Header = new ArrayList<String>();
        Child = new HashMap<String, List<String>>();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        session = new SessionManager(getContext());

        session.checkLogin();
        //this takes you to login page if you are not logged in



        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        name = user.get(SessionManager.KEY_NAME);

        // email
        id = user.get(SessionManager.KEY_ID);

        String url = "https://y2y.herokuapp.com/actionitems/"+id;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("request successful", response );
                try{
                    JSONObject apiResult = new JSONObject(response);
                    int num_action_items;
                    int num_steps;
                    List<String[]> childList = new ArrayList<>();
                    List<String> complete_step_ids = new ArrayList<>();
                    num_action_items = Integer.parseInt(apiResult.getString("size"));
                    JSONArray my_action_items = apiResult.getJSONArray("records");
                    for (int i = 0; i < num_action_items; i++) {
                        Header.add(my_action_items.getJSONObject(i).getString("name"));
                        num_steps = Integer.parseInt(my_action_items.getJSONObject(i).getString("numb_of_step"));
                        action_item_num_steps.add(num_steps);
                        String[] steps = new String[num_steps];
                        //Boolean[] checkboxList = new Boolean[num_steps];
                        String[] step_item_ids = new String[num_steps];
                        String current_action_id = my_action_items.getJSONObject(i).getString("id");
                        action_item_ids.add(current_action_id);
                        JSONObject current_action = apiResult.getJSONObject(current_action_id);
                        for (int j = 0; j < num_steps; j++) {
                            steps[j] = (current_action.getString(Integer.toString(j+1)));
                            step_item_ids[j] = current_action.getString("step_id"+Integer.toString(j+1));
                            Boolean temp = (current_action.getBoolean("completed"+Integer.toString(j+1)));
                            //if (temp == "false")
                                //checkboxList[j] = false;
                            if (temp)
                                complete_step_ids.add(step_item_ids[j]);
                                //checkboxList[j] = true;
                        }
                        childList.add(steps);
                        //childCheckbox.add(checkboxList);
                        completed_steps.put(current_action_id, complete_step_ids);
                        action_item_step_ids.add(step_item_ids);
                        List<String> temp = Arrays.asList(childList.get(i));
                        Child.put(Header.get(i), temp);

                        adapter = new MyCustomAdapter(getContext(), Header, Child);
                        actions.setAdapter(adapter);
                        rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    }
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
    }

    //public static List<Boolean[]> childCheckboxData() {
    //    return childCheckbox;
    //}

    public static List<String> action_item_ids_Data() {
        return action_item_ids;
    }

    public static List<String[]> action_item_step_ids_Data() {
        return action_item_step_ids;
    }

    //public static String get_user_id() { return id; }
    public static HashMap<String, List<String>> getCompleted_steps() { return completed_steps; }

    public static List<Integer> get_num_steps() { return action_item_num_steps; }
}


class MyCustomAdapter extends BaseExpandableListAdapter {
    private
    Context context;
    List<String> Header;
    HashMap<String, List<String>> Child;
    final ViewActionFragment frag = new ViewActionFragment();
    HashMap<String, List<String>> completed_steps = frag.getCompleted_steps();
    final EditText reason = ViewActionFragment.reason;
    final Button save_reason = ViewActionFragment.save_reason;

    public MyCustomAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.Header = listDataHeader;
        this.Child = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.Child.get(this.Header.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        Log.d("check", String.valueOf(groupPosition));
        if (frag.get_num_steps().get(groupPosition) == 0) {
            return null;
        }

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        //final List<Boolean[]> childCheckbox = frag.childCheckboxData();
        final List<String> action_item_ids = frag.action_item_ids_Data();
        final List<String[]> action_item_step_ids = frag.action_item_step_ids_Data();
        final String current_action_id = action_item_ids.get(groupPosition);
        final String current_step_id = action_item_step_ids.get(groupPosition)[childPosition];
        TextView txtListChild = convertView.findViewById(R.id.myListItem);
        CheckBox checkBox3 = convertView.findViewById(R.id.checkBox3);
        CheckBox checkBox4 = convertView.findViewById(R.id.checkBox4);

        txtListChild.setText(childText);
        List<String> completed_steps_list = completed_steps.get(current_action_id);
        Boolean action_completed = false;
        for (int i = 0; i < completed_steps_list.size(); i++) {
            if (completed_steps_list.get(i) == current_step_id) {
                action_completed = true;
            }
        }
        if (action_completed) {
            checkBox3.setChecked(true);
            checkBox3.setClickable(false);
        }
        checkBox4.setChecked(false);
        checkBox4.setClickable(false);

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton complete, boolean isChecked) {

                Log.d("ischeck", String.valueOf(isChecked));

                if (isChecked == true) {
                    //complete.setClickable(false);
                    Log.d("hello", String.valueOf(isChecked));
                    //reason.setVisibility(View.VISIBLE);
                    //save_reason.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Please explain your action in the comment box below.", Toast.LENGTH_LONG).show();
                    save_reason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {
                                final RequestQueue requestQueue = Volley.newRequestQueue(context);
                                String url = "https://y2y.herokuapp.com/actionitemstep";
                                JSONObject jo = new JSONObject();
                                String current_action_id = action_item_ids.get(groupPosition);
                                Log.i("current action id", current_action_id);
                                String current_step_id = action_item_step_ids.get(groupPosition)[childPosition];
                                Log.i("current step id", current_step_id);
                                if (completed_steps == null) {
                                    HashMap<String, List<String>> completed_steps = new HashMap<>();
                                    List<String> current_record = new ArrayList<>();
                                    current_record.add(current_step_id);
                                    completed_steps.put(current_action_id, current_record);
                                    jo.put("size", current_record.size());
                                    jo.put("comment", reason.getText().toString());
                                    jo.putOpt("records", current_record);
                                    jo.put("actionid", current_action_id);
                                }
                                else {
                                    if (completed_steps.get(current_action_id) == null) {
                                        List<String> current_record = new ArrayList<>();
                                        current_record.add(current_step_id);
                                        completed_steps.put(current_action_id, current_record);
                                        jo.put("size", current_record.size());
                                        jo.put("comment", reason.getText().toString());
                                        jo.putOpt("records", current_record);
                                        jo.put("actionid", current_action_id);
                                    }
                                    else {
                                        List<String> current_record = completed_steps.get(current_action_id);
                                        current_record.add(current_step_id);
                                        completed_steps.put(current_action_id, current_record);
                                        jo.put("size", current_record.size());
                                        jo.put("comment", reason.getText().toString());
                                        jo.putOpt("records", current_record);
                                        jo.put("actionid", current_action_id);
                                    }
                                }
                                final String requestBody = jo.toString();

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
                                            Log.i("response", response.toString());
                                        }
                                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                    }
                                };

                                requestQueue.add(stringRequest);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //childCheckbox.get(groupPosition)[childPosition] = true;
                            reason.setText(null);
                            //reason.setVisibility(View.INVISIBLE);
                            //save_reason.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.Child.get(this.Header.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.Header.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.Header.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            TextView myListHeader = convertView.findViewById(R.id.myListHeader);
            CheckBox checkBox1 = convertView.findViewById(R.id.checkBox1);
            CheckBox checkBox2 = convertView.findViewById(R.id.checkBox2);
            final EditText reason = ViewActionFragment.reason;
            final Button save_reason = ViewActionFragment.save_reason;
            myListHeader.setTypeface(null, Typeface.BOLD);
            myListHeader.setText(headerTitle);


            checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton complete, boolean isChecked) {

                    Log.d("ischeck", String.valueOf(isChecked));


                    if (isChecked == true) {

                        Log.d("hello", String.valueOf(isChecked));
                        //reason.setVisibility(View.VISIBLE);
                        //save_reason.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Please explain your action in the comment box below.", Toast.LENGTH_LONG).show();
                        save_reason.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                try {
                                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    String url = "https://y2y.herokuapp.com/actionitems";
                                    String current_action_id = frag.action_item_ids_Data().get(groupPosition);
                                    //completed_steps.put(current_action_id, new ArrayList<String>());
                                    JSONObject jo = new JSONObject();
                                    jo.put("flag", "Completed");
                                    jo.put("actionid", current_action_id);

                                    jo.put("comment", reason.getText().toString());
                                    final String requestBody = jo.toString();
                                    Toast.makeText(context, "Information Saved", Toast.LENGTH_SHORT).show();

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
                                                Log.i("response", response.toString());
                                            }
                                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                        }
                                    };

                                    requestQueue.add(stringRequest);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                reason.setText(null);
                                //reason.setVisibility(View.INVISIBLE);
                                //save_reason.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            });

            checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton drop, boolean isChecked) {

                    Log.d("ischeck", String.valueOf(isChecked));

                    if (isChecked == true) {

                        Log.d("hello", String.valueOf(isChecked));
                        //reason.setVisibility(View.VISIBLE);
                        //save_reason.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Please explain your action in the comment box below.", Toast.LENGTH_LONG).show();
                        save_reason.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                try {
                                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    String url = "https://y2y.herokuapp.com/actionitems";
                                    String current_action_id = frag.action_item_ids_Data().get(groupPosition);
                                    //completed_steps.put(current_action_id, null);
                                    JSONObject jo = new JSONObject();
                                    jo.put("flag", "Dropped");
                                    jo.put("actionid", current_action_id);

                                    jo.put("comment", reason.getText().toString());
                                    final String requestBody = jo.toString();
                                    Toast.makeText(context, "Information Saved", Toast.LENGTH_SHORT).show();

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
                                                Log.i("response", response.toString());
                                            }
                                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                        }
                                    };

                                    requestQueue.add(stringRequest);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                reason.setText(null);
                                //reason.setVisibility(View.INVISIBLE);
                                //save_reason.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            });
        }
        return convertView;
    }
}
