package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ProdActivity extends AppCompatActivity {

    public RecyclerView recyclerviewProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod);

        recyclerviewProd = findViewById(R.id.recyclerviewProd);

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Bolo de morango", 4, "5,00", "200,0",
                "Com certeza um bolo de morango", 7, "Disponível"));

        // Initialize the adapter with the list of products
        adapterProd adapter = new adapterProd(productList);

        //Criando o layout do recyclerView
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerviewProd.setLayoutManager(layoutManager);
      //  recyclerviewProd.setHasFixedSize(true); //Isso deixa a lista com um tamanho fixo
        recyclerviewProd.setAdapter(adapter);
    }
}