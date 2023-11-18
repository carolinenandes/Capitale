package com.example.ObjectClasses;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BancoDeDados extends SQLiteOpenHelper {

    //Atributos
    String TAG = BancoDeDados.class.getSimpleName();
    //Numero da classe
    int flag;
    //Nome do DB
    static String DB_Name = "dbFinanceiro.db";
    //Context para 'pegar' a programacao da activity
    Context Contexto;
    //Arquivo de saida
    String saidaFile = "";
    //Caminho onde ficara salvo o DB
    String DB_Path;
    //Classe para executar as operacoes do DB
    public SQLiteDatabase db;

    public BancoDeDados(Context context) {
        super(context, DB_Name, null, 1);

        //Inserindo o contexto no parametro
        this.Contexto = context;

        //Recuperando o caminho da database
        ContextWrapper cw = new ContextWrapper(this.Contexto);
        DB_Path = cw.getFilesDir().getAbsolutePath()+"/databases/";

        Log.e(TAG,"Banco de Dados: "+DB_Path);

        // Montar os caminhos completos do DB
        File file = new File(DB_Path);

        //Mensagem de Log
        Log.e(TAG,"Banco de Dados:"+file.exists());

        //Se nao existir ele abre o caminho
        if(!file.exists())
        {
            file.mkdir();
        }
    }

    public void copiaDB() throws IOException {

        //Criando um Buffer para passar o DB
        byte[] buffer = new byte[1024];
        OutputStream Saida =null;
        InputStream Entrada = null;
        int tamanho;

        try
        {
            //Pegando o banco de dados da pasta ASSETS
        Entrada = Contexto.getAssets().open(DB_Name);

        //Transferindo do ASSETS para o CEL...
        Saida = new FileOutputStream(DB_Path+DB_Name);

        //Percorrer byte por byte salvando o DB
        while((tamanho = Entrada.read(buffer)) > 0)
        {
            Saida.write(buffer,0,tamanho);
        }

        // Fechar os arquivos
        Entrada.close();
        Saida.close();


        //Chegou ate aqui.... LOG que deu certo!!!!
        Log.e(TAG,"Banco de dados foi copiado!!!!");
    }
        catch(IOException e)
    {
        e.printStackTrace();
    }
    }

    //Verificando se DB ja existe
    public boolean checaDB() throws IOException {
        db = null;

        try
        {
            //Forcando a abertura do Banco de Dados
            SQLiteDatabase.openDatabase(saidaFile,null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        catch(SQLiteException erro)
        {
            copiaDB();
        }

        if(db != null)
        {
            db.close();
        }

        return db != null ? true : false;
    }

    public void verificarECopiarDB() throws IOException {
        File dbFile = new File(DB_Path + DB_Name);

        if (!dbFile.exists()) {
            copiaDB();
        }
    }

    public void openDB() throws IOException {
        verificarECopiarDB();

        try {
            db = getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Cria a tabela TB_USUARIO
        String createTableUsuario = "CREATE TABLE IF NOT EXISTS TB_USUARIO (\n" +
                "    ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    NOME_USUARIO VARCHAR(40) NOT NULL,\n" +
                "    SOBRENOME_USUARIO VARCHAR(60),\n" +
                "    EMAIL_USUARIO VARCHAR(60) NOT NULL UNIQUE,\n" +
                "    SENHA_USUARIO VARCHAR(30) NOT NULL,\n" +
                "    TELEFONE_EMPRESA VARCHAR(16),\n" +
                "    STATUS_USUARIO VARCHAR(60),\n" +
                "    DTA_CADASTRO_USUARIO DATE\n" +
                ");";

        //Cria a tabela TB_EMPRESA
        String createTableEmpresa = "CREATE TABLE IF NOT EXISTS TB_EMPRESA (\n" +
                "    ID_EMPRESA INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    NOME_EMPRESA VARCHAR(60) NOT NULL UNIQUE,\n" +
                "    EMAIL_EMPRESA VARCHAR(60) NOT NULL UNIQUE,\n" +
                "    STATUS_USUARIO VARCHAR(60),\n" +
                "    DTA_CADASTRO_USUARIO DATE,\n" +
                "    CNPJ_EMPRESA VARCHAR(20) NOT NULL UNIQUE,\n" +
                "    SALDO_EMPRESA NUMERIC DEFAULT 0,\n" +
                "    TELEFONE_EMPRESA VARCHAR(16)\n" +
                ");";

        // Cria a tabela TB_CLIENTE
        String createTableCliente = "CREATE TABLE IF NOT EXISTS TB_CLIENTE (\n" +
                "    ID_CLIENTE INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    NOME_CLIENTE VARCHAR(40) NOT NULL,\n" +
                "    EMAIL_CLIENTE VARCHAR(60) NOT NULL UNIQUE,\n" +
                "    STATUS_CLIENTE VARCHAR(60),\n" +
                "    DTA_CADASTRO_CLIENTE DATE UNIQUE,\n" +
                "    FONE_CLIENTE VARCHAR(16)\n" +
                ");";

        // Crie a tabela TB_PRODUTO
        String createTableProduto = "CREATE TABLE IF NOT EXISTS TB_PRODUTO (\n" +
                "    ID_PROD INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    NOME_PROD VARCHAR(60) NOT NULL,\n" +
                "    QTD_PROD DECIMAL(4,1),\n" +
                "    VALOR_VENDA_PROD DECIMAL(6,2),\n" +
                "    VALOR_CUSTO_PROD DECIMAL(6,2),\n" +
                "    DESC_PROD VARCHAR(100),\n" +
                "    QTD_VENDA DECIMAL(4,1),\n" +
                "    STATUS_PROD VARCHAR\n" +
                ");";

        // Crie a tabela TB_METAS_FINANCEIRAS
        String createTableMetas = "CREATE TABLE IF NOT EXISTS TB_METAS_FINANCEIRAS (\n" +
                "    ID_META INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    NOME_META VARCHAR(60) NOT NULL,\n" +
                "    SALDO_EMPRESA_USUARIO NUMERIC DEFAULT 0,\n" +
                "    VALOR_META DECIMAL(6,2),\n" +
                "    VALOR_META_ATUAL DECIMAL(6,2)\n" +  // Adiciona a nova coluna
                ");";


        // Crie a tabela TB_GASTOS_GANHOS
        String createTableGastosGanhos = "CREATE TABLE IF NOT EXISTS TB_GASTOS_GANHOS (\n" +
                "    GASTO NUMERIC(4,2),\n" +
                "    GANHO NUMERIC(4,2),\n" +
                "    LUCRO NUMERIC(4,2)\n" +
                ");";

        // Crie a tabela TB_PEDIDO_COMPRA
        String createTablePedidoCompra = "CREATE TABLE IF NOT EXISTS TB_PEDIDO_COMPRA (\n" +
                "    ID_PED_COMPRA INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    DTA_PED_COMPRA DATE,\n" +
                "    VALOR_PED_COMPRA DECIMAL(6,2),\n" +
                "    ID_CLIENTE INTEGER DEFAULT 0,\n" +
                "    STATUS_PED_COMPRA VARCHAR(60)\n" +
                ");";


        // Execute as queries para criar as tabelas
        sqLiteDatabase.execSQL(createTableUsuario);
        sqLiteDatabase.execSQL(createTableEmpresa);
        sqLiteDatabase.execSQL(createTableCliente);
        sqLiteDatabase.execSQL(createTableProduto);
        sqLiteDatabase.execSQL(createTableMetas);  // Inclui a nova tabela TB_METAS_FINANCEIRAS
        sqLiteDatabase.execSQL(createTableGastosGanhos);
        sqLiteDatabase.execSQL(createTablePedidoCompra);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    // Método para executar queries SQL
    public void executarQuery(String query, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query, args);
    }


    public void atualizarValorMetaAtual(int metaId, double valorInicial) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("VALOR_META_ATUAL", valorInicial);
        String whereClause = "ID_META = ?";
        String[] whereArgs = {String.valueOf(metaId)};
        db.update("TB_METAS_FINANCEIRAS", values, whereClause, whereArgs);
        db.close();
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, selectionArgs);
    }
}
