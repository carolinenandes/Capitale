package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Produto;
import com.example.ObjectClasses.adapterProd;

import java.io.IOException;
import java.util.ArrayList;

//esta activity foi convertida em ProdutosFragment
public class ProdActivity extends AppCompatActivity {
    private RecyclerView recyclerviewProd;
    private ArrayList<Produto> productList;
    private adapterProd adapter;
    private BancoDeDados banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod);

        recyclerviewProd = findViewById(R.id.recyclerviewProd);

        // Crie a lista de produtos
        productList = new ArrayList<>();

        // Cria o adaptador e defina-o para o RecyclerView
        adapter = new adapterProd(productList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerviewProd.setLayoutManager(layoutManager);
        recyclerviewProd.setHasFixedSize(true); // Isso deixa a lista com um tamanho fixo e poupa memória.
        recyclerviewProd.setAdapter(adapter);

        // Cria uma instância da classe BancoDeDados
        banco = new BancoDeDados(getApplicationContext());

        // Carregue os dados do banco para a lista productList
        carregarDadosDoBanco();
    }

    private void carregarDadosDoBanco() {
        try {
            banco.openDB();

            // Execute a consulta SQL para selecionar todos os produtos ou os respectivos dados da página
            String sql = "SELECT * FROM TB_PRODUTO";
            Cursor cursor = banco.db.rawQuery(sql, null);

            //
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String nome = cursor.getString(1);
                    int qtd =  cursor.getInt(2);
                    String valor_venda = cursor.getString(3);
                    String valor_custo =cursor.getString(4);
                    String desc = cursor.getString(5);
                    int vendas = cursor.getInt(6);
                    String status  = cursor.getString(7);

                    // Cria um objeto Produto com os dados do curso
                    Produto produto = new Produto(id, nome, qtd, valor_venda, valor_custo, desc, vendas, status);
                    productList.add(produto);
                }while (cursor.moveToNext());

                cursor.close();
            }

            banco.close(); // Não esqueça de fechar o banco após usar

            // Notifique o adaptador sobre a mudança nos dados após carregar do banco
            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
