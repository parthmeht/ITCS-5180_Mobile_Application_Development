package com.parth.android.inclassassignment7;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class SelectAvatarFragment extends Fragment {

    private SelectAvatarFragmentListener mListener;

    public SelectAvatarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_avatar, container, false);
        final ImageButton b1 = view.findViewById(R.id.avatar1);
        final ImageButton b2 = view.findViewById(R.id.avatar2);
        final ImageButton b3 = view.findViewById(R.id.avatar3);
        final ImageButton b4 = view.findViewById(R.id.avatar4);
        final ImageButton b5 = view.findViewById(R.id.avatar5);
        final ImageButton b6 = view.findViewById(R.id.avatar6);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToMyProfile(R.drawable.avatar_f_1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToMyProfile(R.drawable.avatar_f_2);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToMyProfile(R.drawable.avatar_f_3);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToMyProfile(R.drawable.avatar_m_1);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToMyProfile(R.drawable.avatar_m_2);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToMyProfile(R.drawable.avatar_m_3);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SelectAvatarFragment.SelectAvatarFragmentListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement SelectAvatarFragmentListener interface");
        }
    }


    public interface SelectAvatarFragmentListener {
        // TODO: Update argument type and name
        void goToMyProfile(int id);
    }
}
