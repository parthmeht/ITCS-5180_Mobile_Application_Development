/*
 * Copyright (c)
 * Parth Mehta
 * 801057625
 */

package com.parth.android.expensemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder>{
    FirebaseDatabase database;
    DatabaseReference eyRef;
    DatabaseReference myRef;
    ArrayList<Expense> expenses;
    Context context;
    MonthlyBudget monthlyBudget;
    Listener listener;

    public ExpenseAdapter(ArrayList<Expense> expenses, Context context, Listener listener) {
        this.expenses = expenses;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        ExpenseAdapter.ViewHolder viewHolder = new ExpenseAdapter.ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        final Expense expense = expenses.get(position);
        holder.expenseName.setText(expense.name);
        holder.expenseAmount.setText(String.valueOf(expense.amount));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Are you sure want to delete the expense?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        database = FirebaseDatabase.getInstance();
                        myRef = database.getReference(MainActivity.MONTHLY_BUDGET);
                        monthlyBudget = listener.getMonthlyBudget();
                        float remaining = monthlyBudget.spent-expense.amount;
                        monthlyBudget.setSpent(remaining);
                        Log.d(MainActivity.TAG,monthlyBudget.toString());
                        myRef.setValue(monthlyBudget);
                        eyRef = database.getReference(MainActivity.EXPENSES);
                        eyRef.child(expense.id).removeValue();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView expenseName, expenseAmount;
        ImageButton delete;
        public ViewHolder(View itemView) {
            super(itemView);
            this.expenseName = itemView.findViewById(R.id.expenseNameTextView);
            this.expenseAmount = itemView.findViewById(R.id.expenseAmountTextView);
            this.delete = itemView.findViewById(R.id.deleteImageButton);
        }
    }

    interface Listener{
        MonthlyBudget getMonthlyBudget();
    }
}
