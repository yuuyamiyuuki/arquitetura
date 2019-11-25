package br.com.arquitetura.hotelaria.view;

import java.io.Serializable;
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

import br.com.arquitetura.hotelaria.model.PecasEquipamento;


/**
 * Backing bean for PecasEquipamento entities.
 * <p/>
 * This class provides CRUD functionality for all PecasEquipamento entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PecasEquipamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving PecasEquipamento entities
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

	private PecasEquipamento pecasEquipamento;

	public PecasEquipamento getPecasEquipamento() {
		return this.pecasEquipamento;
	}

	public void setPecasEquipamento(PecasEquipamento pecasEquipamento) {
		this.pecasEquipamento = pecasEquipamento;
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
			this.pecasEquipamento = this.example;
		} else {
			this.pecasEquipamento = findById(getId());
		}
	}

	public PecasEquipamento findById(Long id) {

		return this.entityManager.find(PecasEquipamento.class, id);
	}

	/*
	 * Support updating and deleting PecasEquipamento entities
	 */

	public String update() {

		try {
		
			if (this.id == null) {
				this.entityManager.persist(this.pecasEquipamento);		
				this.conversation.end();
				return "view?faces-redirect=true&id=" + this.pecasEquipamento.getId();
			} else {	    
				this.entityManager.merge(this.pecasEquipamento);
				return "view?faces-redirect=true&id=" + this.pecasEquipamento.getId();
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
			PecasEquipamento deletableEntity = findById(getId());

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
	 * Support searching PecasEquipamento entities with pagination
	 */

	private int page;
	private long count;
	private List<PecasEquipamento> pageItems;

	private PecasEquipamento example = new PecasEquipamento();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public PecasEquipamento getExample() {
		return this.example;
	}

	public void setExample(PecasEquipamento example) {
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
		Root<PecasEquipamento> root = countCriteria.from(PecasEquipamento.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<PecasEquipamento> criteria = builder.createQuery(PecasEquipamento.class);
		root = criteria.from(PecasEquipamento.class);
		criteria.orderBy(builder.asc(root.get("nome")));
		TypedQuery<PecasEquipamento> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
//		query.setFirstResult(this.page * getPageSize()).setMaxResults(
//				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<PecasEquipamento> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		
		  String nomePeca = this.example.getNomePeca(); if (nomePeca != null && !"".equals(nomePeca)) {
		  predicatesList.add(builder.like( builder.lower(root.<String> get("nomePeca")),
		  '%' + nomePeca.toLowerCase().trim() + '%')); }
		  
		  String tipoPecas = this.example.getTipoPecas(); if (tipoPecas != null && !"".equals(tipoPecas)) {
			  predicatesList.add(builder.like( builder.lower(root.<String> get("tipoPecas")),
			  '%' + tipoPecas.toLowerCase().trim() + '%')); }
		  
		  String situacaoPeca = this.example.getSituacaoPeca(); if (situacaoPeca != null && !"".equals(situacaoPeca)) {
			  predicatesList.add(builder.like( builder.lower(root.<String> get("situacaoPeca")),
			  '%' + situacaoPeca.toLowerCase().trim() + '%')); }

		 
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}



	public List<PecasEquipamento> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back PecasEquipamento entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<PecasEquipamento> getAll() {

		CriteriaQuery<PecasEquipamento> criteria = this.entityManager.getCriteriaBuilder()
				.createQuery(PecasEquipamento.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(PecasEquipamento.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PecasEquipamentoBean ejbProxy = this.sessionContext
				.getBusinessObject(PecasEquipamentoBean.class);

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

				return String.valueOf(((PecasEquipamento) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private PecasEquipamento add = new PecasEquipamento();

	public PecasEquipamento getAdd() {
		return this.add;
	}

	public PecasEquipamento getAdded() {
		PecasEquipamento added = this.add;
		this.add = new PecasEquipamento();
		return added;
	}
	
	
}
