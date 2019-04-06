package com.example.balochistanoiltraders;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListForEachSell extends Fragment {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView recyclerView;
    private static final String TAG = "ListForEachSell";
    private ListBorrowCashAdapter mAdapter;
    private ArrayList<String> list = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public ListForEachSell() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cash Borrow");

        recyclerView = (RecyclerView) view.findViewById(R.id.listForSellingItem);
        mDatabase = mDatabase.child("cash_borrow");
        mDatabase.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager li = new LinearLayoutManager(getActivity());
//        li.setReverseLayout(true);
        recyclerView.setLayoutManager(li);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list.add(snapshot.getKey());
                }
                Log.d(TAG, "onDataChange: " + list);
                setupFirebaseRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        getActivity().getResources().getString(R.string.email))) {
                    dialogShow();
                }else {
                    Toast.makeText(getActivity(), "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });



        return view;
    }



    private void dialogShow() {
        LayoutInflater li = LayoutInflater.from(getContext());
        View view= li.inflate(R.layout.layout_for_confirm, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
               getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

//      final TextView textView = (TextView) view.findViewById(R.id.confirm) ;
        // set dialog message
        alertDialogBuilder.setTitle("Are You Sure to Add this item?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                long timeStamp = 1 * new Date().getTime();
                                String NewDescTimeStamp= String.valueOf((9999999999999L+(-1 * timeStamp)));
                                list.add(0, NewDescTimeStamp);
                                mDatabase = mDatabase.child(NewDescTimeStamp).child("money");

                                final Date date1 = new Date();
                                final SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                                mDatabase.child("cash").setValue("0");
                                mDatabase.child("date").setValue(format.format(date1));
                                mDatabase.child("deposite_money").setValue("0");
                                mDatabase.child("money_borrowed").setValue("0");
                                if(mAdapter == null){
                                    Toast.makeText(getActivity(), "No Network find!", Toast.LENGTH_SHORT).show();
                                }else {
                                mAdapter.notifyDataSetChanged();
                                }
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");

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

    private void setupFirebaseRecycler() {
        mAdapter = new ListBorrowCashAdapter(list);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
    class ListBorrowCashAdapter extends RecyclerView.Adapter<ListBorrowCashAdapter.ListHolder>{
        ArrayList<String> list = new ArrayList<>();

        public ListBorrowCashAdapter(ArrayList<String> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_accounts,viewGroup,false);


            return new ListHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final ListHolder listHolder, final int i) {
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
            Drawable mDraw = listHolder.itemView.getContext().getDrawable(R.drawable.circle);
            mDraw.setColorFilter(colors.get(n), PorterDuff.Mode.MULTIPLY);

            listHolder.chracterName.setBackground(mDraw);


            listHolder.chracterName.setText(String.valueOf(i+1));
            listHolder.partyName.setText("Sheet Number");
                listHolder.phoneNumber.setText(list.get(i));
                listHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(listHolder.itemView.getContext(), CashBorrowed.class);
                        intent.putExtra("key", list.get(i));
                        startActivity(intent);
                    }
                });
            listHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuDisplay(listHolder, list.get(i), i);
                    return true;
                }
            });

        }private void dialogShow(final String key, final int i) {
            final LayoutInflater li = LayoutInflater.from(getContext());
            View view= li.inflate(R.layout.layout_for_confirm, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getContext());

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
                                    if(mAuth.getCurrentUser().getEmail()
                                            .equals(getContext()
                                                    .getResources()
                                                    .getString(R.string.email))) {


                                        mDatabase.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    list.remove(i);
                                                    mAdapter.notifyDataSetChanged();

                                                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    }else {
                                        Toast.makeText(getContext(), "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

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

        void menuDisplay(ListHolder holder, final String key, final int i){

            PopupMenu popupMenu = new PopupMenu(getContext(), holder.itemView);
            popupMenu.getMenuInflater().inflate(R.menu.menu_for_long_click, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.editItem){
                        Intent intent = new Intent(getContext(), CashBorrowed.class);
                        intent.putExtra("key", key);
                        startActivity(intent);

                    } else if(item.getItemId() == R.id.deleteItem){
                        dialogShow(key, i);
                    }
                    return true;
                }
            });

            popupMenu.show();
        }



        @Override
        public int getItemCount() {
            return list.size();
        }

        class ListHolder extends RecyclerView.ViewHolder{
            private TextView partyName, phoneNumber, chracterName;


            public ListHolder(@NonNull View itemView) {
                super(itemView);
                partyName = (TextView) itemView.findViewById(R.id.partyName);
                phoneNumber = (TextView) itemView.findViewById(R.id.phoneNumber);
                chracterName = (TextView) itemView.findViewById(R.id.chracterName);
            }
        }
    }
}
