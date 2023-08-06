package com.example.tcc20;

public class Product {

    private String nome;
    private int qtd;
    private String valor_venda;
    private String valor_custo;
    private String desc;
    private int vendas;
    private String status;
        // Constructor, getters, and setters for the Product class...

        public Product(String nome, int qtd, String valor_venda, String valor_custo, String desc, int vendas, String status) {
            this.nome = nome;
            this.qtd = qtd;
            this.valor_venda = valor_venda;
            this.valor_custo = valor_custo;
            this.desc = desc;
            this.vendas = vendas;
            this.status = status;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getQtd() {
            return qtd;
        }

        public void setQtd(int qtd) {
            this.qtd = qtd;
        }

        public String getValor_venda() {
            return valor_venda;
        }

        public void setValor_venda(String valor_venda) {
            this.valor_venda = valor_venda;
        }

        public String getValor_custo() {
            return valor_custo;
        }

        public void setValor_custo(String valor_custo) {
            this.valor_custo = valor_custo;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getVendas() {
            return vendas;
        }

        public void setVendas(int vendas) {
            this.vendas = vendas;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }


