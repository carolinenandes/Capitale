package com.example.tcc20;

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
import java.util.ArrayList;

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
    SQLiteDatabase db;

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


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
