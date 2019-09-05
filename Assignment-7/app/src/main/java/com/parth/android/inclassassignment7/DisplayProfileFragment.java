package com.parth.android.inclassassignment7;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class DisplayProfileFragment extends Fragment {

    private DisplayProfileFragmentListener mListener;
    private User user;
    private TextView name;
    private Button buttonEdit;
    private TextView studentId;
    private TextView department;
    private ImageView avatarImage;
    public static final String USER = "user";

    public DisplayProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_profile, container, false);

        name = view.findViewById(R.id.textViewNameValue);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        studentId = view.findViewById(R.id.textViewStudentIdValue);
        department = view.findViewById(R.id.textViewDepatmentValue);
        avatarImage = view.findViewById(R.id.imageViewDisplay);
        if (getArguments()!=null){
            user = (User) getArguments().getSerializable(USER);
            name.setText(user.getFirstName() + " " +user.getLastName());
            studentId.setText(user.getStudentId());
            department.setText(user.getDepartment());
            avatarImage.setImageResource(user.getAvatarImageValue());
        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToProfileScreen();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DisplayProfileFragment.DisplayProfileFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DisplayProfileFragmentListener interface");
        }
    }


    public interface DisplayProfileFragmentListener {
        void goToProfileScreen();
    }
}
