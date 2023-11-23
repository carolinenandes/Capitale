package com.example.tcc20;

import android.annotation.SuppressLint;
import android.util.Log;
import android.database.Cursor;

import com.example.ObjectClasses.BancoDeDados;

import java.io.IOException;
import java.util.ArrayList;

public class Gasto_Lucros {

    BancoDeDados database;

    // Construtor que recebe a instância de BancoDeDados
    public Gasto_Lucros(BancoDeDados database) {
        this.database = database;
    }


    public Float obterSomaGastos() {
        String query = "SELECT SUM(VALOR_CUSTO_PED_COMPRA) FROM TB_PEDIDO_COMPRA WHERE STATUS_PED_COMPRA = 'Pago'";
        Cursor cursor = database.rawQuery(query, null);

        Float somaGastos = 0.0f;

        if (cursor != null && cursor.moveToFirst()) {
            somaGastos = cursor.getFloat(0);
        }

        if (cursor != null) {
            cursor.close();
        }

        Log.d("Gasto_Lucros", "Soma dos Gastos: " + somaGastos);
        return somaGastos;
    }

    public Float obterSomaGanhos() {
        String query = "SELECT SUM(VALOR_PED_COMPRA) FROM TB_PEDIDO_COMPRA WHERE STATUS_PED_COMPRA = 'Pago'";
        Cursor cursor = database.rawQuery(query, null);

        Float somaGanhos = 0.0f;

        if (cursor != null && cursor.moveToFirst()) {
            somaGanhos = cursor.getFloat(0);
        }

        if (cursor != null) {
            cursor.close();
        }

        Log.d("Gasto_Lucros", "Soma dos Ganhos: " + somaGanhos);
        return somaGanhos;
    }

    public Float calcularLucro() {
        Float gasto = obterSomaGastos();
        Float ganho = obterSomaGanhos();

        Float lucro = null;

        if (gasto != null && ganho != null) {
            lucro = ganho - gasto;
        }

        Log.d("Gasto_Lucros", "Valor do Lucro: " + lucro);
        return lucro;
    }

    @SuppressLint("Range")
    public Float obterGasto() {
        String query = "SELECT GASTO FROM TB_GASTOS_GANHOS WHERE ROWID = 1";
        Cursor cursor = database.rawQuery(query, null);

        Float gasto = null;

        if (cursor != null && cursor.moveToFirst()) {
            gasto = cursor.getFloat(cursor.getColumnIndex("GASTO"));
        }

        if (cursor != null) {
            cursor.close();
        }

        Log.d("Gasto_Lucros", "Valor do Gasto: " + gasto);
        return gasto;
    }

    @SuppressLint("Range")
    public Float obterGanho() {
        String query = "SELECT GANHO FROM TB_GASTOS_GANHOS WHERE ROWID = 1";
        Cursor cursor = database.rawQuery(query, null);

        Float ganho = null;

        if (cursor != null && cursor.moveToFirst()) {
            ganho = cursor.getFloat(cursor.getColumnIndex("GANHO"));
        }

        if (cursor != null) {
            cursor.close();
        }

        Log.d("Gasto_Lucros", "Valor do Ganho: " + ganho);
        return ganho;
    }

    @SuppressLint("Range")
    public Float obterLucro() {
        String query = "SELECT LUCRO FROM TB_GASTOS_GANHOS WHERE ROWID = 1";
        Cursor cursor = database.rawQuery(query, null);

        Float lucro = null;

        if (cursor != null && cursor.moveToFirst()) {
            lucro = cursor.getFloat(cursor.getColumnIndex("LUCRO"));
        }

        if (cursor != null) {
            cursor.close();
        }

        Log.d("Gasto_Lucros", "Valor do Lucro: " + lucro);
        return lucro;
    }

