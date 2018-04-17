package com.rikenmaharjan.y2yc.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.graphics.Typeface;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.rikenmaharjan.y2yc.R;

// FOLLOWED THIS TUTORIAL: https://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
// And referenced this: http://helloiamandroid.blogspot.com/2012/11/hello-android-developers_29.html

public class ViewActionFragment extends BaseFragment {
    //BaseFragmentActivity calls the following method and create LoginFragment to add to this activity

    public static ViewActionFragment newInstance(){
        return new ViewActionFragment();
    }

    private
    ExpandableListView actions;
    ExpandableListAdapter adapter;
    List<String> Header;
    HashMap<String, List<String>> Child;
    TextView txtview;
    ArrayList<String> isCheckedStatus = new ArrayList<String>();
    CheckBox checkBox1;
    CheckBox checkBox2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_action,container,false);
        txtview = rootView.findViewById(R.id.txtView);
        txtview.setText("Here are your ongoing goals and action items. We would love to hear any updates or progress!");
        actions = rootView.findViewById(R.id.actions);
        prepareListData();
        adapter = new MyCustomAdapter(this.getContext(), Header, Child);
        actions.setAdapter(adapter);
        actions.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Toast.makeText(getActivity().getApplicationContext(),"Group Clicked " + Header.get(groupPosition), Toast.LENGTH_SHORT).show();;
                return false;
            }
        });

        actions.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(), Header.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        actions.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),Header.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
            }
        });

        actions.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getActivity().getApplicationContext(),Header.get(groupPosition) + " : " + Child.get(Header.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return rootView;
    }

    private void prepareListData() {
        Header = new ArrayList<String>();
        Child = new HashMap<String, List<String>>();

        // Adding child data
        Header.add("Apply for housing");
        Header.add("Get a replacement Mass ID");
        Header.add("Complete resume");
        Header.add("Get new glasses");

        // Adding child data
        List<String> action1 = new ArrayList<String>();
        action1.add("step 1");
        action1.add("step 2");
        action1.add("step 3");
        action1.add("step 4");

        List<String> action2 = new ArrayList<String>();
        action2.add("step 1");
        action2.add("step 2");
        action2.add("step 3");
        action2.add("step 4");

        List<String> action3 = new ArrayList<String>();
        action3.add("step 1");
        action3.add("step 2");
        action3.add("step 3");
        action3.add("step 4");

        List<String> action4 = new ArrayList<String>();
        action4.add("step 1");
        action4.add("step 2");
        action4.add("step 3");
        action4.add("step 4");

        Child.put(Header.get(0), action1); // Header, Child data
        Child.put(Header.get(1), action2);
        Child.put(Header.get(2), action3);
        Child.put(Header.get(3), action4);
    }
}


class MyCustomAdapter extends BaseExpandableListAdapter {
    private
    Context context;
    List<String> Header;
    HashMap<String, List<String>> Child;
    ArrayList<String> isCheckedStatus = new ArrayList<String>();
    CheckBox checkBox1;
    CheckBox checkBox2;

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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_view_action, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.myListItem);

        txtListChild.setText(childText);
        return convertView;
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView myListHeader = convertView.findViewById(R.id.myListHeader);
        CheckBox checkBox1 = convertView.findViewById(R.id.checkBox1);
        CheckBox checkBox2 = convertView.findViewById(R.id.checkBox2);
        myListHeader.setTypeface(null, Typeface.BOLD);
        myListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
/*
class GroupHolder {
    ImageView checkbox_complete;
    ImageView checkbox_drop;
    TextView myListHeader;
}
*/