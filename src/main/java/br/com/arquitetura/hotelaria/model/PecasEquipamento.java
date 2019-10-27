package br.com.arquitetura.hotelaria.model;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Pecas_Equipamentos")
public class PecasEquipamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name ="Nome_Peca")
	private String nomePeca;
	
	@Column(name ="Tipo_Pecas")
	private String tipoPecas;
	
	@Column(name ="Situacao_Peca")
	private String situacaoPeca;
	
	@Column(name ="Quantidade_Pecas")
	private Integer quantidadePecas;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomePeca() {
		return nomePeca;
	}

	public void setNomePeca(String nomePeca) {
		this.nomePeca = nomePeca;
	}

	public String getTipoPecas() {
		return tipoPecas;
	}

	public void setTipoPecas(String tipoPecas) {
		this.tipoPecas = tipoPecas;
	}

	public String getSituacaoPeca() {
		return situacaoPeca;
	}

	public void setSituacaoPeca(String situacaoPeca) {
		this.situacaoPeca = situacaoPeca;
	}

	public Integer getQuantidadePecas() {
		return quantidadePecas;
	}

	public void setQuantidadePecas(Integer quantidadePecas) {
		this.quantidadePecas = quantidadePecas;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PecasEquipamento)) {
			return false;
		}
		PecasEquipamento other = (PecasEquipamento) obj;
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
