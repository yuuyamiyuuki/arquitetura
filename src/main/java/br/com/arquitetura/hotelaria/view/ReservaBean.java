package br.com.arquitetura.hotelaria.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.event.SelectEvent;

import br.com.arquitetura.hotelaria.model.Funcionario;
import br.com.arquitetura.hotelaria.model.Quarto;
import br.com.arquitetura.hotelaria.model.Reserva;
import br.com.arquitetura.hotelaria.model.Status;
import br.com.uol.pagseguro.api.common.domain.DataList;
import br.com.uol.pagseguro.api.common.domain.ShippingType;
import br.com.uol.pagseguro.api.PagSeguro;
import br.com.uol.pagseguro.api.PagSeguroEnv;
import br.com.uol.pagseguro.api.checkout.CheckoutRegistrationBuilder;
import br.com.uol.pagseguro.api.checkout.RegisteredCheckout;
import br.com.uol.pagseguro.api.common.domain.builder.AcceptedPaymentMethodsBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.AddressBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.ConfigBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.PaymentItemBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.PaymentMethodBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.PaymentMethodConfigBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.PhoneBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.SenderBuilder;
import br.com.uol.pagseguro.api.common.domain.builder.ShippingBuilder;
import br.com.uol.pagseguro.api.common.domain.enums.ConfigKey;
import br.com.uol.pagseguro.api.common.domain.enums.Currency;
import br.com.uol.pagseguro.api.common.domain.enums.PaymentMethodGroup;
import br.com.uol.pagseguro.api.common.domain.enums.State;
import br.com.uol.pagseguro.api.credential.Credential;
import br.com.uol.pagseguro.api.http.JSEHttpClient;
import br.com.uol.pagseguro.api.transaction.search.TransactionDetail;
import br.com.uol.pagseguro.api.transaction.search.TransactionSummary;
import br.com.uol.pagseguro.api.utils.logging.SimpleLoggerFactory;


/**
 * Backing bean for Reserva entities.
 * <p/>
 * This class provides CRUD functionality for all Reserva entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ReservaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Reserva entities
	 */
	
	
