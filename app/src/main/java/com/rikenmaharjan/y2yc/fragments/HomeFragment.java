package com.rikenmaharjan.y2yc.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.R2;
//import com.rikenmaharjan.y2yc.activities.HomeActivity;
import com.rikenmaharjan.y2yc.activities.Main2Activity;
import com.rikenmaharjan.y2yc.utils.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Riken on 3/18/18.
 */

public class HomeFragment extends BaseFragment {



    String rate= new String();



    @BindView(R2.id.fragment_home_commentEt)
    EditText mCommentEt;

    @BindView(R2.id.fragment_home_btnsave)
    Button mSaveButton;

    @BindView(R2.id.fragment_home_btnrating1)
    Button mRate1Button;

    @BindView(R2.id.fragment_home_introEt)
    TextView introTxt;


    String response;

    private Unbinder mUnbinder;

    String id;
    String name;
    SessionManager session;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        mUnbinder = ButterKnife.bind(this,rootView);

        session = new SessionManager(getContext());

        session.checkLogin();




        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_ID);

        //Log.d("session",session.checkLogin());





        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



    }

    @Override
    public void onResume() {
        super.onResume();




        if (getArguments()!=null){

            id = getArguments().getString("id"); //gives me null why??????
            name = getArguments().getString("name");

            Log.e("name",name);

            introTxt.setText("Hello, "+name);



        }







    }

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }



    @OnClick(R2.id.fragment_home_btnrating1)
    public void setmRate1Button(){

        rate = "1";
        mRate1Button.setBackgroundColor(Color.RED);



    }

    @OnClick(R2.id.fragment_home_btnsave)
    public void setmSaveButton(){

        String sComment = mCommentEt.getText().toString();

        if(sComment.equals("")) {
            mCommentEt.setError("Your message");
            //return;
        }
        else{
            Log.i("hell0","enter");
            //Log.i("Id",id);

            new HomeFragment.MyAsyncTaskget().execute(id, "hello", "hello");

        }




    }



    public class MyAsyncTaskget extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            //before works
        }
        @Override
        protected String  doInBackground(String... params) {
            // TODO Auto-generated method stub

            //String json =  "{\"username\": \"sampada\",\"password\":\"password123\"}";

            //id = params[1];
            Log.i("param",params[0]+"."+params[1]);

            String json =  "{\"rating\": \""+rate+"\",\"comment\":\""+mCommentEt.getText().toString()+"\",\"id\":\""+id+"\"}";
            HttpURLConnection urlConnection = null;
            response = new String();


            try {
                //URL url = new URL("http://168.122.222.63:3000/login");  // for BU localhost

                //URL url = new URL("http://192.168.0.38:3000/edituser"); // riken's house
                URL url = new URL("https://y2y.herokuapp.com/edituser");

                //URL url = new URL("http://155.41.34.62:3000/edituser");
                //https://y2y.herokuapp.com/login  my heroku
                //URL url = new URL("http://192.168.0.38:3000/login");  // home local host
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Type","application/json");

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
                //display response dataken


            } catch (Exception ex) {
            }


        }
        protected void onPostExecute(String  result2){

            result2 = result2.substring(0,result2.length()-1); //removing the null char

            Log.i("isSuccess", result2);

            if(result2.equals("success") ) {

                Log.i("isSucess", "sucess");
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
            else if (result2.equals("error")){

                Log.i("isSuccess", "Error");
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
