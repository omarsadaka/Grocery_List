package com.example.omar.grocery_list.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.omar.grocery_list.Activities.DetailsActivity;
import com.example.omar.grocery_list.Data.DataBaseHandler;
import com.example.omar.grocery_list.Model.Grocery;
import com.example.omar.grocery_list.R;

import java.util.List;

/**
 * Created by OMAR on 10/18/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {
    List<Grocery> groceryList;
    Context context;
    AlertDialog.Builder alertDialog;
    Dialog dialog;

    public RecyclerViewAdapter(List<Grocery> groceryList, Context context) {
        this.groceryList = groceryList;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new viewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.viewHolder holder, int position) {
        Grocery grocery = groceryList.get(position);
        holder.grocery_name.setText(grocery.getName());
        holder.grocery_qty.setText(grocery.getQuantity());
        holder.grocery_date.setText(grocery.getDataItemAdded());


    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView grocery_name;
        TextView grocery_qty;
        TextView grocery_date;
        Button delete, edite;

        public viewHolder(View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            grocery_name = (TextView) itemView.findViewById(R.id.name);
            grocery_qty = (TextView) itemView.findViewById(R.id.quality);
            grocery_date = (TextView) itemView.findViewById(R.id.dateAdd);
            delete = (Button) itemView.findViewById(R.id.deleteBtn);
            edite = (Button) itemView.findViewById(R.id.editBtn);

            delete.setOnClickListener(this);
            edite.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go next DetailsActivity
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDataItemAdded());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.deleteBtn:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);
                    deleteItem(grocery.getId());

                    break;
                case R.id.editBtn:
                    position =getAdapterPosition();
                    grocery = groceryList.get(position);
                    editItem(grocery);
                    break;
            }


        }


        public void deleteItem(final int id) {

            alertDialog = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.confirmation_dialog, null);
            Button noButton = (Button) view.findViewById(R.id.noBtn);
            Button yesButton = (Button) view.findViewById(R.id.yesBtn);
            alertDialog.setView(view);
            dialog = alertDialog.create();
            dialog.show();
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHandler db = new DataBaseHandler(context);
                    db.deleteGrocery(id);
                    groceryList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();

                }
            });

        }

        public void editItem(final Grocery grocery) {
            alertDialog = new AlertDialog.Builder(context);
            final View view = LayoutInflater.from(context).inflate(R.layout.popup, null);

            final EditText groceryItem = (EditText) view.findViewById(R.id.edit1);
            final EditText quantity = (EditText) view.findViewById(R.id.edit2);
            Button saveButton = (Button) view.findViewById(R.id.saveBtn);
            TextView textView = (TextView)view.findViewById(R.id.title);
            textView.setText("Edit Grocery Item");
            alertDialog.setView(view);
            dialog = alertDialog.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHandler db = new DataBaseHandler(context);
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()) {
                        db.ubdateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }
                    else {
                        Snackbar.make(view , "Add Grocery And Quantity",Snackbar.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });


        }
    }
}
