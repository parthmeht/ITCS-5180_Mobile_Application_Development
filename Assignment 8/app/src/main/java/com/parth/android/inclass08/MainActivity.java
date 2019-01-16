package com.parth.android.inclass08;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskOperations {

    public static final String TAG = "DEMO";
    private Spinner spinner;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button addButton;
    private EditText note;
    private ArrayList<Task> taskArrayList;
    private ArrayList<Task> completedList;
    private ArrayList<Task> pendingList;
    private ListView listView;
    private TaskAdapter adapter;
    final List<String> priority = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("todolist");

        note = findViewById(R.id.editText);
        addButton = findViewById(R.id.buttonAdd);
        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);
        priority.add("Priority");
        priority.add("High");
        priority.add("Medium");
        priority.add("Low");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                priority
        );
        spinner.setSelection(0);
        spinner.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.getText().toString() == null || note.getText().toString().matches("")) {
                    Toast.makeText(v.getContext(), "Enter a Note", Toast.LENGTH_LONG).show();
                } else if (spinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(v.getContext(), "Select a Priority", Toast.LENGTH_LONG).show();
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                    Date convertedDate = new Date();
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String id = myRef.push().getKey();
                    Task task = new Task(id, note.getText().toString(), spinner.getSelectedItem().toString(), dateFormat.format(convertedDate), false, "Pending");
                    Log.d(TAG, task.toString());
                    myRef.child(id).setValue(task);
                    note.setText("");
                    spinner.setSelection(0);
                }
            }
        });


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                taskArrayList = new ArrayList<>();
                completedList = new ArrayList<>();
                pendingList = new ArrayList<>();
                Log.d(TAG, String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Task task = child.getValue(Task.class);
                    if (task.isCheck())
                        completedList.add(task);
                    else
                        pendingList.add(task);
                    Log.d(TAG, task.toString());
                }
                Comparator<Task> comparator = new Comparator<Task>() {
                    public int compare(Task o1, Task o2) {
                        int p1 = priority.indexOf(o1.getPriority());
                        int p2 = priority.indexOf(o2.getPriority());
                        if (p1 == -1 && p2 != -1) {
                            return 1;
                        }
                        if (p1 != -1 && p2 == -1) {
                            return -1;
                        }
                        if (p1 != p2) {
                            return p1 - p2;
                        }
                        return o1.getNote().compareTo(o2.getNote());
                    }
                };
                Collections.sort(pendingList, comparator);
                Collections.sort(completedList, comparator);
                taskArrayList.addAll(pendingList);
                taskArrayList.addAll(completedList);
                setListView(taskArrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void setListView(ArrayList<Task> arrayList) {
        adapter = new TaskAdapter(this, R.layout.add_task_layout, arrayList, this);
        listView.setAdapter(adapter);
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

    /*private ArrayList<Task> getFilteredListCompleted(){
        ArrayList<Task> t1 = new ArrayList<>();
        for (Task t: taskArrayList) {
            if (t.isCheck())
                t1.add(t);
        }
        return t1;
    }

    private ArrayList<Task> getFilteredListPending(){
        ArrayList<Task> t1 = new ArrayList<>();
        for (Task t: taskArrayList) {
            if (!t.isCheck())
                t1.add(t);
        }
        return t1;
    }*/

    @Override
    public void onCheckBoxClick(Task param) {
        Toast.makeText(getApplicationContext(), "Your list have been updated", Toast.LENGTH_LONG).show();
        myRef.child(param.getId()).setValue(param);
    }

    @Override
    public void deleteTask(final Task task) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Confirm Delete");

        alertDialog.setMessage("Are you sure you want delete this?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                myRef.child(task.getId()).setValue(null);
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

}
