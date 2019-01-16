package com.parth.android.inclassassignment7;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MyProfileFragment.MyProfileFragmentListener,SelectAvatarFragment.SelectAvatarFragmentListener,DisplayProfileFragment.DisplayProfileFragmentListener {

    private int selectedProfile;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new MyProfileFragment(), "MyProfileFragment")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void goToSelectAvatar() {
        setTitle("Select Avatar");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SelectAvatarFragment(), "SelectAvatarFragment")
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void goToDisplayAvatar(User user) {
        setTitle("Display My Profile");
        this.user = user;
        DisplayProfileFragment displayProfileFragment = new DisplayProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(DisplayProfileFragment.USER, user);
        displayProfileFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, displayProfileFragment, "DisplayProfileFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToMyProfile(int id) {
        setTitle("My Profile");
        selectedProfile = id;
        Log.d("Demo","Selected id = "+selectedProfile);
        MyProfileFragment myProfileFragment = (MyProfileFragment) getSupportFragmentManager().findFragmentByTag("MyProfileFragment");
        if (myProfileFragment == null) {
            myProfileFragment = new MyProfileFragment();
        }
        Bundle args = new Bundle();
        args.putInt(MyProfileFragment.IMAGEVALUE,id);
        myProfileFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myProfileFragment, "MyProfileFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToProfileScreen() {
        setTitle("My Profile");
        MyProfileFragment myProfileFragment = (MyProfileFragment) getSupportFragmentManager().findFragmentByTag("MyProfileFragment");
        if (myProfileFragment == null) {
            myProfileFragment = new MyProfileFragment();
        }
        Bundle args = new Bundle();
        args.putInt(MyProfileFragment.IMAGEVALUE,selectedProfile);
        myProfileFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, myProfileFragment, "MyProfileFragment")
                .addToBackStack(null)
                .commit();
    }
}
