package com.example.vidracaria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class TelaUsuario extends AppCompatActivity {

    private EditText editTextNome, editTextSobrenome, editTextTelefone, editTextEndereco, editTextEmail;
    private ImageButton buttonEnviar, buttonCompartilharDados, btnVoltar;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_usuario);

        // Inicialização dos campos e ImageButtons
        editTextNome = findViewById(R.id.editTextNome);
        editTextSobrenome = findViewById(R.id.editTextSobrenome);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextEndereco = findViewById(R.id.editTextEndereco);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonEnviar = findViewById(R.id.imageButtonConfirmar); // Referência ao ImageButton
        buttonCompartilharDados = findViewById(R.id.imageButtonCompartilharDados); // Referência ao ImageButton

        // Instância do DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Recupera os dados dos orçamentos e espelhos do banco de dados
        String orcamentosDetalhes = recuperarOrcamentos();
        String espelhosDetalhes = recuperarEspelhos();
        double precoOrcamentos = calcularPrecoTotalOrcamentos(databaseHelper.getAllOrcamentos()); // Calcula o preço total dos orçamentos
        double precoEspelhos = calcularPrecoTotalEspelhos(databaseHelper.getAllEspelhos()); // Calcula o preço total dos espelhos
        double precoTotal = precoOrcamentos + precoEspelhos; // Soma dos preços de orçamentos e espelhos

        // Inicializando e configurando o botão Voltar
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        // Configuração do ImageButton de envio de dados de usuário
        buttonEnviar.setOnClickListener(v -> {
            String nome = editTextNome.getText().toString().trim();
            String sobrenome = editTextSobrenome.getText().toString().trim();
            String telefone = editTextTelefone.getText().toString().trim();
            String endereco = editTextEndereco.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();

            if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty() || endereco.isEmpty() || email.isEmpty()) {
                Toast.makeText(TelaUsuario.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                boolean isInserted = databaseHelper.addUser(nome, sobrenome, telefone, endereco, email);
                if (isInserted) {
                    Toast.makeText(TelaUsuario.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TelaUsuario.this, "Erro ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuração do ImageButton para compartilhar os dados
        buttonCompartilharDados.setOnClickListener(v -> {
            String nome = editTextNome.getText().toString().trim();
            String sobrenome = editTextSobrenome.getText().toString().trim();
            String telefone = editTextTelefone.getText().toString().trim();
            String endereco = editTextEndereco.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();

            if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty() || endereco.isEmpty() || email.isEmpty()) {
                Toast.makeText(TelaUsuario.this, "Por favor, preencha todos os campos antes de compartilhar.", Toast.LENGTH_SHORT).show();
            } else {
                // Dados do usuário formatados
                String dadosUsuario = "Nome: " + nome + "\nSobrenome: " + sobrenome + "\nTelefone: " + telefone +
                        "\nEndereço: " + endereco + "\nEmail: " + email;

                // Montagem da mensagem para envio no WhatsApp
                String mensagemCompleta = dadosUsuario + "\n\nPreço Total Combinado: R$ " + String.format("%.2f", precoTotal) +
                        "\n\nDetalhes dos Orçamentos:\n" + orcamentosDetalhes +
                        "\n\nDetalhes dos Espelhos:\n" + espelhosDetalhes;

                // Preparação e envio da mensagem via WhatsApp
                String phoneNumber = "61998436267";
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(mensagemCompleta);

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse(url));
                startActivity(sendIntent);
            }
        });
    }

    // Método para recuperar os orçamentos e montar a string
    private String recuperarOrcamentos() {
        List<String> orcamentosList = databaseHelper.getAllOrcamentos();
        StringBuilder orcamentosDetalhes = new StringBuilder();
        for (String orcamento : orcamentosList) {
            orcamentosDetalhes.append(orcamento).append("\n\n"); // Adiciona quebra de linha entre os orçamentos
        }
        return orcamentosDetalhes.toString().trim(); // Remove espaços extras
    }

    // Método para recuperar os espelhos e montar a string
    private String recuperarEspelhos() {
        List<String> espelhosList = databaseHelper.getAllEspelhos();
        StringBuilder espelhosDetalhes = new StringBuilder();
        for (String espelho : espelhosList) {
            espelhosDetalhes.append(espelho).append("\n\n"); // Adiciona quebra de linha entre os espelhos
        }
        return espelhosDetalhes.toString().trim(); // Remove espaços extras
    }

    // Método para calcular o preço total dos orçamentos
    private double calcularPrecoTotalOrcamentos(List<String> orcamentos) {
        double total = 0;
        for (String orcamento : orcamentos) {
            double precoFinal = extractPrecoFromOrcamento(orcamento);
            total += precoFinal;
        }
        return total;
    }

    // Método para calcular o preço total dos espelhos
    private double calcularPrecoTotalEspelhos(List<String> espelhos) {
        double total = 0;
        for (String espelho : espelhos) {
            double precoFinal = extractPrecoFromEspelho(espelho);
            total += precoFinal;
        }
        return total;
    }

    // Método para extrair o preço de um orçamento
    private double extractPrecoFromOrcamento(String orcamento) {
        String[] partes = orcamento.split("\n");
        for (String parte : partes) {
            if (parte.contains("Preço Total")) {
                String precoStr = parte.replace("Preço Total: R$ ", "").replace(",", ".");
                return Double.parseDouble(precoStr);
            }
        }
        return 0;
    }

    // Método para extrair o preço de um espelho
    private double extractPrecoFromEspelho(String espelho) {
        String[] partes = espelho.split("\n");
        for (String parte : partes) {
            if (parte.contains("Preço Total")) {
                String precoStr = parte.replace("Preço Total: R$ ", "").replace(",", ".");
                return Double.parseDouble(precoStr);
            }
        }
        return 0;
    }
}
