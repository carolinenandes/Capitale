package com.example.tcc20;

import android.util.Log;
import android.database.Cursor;

public class Gasto_Lucros {

    BancoDeDados database;

    // Construtor que recebe a instância de BancoDeDados
    public Gasto_Lucros(BancoDeDados database) {
        this.database = database;
    }


    public Float obterSomaGastos() {
        String query = "SELECT SUM(VALOR_CUSTO_PROD) FROM TB_PRODUTO";
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
        String query = "SELECT SUM(VALOR_PED_COMPRA) FROM TB_PEDIDO_COMPRA";
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

    public void atualizarGastoGanhoLucro(float gasto, float ganho) {
        Log.d("Gasto_Lucros", "Valores antes da atualização - Gasto: " + obterSomaGastos() + ", Ganho: " + obterSomaGanhos() + ", Lucro: " + calcularLucro());

        String updateQuery = "UPDATE TB_GASTOS_GANHOS SET GASTO = ?, GANHO = ?, LUCRO = ? WHERE ROWID = 1";
        database.executarQuery(updateQuery, new String[]{String.valueOf(obterSomaGastos()), String.valueOf(obterSomaGanhos()), String.valueOf(obterSomaGanhos() - obterSomaGastos())});

        Log.d("Gasto_Lucros", "Valores após a atualização - Gasto: " + obterSomaGastos() + ", Ganho: " + obterSomaGanhos() + ", Lucro: " + calcularLucro());
    }

}
