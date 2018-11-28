package com.example.omar.grocery_list.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.omar.grocery_list.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView itemName;
    private TextView quantity;
    private TextView itemDate;
    private int groceryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName = (TextView)findViewById(R.id.itemNameDetil);
        quantity = (TextView)findViewById(R.id.itemQtyDetil);
        itemDate = (TextView)findViewById(R.id.itemDateDetil);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            itemDate.setText(bundle.getString("date"));
            groceryId = bundle.getInt("id");

        }

    }
}
