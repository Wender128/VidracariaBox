package com.example.vidracaria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TelaOrcamento extends AppCompatActivity implements OrcamentoAdapter.OnOrcamentoListener, OrcamentoAdapter.OnDeleteOrcamentoListener {

    private DatabaseHelper databaseHelper;
    private OrcamentoAdapter orcamentoAdapter;
    private List<String> orcamentos; // Lista para armazenar orçamentos e espelhos combinados
    private TextView textViewTotalPreco;
    private ImageButton btnConfirmar; // Declarado como ImageButton
    private ImageButton btnVoltar; // Declarado como ImageButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_orcamento);

        // Inicializando os componentes
        databaseHelper = new DatabaseHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrcamentos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Carregar os orçamentos e espelhos
        orcamentos = new ArrayList<>();
        orcamentos.addAll(databaseHelper.getAllOrcamentos()); // Adiciona os orçamentos
        orcamentos.addAll(databaseHelper.getAllEspelhos()); // Adiciona os espelhos

        // Configurar o adapter
        orcamentoAdapter = new OrcamentoAdapter(orcamentos, this, this);
        recyclerView.setAdapter(orcamentoAdapter);

        // Inicializando o campo de texto do preço total
        textViewTotalPreco = findViewById(R.id.textViewFinalPreco);
        atualizarPrecoTotal();

        // Inicializando e configurando o botão Voltar
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish()); // Encerra a atividade atual e volta para a tela anterior (TelaVidro)

        // Inicializando e configurando o botão Confirmar
        btnConfirmar = findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(view -> {
            if (orcamentos != null && !orcamentos.isEmpty()) {
                boolean todosSalvos = true;

                // Salvar os orçamentos, implementando qualquer lógica de salvar aqui
                // Se todos os orçamentos forem salvos com sucesso, a lógica abaixo será executada
                if (todosSalvos) {
                    Toast.makeText(TelaOrcamento.this, "Orçamentos salvos com sucesso!", Toast.LENGTH_SHORT).show();
                    // Inicia a TelaUsuario
                    Intent intent = new Intent(TelaOrcamento.this, TelaUsuario.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(TelaOrcamento.this, "Erro ao salvar os orçamentos.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TelaOrcamento.this, "Não há orçamentos para confirmar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Atualiza o total do preço na interface
    private void atualizarPrecoTotal() {
        if (orcamentos != null && !orcamentos.isEmpty()) {
            double totalPreco = calcularPrecoTotal(orcamentos);
            textViewTotalPreco.setText("Preço Final: R$ " + String.format("%.2f", totalPreco));
        } else {
            textViewTotalPreco.setText("Preço Final: R$ 0,00");
        }
    }

    // Calcula o preço total dos orçamentos e espelhos combinados
    private double calcularPrecoTotal(List<String> orcamentos) {
        double total = 0;
        for (String orcamento : orcamentos) {
            double precoFinal = extractPrecoFromOrcamento(orcamento);
            total += precoFinal;
        }
        return total;
    }

    // Extrai o preço total de um orçamento ou espelho
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

    // Extrai o comprimento de um orçamento
    private double extractComprimentoFromOrcamento(String orcamento) {
        String[] partes = orcamento.split("\n");
        for (String parte : partes) {
            if (parte.contains("Comprimento:")) {
                String valorStr = parte.replace("Comprimento:", "").replace(" m", "").trim();
                try {
                    return Double.parseDouble(valorStr);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    // Extrai a largura de um orçamento
    private double extractLarguraFromOrcamento(String orcamento) {
        String[] partes = orcamento.split("\n");
        for (String parte : partes) {
            if (parte.contains("Largura:")) {
                String valorStr = parte.replace("Largura:", "").replace(" m", "").trim();
                try {
                    return Double.parseDouble(valorStr);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    // Extrai o produto de um orçamento
    private String extractProdutoFromOrcamento(String orcamento) {
        String[] partes = orcamento.split("\n");
        for (String parte : partes) {
            if (parte.contains("Produto:")) {
                return parte.replace("Produto:", "").trim();
            }
        }
        return "";
    }

    // Extrai o tipo de vidro de um orçamento
    private String extractVidroFromOrcamento(String orcamento) {
        String[] partes = orcamento.split("\n");
        for (String parte : partes) {
            if (parte.contains("Vidro:")) {
                return parte.replace("Vidro:", "").trim();
            }
        }
        return "";
    }

    // Método chamado quando um orçamento é clicado
    @Override
    public void onOrcamentoClick(int position) {
        String orcamento = orcamentoAdapter.getItem(position);
        // Aqui você pode adicionar a ação para visualizar detalhes do orçamento
    }

    // Método chamado quando o botão de excluir de um orçamento é clicado
    @Override
    public void onDeleteClick(int position) {
        String orcamentoString = orcamentoAdapter.getItem(position);
        int orcamentoId = extractIdFromOrcamento(orcamentoString);

        boolean sucesso = databaseHelper.deleteData(orcamentoId);

        if (sucesso) {
            Toast.makeText(this, "Orçamento excluído com sucesso!", Toast.LENGTH_SHORT).show();
            // Atualiza a lista de orçamentos e espelhos
            orcamentos = new ArrayList<>();
            orcamentos.addAll(databaseHelper.getAllOrcamentos());
            orcamentos.addAll(databaseHelper.getAllEspelhos());
            orcamentoAdapter.updateData(orcamentos); // Atualiza o adapter
            atualizarPrecoTotal();
        } else {
            Toast.makeText(this, "Erro ao excluir o orçamento.", Toast.LENGTH_SHORT).show();
        }
    }

    // Extrai o ID de um orçamento a partir de sua representação em texto
    private int extractIdFromOrcamento(String orcamento) {
        String[] partes = orcamento.split("\n");
        for (String parte : partes) {
            if (parte.contains("ID:")) {
                return Integer.parseInt(parte.replace("ID: ", ""));
            }
        }
        return -1;
    }
}
