package com.example.vidracaria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome do banco de dados e tabelas
    private static final String DATABASE_NAME = "Vidraaria.db";
    private static final String TABLE_ORCAMENTOS = "orcamentos";
    private static final String TABLE_ESPOLHO = "espelho";
    private static final String TABLE_USERS = "users";

    // Colunas da tabela de orçamentos
    private static final String COL_1 = "ID";
    private static final String COL_2 = "COMPRIMENTO";
    private static final String COL_3 = "LARGURA";
    private static final String COL_4 = "PRODUTO";
    private static final String COL_5 = "VIDRO";
    private static final String COL_6 = "PRECO_TOTAL";

    // Colunas da tabela de usuários
    private static final String USER_COL_1 = "ID";
    private static final String USER_COL_2 = "NOME";
    private static final String USER_COL_3 = "SOBRENOME";
    private static final String USER_COL_4 = "TELEFONE";
    private static final String USER_COL_5 = "ENDERECO";
    private static final String USER_COL_6 = "EMAIL";

    // Colunas da tabela de espelhos
    private static final String ESPOLHO_COL_1 = "ID";
    private static final String ESPOLHO_COL_2 = "COMPRIMENTO";
    private static final String ESPOLHO_COL_3 = "LARGURA";
    private static final String ESPOLHO_COL_4 = "ESPELHO";
    private static final String ESPOLHO_COL_5 = "PRECO_TOTAL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3); // Versão 3 do banco de dados (incrementada)
    }

    // Criação das tabelas quando o banco de dados é criado pela primeira vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela de orçamentos
        db.execSQL("CREATE TABLE " + TABLE_ORCAMENTOS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "COMPRIMENTO REAL, LARGURA REAL, PRODUTO TEXT, VIDRO TEXT, PRECO_TOTAL REAL)");

        // Criação da tabela de usuários
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NOME TEXT, SOBRENOME TEXT, TELEFONE TEXT, ENDERECO TEXT, EMAIL TEXT)");

        // Criação da tabela de espelhos
        db.execSQL("CREATE TABLE " + TABLE_ESPOLHO + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "COMPRIMENTO REAL, LARGURA REAL, ESPELHO TEXT, PRECO_TOTAL REAL)");
    }

    // Atualização do banco de dados quando a versão é alterada
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Remove as tabelas antigas e recria as novas tabelas se a versão do banco for atualizada
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORCAMENTOS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESPOLHO);
            onCreate(db); // Recria as tabelas
        }
    }

    // Método para adicionar dados na tabela de orçamentos
    public boolean addData(double comprimento, double largura, String produto, String vidro, double precoTotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, comprimento);
        contentValues.put(COL_3, largura);
        contentValues.put(COL_4, produto);
        contentValues.put(COL_5, vidro);
        contentValues.put(COL_6, precoTotal);

        long result = db.insert(TABLE_ORCAMENTOS, null, contentValues);
        return result != -1; // Retorna true se a inserção for bem-sucedida
    }

    // Método para adicionar dados na tabela de espelhos
    public boolean addEspelhoData(double comprimento, double largura, String espelho, double precoTotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ESPOLHO_COL_2, comprimento);
        contentValues.put(ESPOLHO_COL_3, largura);
        contentValues.put(ESPOLHO_COL_4, espelho);
        contentValues.put(ESPOLHO_COL_5, precoTotal);

        long result = db.insert(TABLE_ESPOLHO, null, contentValues);
        return result != -1;
    }

    // Método para adicionar dados na tabela de usuários
    public boolean addUser(String nome, String sobrenome, String telefone, String endereco, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COL_2, nome);
        values.put(USER_COL_3, sobrenome);
        values.put(USER_COL_4, telefone);
        values.put(USER_COL_5, endereco);
        values.put(USER_COL_6, email);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Método para recuperar todos os dados da tabela de orçamentos
    public Cursor getAllDataOrcamentos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ORCAMENTOS, null);
    }

    // Método para recuperar todos os dados da tabela de usuários
    public Cursor getAllDataUsuarios() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    // Método para recuperar todos os dados da tabela de espelhos
    public Cursor getAllDataEspelhos() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ESPOLHO, null);
    }

    // Método para deletar dados de orçamentos ou espelhos
    public boolean deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Exclui da tabela de orçamentos
        int orcamentosDeleted = db.delete(TABLE_ORCAMENTOS, "ID = ?", new String[]{String.valueOf(id)});

        // Exclui da tabela de espelhos
        int espelhosDeleted = db.delete(TABLE_ESPOLHO, "ID = ?", new String[]{String.valueOf(id)});

        db.close();

        // Retorna verdadeiro se algum dos itens foi excluído com sucesso
        return orcamentosDeleted > 0 || espelhosDeleted > 0;
    }

    // Método para recuperar todos os orçamentos formatados como uma lista de strings
    public List<String> getAllOrcamentos() {
        List<String> orcamentos = new ArrayList<>();
        Cursor cursor = getAllDataOrcamentos();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                StringBuilder orcamento = new StringBuilder();

                // Recupera os índices das colunas
                int idIndex = cursor.getColumnIndex(COL_1);
                int comprimentoIndex = cursor.getColumnIndex(COL_2);
                int larguraIndex = cursor.getColumnIndex(COL_3);
                int produtoIndex = cursor.getColumnIndex(COL_4);
                int vidroIndex = cursor.getColumnIndex(COL_5);
                int precoTotalIndex = cursor.getColumnIndex(COL_6);

                // Formata os dados em uma string
                if (idIndex != -1 && comprimentoIndex != -1 && larguraIndex != -1 &&
                        produtoIndex != -1 && vidroIndex != -1 && precoTotalIndex != -1) {

                    orcamento.append("ID: ").append(cursor.getInt(idIndex)).append("\n")
                            .append("Comprimento: ").append(cursor.getDouble(comprimentoIndex)).append(" m\n")
                            .append("Largura: ").append(cursor.getDouble(larguraIndex)).append(" m\n")
                            .append("Produto: ").append(cursor.getString(produtoIndex)).append("\n")
                            .append("Vidro: ").append(cursor.getString(vidroIndex)).append("\n")
                            .append("Preço Total: R$ ").append(cursor.getDouble(precoTotalIndex)).append("\n");

                    orcamentos.add(orcamento.toString());
                }
            }
            cursor.close();
        }
        return orcamentos;
    }

    // Método para recuperar todos os espelhos formatados como uma lista de strings
    public List<String> getAllEspelhos() {
        List<String> espelhos = new ArrayList<>();
        Cursor cursor = getAllDataEspelhos();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                StringBuilder espelho = new StringBuilder();

                // Recupera os índices das colunas
                int idIndex = cursor.getColumnIndex(ESPOLHO_COL_1);
                int comprimentoIndex = cursor.getColumnIndex(ESPOLHO_COL_2);
                int larguraIndex = cursor.getColumnIndex(ESPOLHO_COL_3);
                int espelhoIndex = cursor.getColumnIndex(ESPOLHO_COL_4);
                int precoTotalIndex = cursor.getColumnIndex(ESPOLHO_COL_5);

                // Formata os dados em uma string
                if (idIndex != -1 && comprimentoIndex != -1 && larguraIndex != -1 &&
                        espelhoIndex != -1 && precoTotalIndex != -1) {

                    espelho.append("ID: ").append(cursor.getInt(idIndex)).append("\n")
                            .append("Comprimento: ").append(cursor.getDouble(comprimentoIndex)).append(" m\n")
                            .append("Largura: ").append(cursor.getDouble(larguraIndex)).append(" m\n")
                            .append("Espelho: ").append(cursor.getString(espelhoIndex)).append("\n")
                            .append("Preço Total: R$ ").append(cursor.getDouble(precoTotalIndex)).append("\n");

                    espelhos.add(espelho.toString());
                }
            }
            cursor.close();
        }
        return espelhos;
    }

    // Método para fechar o banco de dados
    public void closeDatabase() {
        this.close();
    }
}
