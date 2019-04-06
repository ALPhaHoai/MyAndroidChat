package com.along.androidchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.along.androidchat.utils.HTTPPostTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Long
 * Date: 4/5/2019
 * Time: 11:21 PM
 */
public class MainActivity extends AppCompatActivity {


    private Button btn;
    private EditText nickname;
    public static final String NICKNAME = "usernickname";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //call UI component  by id
        btn = (Button) findViewById(R.id.enterchat) ;
        nickname = (EditText) findViewById(R.id.nickname);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the nickname is not empty go to chatbox activity and add the nickname to the intent extra
                if(!nickname.getText().toString().isEmpty()){
                    calc(nickname.getText().toString());
                }
            }
        });



    }

    public void calc(String nickname) {
        Toast.makeText(this,"Sending data", Toast.LENGTH_SHORT).show();

        //getting data from view
        /* creating request
         *   passing context, url and listener
         */
        HTTPPostTask httpPostTask=new HTTPPostTask(this, "http://192.168.1.114:3000/login", new HTTPPostTask.HTTPPostTaskListener() {

            //What to do when the result is available
            @Override
            public void onDataReceived(String result) {
                //decoding json string
                try {
                    final JSONObject jsona=new JSONObject(result);
                    JSONObject data = jsona.getJSONObject("data");
                    String token = data.getString("token");
                    PreferencesHelper pre = new PreferencesHelper(MainActivity.this);
                    pre.setToken(token);
                    pre.setUsers(result);

                    Intent i  = new Intent(MainActivity.this, RoomListActivity.class);
                    //retreive nickname from textview and add it to intent extra
                    i.putExtra(NICKNAME,token);
                    i.putExtra("RESULT",result);

                    startActivity(i);

                }catch (JSONException jsone){
                    Log.e("MainActivity", "Something went wrong!");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Context context){
                Log.d("MainActivity", "An error has occurred");
            }
        });

        /* adding post parameter:
         *   parameter name: num
         *  parameter value: et.getText().toString()
         */
        httpPostTask.addPostParam("username", nickname);
        // sending request
        httpPostTask.execute();
    }
}
