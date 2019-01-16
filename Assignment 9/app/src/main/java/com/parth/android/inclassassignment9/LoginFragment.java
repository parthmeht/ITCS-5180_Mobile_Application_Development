package com.parth.android.inclassassignment9;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragmentListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private LoginFragmentListener mListener;
    private Button loginBtn, signUpBtn;
    private EditText email, password;
    private String TAG = "LoginTag";

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginBtn = view.findViewById(R.id.btnLogin);
        signUpBtn = view.findViewById(R.id.btnSignUp);
        email = view.findViewById(R.id.editTextLoginEmail);
        password = view.findViewById(R.id.editTextLoginPassword);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Login Button Clicked");
                if (!MainActivity.validateEmail(email.getText().toString())) {
                    Toast.makeText(v.getContext(), "Blank/Invalid Email Id!", Toast.LENGTH_SHORT).show();
                } else if (!MainActivity.validatePassword(password.getText().toString())) {
                    Toast.makeText(v.getContext(), "Blank/Invalid Password!", Toast.LENGTH_SHORT).show();
                } else {
                    mListener.login(email.getText().toString(),password.getText().toString());
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSignUp();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (LoginFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement LoginFragmentListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface LoginFragmentListener {
        void goToSignUp();
        void login(String email, String password);
    }
}
