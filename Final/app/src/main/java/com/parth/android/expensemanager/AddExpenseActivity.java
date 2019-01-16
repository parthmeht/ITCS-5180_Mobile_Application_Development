/*
 * Copyright (c)
 * Parth Mehta
 * 801057625
 */

package com.parth.android.expensemanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExpenseActivity extends AppCompatActivity {
    MonthlyBudget monthlyBudget;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference eyRef;
    EditText expenseName;
    EditText expenseAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        setTitle("Add Expense");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(MainActivity.MONTHLY_BUDGET);
        eyRef = database.getReference(MainActivity.EXPENSES);
        expenseName = findViewById(R.id.expense_name_edit_text);
        expenseAmount = findViewById(R.id.expense_amount_edit_text);
        if (getIntent().getExtras()!=null)
            monthlyBudget = (MonthlyBudget) getIntent().getExtras().getSerializable(MainActivity.MONTHLY_BUDGET);
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = expenseName.getText().toString();
                String amountText = expenseAmount.getText().toString();
                if (name == null || name.equalsIgnoreCase("")){
                    Toast.makeText(view.getContext(),"Enter Expense Name",Toast.LENGTH_LONG).show();
                } else if (amountText == null || amountText.equalsIgnoreCase("")){
                    Toast.makeText(view.getContext(),"Enter Expense Amount",Toast.LENGTH_LONG).show();
                } else{
                    float amount = Float.parseFloat(amountText);
                    if (monthlyBudget!= null && (amount<=(monthlyBudget.budget-monthlyBudget.spent))){
                        String id = myRef.push().getKey();
                        Expense expense = new Expense(id, name, amount);
                        eyRef.child(id).setValue(expense);
                        monthlyBudget.setSpent(monthlyBudget.spent+amount);
                        myRef.setValue(monthlyBudget);
                        finish();
                    }else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddExpenseActivity.this);

                        alertDialog.setTitle("Budget Exceeded!!!");

                        alertDialog.setMessage("Oops! Check your budget please!");

                        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertDialog.show();
                    }
                }
            }
        });
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
