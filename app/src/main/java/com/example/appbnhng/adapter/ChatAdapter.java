package com.example.appbnhng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbnhng.R;
import com.example.appbnhng.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ChatMessage> chatMessageList;
    private String senderId;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;

    public ChatAdapter(Context context, List<ChatMessage> chatMessageList, String senderId) {
        this.context = context;
        this.chatMessageList = chatMessageList;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View view;
        if (ViewType == TYPE_SEND) {
            view = LayoutInflater.from(context).inflate(R.layout.item_send_message,parent,false);
            return new SendMessViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_recevied_message,parent,false);
            return new ReceivedMessViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SEND) {
            ((SendMessViewHolder) holder).txtmess.setText(chatMessageList.get(position).mess);
            ((SendMessViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
        } else {
            ((ReceivedMessViewHolder) holder).txtmess.setText(chatMessageList.get(position).mess);
            ((ReceivedMessViewHolder) holder).txttime.setText(chatMessageList.get(position).datetime);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessageList.get(position).sendid.equals(senderId)) {
            return TYPE_SEND;
        } else {
            return TYPE_RECEIVE;
        }
    }

    class SendMessViewHolder extends RecyclerView.ViewHolder {
        TextView txtmess, txttime;
        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.text_message_send);
            txttime = itemView.findViewById(R.id.time_message_send);
        }
    }

    class ReceivedMessViewHolder extends RecyclerView.ViewHolder {
        TextView txtmess, txttime;
        public ReceivedMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.text_message_received);
            txttime = itemView.findViewById(R.id.time_message_received);
        }
    }
}
