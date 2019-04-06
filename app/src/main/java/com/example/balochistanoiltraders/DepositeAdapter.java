package com.example.balochistanoiltraders;

import android.content.Context;
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
import android.view.Gravity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;

public class DepositeAdapter extends FirebaseRecyclerAdapter<Note, DepositeAdapter.DepositeHolder> {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().
            getReference().child("cash_borrow");
    private static final String TAG = "DepositeAdapter";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String key;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DepositeAdapter(@NonNull FirebaseRecyclerOptions options, String key) {

        super(options);
        this.key = key;
        Log.d(TAG, "DepositeAdapter: " + this.key);
//        options.getSnapshots().get
    }


    @NonNull
    @Override
    public DepositeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View li = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_deposite,viewGroup, false);
        return new DepositeHolder(li);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onBindViewHolder(@NonNull final DepositeHolder holder, int position, @NonNull final Note model) {
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


            holder.moneyT.setText(model.getMoney_transfer());
        final String uid = this.getRef(position).getKey();
        char ch = 'H';
        try{
        ch = model.getBank_name().charAt(0);
        }catch (Exception e){

        }
        holder.bankN.setText(model.getBank_name());
        holder.des.setText(model.getDescription());
        holder.chracterName.setText(String.valueOf(ch));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                menuDisplay(holder, model, uid);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DepositeAndPayementDetail.class);
                intent.putExtra("moneyTransfer",model.getMoney_transfer());
                intent.putExtra("bankName",model.getBank_name());
                intent.putExtra("date",model.getDate());
                intent.putExtra("description",model.getDescription());
                intent.putExtra("key",uid);
                intent.putExtra("parentKey",key);
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    private void dialogShow(final DepositeHolder holder, final Note model, final String uid) {
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


                                    mDatabase.child(key).child("deposite_money").child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                final ArrayList<String> arrayList = new ArrayList<>();
                                                final ArrayList<String> arrayListAddition = new ArrayList<>();



                                                DatabaseReference databaseReference = FirebaseDatabase
                                                        .getInstance()
                                                        .getReference().child("cash_borrow");
                                                databaseReference.child(key).child("deposite_money").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                                                            arrayList.add(snapshot.getKey());
                                                        }
                                                        Log.d(TAG, "onDataChange: " + arrayList);
                                                        for(int i = 0 ; i < dataSnapshot.getChildrenCount() ; i++ ){
                                                            String value = dataSnapshot
                                                                    .child(arrayList.get(i))
                                                                    .child("money_transfer").getValue().toString();
                                                            arrayListAddition.add(value);
                                                        }
                                                        Log.d(TAG, "onDataChange: " + arrayListAddition);
                                                        FirebaseDatabase.getInstance()
                                                                .getReference().child("cash_borrow").child(key).child("money")
                                                                .child("deposite_money").setValue(setupOverViewValue(arrayListAddition));

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                    });

                                }
                                else {
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

    private String setupOverViewValue(ArrayList<String> arrayListAddition) {
        int t = 0;
        for (int i = 0; i < arrayListAddition.size(); i++) {
            t = t + Integer.parseInt(arrayListAddition.get(i));
        }
        return String.valueOf(t);
    }

    void menuDisplay(final DepositeHolder holder, final Note model, final String uid){

        PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_for_long_click, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.editItem){
                    Intent intent = new Intent(holder.itemView.getContext(), DepositeAndPayementDetail.class);
                    intent.putExtra("moneyTransfer",model.getMoney_transfer());
                    intent.putExtra("bankName",model.getBank_name());
                    intent.putExtra("date",model.getDate());
                    intent.putExtra("description",model.getDescription());
                    intent.putExtra("key",uid);
                    intent.putExtra("parentKey",key);

                    holder.itemView.getContext().startActivity(intent);

                } else if(item.getItemId() == R.id.deleteItem){
                    dialogShow(holder, model, uid);
                }
                return true;
            }
        });

        popupMenu.show();
        }

    class DepositeHolder extends RecyclerView.ViewHolder{
        TextView moneyT, des, bankN, chracterName;

        public DepositeHolder(@NonNull View itemView) {
            super(itemView);
            moneyT = (TextView) itemView.findViewById(R.id.moneyTransfer);
            des = (TextView) itemView.findViewById(R.id.Description);
            bankN = (TextView) itemView.findViewById(R.id.bankName);
            chracterName = (TextView) itemView.findViewById(R.id.chracterName);
        }
    }
}
