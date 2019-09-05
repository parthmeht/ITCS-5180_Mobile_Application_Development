package com.groupr4.android.inclassassignment6;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MsgAdapter extends BaseAdapter implements ListAdapter {

    private final OkHttpClient client = new OkHttpClient();
    private static String token;
    private User user;
    private ArrayList<Msg> list;
    private Context context;
    ChatOperations chatOperations;


    public MsgAdapter(User user, ArrayList<Msg> list, Context context, ChatOperations chatOperations) {
        this.user = user;
        this.list = list;
        this.context = context;
        this.chatOperations = chatOperations;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Msg getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Msg current_msg = getItem(position);
        if (current_msg != null) {

            ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.add_message_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.UserName = (TextView) convertView.findViewById(R.id.txtUserName);
                viewHolder.Messages = (TextView) convertView.findViewById(R.id.txteachMessgae);
                viewHolder.MsgTime = (TextView) convertView.findViewById(R.id.txtMsgTime);
                viewHolder.DeleteMsg = (ImageButton) convertView.findViewById(R.id.imgdeletemsg);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(convertView.getContext());
            token = preferences.getString("Token", "");
            Msg m = (Msg) getItem(position);
            if (m.user_id.equals(user.userId.toString())) {
                viewHolder.DeleteMsg.setVisibility(View.VISIBLE);
                viewHolder.UserName.setText("Me");
            } else {
                viewHolder.UserName.setText(current_msg.user_fname.trim() + " " + current_msg.user_lname.trim());
            }
            viewHolder.DeleteMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(position);
                }
            });
            if (current_msg.msgContent != null) {
                viewHolder.Messages.setText(current_msg.msgContent.trim());
            }
            String date = messageTime(current_msg);
            if (!(date.equals("") || date.equals(null))) {
                viewHolder.MsgTime.setText(date);
            }


        }
        return convertView;
    }


    private static class ViewHolder {
        TextView Messages;
        TextView UserName;
        TextView MsgTime;
        ImageButton DeleteMsg;

    }

    public void delete(final int position) {
        Msg msg = (Msg) getItem(position);
        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/message/delete/" + msg.msg_id)
                .header("Authorization", "BEARER " + token)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                chatOperations.deleteChat(position);
            }
        });
    }

    public String messageTime(Msg msg) {
        PrettyTime prettyTime = new PrettyTime();

        String dateString = msg.createdAt;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return prettyTime.format(convertedDate);

    }
}
