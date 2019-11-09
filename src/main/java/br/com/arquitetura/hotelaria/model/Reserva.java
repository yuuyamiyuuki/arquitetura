package br.com.arquitetura.hotelaria.model;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Reserva")
public class Reserva implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", updatable = false, nullable = false)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_Quarto", nullable = true)
	private Quarto quarto;

	@ManyToOne
	@JoinColumn(name = "id_Pagamento", nullable = true)
	private Pagamento pagamento;
	
	@Column(name ="Data_Checkin")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCheckin;
	
	@Column(name ="Data_Checkout")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCheckout;
	
	@Column(name ="Status_Reserva")
	private Status status;
	
	@Column(name ="Desconto_Reserva")
	private int desconto;
	
	@Column(name="Extras_Reservas")
	private float extras;
	
	@Column(name="Reserva_Paga")
	private Boolean reservaPaga;
	
	@Column(name="Ultima_Transcancao")
	private String ultimaTransacao;
	
	@Column(name="Status_Pagamento")
	private String statusPagamento;
	
	@Transient
	private String codigo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Quarto getQuarto() {
		return quarto;
	}

	public void setQuarto(Quarto quarto) {
		this.quarto = quarto;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Date getDataCheckin() {
		return dataCheckin;
	}

	public void setDataCheckin(Date dataCheckin) {
		this.dataCheckin = dataCheckin;
	}

	public Date getDataCheckout() {
		return dataCheckout;
	}

	public void setDataCheckout(Date dataCheckout) {
		this.dataCheckout = dataCheckout;
	}


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public int getDesconto() {
		return desconto;
	}

	public void setDesconto(int desconto) {
		this.desconto = desconto;
	}

	public float getExtras() {
		return extras;
	}

	public void setExtras(float extras) {
		this.extras = extras;
	}

	public String getUltimaTransacao() {
		return ultimaTransacao;
	}

	public void setUltimaTransacao(String ultimaTransacao) {
		this.ultimaTransacao = ultimaTransacao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(String statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Reserva)) {
			return false;
		}
		Reserva other = (Reserva) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}
	
	public Boolean getReservaPaga() {
		return reservaPaga;
	}

	public void setReservaPaga(Boolean reservaPaga) {
		this.reservaPaga = reservaPaga;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}
