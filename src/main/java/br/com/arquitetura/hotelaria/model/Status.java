package br.com.arquitetura.hotelaria.model;


public enum Status {
    PEDIDO_RESERVA(1,"PEDIDO DE RESERVA"), 
    QUARTO_INDISPONIVEL(2,"QUARTO INDISPONÍVEL"), 
    RESERVA(3,"RESERVA");
    private final int valor;
    private final String descricao;
    
    Status(int valorOpcao, String descricaoOpcao){
        valor = valorOpcao;
        descricao = descricaoOpcao;
    }
    
    public int getValor(){
        return valor;
    }
    
    public String getDescricao(){
        return descricao;
    }
}