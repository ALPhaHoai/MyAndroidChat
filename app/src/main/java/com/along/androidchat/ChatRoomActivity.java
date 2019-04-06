package com.along.androidchat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.along.androidchat.adapter.MessageListAdapter;
import com.along.androidchat.model.Message;
import com.along.androidchat.model.Room;
import com.along.androidchat.utils.MyLayoutManager;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long
 * Date: 4/5/2019
 * Time: 11:21 PM
 */
public class ChatRoomActivity extends AppCompatActivity {
    public FloatingActionButton fab;
    public RecyclerView myRecylerView;
    public List<Message> MessageList = new ArrayList<>();
    public MessageListAdapter messageListAdapter;
    public EditText messagetxt;
    public ImageButton send;
    //declare socket object
    private Socket socket;

    public String Nickname;

    private JSONObject data;
    private JSONObject user;
    private Room currentRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_fragment);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentRoom = new Room(extras.getString("currentRoom"), extras.getString("sender"), extras.getString("name"), extras.getString("avatar"));
            // get the nickame of the user
            Nickname = extras.getString(MainActivity.NICKNAME);
        }

        messagetxt = findViewById(R.id.inputMsg);
//        send = findViewById(R.id.btnSend);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ChatRoomActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                myRecylerView.scrollToPosition(messageListAdapter.getItemCount() - 1);
            }
        });

        //setting up recyler message list
        myRecylerView = findViewById(R.id.conversationRecyclerView);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayoutManagerRoom = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManagerRoom);

        setUpMessageListData();


        // add the new updated list to the dapter
        messageListAdapter = new MessageListAdapter(MessageList);

        // notify the adapter to update the recycler view
        messageListAdapter.notifyDataSetChanged();

        //set the adapter for the recycler view
        myRecylerView.setAdapter(messageListAdapter);
//        myRecylerView.scrollToPosition(MessageList.size() - 1);

        myRecylerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
//                    Toast.makeText(ChatRoomActivity.this, "Scrolling up", Toast.LENGTH_SHORT).show();
                    fab.setVisibility(View.VISIBLE);
                    // Scrolling up
                } else {
//                    Toast.makeText(ChatRoomActivity.this, "Scrolling down", Toast.LENGTH_SHORT).show();
                    fab.setVisibility(View.INVISIBLE);
                    // Scrolling down
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });


        setUpSocket();

        // message send action
        if (send != null) {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //retrieve the nickname and the message content and fire the event messagedetection
                    if (!messagetxt.getText().toString().isEmpty()) {
                        try {
                            //send message to server sender_id,receiver_id,room_name, message
                            if (socket != null) {
                                socket.emit("messagedetection", user.getString("User_ID"), currentRoom.getUserID(), currentRoom.getRoomName(), messagetxt.getText().toString());
                            }
                            messagetxt.setText("");
                        } catch (JSONException ex) {

                        }
                    }
                }
            });
        }


    }


    private void setUpMessageListData() {
        for (int i = 0; i < 30; i++) {
            this.MessageList.add(new Message("xin chao", "viet nam"));
        }
    }

    private void setUpSocket() {
        if (socket == null) {
            return;
        }
//        //Connected event
//        //When connected done, then join to chat
//        socket.on("connect", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            final JSONObject jsona = new JSONObject(result);
//                            data = jsona.getJSONObject("data");
//
//                            user = data.getJSONObject("user");
//                            String User_ID = user.getString("User_ID");
//                            socket.emit("join", User_ID);
//
//                        } catch (JSONException jsone) {
//
//                        }
//                    }
//                });
//            }
//        });
//        //implementing socket listeners
//        socket.on("joined", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String data = (String) args[0];
//
//                        Toast.makeText(ChatRoomActivity.this, data, Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatRoomActivity.this, data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event
                            if (!data.isNull("histories")) {
                                JSONArray history = data.getJSONArray("histories");
                                for (int i = 0; i < history.length(); i++) {
                                    JSONObject msg = history.getJSONObject(i);
                                    String nickname = msg.getString("sender");
                                    String message = msg.getString("message");
                                    // make instance of message
                                    Message m = new Message(nickname, message);
                                    //add the message to the messageList
                                    MessageList.add(m);
                                }
                            } else {
                                String nickname = data.getString("sender");
                                String message = data.getString("message");
                                // make instance of message
                                Message m = new Message(nickname, message);

                                //add the message to the messageList
                                MessageList.add(m);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null) {
            socket.disconnect();
        }
    }
}
