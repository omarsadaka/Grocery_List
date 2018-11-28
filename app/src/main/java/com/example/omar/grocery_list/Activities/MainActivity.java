package com.example.omar.grocery_list.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.omar.grocery_list.Data.DataBaseHandler;
import com.example.omar.grocery_list.Model.Grocery;
import com.example.omar.grocery_list.R;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    EditText groceryItem;
    EditText quantity;
    Button button;
    DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        CheckItemExist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        Snackbar.make(v , "Item Saved!",Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                startActivity(new Intent(MainActivity.this , ListActivity.class));

            }
        },1000);

    }
    public void CheckItemExist(){
        if (db.getGroceriesCount() >0){
            startActivity(new Intent(MainActivity.this , ListActivity.class));
            finish();

        }

    }
}
