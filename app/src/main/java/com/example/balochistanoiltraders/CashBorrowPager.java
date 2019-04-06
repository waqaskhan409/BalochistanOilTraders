package com.example.balochistanoiltraders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CashBorrowPager extends PagerAdapter {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");
    ArrayList<String> arrayList = new ArrayList<>();
    Context mContext ;
    public CashBorrowPager(Context mContext, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RecyclerView recyclerView = new RecyclerView(mContext);
        mDatabase = mDatabase.child(arrayList.get(position));
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>().setQuery(
                mDatabase, Note.class
        ).build();
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return false;
    }
}
