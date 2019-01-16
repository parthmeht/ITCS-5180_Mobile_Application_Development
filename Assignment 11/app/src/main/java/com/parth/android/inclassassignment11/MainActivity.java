package com.parth.android.inclassassignment11;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskOperations {

    public static final String TAG = "DEMO";
    private Spinner spinner;
    private Button addButton;
    private EditText note;
    private ListView listView;
    final List<String> priority = new ArrayList<String>();
    private Realm mRealm;
    private ArrayList<Task> taskArrayList;
    private ArrayList<Task> completedList;
    private ArrayList<Task> pendingList;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Realm
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
        Log.i("Realm", mRealm.getPath());
        note = findViewById(R.id.editText);
        addButton = findViewById(R.id.buttonAdd);
        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);
        priority.add("Priority");
        priority.add("High");
        priority.add("Medium");
        priority.add("Low");
        getList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                priority
        );
        spinner.setSelection(0);
        spinner.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (note.getText().toString() == null || note.getText().toString().matches("")) {
                    Toast.makeText(view.getContext(), "Enter a Note", Toast.LENGTH_LONG).show();
                } else if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(view.getContext(), "Select a Priority", Toast.LENGTH_LONG).show();
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    Date convertedDate = new Date();
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    mRealm.beginTransaction();
                    Task task = mRealm.createObject(Task.class, getNextKey());
                    //task.setId(getNextKey());
                    task.setNote(note.getText().toString());
                    task.setPriority(spinner.getSelectedItem().toString());
                    task.setTime(dateFormat.format(convertedDate));
                    task.setStatus("Pending");
                    mRealm.commitTransaction();
                    Log.d(TAG, task.toString());
                    note.setText("");
                    spinner.setSelection(0);
                    getList();
                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public void setListView(ArrayList<Task> arrayList) {
        adapter = new TaskAdapter(this, R.layout.add_task_layout, arrayList, this);
        listView.setAdapter(adapter);
    }

    public void getList() {
        RealmResults<Task> results = mRealm.where(Task.class).findAllAsync().sort("priorityId");
        //fetching the data
        results.load();
        taskArrayList = new ArrayList<>();
        completedList = new ArrayList<>();
        pendingList = new ArrayList<>();
        for (Task task : results) {
            if (task.getStatus().equalsIgnoreCase("Pending"))
                pendingList.add(task);
            else
                completedList.add(task);
            Log.d(TAG, task.toString());
        }
        taskArrayList.addAll(pendingList);
        taskArrayList.addAll(completedList);
        Toast.makeText(getApplicationContext(), "Your list have been updated", Toast.LENGTH_LONG).show();
        setListView(taskArrayList);
    }

    public int getNextKey() {
        try {
            Number number = mRealm.where(Task.class).max("id");
            if (number != null) {
                return number.intValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }


    @Override
    public void onCheckBoxClick(Task task, String status, String time) {
        Task first = mRealm.where(Task.class)
                .equalTo("id", task.getId())
                .findFirst();
        mRealm.beginTransaction();
        if (first == null) {
            Task object = mRealm.createObject(Task.class, task.getId());
            object.setNote(task.getNote());
            object.setPriority(task.getPriority());
            object.setTime(time);
            object.setStatus(status);
        } else {
            first.setTime(time);
            first.setStatus(status);
        }
        mRealm.commitTransaction();
        getList();
    }

    @Override
    public void deleteTask(final Task task) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Confirm Delete");

        alertDialog.setMessage("Are you sure you want delete this?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mRealm.beginTransaction();
                Task result = mRealm.where(Task.class).equalTo("id", task.getId()).findFirst();
                result.deleteFromRealm();
                mRealm.commitTransaction();
                getList();            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.show_all) {
            setListView(taskArrayList);
        } else if (id == R.id.show_completed) {
            setListView(completedList);
        } else if (id == R.id.show_pending) {
            setListView(pendingList);
        }
        return super.onOptionsItemSelected(item);
    }
}
