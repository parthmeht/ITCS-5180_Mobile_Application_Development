/*
 * Copyright (c)
 * Parth Mehta
 * 801057625
 */

package com.parth.android.expensemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.Listener {

    public static final String TAG = "demo";
    public static final String MONTHLY_BUDGET = "Monthly_Budget";
    public static final String EXPENSES = "Expenses";
    Button setButton;
    EditText monthlyBudget;
    TextView spentValue;
    TextView displayTextView;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference eyRef;
    MonthlyBudget budgetObj;
    ArrayList<Expense> expenses= new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Expense Manager");
        setButton = findViewById(R.id.set_button);
        monthlyBudget = findViewById(R.id.set_budget_edit_text);
        spentValue = findViewById(R.id.spent_value_text_view);
        progressBar = findViewById(R.id.progressBar);
        displayTextView = findViewById(R.id.displayTextView);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(MONTHLY_BUDGET);
        eyRef = database.getReference(EXPENSES);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setButtonText = (String) setButton.getText();
                if (setButtonText.equalsIgnoreCase("SET")){
                    String monthlyBudgetValue = monthlyBudget.getText().toString();
                    if (monthlyBudgetValue==null || monthlyBudgetValue.equalsIgnoreCase("")){
                        Toast.makeText(view.getContext(),"Enter a monthly budget", Toast.LENGTH_LONG).show();
                    }else{
                        float budget = Float.parseFloat(monthlyBudgetValue);
                        if (budgetObj==null){
                            budgetObj = new MonthlyBudget(budget);
                        }else{
                            if (budget>=budgetObj.spent)
                                budgetObj.setBudget(budget);
                            else
                                Toast.makeText(view.getContext(),"Invalid monthly budget",Toast.LENGTH_LONG).show();
                        }
                        myRef.setValue(budgetObj);
                    }
                }else{
                    Log.d(TAG, "Inside Edit");
                    monthlyBudget.setVisibility(View.VISIBLE);
                    displayTextView.setVisibility(View.INVISIBLE);
                    setButton.setText("SET");
                }
            }
        });

        findViewById(R.id.add_new_expense_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                intent.putExtra(MONTHLY_BUDGET, budgetObj);
                startActivity(intent);
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                budgetObj = dataSnapshot.getValue(MonthlyBudget.class);
                Log.d(TAG, "Value is: " + budgetObj);
                if (budgetObj != null){
                    setButton.setText("Edit");
                    spentValue.setText(budgetObj.spent+"/"+budgetObj.budget);
                    progressBar.setMax((int) budgetObj.budget);
                    progressBar.setProgress((int) budgetObj.spent);
                    monthlyBudget.setVisibility(View.INVISIBLE);
                    displayTextView.setVisibility(View.VISIBLE);
                    displayTextView.setText("Monthly Budget: "+budgetObj.budget);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        eyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenses.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Expense expense = child.getValue(Expense.class);
                    expenses.add(expense);
                }
                adapter = new ExpenseAdapter(expenses, getBaseContext(), MainActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public MonthlyBudget getMonthlyBudget() {
        return budgetObj;
    }
}
