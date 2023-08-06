package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ProdActivity extends AppCompatActivity {

    public RecyclerView recyclerviewProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod);

        recyclerviewProd = findViewById(R.id.recyclerviewProd);

        //Recuperando o adapter
        adapterProd AP = new adapterProd();

        //Criando o layout do recyclerView
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerviewProd.setLayoutManager(layoutManager);
      //  recyclerviewProd.setHasFixedSize(true); //Isso deixa a lista com um tamanho fixo
        recyclerviewProd.setAdapter(AP);
    }
}