package com.along.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.along.androidchat.adapter.RoomListAdapter;
import com.along.androidchat.model.Room;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Long
 * Date: 4/5/2019
 * Time: 11:21 PM
 */
public class RoomListActivity extends AppCompatActivity {
    public RecyclerView myRecylerViewRoomList;
    public List<Room> RoomList = new ArrayList<>();
    public RoomListAdapter roomListAdapter;

    //declare socket object
    private Socket socket;

    public String Nickname;

    private String token;
    private JSONObject data;
    private JSONObject user;
    private Room currentRoom;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesHelper pre = new PreferencesHelper(RoomListActivity.this);
        token = pre.getToken();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        if (getIntent().getExtras() != null) {
            result = getIntent().getExtras().getString("RESULT");
            // get the nickame of the user
            Nickname = getIntent().getExtras().getString(MainActivity.NICKNAME);
            token = Nickname;
            createConnection();
        }

        myRecylerViewRoomList = findViewById(R.id.roomList);
        RecyclerView.LayoutManager mLayoutManagerRoom = new LinearLayoutManager(getApplicationContext());
        myRecylerViewRoomList.setLayoutManager(mLayoutManagerRoom);
        myRecylerViewRoomList.setItemAnimator(new DefaultItemAnimator());


//            setUpRoomListData();
        setUpRoomListDataFake();

        // add the new updated list to the dapter
        roomListAdapter = new RoomListAdapter(RoomList);

        // notify the adapter to update the recycler view

        roomListAdapter.notifyDataSetChanged();

        //set the adapter for the recycler view

        myRecylerViewRoomList.setAdapter(roomListAdapter);


        myRecylerViewRoomList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, myRecylerViewRoomList, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        currentRoom = RoomList.get(position);
                        try {
                            // Create room, that mean user need to create chat room with an admin
                            //we need 3 parameters current user id, recever id and room_name
                            JSONObject msgData = new JSONObject();
                            if (user != null) {
                                msgData.put("sender", user.getString("User_ID"));
                            }
                            msgData.put("receive", currentRoom.getUserID());
                            msgData.put("currentRoom", currentRoom.getRoomName());

                            if (socket != null) {
                                socket.emit("createroom", msgData);
                            }


                            Intent intent = new Intent(RoomListActivity.this, ChatRoomActivity.class);
                            intent.putExtra("currentRoom", new Gson().toJson(currentRoom));
                            startActivity(intent);

//                            chatBoxAdapter.notifyDataSetChanged();

                        } catch (Exception jsone) {
                            Log.e("Chat box activity", "Something went wrong!");
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        setUpSocket();
    }

    private void setUpSocket() {
        if (socket == null) {
            return;
        }
        //Connected event
        //When connected done, then join to chat
        socket.on("connect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsona = new JSONObject(result);
                            data = jsona.getJSONObject("data");

                            user = data.getJSONObject("user");
                            String User_ID = user.getString("User_ID");
                            socket.emit("join", User_ID);

                        } catch (JSONException jsone) {

                        }
                    }
                });
            }
        });
        //implementing socket listeners
        socket.on("joined", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(RoomListActivity.this, data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(RoomListActivity.this, data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
//        socket.on("message", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        JSONObject data = (JSONObject) args[0];
//                        try {
//                            //extract data from fired event
//
//
//                            if (!data.isNull("histories")) {
//                                JSONArray history = data.getJSONArray("histories");
//                                for (int i = 0; i < history.length(); i++) {
//                                    JSONObject msg = history.getJSONObject(i);
//                                    String nickname = msg.getString("sender");
//                                    String message = msg.getString("message");
//
//                                    // make instance of message
//
//                                    Message m = new Message(nickname, message);
//                                    //add the message to the messageList
//
//                                    MessageList.add(m);
//                                }
//
//                            } else {
//                                String nickname = data.getString("sender");
//                                String message = data.getString("message");
//
//                                // make instance of message
//
//                                Message m = new Message(nickname, message);
//
//
//                                //add the message to the messageList
//
//                                MessageList.add(m);
//                            }
//
//
//                            // add the new updated list to the dapter
//                            chatBoxAdapter = new ChatBoxAdapter(MessageList);
//
//                            // notify the adapter to update the recycler view
//
//                            chatBoxAdapter.notifyDataSetChanged();
//
//                            //set the adapter for the recycler view
//
//                            myRecylerView.setAdapter(chatBoxAdapter);
//                            myRecylerView.scrollToPosition(MessageList.size() - 1);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                });
//            }
//        });
    }

    private void createConnection() {
        try {
            IO.Options options = new IO.Options();
            options.query = "token=" + token;
            socket = IO.socket("http://192.168.1.114:3000", options);
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpRoomListData() {
        try {
            //parse user| room list
            final JSONObject jsona = new JSONObject(result);
            JSONObject data = jsona.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");
            JSONArray clients = user.getJSONArray("clients");
            for (int i = 0; i < clients.length(); i++) {
                JSONObject item = clients.getJSONObject(i);
                Room room = new Room(
                        item.getString("Room_Name"),
                        item.getString("User_ID"),
                        item.getString("name "),
                        item.getString("Avatar"),
                        Boolean.valueOf(item.getString("isActive"))
                );
                if (i == 0) {
                    currentRoom = room;
                }
                RoomList.add(room);
            }
        } catch (JSONException e) {
            Log.e("Chat box activity", "Something went wrong!");
        }
    }

    private void setUpRoomListDataFake() {
        for (int i = 0; i < 30; i++) {
            Room room = new Room(
                    ("Room_Name hhhhhhh hhhhhhhhhhhhhh hhhhhhhhhhhhhhhhhhhhh hhhhhhhhhhhhhhhhhhhh"),
                    ("user_id"),
                    ("name hhhhhhhhhhh hhhhhhhhhhhhhh hhhhhhhhhhhhhhhhhhhhh hhhhhhhhhhhhhhhhhhhhh"),
                    ("Avatar"),
                    new Random().nextInt(2) == 1
            );
            room.setUnread((new Random().nextInt(10)));
            if (i == 0) {
                currentRoom = room;
            }
            RoomList.add(room);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null) {
            socket.disconnect();
        }
    }
}
