package com.example.omar.grocery_list.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.omar.grocery_list.Data.DataBaseHandler;
import com.example.omar.grocery_list.Model.Grocery;
import com.example.omar.grocery_list.R;
import com.example.omar.grocery_list.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Grocery> groceryList;
    List<Grocery> listItem;
    DataBaseHandler db;

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    EditText groceryItem;
    EditText quantity;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                createpopupDialog();
            }
        });
        db = new DataBaseHandler(this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItem = new ArrayList<>();
        groceryList = db.getAllGrocery();

        for (Grocery c : groceryList){
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity("Qty "+ c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDataItemAdded("Added On: "+ c.getDataItemAdded());
            listItem.add(grocery);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(listItem,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
    private void createpopupDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        groceryItem = (EditText) view.findViewById(R.id.edit1);
        quantity = (EditText) view.findViewById(R.id.edit2);
        button = (Button) view.findViewById(R.id.saveBtn);
        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo Save to DataBase
                //Todo Goto Next
                if (!groceryItem.getText().toString().isEmpty()
                        && !quantity.getText().toString().isEmpty()) {
                    saveGroceryToBD(v);
                }
            }
        });

    }
    private void saveGroceryToBD(View v) {
        Grocery grocery = new Grocery();
        String newGrocery = groceryItem.getText().toString();
        String newGroceryQty = quantity.getText().toString();
        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQty);
        db.addGrocery(grocery);
        Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
               ListActivity.this.recreate();

            }
        },1000);
    }

}
