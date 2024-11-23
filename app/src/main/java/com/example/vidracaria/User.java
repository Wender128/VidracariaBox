package com.example.vidracaria;
public class User {
    private String nome;
    private String sobrenome;
    private String telefone;
    private String endereco;
    private String email;

    // Construtor da classe User
    public User(String nome, String sobrenome, String telefone, String endereco, String email) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
    }

    // Getters (Métodos para acessar os valores das variáveis)
    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEmail() {
        return email;
    }
}