private Long queryId;
	
    private boolean updateTeste = true;
	
	private String testeID;

	public String getTesteID() {
		return testeID;
	}
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Reserva reserva;

	public Reserva getReserva() {
		return this.reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "hotelaria-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.reserva = this.example;
		} else {
			this.reserva = findById(getId());
		}
	}

	public Reserva findById(Long id) {

		return this.entityManager.find(Reserva.class, id);
	}

	/*
	 * Support updating and deleting Reserva entities
	 */

	public String update() {

		try {
		
			if (this.id == null) {
				this.reserva.setStatus(Status.PEDIDO_RESERVA);
				this.entityManager.persist(this.reserva);		
				this.conversation.end();
				return "view?faces-redirect=true&id=" + this.reserva.getId();
			} else {	    
				this.entityManager.merge(this.reserva);
				return "view?faces-redirect=true&id=" + this.reserva.getId();
			}
		} 
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			
			System.out.println(e);
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			Reserva deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	
	/*
	 * Support searching Reserva entities with pagination
	 */

	private int page;
	private long count;
	private List<Reserva> pageItems;

	private Reserva example = new Reserva();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Reserva getExample() {
		return this.example;
	}

	public void setExample(Reserva example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Reserva> root = countCriteria.from(Reserva.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Reserva> criteria = builder.createQuery(Reserva.class);
		root = criteria.from(Reserva.class);
		criteria.orderBy(builder.asc(root.get("id")));
		TypedQuery<Reserva> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
//		query.setFirstResult(this.page * getPageSize()).setMaxResults(
//				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Reserva> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
            
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}


	public List<Reserva> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Reserva entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Reserva> getAll() {

		CriteriaQuery<Reserva> criteria = this.entityManager.getCriteriaBuilder()
				.createQuery(Reserva.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Reserva.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ReservaBean ejbProxy = this.sessionContext
				.getBusinessObject(ReservaBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Reserva) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Reserva add = new Reserva();

	public Reserva getAdd() {
		return this.add;
	}

	public Reserva getAdded() {
		Reserva added = this.add;
		this.add = new Reserva();
		return added;
	}
	
	public void confirmarReserva()
	{
		this.reserva.setStatus(Status.RESERVA);
		
	}
	
	
		
	 private String sellerEmail = "heian.alien27@gmail.com";
	 private   String sellerToken = "C29C620B7C4C4435BD17580A8886D609"; 
	 private  PagSeguro pagSeguro =
	  PagSeguro.instance(new SimpleLoggerFactory(), new JSEHttpClient(),
	  Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.SANDBOX);
	 
	/*
	 * public String getPagamento() { String se = new Date().getSeconds()+""; String
	 * idT = ("Q"+this.reserva.getId()+se); System.out.println(idT);
	 * 
	 * 
	 * int dias = this.reserva.getDataCheckout().getDate() -
	 * this.reserva.getDataCheckin().getDate(); float valorDias = (dias *
	 * this.reserva.getQuarto().getValorDiaria());
	 * 
	 * 
	 * RegisteredCheckout registeredCheckout = pagSeguro.checkouts().register( new
	 * CheckoutRegistrationBuilder() .withCurrency(Currency.BRL)
	 * .withExtraAmount(new BigDecimal(this.reserva.getExtras()+0))
	 * .withReference("XXXXXX") .addItem(new PaymentItemBuilder() .withId(idT)
	 * .withDescription("HOTEL - " + this.reserva.getQuarto().getNomeQuarto())
	 * .withAmount(new BigDecimal(valorDias)) .withQuantity(1) .withWeight(0))
	 * .withAcceptedPaymentMethods(new AcceptedPaymentMethodsBuilder()
	 * .addInclude(new PaymentMethodBuilder()
	 * .withGroup(PaymentMethodGroup.CREDIT_CARD) ) ) .addPaymentMethodConfig(new
	 * PaymentMethodConfigBuilder() .withPaymentMethod(new PaymentMethodBuilder()
	 * .withGroup(PaymentMethodGroup.CREDIT_CARD) ) .withConfig(new ConfigBuilder()
	 * .withKey(ConfigKey.DISCOUNT_PERCENT) .withValue(new BigDecimal(10.00)) ) )
	 * .addPaymentMethodConfig(new PaymentMethodConfigBuilder()
	 * .withPaymentMethod(new PaymentMethodBuilder()
	 * .withGroup(PaymentMethodGroup.BANK_SLIP) ) .withConfig(new ConfigBuilder()
	 * .withKey(ConfigKey.DISCOUNT_PERCENT) .withValue(new BigDecimal(10.00)) ) ));
	 * 
	 * this.reserva.setUltimaTransacao(idT);
	 * System.out.println(registeredCheckout.getRedirectURL()); return
	 * registeredCheckout.getRedirectURL(); }
	 */
	 
	 public String getPagamento()
	  { 
	  String sellerEmail = "heian.alien27@gmail.com";
	  String sellerToken = "C29C620B7C4C4435BD17580A8886D609"; 
	  PagSeguro pagSeguro = PagSeguro.instance(new SimpleLoggerFactory(), new JSEHttpClient(),
	  Credential.sellerCredential(sellerEmail, sellerToken), PagSeguroEnv.SANDBOX);
	     String se = new Date().getSeconds()+""; 
		 String idT = ("Q"+this.reserva.getId()+se); System.out.println(idT); 
		 
		 int dias = this.reserva.getDataCheckout().getDate() - this.reserva.getDataCheckin().getDate();
		 System.out.println(dias);
		 
		 if(dias ==0)
		 {
			 dias = 1 ;
		 }
		 
		 
		 
		 float valorDias = (dias *this.reserva.getQuarto().getValorDiaria());
	  
	  RegisteredCheckout registeredCheckout = pagSeguro.checkouts().register(
           new CheckoutRegistrationBuilder()
               .withCurrency(Currency.BRL)
               .withExtraAmount(new BigDecimal(this.reserva.getExtras()))     
               .withReference(idT)
               .addItem(new PaymentItemBuilder()
                   .withId(idT)
                   .withDescription("HOTEL - " + valorDias)
                   .withAmount(new BigDecimal(valorDias))
                   .withQuantity(1)
                   .withWeight(0))
               .withAcceptedPaymentMethods(new AcceptedPaymentMethodsBuilder()
                       .addInclude(new PaymentMethodBuilder()
                               .withGroup(PaymentMethodGroup.CREDIT_CARD)
                           )
                   )
               .addPaymentMethodConfig(new PaymentMethodConfigBuilder()
                       .withPaymentMethod(new PaymentMethodBuilder()
                           .withGroup(PaymentMethodGroup.CREDIT_CARD)
                       )
                       .withConfig(new ConfigBuilder()
                           .withKey(ConfigKey.DISCOUNT_PERCENT)
                           .withValue(new BigDecimal(this.reserva.getDesconto()))
                       )
                   ));

	  
	  System.out.println(registeredCheckout.getRedirectURL());
	  this.reserva.setUltimaTransacao(idT);
	  return registeredCheckout.getRedirectURL();
	  }
	 
	 public void statusPagamento()
	 {      
		 if(this.reserva.getUltimaTransacao() != null)
		 {
		 String code = this.example.getCodigo();
		 
			 System.out.println(this.reserva.getUltimaTransacao());
			 System.out.println(code);
			 TransactionDetail transaction = pagSeguro.transactions().search().byCode(code);
		 this.reserva.setStatusPagamento(transaction.getStatus().getStatus().name());
		 }
		 }
	 
	 public void testePag()
	 {
		 final DataList<? extends TransactionSummary> transactionDate = pagSeguro
					.transactions().search().byReference(this.reserva.getUltimaTransacao());
		 System.out.println(transactionDate.toString());
		 for (int i = 0; i < transactionDate.getData().size(); i++)
		 {
			 System.out.println(transactionDate.getData().get(i).getStatus().getStatus().name());
		 }
		/*String codigo = transactionDate.getData().get(0).getStatus().getStatus().name();
		System.out.println("codigo:" + codigo);*/
		 
	 }
			 
	 
	 public void limparTransacao()// caso seja necessario um novo pagamento/status cancelado
	 {
		 this.reserva.setUltimaTransacao(null);
	 }
	

}