    public void atualizarGastoGanhoLucro(float gasto, float ganho) {
        Log.d("Gasto_Lucros", "Valores antes da atualização - Gasto: " + obterSomaGastos() + ", Ganho: " + obterSomaGanhos() + ", Lucro: " + calcularLucro());

        String updateQuery = "UPDATE TB_GASTOS_GANHOS SET GASTO = ?, GANHO = ?, LUCRO = ? WHERE ROWID = 1";
        database.executarQuery(updateQuery, new String[]{String.valueOf(gasto), String.valueOf(ganho), String.valueOf(ganho - gasto)});

        Log.d("Gasto_Lucros", "Valores após a atualização - Gasto: " + obterSomaGastos() + ", Ganho: " + obterSomaGanhos() + ", Lucro: " + calcularLucro());
    }

    public void cadastrarLucroNaMeta(ArrayList<Integer> idsPedidosSelecionados) {
        Log.d("Gasto_Lucros", "Saldo antes do cadastro na meta: " + obterSaldoMeta());

        float lucroTotal = 0;

        // Itera sobre os IDs dos pedidos selecionados
        for (int idPedido : idsPedidosSelecionados) {
            // Obtém o valor de venda e o valor de custo do pedido de compra
            float valorVenda = obterValorVendaPedidoCompra(idPedido);
            float valorCusto = obterValorCustoPedidoCompra(idPedido);

            // Calcula o lucro para este pedido
            float lucroPedido = valorVenda - valorCusto;

            // Adiciona o lucro ao total
            lucroTotal += lucroPedido;
        }

        // Atualiza o saldo na tabela TB_EMPRESA
        String updateQuery = "UPDATE TB_EMPRESA SET SALDO_EMPRESA = ? WHERE ROWID = 1";
        float saldoAtualizado = obterSaldoMeta() + lucroTotal;
        database.executarQuery(updateQuery, new String[]{String.valueOf(saldoAtualizado)});

        Log.d("Gasto_Lucros", "Saldo após o cadastro na meta: " + obterSaldoMeta());
    }

    @SuppressLint("Range")
    private float obterSaldoMeta() {
        String query = "SELECT SALDO_EMPRESA FROM TB_EMPRESA WHERE ROWID = 1";
        Cursor cursor = database.rawQuery(query, null);

        float saldo = 0.0f;

        if (cursor != null && cursor.moveToFirst()) {
            saldo = cursor.getFloat(cursor.getColumnIndex("SALDO_EMPRESA"));
        }

        if (cursor != null) {
            cursor.close();
        }

        Log.d("Gasto_Lucros", "Valor do Saldo na Meta: " + saldo);
        return saldo;
    }

    public void GanhoGastoLucro(){
        float gasto, ganho;

        gasto = obterSomaGastos();
        ganho = obterSomaGanhos();

        atualizarGastoGanhoLucro(gasto, ganho);
    }

    // Método para obter o valor de venda de um pedido de compra específico
    private float obterValorVendaPedidoCompra(int idPedido) {
        float valorVenda = 0;

        try {
            database.openDB();
            String sql = "SELECT VALOR_PED_COMPRA FROM TB_PEDIDO_COMPRA WHERE ID_PED_COMPRA = ?";
            Cursor cursor = database.db.rawQuery(sql, new String[]{String.valueOf(idPedido)});

            if (cursor != null && cursor.moveToFirst()) {
                valorVenda = cursor.getFloat(cursor.getColumnIndex("VALOR_PED_COMPRA"));
                cursor.close();
            }

            database.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return valorVenda;
    }

    // Método para obter o valor de custo de um pedido de compra específico
    private float obterValorCustoPedidoCompra(int idPedido) {
        float valorCusto = 0;

        try {
            database.openDB();
            String sql = "SELECT VALOR_CUSTO_PED_COMPRA FROM TB_PEDIDO_COMPRA WHERE ID_PED_COMPRA = ?";
            Cursor cursor = database.db.rawQuery(sql, new String[]{String.valueOf(idPedido)});

            if (cursor != null && cursor.moveToFirst()) {
                valorCusto = cursor.getFloat(cursor.getColumnIndex("VALOR_CUSTO_PED_COMPRA"));
                cursor.close();
            }

            database.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return valorCusto;
    }
}