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

import br.com.arquitetura.hotelaria.model.Funcionario;


/**
 * Backing bean for Funcionario entities.
 * <p/>
 * This class provides CRUD functionality for all Funcionario entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class FuncionarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Funcionario entities
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

	private Funcionario funcionario;

	public Funcionario getFuncionario() {
		return this.funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
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
			this.funcionario = this.example;
		} else {
			this.funcionario = findById(getId());
		}
	}

	public Funcionario findById(Long id) {

		return this.entityManager.find(Funcionario.class, id);
	}

	/*
	 * Support updating and deleting Funcionario entities
	 */

	public String update() {

		try {
		
			if (this.id == null) {
				this.entityManager.persist(this.funcionario);		
				this.conversation.end();
				return "view?faces-redirect=true&id=" + this.funcionario.getId();
			} else {	    
				this.entityManager.merge(this.funcionario);
				return "view?faces-redirect=true&id=" + this.funcionario.getId();
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
			Funcionario deletableEntity = findById(getId());

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
	 * Support searching Funcionario entities with pagination
	 */

	private int page;
	private long count;
	private List<Funcionario> pageItems;

	private Funcionario example = new Funcionario();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Funcionario getExample() {
		return this.example;
	}

	public void setExample(Funcionario example) {
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
		Root<Funcionario> root = countCriteria.from(Funcionario.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Funcionario> criteria = builder.createQuery(Funcionario.class);
		root = criteria.from(Funcionario.class);
		criteria.orderBy(builder.asc(root.get("nome")));
		TypedQuery<Funcionario> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
//		query.setFirstResult(this.page * getPageSize()).setMaxResults(
//				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Funcionario> root) {

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

	//Metodos de validação	
	//Percorre o banco e retorna se existe um valor igual a funcionario digitada , True = Se existe , False = Nao existe
	public boolean getComparaCpfExistente(String cpf){ 		
		
		System.out.println("CPF: " + cpf);
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Funcionario> query = builder.createQuery(Funcionario.class);
		Root<Funcionario> root = query.from(Funcionario.class);
		
		Predicate predicateList = builder.and();
		//testa se a funcionario a ser inserida já tem carteira
		if (cpf != null && !"".equals(cpf)) { 
			predicateList = builder.equal(builder.literal(cpf), root.get("cpf"));
		}
	
		
		//Salva funcionarios iguais existentes 		
		TypedQuery<Funcionario> typedQuery = entityManager.createQuery(		
			query.select(root).where(predicateList));				
		
		if(typedQuery.getResultList().size() > 0){
			 FacesContext.getCurrentInstance().addMessage(null,
				        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "CPF já cadastrado em nosso banco de dados."));
			this.queryId  = typedQuery.getResultList().get(0).getId();
			
			return true;	
		}else{
			return false;
		}
	}
	

	public List<Funcionario> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Funcionario entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Funcionario> getAll() {

		CriteriaQuery<Funcionario> criteria = this.entityManager.getCriteriaBuilder()
				.createQuery(Funcionario.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Funcionario.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final FuncionarioBean ejbProxy = this.sessionContext
				.getBusinessObject(FuncionarioBean.class);

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

				return String.valueOf(((Funcionario) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Funcionario add = new Funcionario();

	public Funcionario getAdd() {
		return this.add;
	}

	public Funcionario getAdded() {
		Funcionario added = this.add;
		this.add = new Funcionario();
		return added;
	}
	
	
}
