package com.example.balochistanoiltraders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Random;

public class EmployeeAdapter extends FirebaseRecyclerAdapter<Note, EmployeeAdapter.EmployeeHolder> {
    private static final String TAG = "EmployeeAdapter";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("employees");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EmployeeAdapter(@NonNull FirebaseRecyclerOptions<Note> options) {
        super(options);
    }

    @NonNull
    @Override
    public EmployeeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_accounts, viewGroup, false);
        return new EmployeeHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onBindViewHolder(@NonNull final EmployeeHolder holder, int position, @NonNull final Note model) {
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        colors.add(Color.CYAN);
        colors.add(Color.DKGRAY);
        colors.add(Color.BLACK);
        Random rand = new Random();
        // Obtain a number between [0 - 13].
        int n = rand.nextInt(colors.size());
        Drawable mDraw = holder.itemView.getContext().getDrawable(R.drawable.circle);
        mDraw.setColorFilter(colors.get(n), PorterDuff.Mode.MULTIPLY);
        holder.chracterName.setBackground(mDraw);
        Log.d(TAG, "onBindViewHolder: " + model.getEmployee_name());
        holder.partyName.setText(model.getEmployee_name());
        final String uid = this.getRef(position).getKey();
        char ch = model.getEmployee_name().charAt(0);
        holder.chracterName.setText(String.valueOf(ch));
        holder.phoneNumber.setText(model.getPhone_number());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), EmployeeDetails.class);
                intent.putExtra("partyName",model.getEmployee_name());
                intent.putExtra("phoneNumber",model.getPhone_number());
                intent.putExtra("jobDescription",model.getSalary());
                intent.putExtra("address",model.getAddress());
                intent.putExtra("key",uid);

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

    }

    private void dialogShow(final EmployeeHolder holder, final Note model, final String uid) {
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

    void menuDisplay(final EmployeeHolder holder, final Note model, final String uid){

        PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_for_long_click, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.editItem){
                    Intent intent = new Intent(holder.itemView.getContext(), EmployeeDetails.class);
                    intent.putExtra("partyName",model.getEmployee_name());
                    intent.putExtra("phoneNumber",model.getPhone_number());
                    intent.putExtra("jobDescription",model.getSalary());
                    intent.putExtra("address",model.getAddress());
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

    class EmployeeHolder extends RecyclerView.ViewHolder{
        TextView partyName, phoneNumber, chracterName;
        public EmployeeHolder(@NonNull View itemView) {
            super(itemView);
            partyName = (TextView) itemView.findViewById(R.id.partyName);
            phoneNumber = (TextView) itemView.findViewById(R.id.phoneNumber);
            chracterName = (TextView) itemView.findViewById(R.id.chracterName);
        }
    }
}
