package com.scanba.hibuddy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scanba.hibuddy.R;
import com.scanba.hibuddy.models.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Chat> chatHistory;
    private SimpleDateFormat dateFormat;

    public ChatHistoryAdapter(Context context, ArrayList<Chat> chatHistory) {
        this.chatHistory = chatHistory;
        layoutInflater = LayoutInflater.from(context);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    }

    @Override
    public ChatHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.chat_message, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatHistoryAdapter.MyViewHolder holder, int position) {
        Chat chat = chatHistory.get(position);
        holder.author.setText(chat.getAuthor());
        holder.message.setText(chat.getMessage());
        Date date = new Date(chat.getSentAt());
        holder.sentAt.setText(dateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return chatHistory.size();
    }

    public int add(Chat chat) {
        chatHistory.add(chat);
        int position = chatHistory.size() - 1;
        notifyItemInserted(position);
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView author, message, sentAt;

        public MyViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            message = (TextView) itemView.findViewById(R.id.message);
            sentAt = (TextView) itemView.findViewById(R.id.sent_at);
        }
    }
}
