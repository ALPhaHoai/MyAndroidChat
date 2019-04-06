package com.along.androidchat.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.along.androidchat.R;
import com.along.androidchat.model.Room;

import java.util.List;

/**
 * Created by Long
 * Date: 4/5/2019
 * Time: 11:19 PM
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.MyViewHolder> {
    private List<Room> RoomList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView smReceivers;
        public TextView createdAtTime;
        public TextView latestMessage;
        public TextView unreadSmsCount;
        ImageView activeStatus;


        public MyViewHolder(View view) {
            super(view);

            smReceivers = view.findViewById(R.id.smReceivers);
            createdAtTime = view.findViewById(R.id.createdAtTime);
            latestMessage = view.findViewById(R.id.message);
            unreadSmsCount = view.findViewById(R.id.unreadSmsCount);
            activeStatus = view.findViewById(R.id.active_status_circle);
        }
    }

    public RoomListAdapter(List<Room> RoomList) {
        this.RoomList = RoomList;
    }

    @Override
    public int getItemCount() {
        return RoomList.size();
    }

    @Override
    public RoomListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item_layout, parent, false);
        return new RoomListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RoomListAdapter.MyViewHolder holder, final int position) {
        final Room m = RoomList.get(position);
        holder.smReceivers.setText(m.getRoomName() + " : ");
        holder.createdAtTime.setText(m.getUserID());
        holder.latestMessage.setText(m.getName());

        if (m.getUnread() == 0) {
            holder.unreadSmsCount.setVisibility(View.INVISIBLE);
        } else {
            holder.unreadSmsCount.setVisibility(View.VISIBLE);
            holder.unreadSmsCount.setText("" + m.getUnread());
        }

        holder.activeStatus.setVisibility(m.isActive() ? View.VISIBLE : View.GONE);
    }


}