package br.com.arquitetura.hotelaria.model;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Pagamento")
public class Pagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name ="Metodo_Pagamento")
	private String metodoPagamento;
	
	@Column(name ="Valor_Pagamento")
	private float valorPagamento;
	
	@Column(name ="Situacao")
	private String situacoa;
	
	@Column(name ="Promocao")
	private Boolean promoacao;
	
	@Column(name ="Extras")
	private Boolean extras;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMetodoPagamento() {
		return metodoPagamento;
	}

	public void setMetodoPagamento(String metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
	}

	public float getValorPagamento() {
		return valorPagamento;
	}

	public void setValorPagamento(float valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public String getSituacoa() {
		return situacoa;
	}

	public void setSituacoa(String situacoa) {
		this.situacoa = situacoa;
	}

	public Boolean getPromoacao() {
		return promoacao;
	}

	public void setPromoacao(Boolean promoacao) {
		this.promoacao = promoacao;
	}

	public Boolean getExtras() {
		return extras;
	}

	public void setExtras(Boolean extras) {
		this.extras = extras;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Pagamento)) {
			return false;
		}
		Pagamento other = (Pagamento) obj;
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
