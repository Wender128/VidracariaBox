package com.example.vidracaria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrcamentoAdapter extends RecyclerView.Adapter<OrcamentoAdapter.ViewHolder> {

    private List<String> orcamentos; // Lista de orçamentos
    private final OnOrcamentoListener onOrcamentoListener; // Listener para clique no orçamento
    private final OnDeleteOrcamentoListener onDeleteOrcamentoListener; // Listener para clique no botão de deletar

    // Interface para capturar o clique no orçamento
    public interface OnOrcamentoListener {
        void onOrcamentoClick(int position);
    }

    // Interface para capturar o clique no botão de deletar
    public interface OnDeleteOrcamentoListener {
        void onDeleteClick(int position);
    }

    // Construtor que inicializa a lista de orçamentos e os listeners
    public OrcamentoAdapter(List<String> orcamentos, OnOrcamentoListener onOrcamentoListener, OnDeleteOrcamentoListener onDeleteOrcamentoListener) {
        this.orcamentos = orcamentos;
        this.onOrcamentoListener = onOrcamentoListener;
        this.onDeleteOrcamentoListener = onDeleteOrcamentoListener;
    }

    // Método para criar a view holder (controle visual de cada item na lista)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout para o item da lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orcamento, parent, false);
        return new ViewHolder(view, onOrcamentoListener, onDeleteOrcamentoListener); // Retorna um novo ViewHolder
    }

    // Método para vincular os dados do item com o ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Define o texto do item da lista
        holder.textViewOrcamento.setText(orcamentos.get(position));
    }

    // Método para retornar o número total de itens da lista
    @Override
    public int getItemCount() {
        return orcamentos.size(); // Retorna o tamanho da lista de orçamentos
    }

    // Método para retornar o item na posição especificada
    public String getItem(int position) {
        return orcamentos.get(position); // Retorna o orçamento na posição fornecida
    }

    // Método para atualizar a lista de orçamentos
    public void updateData(List<String> newOrcamentos) {
        this.orcamentos = newOrcamentos;
        notifyDataSetChanged(); // Notifica o RecyclerView para atualizar os itens
    }

    // Classe ViewHolder que é responsável por gerenciar a visualização de cada item da lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrcamento; // TextView para mostrar o orçamento
        ImageButton btnDelete; // Botão de deletar o orçamento

        // Construtor do ViewHolder que recebe a view do item e os listeners
        public ViewHolder(View itemView, OnOrcamentoListener onOrcamentoListener, OnDeleteOrcamentoListener onDeleteOrcamentoListener) {
            super(itemView);

            textViewOrcamento = itemView.findViewById(R.id.textViewOrcamento); // Inicializa o TextView
            btnDelete = itemView.findViewById(R.id.btnDelete); // Inicializa o botão de deletar

            // Define o comportamento para quando o item da lista for clicado
            itemView.setOnClickListener(v -> {
                if (onOrcamentoListener != null) {
                    int position = getAdapterPosition(); // Pega a posição do item clicado
                    if (position != RecyclerView.NO_POSITION) {
                        onOrcamentoListener.onOrcamentoClick(position); // Chama o método do listener
                    }
                }
            });

            // Define o comportamento para quando o botão de deletar for clicado
            btnDelete.setOnClickListener(v -> {
                if (onDeleteOrcamentoListener != null) {
                    int position = getAdapterPosition(); // Pega a posição do item
                    if (position != RecyclerView.NO_POSITION) {
                        onDeleteOrcamentoListener.onDeleteClick(position); // Chama o método do listener para deletar
                    }
                }
            });
        }
    }
}
