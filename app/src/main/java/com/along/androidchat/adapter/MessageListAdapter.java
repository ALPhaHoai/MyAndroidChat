package com.along.androidchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.along.androidchat.R;
import com.along.androidchat.model.Message;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Long
 * Date: 4/5/2019
 * Time: 11:18 PM
 */
public class MessageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int SENDING_MESSAGE = 0;
    public static final int INCOMING_MESSAGE = 1;

    public static String[] messageSample = new String[]{
            "lf, that is far far from as good as the proposed solution. new Random() tries to create an instance that has a different seed than any previously created Random. Your approach would break horribly just by invoking the function twice in short ti",
            "lf, that is far far from as good as the proposed solution. new Random() tries to create an instance that has a different seed than any",
            "lf, that is far far from as good as the proposed solution. new Ran",
            "lf, that is far far from a",
            "lf, that is far ",
            "lf, that i",
            "lf, tha",
            "lf"
    };

    private List<Message> MessageList;

    public class IncommingMessageViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView avatar;
        public TextView message;

        public IncommingMessageViewHolder(View view) {
            super(view);
            avatar = view.findViewById(R.id.avatar);
            message = view.findViewById(R.id.message_body);
        }
    }

    public class SendingMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView message;

        public SendingMessageViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message_body);
        }
    }

    public MessageListAdapter(List<Message> MessagesList) {
        this.MessageList = MessagesList;
    }

    @Override
    public int getItemViewType(int position) {
        Random rand = new Random();
        int n = rand.nextInt(2);
        return (n == 0) ? INCOMING_MESSAGE : SENDING_MESSAGE;
    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case INCOMING_MESSAGE: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incoming_message_item, parent, false);
                return new IncommingMessageViewHolder(view);
            }
            case SENDING_MESSAGE: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sending_message_item, parent, false);
                return new SendingMessageViewHolder(view);
            }
            default: {
                throw new RuntimeException("The type has to be INCOMING_MESSAGE or SENDING_MESSAGE");
            }
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder == null) {
            return;
        }
        final Message m = MessageList.get(position);
        String messageBody = messageSample[new Random().nextInt(messageSample.length)];
//        String messageBody = m.getMessage();
        switch (holder.getItemViewType()) {
            case INCOMING_MESSAGE:
//                ((IncommingMessageViewHolder) holder).avatar.
                ((IncommingMessageViewHolder) holder).message.setText(messageBody);
                break;
            case SENDING_MESSAGE:
                ((SendingMessageViewHolder) holder).message.setText(messageBody);
                break;
        }
    }

}