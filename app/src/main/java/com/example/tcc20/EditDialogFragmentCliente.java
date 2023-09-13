/*package com.example.tcc20;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class EditDialogFragmentCliente extends DialogFragment {

    private Context context;
    private BancoDeDados banco;
    private adapterCliente adapter;
    private Cliente clienteParaEditar;

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

        Button btnEditProd = view.findViewById(R.id.btnEditProd);
        Button btnExcluir = view.findViewById(R.id.btnExcluir);
        EditText etxtNomeProd = view.findViewById(R.id.etxtNomeProd);
        EditText etxQtdProd = view.findViewById(R.id.etxQtdProd);
        EditText etxtValorVenda = view.findViewById(R.id.etxtValorVenda);
        EditText etxtValorCustoProd = view.findViewById(R.id.etxtValorCusto_prod);
        EditText etxtDescProd = view.findViewById(R.id.extFoneCliente);
        EditText etxtStatusProd = view.findViewById(R.id.etxtStatusCliente);
        EditText etxtQtdVendas = view.findViewById(R.id.etxtEmailCliente);

        // Preenche os campos de edição com os detalhes do produto a ser editado
        etxtNomeProd.setText(clienteParaEditar.getNome());
        etxQtdProd.setText(String.valueOf(clienteParaEditar.getQtd()));
        etxtValorVenda.setText(clienteParaEditar.getValor_venda());
        etxtValorCustoProd.setText(clienteParaEditar.getValor_custo());
        etxtDescProd.setText(clienteParaEditar.getDesc());
        etxtStatusProd.setText(clienteParaEditar.getStatus());
        etxtQtdVendas.setText(String.valueOf(clienteParaEditar.getVendas()));

        btnEditProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etxtNomeProd.getText().toString();
                int qtd = Integer.parseInt(etxQtdProd.getText().toString());
                String valorVenda = etxtValorVenda.getText().toString();
                String valorCusto = etxtValorCustoProd.getText().toString();
                String desc = etxtDescProd.getText().toString();
                String status = etxtStatusProd.getText().toString();
                int vendas = Integer.parseInt(etxtQtdVendas.getText().toString());

                // Atualiza os detalhes do produto no banco de dados
                clienteParaEditar.setNome(nome);
                clienteParaEditar.setQtd(qtd);
                clienteParaEditar.setValor_venda(valorVenda);
                clienteParaEditar.setValor_custo(valorCusto);
                clienteParaEditar.setDesc(desc);
                clienteParaEditar.setStatus(status);
                clienteParaEditar.setVendas(vendas);

                atualizarProdutoNoBanco(clienteParaEditar, adapter);

                dismiss(); // Fecha o diálogo após a edição
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletaProdutoDaDatabase(clienteParaEditar.getId());
                dismiss(); // Fecha o diálogo após a exclusão
            }
        });

        return view;
    }

    private void atualizarProdutoNoBanco(Produto produto, adapterProd adapter) {
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME_PROD", produto.getNome());
        values.put("QTD_PROD", produto.getQtd());
        values.put("VALOR_VENDA_PROD", produto.getValor_venda());
        values.put("VALOR_CUSTO_PROD", produto.getValor_custo());
        values.put("DESC_PROD", produto.getDesc());
        values.put("STATUS_PROD", produto.getStatus());
        values.put("QTD_VENDA", produto.getVendas());

        db.update("TB_PRODUTO", values, "ID_PROD = ?", new String[]{String.valueOf(produto.getId())});
        db.close();

        adapter.notifyDataSetChanged();
    }

    public void deletaProdutoDaDatabase(int productId) {
        SQLiteDatabase db = banco.getWritableDatabase(); // Abre o banco de dados em modo de escrita

        // Define o WHERE para excluir o registro com base no ID
        String whereClause = "ID_PROD = ?";
        String[] whereArgs = {String.valueOf(productId)};

        // Exclua o registro da tabela
        int deletedRows = db.delete("TB_PRODUTO", whereClause, whereArgs);

        if (deletedRows > 0) {
            // Registro excluído com sucesso
            Toast.makeText(context, "Produto deletado com sucesso.", Toast.LENGTH_SHORT).show();

        } else {
            // Não foi possível excluir o registro
            Toast.makeText(context, "Falha ao excluir o produto", Toast.LENGTH_SHORT).show();
        }
        db.close(); // Feche o banco de dados após a operação
        adapter.notifyItemRemoved(productId);
    }
}
*/