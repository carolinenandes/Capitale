package com.example.tcc20;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EditDialogFragmentCliente extends DialogFragment {

    private Context context;
    private BancoDeDados banco;
    private adapterCliente adapter;
    private Cliente clienteParaEditar;
    private static final String SELECIONAR_PRODUTOS_REQUEST_KEY = "selecionar_produtos_request";


    // Construtor para passar o contexto
    public EditDialogFragmentCliente(Context context) {
        this.context = context;
    }

    // Construtor para passar a instância do BancoDeDados, do adaptador e o produto a ser editado
    public EditDialogFragmentCliente(BancoDeDados banco, adapterCliente adapter, Cliente cliente) {
        this.banco = banco;
        this.adapter = adapter;
        this.clienteParaEditar = cliente;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_dialog_cliente, container, false);

        context = getContext();

        Button btnEditCliente = view.findViewById(R.id.btnEditCliente);
        Button btnExcluir = view.findViewById(R.id.btnExcluir);
        Button btnAdicionarPedido = view.findViewById(R.id.btnAdicionarPedido);
        EditText etxtNomeCliente = view.findViewById(R.id.etxtNomeCliente);
        EditText etxEmailCliente = view.findViewById(R.id.etxtEmailCliente);
        EditText etxtStatusCliente = view.findViewById(R.id.etxtStatusCliente);
        EditText etxtFoneCliente = view.findViewById(R.id.etxtFoneCliente);


        // Preenche os campos de edição com os detalhes do produto a ser editado
        etxtNomeCliente.setText(clienteParaEditar.getNome());
        etxEmailCliente.setText(String.valueOf(clienteParaEditar.getEmail()));
        etxtStatusCliente.setText(clienteParaEditar.getStatus());
        etxtFoneCliente.setText(clienteParaEditar.getFone());


        btnEditCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etxtNomeCliente.getText().toString();
                String email = etxEmailCliente.getText().toString();
                String status = etxtStatusCliente.getText().toString();
                String fone = etxtFoneCliente.getText().toString();


                // Atualiza os detalhes do produto no banco de dados
                clienteParaEditar.setNome(nome);
                clienteParaEditar.setEmail(email);
                clienteParaEditar.setFone(fone);
                clienteParaEditar.setStatus(status);


                atualizarClienteNoBanco(clienteParaEditar, adapter);

                dismiss(); // Fecha o diálogo após a edição
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletaClienteDaDatabase(clienteParaEditar.getId());
                dismiss(); // Fecha o diálogo após a exclusão
            }
        });

        btnAdicionarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSelecaoProdutos();
            }
        });

        return view;
    }

    // Método para mostrar o Dialog de seleção de produtos
    private void showDialogSelecaoProdutos() {
        getParentFragmentManager().setFragmentResultListener(SELECIONAR_PRODUTOS_REQUEST_KEY, this, (requestKey, result) -> {
            ArrayList<produtoSelecao> produtosSelecionados = result.getParcelableArrayList("produtos_selecionados");
            adicionarPedidosAoCliente(produtosSelecionados);
        });

        DialogSelecaoProdutos dialog = new DialogSelecaoProdutos();
        dialog.show(getParentFragmentManager(), "selecao_produtos_dialog");
    }

    private void atualizarClienteNoBanco(Cliente cliente, adapterCliente adapter) {
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME_CLIENTE", cliente.getNome());
        values.put("EMAIL_CLIENTE", cliente.getEmail());
        values.put("STATUS_CLIENTE", cliente.getStatus());
        values.put("FONE_CLIENTE", cliente.getFone());


        db.update("TB_CLIENTE", values, "ID_CLIENTE = ?", new String[]{String.valueOf(cliente.getId())});
        db.close();

        adapter.notifyDataSetChanged();
    }

    public void deletaClienteDaDatabase(int clienteId) {
        SQLiteDatabase db = banco.getWritableDatabase(); // Abre o banco de dados em modo de escrita

        // Define o WHERE para excluir o registro com base no ID
        String whereClause = "ID_CLIENTE = ?";
        String[] whereArgs = {String.valueOf(clienteId)};

        // Exclua o registro da tabela
        int deletedRows = db.delete("TB_CLIENTE", whereClause, whereArgs);

        if (deletedRows > 0) {
            // Registro excluído com sucesso
            Toast.makeText(context, "Cliente deletado com sucesso.", Toast.LENGTH_SHORT).show();

        } else {
            // Não foi possível excluir o registro
            Toast.makeText(context, "Falha ao excluir o cliente", Toast.LENGTH_SHORT).show();
        }
        db.close(); // Feche o banco de dados após a operação
        adapter.notifyItemRemoved(clienteId);
    }

    public void adicionarPedidosAoCliente(ArrayList<produtoSelecao> produtosSelecionados) {
        SQLiteDatabase db = banco.getWritableDatabase();
        double totalPedido = 0.0;

        Log.d("TAG", "Tentando adicionar pedidos. Número de produtos selecionados: " + produtosSelecionados.size());

        if (produtosSelecionados == null) {
            Log.e("TAG", "A lista de produtos selecionados está nula");
            return;
        }

        for (produtoSelecao produto : produtosSelecionados) {
            try {
                String valorProdutoString = produto.getValor_venda().replace(",", ".");
                double valorProduto = Double.parseDouble(valorProdutoString);
                totalPedido += valorProduto;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Arredonda o valor para duas casas decimais
        totalPedido = Math.round(totalPedido * 100.0) / 100.0;

        // Formata o valor para o formato desejado
        String valorFormatado = String.format(Locale.getDefault(), "%.2f", totalPedido).replace(".", ",");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dta_ped_compra = dateFormat.format(calendar.getTime());

        ContentValues values = new ContentValues();
        values.put("ID_CLIENTE", clienteParaEditar.getId());
        values.put("STATUS_PED_COMPRA", "Pendente");
        values.put("DTA_PED_COMPRA", dta_ped_compra);
        values.put("VALOR_PED_COMPRA", valorFormatado);

        long idPedido = db.insertWithOnConflict("TB_PEDIDO_COMPRA", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (idPedido != -1) {
            Toast.makeText(context, "Pedido adicionado com sucesso.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Falha ao adicionar pedido", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
