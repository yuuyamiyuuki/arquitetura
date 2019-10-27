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

import br.com.arquitetura.hotelaria.model.Quarto;
import br.com.uol.pagseguro.api.PagSeguro;
import br.com.uol.pagseguro.api.PagSeguroEnv;
import br.com.uol.pagseguro.api.checkout.CheckoutRegistrationBuilder;
import br.com.uol.pagseguro.api.checkout.RegisteredCheckout;
import br.com.uol.pagseguro.api.common.domain.ShippingType;
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
import br.com.uol.pagseguro.api.utils.logging.SimpleLoggerFactory;


/**
 * Backing bean for Quarto entities.
 * <p/>
 * This class provides CRUD functionality for all Quarto entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class QuartoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Quarto entities
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

	private Quarto quarto;

	public Quarto getQuarto() {
		return this.quarto;
	}

	public void setQuarto(Quarto quarto) {
		this.quarto = quarto;
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
			this.quarto = this.example;
		} else {
			this.quarto = findById(getId());
		}
	}

	public Quarto findById(Long id) {

		return this.entityManager.find(Quarto.class, id);
	}

	/*
	 * Support updating and deleting Quarto entities
	 */

	public String update() {

		try {
		
			if (this.id == null) {
				this.entityManager.persist(this.quarto);		
				this.conversation.end();
				return "view?faces-redirect=true&id=" + this.quarto.getId();
			} else {	    
				this.entityManager.merge(this.quarto);
				return "view?faces-redirect=true&id=" + this.quarto.getId();
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
			Quarto deletableEntity = findById(getId());

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
	 * Support searching Quarto entities with pagination
	 */

	private int page;
	private long count;
	private List<Quarto> pageItems;

	private Quarto example = new Quarto();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Quarto getExample() {
		return this.example;
	}

	public void setExample(Quarto example) {
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
		Root<Quarto> root = countCriteria.from(Quarto.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Quarto> criteria = builder.createQuery(Quarto.class);
		root = criteria.from(Quarto.class);
		criteria.orderBy(builder.asc(root.get("nomeQuarto")));
		TypedQuery<Quarto> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
//		query.setFirstResult(this.page * getPageSize()).setMaxResults(
//				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Quarto> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		
		  String nomeQuarto = this.example.getNomeQuarto(); 
		  if (nomeQuarto != null && !"".equals(nomeQuarto)) {
		  predicatesList.add(builder.like( builder.lower(root.<String> get("nomeQuarto")),
		  '%' + nomeQuarto.toLowerCase().trim() + '%')); }
		  
		  
		  String disponivelPesquisa = this.example.getDisponivelPesquisa();
		  if (disponivelPesquisa != null && !"".equals(disponivelPesquisa)) 
		  {
			  if (disponivelPesquisa.equals("Sim")) {
		  predicatesList.add(builder.equal(root.get("disponivel"), true)); 
		  } 
			  else if(disponivelPesquisa.equals("Nao")) 
			  {
		  predicatesList.add(builder.equal(root.get("disponivel"), false)); 
		  } 
			  }
		  
		  String quartoPesquisa = this.example.getQuartoPesquisa();
		  if (quartoPesquisa != null && !"".equals(quartoPesquisa)) 
		  {
			  if (quartoPesquisa.equals("Sim")) {
		  predicatesList.add(builder.equal(root.get("quartoLimpo"), true)); 
		  } 
			  else if(quartoPesquisa.equals("Nao")) 
			  {
		  predicatesList.add(builder.equal(root.get("quartoLimpo"), false)); 
		  } 
			  }

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}


	

	public List<Quarto> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Quarto entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Quarto> getAll() {

		CriteriaQuery<Quarto> criteria = this.entityManager.getCriteriaBuilder()
				.createQuery(Quarto.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Quarto.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final QuartoBean ejbProxy = this.sessionContext
				.getBusinessObject(QuartoBean.class);

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

				return String.valueOf(((Quarto) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */
      
	public List<Quarto> getAllDisponivel()
	{
		List <Quarto> quartos = this.getAll();
		List <Quarto> disponiveis = new ArrayList<Quarto>();
		
		for (int i =0; i < quartos.size(); i++)
		{
			Quarto quarto = quartos.get(i);
			
			if (quarto.isDisponivel() == true && quarto.isQuartoLimpo() == true)
			{
				disponiveis.add(quarto);	
			}
			
		}
		
		return disponiveis;
		
	}
 	
	
	private Quarto add = new Quarto();

	public Quarto getAdd() {
		return this.add;
	}

	public Quarto getAdded() {
		Quarto added = this.add;
		this.add = new Quarto();
		return added;
	}
	
	public  void limparQuarto()
	{
		this.quarto.setQuartoLimpo(true);
		
	}
	
	public  void disponibilizarQuarto()
	{
		this.quarto.setDisponivel(true);
		
	}
	
	public List<Quarto> getAllAtivos() {

		List<Quarto> allQuartos= this.getAll();
		List<Quarto> allAtivos = new ArrayList<Quarto>();
		
		 for (int i = 0; i < allQuartos.size(); i++) {
			 Quarto quarto = allQuartos.get(i);
	     	  
	     	   if(quarto.isDisponivel() == true ){
	        	   allAtivos.add(quarto);
	           }
	          
	       }
	        
	       return allAtivos;
	}
	
	
}
