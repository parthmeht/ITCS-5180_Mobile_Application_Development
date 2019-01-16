package com.parth.android.inclassassignment9;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpListener} interface
 * to handle interaction events.
 */
public class SignUpFragment extends Fragment {

    private SignUpListener mListener;
    private Button loginButton, signUpButton;
    private String TAG = "SignUpTag";
    private EditText firstNameEdit, lastNameEdit, emailEdit, passwordEdit, confirmPasswordEdit;
    private User user;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        loginButton = view.findViewById(R.id.buttonLogin);
        signUpButton = view.findViewById(R.id.buttonSignUp);
        firstNameEdit = view.findViewById(R.id.editTextFirstName);
        lastNameEdit = view.findViewById(R.id.editTextLastName);
        emailEdit = view.findViewById(R.id.editTextEmail);
        passwordEdit = view.findViewById(R.id.editTextPassword);
        confirmPasswordEdit = view.findViewById(R.id.editTextConfirmPassword);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Sign Up Clicked");
                if (firstNameEdit.getText() == null || firstNameEdit.getText().toString().equalsIgnoreCase("")) {
                    firstNameEdit.setError("Enter First Name");
                } else if (lastNameEdit.getText() == null || lastNameEdit.getText().toString().equalsIgnoreCase("")) {
                    lastNameEdit.setError("Enter Last Name");
                } else if (emailEdit.getText() == null || emailEdit.getText().toString().equalsIgnoreCase("")) {
                    emailEdit.setError("Enter Email Id");
                } else if (passwordEdit.getText() == null || passwordEdit.getText().toString().equalsIgnoreCase("")) {
                    passwordEdit.setError("Enter Password");
                } else if (confirmPasswordEdit.getText() == null || confirmPasswordEdit.getText().toString().equalsIgnoreCase("")) {
                    confirmPasswordEdit.setError("Enter Confirm Password");
                } else if (passwordEdit.getText() != null
                        && !passwordEdit.getText().toString().equalsIgnoreCase("")
                        && confirmPasswordEdit.getText() != null
                        && !confirmPasswordEdit.getText().toString().equalsIgnoreCase("")
                        && !passwordEdit.getText().toString().equalsIgnoreCase(confirmPasswordEdit.getText().toString())) {
                    confirmPasswordEdit.setError("Password and Confirm Password does not match");
                } else if (passwordEdit.getText().length() < 6 || confirmPasswordEdit.getText().length() < 6) {
                    passwordEdit.setError("Password has to be 6 or more than 6 characters");
                } else {
                    user = new User(firstNameEdit.getText().toString(), lastNameEdit.getText().toString(), emailEdit.getText().toString(), passwordEdit.getText().toString());
                    Log.d("User", user.toString());
                    mListener.signUp(user);
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToLogin();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SignUpListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement SignUpListener interface");
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
    public interface SignUpListener {
        // TODO: Update argument type and name
        void goToLogin();
        void signUp(User user);
    }
}
