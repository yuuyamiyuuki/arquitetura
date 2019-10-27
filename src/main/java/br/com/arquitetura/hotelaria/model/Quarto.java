package br.com.arquitetura.hotelaria.model;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Quarto")
public class Quarto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name ="Nome_Quarto")
	private String nomeQuarto;
	
	@Column(name ="Valor_Diaria")
	private float valorDiaria;
	
	@Column(name ="Disponivel", nullable = false)
	private boolean disponivel = true;
	
	@Column(name ="Quarto_Limpo")
	private boolean quartoLimpo = true;
	
	@Transient
	private String disponivelPesquisa;
	
	@Transient
	private String quartoPesquisa;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeQuarto() {
		return nomeQuarto;
	}

	public void setNomeQuarto(String nomeQuarto) {
		this.nomeQuarto = nomeQuarto;
	}

	public float getValorDiaria() {
		return valorDiaria;
	}

	public void setValorDiaria(float valorDiaria) {
		this.valorDiaria = valorDiaria;
	}

	public boolean isDisponivel() {
		return disponivel;
	}

	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}

	public boolean isQuartoLimpo() {
		return quartoLimpo;
	}

	public void setQuartoLimpo(boolean quartoLimpo) {
		this.quartoLimpo = quartoLimpo;
	}
	
	public String getDisponivelPesquisa() {
		return disponivelPesquisa;
	}

	public void setDisponivelPesquisa(String disponivelPesquisa) {
		this.disponivelPesquisa = disponivelPesquisa;
	}

	public String getQuartoPesquisa() {
		return quartoPesquisa;
	}

	public void setQuartoPesquisa(String quartoPesquisa) {
		this.quartoPesquisa = quartoPesquisa;
	}
    
	public String getQuartoSim()
	{
		if(this.isQuartoLimpo() == true)
		{
			return "Sim";
		}
		else
		{
			return "Não";
		}	
	}
	
	public String getDisponivelSim()
	{
		if(this.isDisponivel() == true)
		{
			return "Sim";
		}
		else
		{
			return "Não";
		}	
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Quarto)) {
			return false;
		}
		Quarto other = (Quarto) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}
