package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NoticiasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, new NewsFragment())
                    .commit();
        }
    }}