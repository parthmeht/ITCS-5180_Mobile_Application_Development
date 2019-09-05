package com.parth.android.inclassassignment9;

import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private List<Message> messageList;
    private User user;
    private MessageAdapterListener messageAdapterListener;
    public MessageAdapter(List<Message> messageList, User user, MessageAdapterListener messageAdapterListener) {
        this.messageList = messageList;
        this.user = user;
        this.messageAdapterListener = messageAdapterListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Message message = messageList.get(position);
        holder.message.setText(message.getMessage());
        holder.displayName.setText(message.getUserName());
        holder.time.setText(messageTime(message.getTime()));

        if (message.getUri()==null){
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.constraintLayout);
            constraintSet.connect(R.id.textViewName,ConstraintSet.TOP,R.id.textViewMessageDisplay,ConstraintSet.BOTTOM,8);
            constraintSet.applyTo(holder.constraintLayout);
            holder.constraintLayout.removeView(holder.imageView);


            //holder.imageView.setVisibility(View.INVISIBLE);
        }
        else{
            holder.imageView.setImageURI(Uri.parse(message.getUri()));
            holder.imageView.setVisibility(View.VISIBLE);
            Log.d("MESSAGE",message.getUri());
        }

        if (!user.getUid().equalsIgnoreCase(message.getUserId())){
            holder.delete.setVisibility(View.INVISIBLE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageAdapterListener.delete(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message, displayName, time;
        public ImageButton delete;
        public ImageView imageView;
        public ConstraintLayout constraintLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.textViewMessageDisplay);
            displayName = itemView.findViewById(R.id.textViewName);
            time = itemView.findViewById(R.id.textViewTime);
            delete = itemView.findViewById(R.id.imageButtonDelete);
            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.messageRowConstraint);
        }
    }

    public String messageTime(String time) {
        PrettyTime prettyTime = new PrettyTime();
        String dateString = time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return prettyTime.format(convertedDate);
    }

    interface MessageAdapterListener{
        void delete(Message message);
    }
}
