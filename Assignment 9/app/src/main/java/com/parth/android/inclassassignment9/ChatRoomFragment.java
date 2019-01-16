package com.parth.android.inclassassignment9;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatRoomListener} interface
 * to handle interaction events.
 */
public class ChatRoomFragment extends Fragment implements MessageAdapter.MessageAdapterListener {

    private static final String TAG = "ChatRoomTag";
    private ChatRoomListener mListener;
    private ImageButton signOut,addImage,sendMessage;
    private TextView displayName;
    private User user;
    private EditText messageText;
    public Message message;
    public Uri uri;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef;
    private ArrayList<Message> messageArrayList;
    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private byte[] byteArray;

    public ChatRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            this.uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                addImage.setImageBitmap(bitmap);
                addImage.setDrawingCacheEnabled(true);
                addImage.buildDrawingCache();
                bitmap = addImage.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                addImage.setDrawingCacheEnabled(false);
                //uploadImage(uri);
                byteArray = byteArrayOutputStream.toByteArray();

                Fragment frg = null;
                frg = getFragmentManager().findFragmentByTag("ChatRoomFragment1");
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        signOut = view.findViewById(R.id.signOutButton);
        displayName = view.findViewById(R.id.textViewDisplayName);
        addImage = view.findViewById(R.id.addImageButton);
        sendMessage = view.findViewById(R.id.sendButton);
        messageText = view.findViewById(R.id.editTextMessage);
        recyclerView = view.findViewById(R.id.recyclerView);
        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.addimage);
        addImage.setImageBitmap(icon);
        if (getArguments()!=null){
            user = (User) getArguments().getSerializable(MainActivity.USER);
            displayName.setText(user.getDisplayName());
        }

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageArrayList = new ArrayList<>();
                Log.d(TAG, String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Message m = child.getValue(Message.class);
                    messageArrayList.add(m);
                }

                mAdapter = new MessageAdapter(messageArrayList,user,ChatRoomFragment.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage.setImageResource(0);
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
                }
                else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), MainActivity.PICK_IMAGE_REQUEST);
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.signOut();
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageText.getText() == null || messageText.getText().toString().equalsIgnoreCase("")) {
                    messageText.setError("Enter Message");
                }else {
                    String id = myRef.push().getKey();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    Date convertedDate = new Date();
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    message = new Message(id,user.getUid(),messageText.getText().toString(),dateFormat.format(convertedDate),user.getDisplayName());
                    if (uri!=null){
                        StorageReference riversRef = MainActivity.mStorageRef.child("images/"+id+".png");
                        riversRef.putBytes(byteArray).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                message.setUri(taskSnapshot.getUploadSessionUri().toString());
                            }
                        });
                    }
                    addMessage(message);
                }
            }
        });
        return view;
    }



    private void addMessage(Message message) {
        myRef.child(message.getId()).setValue(message);
        messageText.setText("");
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ChatRoomListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ChatRoomListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void delete(Message message) {
        myRef.child(message.getId()).setValue(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ChatRoomListener {
        void signOut();
    }
}
