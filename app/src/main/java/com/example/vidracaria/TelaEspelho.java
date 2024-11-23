package com.example.vidracaria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TelaEspelho extends AppCompatActivity {

    private EditText editTextComprimento, editTextLargura; // Campos de entrada de comprimento e largura
    private RadioGroup radioGroupCaracteristicas; // Grupo de botões de rádio para selecionar a característica do espelho
    private TextView textViewResultado; // TextView para exibir o resultado
    private ImageButton btnArrow, btnVoltar; // Botões para navegação
    private DatabaseHelper databaseHelper; // Ajudante de banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_espelho);

        // Inicializa os componentes da interface
        editTextComprimento = findViewById(R.id.editComprimentoEspelho);
        editTextLargura = findViewById(R.id.editLarguraEspelho);
        radioGroupCaracteristicas = findViewById(R.id.radioEspelho);
        textViewResultado = findViewById(R.id.textViewResultadoEspelho);
        Button buttonCalcular = findViewById(R.id.buttonCalcularEspelho);
        btnArrow = findViewById(R.id.bt_Proximo);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Inicializa o database helper para manipulação de banco de dados
        databaseHelper = new DatabaseHelper(this);

        // Configura o listener para o botão calcular
        buttonCalcular.setOnClickListener(v -> calcularResultado());

        // Configura o listener do botão de seta (btnArrow) para abrir a TelaOrcamento
        btnArrow.setOnClickListener(view -> {
            // Passa os dados do espelho para a TelaOrcamento
            String dadosEspelho = getDadosEspelho();
            Intent intent = new Intent(TelaEspelho.this, TelaOrcamento.class);
            intent.putExtra("dadosEspelho", dadosEspelho);
            startActivity(intent);
        });

        // Configura o listener do botão de voltar (se necessário)
        btnVoltar.setOnClickListener(v -> finish());
    }

    // Método para calcular o resultado do orçamento
    private void calcularResultado() {
        // Obtém os valores dos campos de comprimento e largura
        String comprimentoStr = editTextComprimento.getText().toString();
        String larguraStr = editTextLargura.getText().toString();

        // Verifica se os campos estão preenchidos
        if (comprimentoStr.isEmpty() || larguraStr.isEmpty()) {
            textViewResultado.setText("Por favor, preencha o comprimento e a largura.");
            return;
        }

        // Converte os valores de String para double
        double comprimento = Double.parseDouble(comprimentoStr);
        double largura = Double.parseDouble(larguraStr);

        // Obtém a característica selecionada
        int selectedCaracteristicaId = radioGroupCaracteristicas.getCheckedRadioButtonId();
        RadioButton radioButtonCaracteristica = findViewById(selectedCaracteristicaId);
        String caracteristicaSelecionada = radioButtonCaracteristica != null ? radioButtonCaracteristica.getText().toString() : "Nenhuma característica selecionada";

        // Calcula a área (em metros quadrados, já que a entrada é em metros)
        double area = comprimento * largura;

        // Obtém o preço por metro quadrado com base na característica
        double precoPorMetroQuadrado = getPrecoPorMetroQuadrado(caracteristicaSelecionada);

        // Verifica se o preço é válido
        if (precoPorMetroQuadrado == 0) {
            textViewResultado.setText("Combinação de característica inválida.");
            return;
        }

        // Calcula o preço total
        double precoTotal = area * precoPorMetroQuadrado;

        // Exibe o resultado
        String resultado = String.format("Característica: %s\nÁrea: %.2f m²\nPreço Total: R$ %.2f", caracteristicaSelecionada, area, precoTotal);
        textViewResultado.setText(resultado);

        // Salva os dados no banco de dados
        boolean sucesso = databaseHelper.addEspelhoData(comprimento, largura, caracteristicaSelecionada, precoTotal);
        if (sucesso) {
            Toast.makeText(this, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao salvar o pedido.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método que retorna o preço por metro quadrado com base na característica do espelho
    private double getPrecoPorMetroQuadrado(String caracteristica) {
        switch (caracteristica) {
            case "Normal":
                return 130; // Preço para espelho normal
            case "Bisotado":
                return 270; // Preço para espelho bisotado
            default:
                return 0; // Caso nenhuma combinação seja válida
        }
    }

    // Método que retorna uma string com os dados do espelho para enviar à próxima tela
    private String getDadosEspelho() {
        // Obtém os valores do espelho para concatenar
        String comprimentoStr = editTextComprimento.getText().toString();
        String larguraStr = editTextLargura.getText().toString();

        // Obtém a característica selecionada
        int selectedCaracteristicaId = radioGroupCaracteristicas.getCheckedRadioButtonId();
        RadioButton radioButtonCaracteristica = findViewById(selectedCaracteristicaId);
        String caracteristicaSelecionada = radioButtonCaracteristica != null ? radioButtonCaracteristica.getText().toString() : "Nenhuma característica selecionada";

        // Concatena os dados e retorna a string
        return "Espelho: \nComprimento: " + comprimentoStr + " m\n" +
                "Largura: " + larguraStr + " m\n" +
                "Característica: " + caracteristicaSelecionada;
    }
}
