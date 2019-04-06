package com.example.balochistanoiltraders;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

public class BalochistanOilTraders extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
