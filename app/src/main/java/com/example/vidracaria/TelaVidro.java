package com.example.vidracaria;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TelaVidro extends AppCompatActivity {

    private EditText editTextComprimento, editTextLargura;
    private RadioGroup radioGroupProdutos, radioGroupCaracteristicas;
    private TextView textViewResultado;
    private ImageButton btnArrow, btnVoltar;
    private DatabaseHelper databaseHelper; // Objeto para o banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_vidro);

        // Inicializa os componentes da interface
        editTextComprimento = findViewById(R.id.editComprimentoVidro);
        editTextLargura = findViewById(R.id.editLarguraVidro);
        radioGroupProdutos = findViewById(R.id.radioGroupProdutos);
        radioGroupCaracteristicas = findViewById(R.id.radioGroupCaracteristicas);
        textViewResultado = findViewById(R.id.textViewResultadoVidro);
        Button buttonCalcular = findViewById(R.id.buttonCalcularVidro);
        btnArrow = findViewById(R.id.bt_Proximo);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Inicialização do database helper
        databaseHelper = new DatabaseHelper(this);

        // Configura o listener para o botão calcular usando lambda
        buttonCalcular.setOnClickListener(v -> calcularResultado());

        // Configura o listener do botão de seta (btnArrow) para abrir a TelaOrcamento com todos os pedidos
        btnArrow.setOnClickListener(view -> {
            Intent intent = new Intent(TelaVidro.this, TelaOrcamento.class);
            startActivity(intent);
        });

        // Configura o listener para o botão de voltar
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void calcularResultado() {
        // Obtém os valores dos campos
        String comprimentoStr = editTextComprimento.getText().toString();
        String larguraStr = editTextLargura.getText().toString();

        if (comprimentoStr.isEmpty() || larguraStr.isEmpty()) {
            textViewResultado.setText("Por favor, preencha o comprimento e a largura.");
            return;
        }

        double comprimento = Double.parseDouble(comprimentoStr);
        double largura = Double.parseDouble(larguraStr);

        // Verifica se um produto foi selecionado
        int selectedProdutoId = radioGroupProdutos.getCheckedRadioButtonId();
        if (selectedProdutoId == -1) {
            textViewResultado.setText("Por favor, selecione um produto.");
            return;
        }
        RadioButton radioButtonProduto = findViewById(selectedProdutoId);
        String produtoSelecionado = radioButtonProduto.getText().toString();

        // Verifica se um tipo de vidro foi selecionado
        int selectedVidroId = radioGroupCaracteristicas.getCheckedRadioButtonId();
        if (selectedVidroId == -1) {
            textViewResultado.setText("Por favor, selecione um tipo de vidro.");
            return;
        }
        RadioButton radioButtonVidro = findViewById(selectedVidroId);
        String vidroSelecionado = radioButtonVidro.getText().toString();

        // Calcula a área (em metros quadrados, já que a entrada é em metros)
        double area = comprimento * largura;

        // Obtém o preço por metro quadrado com base nas seleções
        double precoPorMetroQuadrado = getPrecoPorMetroQuadrado(produtoSelecionado, vidroSelecionado);

        if (precoPorMetroQuadrado == 0) {
            textViewResultado.setText("Combinação de produto e tipo de vidro inválida.");
            return;
        }

        // Calcula o preço total
        double precoTotal = area * precoPorMetroQuadrado;

        // Exibe o resultado
        String resultado = String.format("Produto: %s\nTipo de Vidro: %s\nÁrea: %.2f m²\nPreço Total: R$ %.2f",
                produtoSelecionado, vidroSelecionado, area, precoTotal);
        textViewResultado.setText(resultado);

        // Salva os dados no banco de dados
        boolean isInserted = databaseHelper.addData(comprimento, largura, produtoSelecionado, vidroSelecionado, precoTotal);
        if (isInserted) {
            Toast.makeText(this, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao salvar o pedido.", Toast.LENGTH_SHORT).show();
        }
    }

    private double getPrecoPorMetroQuadrado(String produto, String vidro) {
        switch (vidro) {
            case "Temperado":
                switch (produto) {
                    case "Box": return 270;
                    case "Janela": return 360;
                    case "Porta": return 360;
                    case "Mesa": return 400;
                }
                break;
            case "Fumê":
                switch (produto) {
                    case "Box": return 290;
                    case "Janela": return 370;
                    case "Porta": return 370;
                    case "Mesa": return 410;
                }
                break;
            case "Laminado":
                switch (produto) {
                    case "Box": return 275;
                    case "Janela": return 365;
                    case "Porta": return 365;
                    case "Mesa": return 405;
                }
                break;
            case "Jateado":
                switch (produto) {
                    case "Box": return 320;
                    case "Janela": return 400;
                    case "Porta": return 400;
                    case "Mesa": return 430;
                }
                break;
        }
        return 0; // Caso nenhuma combinação seja válida
    }
}
