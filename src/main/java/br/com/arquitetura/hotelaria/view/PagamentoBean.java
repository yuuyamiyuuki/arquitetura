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

import br.com.arquitetura.hotelaria.model.Pagamento;


/**
 * Backing bean for Pagamento entities.
 * <p/>
 * This class provides CRUD functionality for all Pagamento entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PagamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Pagamento entities
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

	private Pagamento pagamento;

	public Pagamento getPagamento() {
		return this.pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
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
			this.pagamento = this.example;
		} else {
			this.pagamento = findById(getId());
		}
	}

	public Pagamento findById(Long id) {

		return this.entityManager.find(Pagamento.class, id);
	}

	/*
	 * Support updating and deleting Pagamento entities
	 */

	public String update() {

		try {
		
			if (this.id == null) {
				this.entityManager.persist(this.pagamento);		
				this.conversation.end();
				return "view?faces-redirect=true&id=" + this.pagamento.getId();
			} else {	    
				this.entityManager.merge(this.pagamento);
				return "view?faces-redirect=true&id=" + this.pagamento.getId();
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
			Pagamento deletableEntity = findById(getId());

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
	 * Support searching Pagamento entities with pagination
	 */

	private int page;
	private long count;
	private List<Pagamento> pageItems;

	private Pagamento example = new Pagamento();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Pagamento getExample() {
		return this.example;
	}

	public void setExample(Pagamento example) {
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
		Root<Pagamento> root = countCriteria.from(Pagamento.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Pagamento> criteria = builder.createQuery(Pagamento.class);
		root = criteria.from(Pagamento.class);
		criteria.orderBy(builder.asc(root.get("nome")));
		TypedQuery<Pagamento> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
//		query.setFirstResult(this.page * getPageSize()).setMaxResults(
//				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Pagamento> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		/*
		 * String nome = this.example.getNome(); if (nome != null && !"".equals(nome)) {
		 * predicatesList.add(builder.like( builder.lower(root.<String> get("nome")),
		 * '%' + nome.toLowerCase().trim() + '%')); }
		 * 
		 * 
		 * Date dataNascimento = this.example.getDataNascimento(); if (dataNascimento
		 * !=null && !"".equals(dataNascimento)) {
		 * predicatesList.add(builder.equal(root.get("dataNascimento"),
		 * dataNascimento)); }
		 * 
		 * String rg = this.example.getRg(); if (rg != null && !"".equals(rg)) {
		 * predicatesList.add(builder.like( builder.lower(root.<String> get("rg")), '%'
		 * + rg.toLowerCase().trim() + '%')); }
		 * 
		 * String cpf = this.example.getCpf().replace(".", "").replace("-", "");
		 * System.out.println(cpf); if (cpf != null && !"".equals(cpf)) {
		 * predicatesList.add(builder.like( builder.lower(root.<String> get("cpf")), '%'
		 * + cpf.toLowerCase().trim() + '%')); }
		 * 
		 * String nomeMae = this.example.getNomeMae(); if (nomeMae != null &&
		 * !"".equals(nomeMae)) { predicatesList.add(builder.like(
		 * builder.lower(root.<String> get("nomeMae")), '%' +
		 * nomeMae.toLowerCase().trim() + '%')); }
		 * 
		 * 
		 * String telefone = this.example.getTelefone(); if (telefone != null &&
		 * !"".equals(telefone)) { predicatesList.add(builder.like(
		 * builder.lower(root.<String> get("telefone")), '%' +
		 * telefone.toLowerCase().trim() + '%')); }
		 * 
		 * String telefoneFixo = this.example.getTelefoneFixo(); if (telefoneFixo !=
		 * null && !"".equals(telefoneFixo)) { predicatesList.add(builder.like(
		 * builder.lower(root.<String> get("telefoneFixo")), '%' +
		 * telefoneFixo.toLowerCase() + '%')); }
		 * 
		 * 
		 * String ativoTodos = this.example.getAtivoTodos();
		 * 
		 * 
		 * if (ativoTodos != null && !"".equals(ativoTodos)) { if
		 * (ativoTodos.equals("Sim")) {
		 * predicatesList.add(builder.equal(root.get("ativo"), true)); } else if
		 * (ativoTodos.equals("Nao")) {
		 * predicatesList.add(builder.equal(root.get("ativo"), false)); } }
		 * 
		 * 
		 * //antiga EnderecoBean
		 * 
		 * String endNomeLogradouro = this.example.getNomeLogradouro(); if
		 * (endNomeLogradouro != null && !"".equals(endNomeLogradouro)) {
		 * predicatesList.add(builder.like( builder.lower(root.<String>
		 * get("endNomeLogradouro")), '%' + endNomeLogradouro.toLowerCase() + '%')); }
		 * Integer endNumero = this.example.getNumero(); if (endNumero != null) {
		 * predicatesList.add(builder.equal(root.get("endNumero"), endNumero)); }
		 * 
		 * String endComplemento = this.example.getComplemento(); if (endComplemento !=
		 * null && !"".equals(endComplemento)) { predicatesList.add(builder.like(
		 * builder.lower(root.<String> get("endComplemento")), '%' +
		 * endComplemento.toLowerCase() + '%')); }
		 * 
		 * String endBairro = this.example.getBairro(); if (endBairro != null &&
		 * !"".equals(endBairro)) { predicatesList.add(builder.like(
		 * builder.lower(root.<String> get("endBairro")), '%' + endBairro.toLowerCase()
		 * + '%')); }
		 * 
		 * String endCidade = this.example.getCidade(); if (endCidade != null &&
		 * !"".equals(endCidade)) { predicatesList.add(builder.like(
		 * builder.lower(root.<String> get("endCidade")), '%' + endCidade.toLowerCase()
		 * + '%')); }
		 * 
		 * String endUf = this.example.getUf(); if (endUf != null && !"".equals(endUf))
		 * { predicatesList.add(builder.like( builder.lower(root.<String> get("endUf")),
		 * '%' + endUf.toLowerCase() + '%')); }
		 * 
		 * String endCep = this.example.getCep(); if (endCep != null &&
		 * !"".equals(endCep)) { predicatesList.add(builder.like(
		 * builder.lower(root.<String> get("endCep")), '%' + endCep.toLowerCase() +
		 * '%')); }
		 */
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}


	public List<Pagamento> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Pagamento entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Pagamento> getAll() {

		CriteriaQuery<Pagamento> criteria = this.entityManager.getCriteriaBuilder()
				.createQuery(Pagamento.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Pagamento.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final PagamentoBean ejbProxy = this.sessionContext
				.getBusinessObject(PagamentoBean.class);

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

				return String.valueOf(((Pagamento) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Pagamento add = new Pagamento();

	public Pagamento getAdd() {
		return this.add;
	}

	public Pagamento getAdded() {
		Pagamento added = this.add;
		this.add = new Pagamento();
		return added;
	}
	
	
}
