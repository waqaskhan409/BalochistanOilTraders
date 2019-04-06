package com.example.balochistanoiltraders;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.zip.Inflater;

import static android.support.constraint.Constraints.TAG;

public class ExpancesAdapter extends FirebaseRecyclerAdapter<Note, ExpancesAdapter.ExpancesHolder> {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("expances");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ArrayList<Integer> colors = new ArrayList<>();
    Date date = new Date();
    SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");


    int total = 0;
    String mDate = "";

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ExpancesAdapter(@NonNull FirebaseRecyclerOptions<Note> options) {
        super(options);
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onBindViewHolder(@NonNull final ExpancesHolder holder, int position, @NonNull final Note model) {
        Log.d(TAG, "onBindViewHolder: " + this.getItemCount());
//    mDate = model.getDate();
    colors.add(Color.BLUE);
    colors.add(Color.GREEN);
    colors.add(Color.RED);
    colors.add(Color.YELLOW);
    colors.add(Color.MAGENTA);
    colors.add(Color.CYAN);
    colors.add(Color.DKGRAY);
    colors.add(Color.BLACK);


    String[] color = holder.itemView.getContext().getResources().getStringArray(R.array.colors);

    Random rand = new Random();
    // Obtain a number between [0 - 13].
    int n = rand.nextInt(colors.size());
    char ch = model.getName().charAt(0);
    final String uid = this.getRef(position).getKey();
    holder.name.setText(model.getName());
    holder.money.setText(model.getSpend_money());


    holder.text.setText(String.valueOf(ch));
    Drawable mDraw = holder.itemView.getContext().getDrawable(R.drawable.circle);
    mDraw.setColorFilter(colors.get(n), PorterDuff.Mode.MULTIPLY);
    holder.text.setBackground(mDraw);
    holder.description.setText(model.getDescription());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(holder.itemView.getContext(), ExpanceDetails.class);
            intent.putExtra("name", model.getName());
            intent.putExtra("moneySpend", model.getSpend_money());
            intent.putExtra("description", model.getDescription());
            intent.putExtra("key", uid);
            holder.itemView.getContext().startActivity(intent);
        }
    });
    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            menuDisplay(holder, model, uid);
            return true;
        }
    });

        if(format.format(date).equals(model.getDate())) {
            holder.Date.setVisibility(View.VISIBLE);
            holder.Date.setTextColor(Color.DKGRAY);
            holder.Date.setText("Today");
        }else {
            holder.Date.setVisibility(View.VISIBLE);
            holder.Date.setText(model.getDate());
        }
    }

    @NonNull
    @Override
    public ExpancesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_expances_list, viewGroup, true);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_expances_list, viewGroup, false);

        return new ExpancesHolder(view);
    }
    private void dialogShow(final ExpancesHolder holder, final Note model, final String uid) {
        LayoutInflater li = LayoutInflater.from(holder.itemView.getContext());
        View view= li.inflate(R.layout.layout_for_confirm, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                holder.itemView.getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

//      final TextView textView = (TextView) view.findViewById(R.id.confirm) ;
        // set dialog message
        alertDialogBuilder.setTitle("Are You Sure to Delete this item?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(mAuth.getCurrentUser().getEmail().equals(holder.itemView.
                                        getContext().getResources().getString(R.string.email))) {
                                    mDatabase.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(holder.itemView.getContext(), "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                                }


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    void menuDisplay(final ExpancesHolder holder, final Note model, final String uid){

        PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_for_long_click, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.editItem){
                    Intent intent = new Intent(holder.itemView.getContext(), ExpanceDetails.class);
                    intent.putExtra("name",model.getName());
                    intent.putExtra("moneySpend",model.getSpend_money());
                    intent.putExtra("description",model.getDescription());
                    intent.putExtra("key",uid);
                    holder.itemView.getContext().startActivity(intent);

                } else if(item.getItemId() == R.id.deleteItem){
                    dialogShow(holder, model, uid);
                }
                return true;
            }
        });

        popupMenu.show();
    }


    static class ExpancesHolder extends RecyclerView.ViewHolder{
        TextView text;
        TextView money, name, description, Date;


        public ExpancesHolder(@NonNull View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.chracterName);
            name = (TextView) itemView.findViewById(R.id.name);
            money = (TextView) itemView.findViewById(R.id.spend);
            description = (TextView) itemView.findViewById(R.id.description);
            Date = (TextView) itemView.findViewById(R.id.head);

        }
    }
}
