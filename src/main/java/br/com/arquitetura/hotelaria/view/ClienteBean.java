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

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import br.com.arquitetura.hotelaria.model.Cliente;
import br.com.arquitetura.hotelaria.model.Quarto;


/**
 * Backing bean for Cliente entities.
 * <p/>
 * This class provides CRUD functionality for all Cliente entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Cliente entities
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

	private Cliente cliente;

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
			this.cliente = this.example;
		} else {
			this.cliente = findById(getId());
		}
	}

	public Cliente findById(Long id) {

		return this.entityManager.find(Cliente.class, id);
	}

	/*
	 * Support updating and deleting Cliente entities
	 */

	public String update() {

		try {
		
			if (this.id == null) {
				this.entityManager.persist(this.cliente);		
				this.conversation.end();
				return "view?faces-redirect=true&id=" + this.cliente.getId();
			} else {	    
				this.entityManager.merge(this.cliente);
				return "view?faces-redirect=true&id=" + this.cliente.getId();
			}
		} 
		catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			
			System.out.println(e);
			return null;
		}
	}
	
	public String update(Cliente cliente) {
            
		if (this.getComparaCpfExistente(cliente.getNomeCompleto()))
		{
			cliente.setId(this.queryId);
		}
		    
		try {
		
			if (cliente.getId() == null) {
				this.entityManager.persist(cliente);		
				return "ok";
			} else {	    
				this.entityManager.merge(cliente);
				return "ok";
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
			Cliente deletableEntity = findById(getId());

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
	 * Support searching Cliente entities with pagination
	 */

	private int page;
	private long count;
	private List<Cliente> pageItems;

	private Cliente example = new Cliente();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Cliente getExample() {
		return this.example;
	}

	public void setExample(Cliente example) {
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
		Root<Cliente> root = countCriteria.from(Cliente.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Cliente> criteria = builder.createQuery(Cliente.class);
		root = criteria.from(Cliente.class);
		criteria.orderBy(builder.asc(root.get("nomeCompleto")));
		TypedQuery<Cliente> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
//		query.setFirstResult(this.page * getPageSize()).setMaxResults(
//				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Cliente> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

	
		  String nome = this.example.getNomeCompleto(); if (nome != null && !"".equals(nome)) {
		  predicatesList.add(builder.like( builder.lower(root.<String> get("nomeCompleto")),
		 '%' + nome.toLowerCase().trim() + '%')); }
		
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	//Metodos de validação	
	//Percorre o banco e retorna se existe um valor igual a cliente digitada , True = Se existe , False = Nao existe
	public boolean getComparaCpfExistente(String nomeCompleto){ 		
		
		System.out.println("Nome: " + nomeCompleto);
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Cliente> query = builder.createQuery(Cliente.class);
		Root<Cliente> root = query.from(Cliente.class);
		
		Predicate predicateList = builder.and();
		//testa se a cliente a ser inserida já tem carteira
		if (nomeCompleto != null && !"".equals(nomeCompleto)) { 
			predicateList = builder.equal(builder.literal(nomeCompleto), root.get("nomeCompleto"));
		}
	
		
		//Salva clientes iguais existentes 		
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(		
			query.select(root).where(predicateList));				
		System.out.println(typedQuery.getResultList().size());
		
		if(typedQuery.getResultList().size() > 0){
			this.queryId = typedQuery.getResultList().get(0).getId();
			
			
			return true;	
		}else{
			return false;
		}
	}
	

	public List<Cliente> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Cliente entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Cliente> getAll() {

		CriteriaQuery<Cliente> criteria = this.entityManager.getCriteriaBuilder()
				.createQuery(Cliente.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Cliente.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ClienteBean ejbProxy = this.sessionContext
				.getBusinessObject(ClienteBean.class);

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

				return String.valueOf(((Cliente) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Cliente add = new Cliente();

	public Cliente getAdd() {
		return this.add;
	}

	public Cliente getAdded() {
		Cliente added = this.add;
		this.add = new Cliente();
		return added;
	}
	
	public void addClientes(File file) throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		 

	       
	        DataFormatter dataFormatter = new DataFormatter();
		
		
		 Workbook workbook = WorkbookFactory.create(file);
		  System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
		  Iterator<Sheet> sheetIterator = workbook.sheetIterator();
	        System.out.println("Retrieving Sheets using Iterator");
	        while (sheetIterator.hasNext()) {
	            Sheet sheet = sheetIterator.next();
	            System.out.println("=> " + sheet.getSheetName());
	        }
	        
	        
	        Sheet sheet = workbook.getSheetAt(0);
	        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
	        Iterator<Row> rowIterator = sheet.rowIterator();
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();
	            Cliente cliente = new Cliente();

	            // Now let's iterate over the columns of the current row
	            Iterator<Cell> cellIterator = row.cellIterator();
                int contador = 0;
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                String cellValue = dataFormatter.formatCellValue(cell);
	                
	                
	                
	                if(contador == 0)
	                {
	                	cliente.setNomeCompleto(cellValue);
	                	System.out.print(cellValue);
	                }
	                
	                else  if(contador == 1)
	                {
	                	cliente.setCpf(cellValue);
	                	System.out.print(cellValue);
	                }
	                
	                else  if(contador == 2)
	                {
	                	cliente.setTelefone(cellValue);
	                	System.out.print(cellValue);
	                }
	                
	                else  if(contador == 3)
	                {
	                	cliente.setEmail(cellValue);
	                	System.out.print(cellValue);
	                }
	                
	                else  if(contador == 4)
	                {
	                	cliente.setEndereco(cellValue);
	                	System.out.print(cellValue);
	                }
	                
	                else  if(contador == 5)
	                {
	                	cliente.setNumero(cellValue);
	                	System.out.print(cellValue);
	                }
	                
	                else  if(contador == 6)
	                {
	                	cliente.setStatus(cellValue);
	                	System.out.print(cellValue);
	                }
	                
	                
	                
	                
	                contador++;
	            }
	            System.out.println();
	            if (!cliente.getNomeCompleto().equals("Nome"))
	            {
	            this.update(cliente);
	            }
	            }
	        
	}
	
	 private UploadedFile uploadedFile;
	 
	 public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public void mensagemOk()
	{
		FacesContext.getCurrentInstance().addMessage(null,
		        new FacesMessage(FacesMessage.SEVERITY_INFO, "OK!", "Clientes Adicionados."));
	}

	  public void upload(FileUploadEvent event) throws EncryptedDocumentException, InvalidFormatException, IOException {
		  byte[] bytes = event.getFile().getContents();
		  
		
		   File someFile = new File("etste");
		   FileOutputStream fos;
		   fos = new FileOutputStream(someFile);
		   fos.write(bytes);
		   fos.flush();
		   fos.close(); 

		    this.addClientes(someFile);
	  }
	  
	  public List<Cliente> getAllAtivos() {

			List<Cliente> allQuartos= this.getAll();
			List<Cliente> allAtivos = new ArrayList<Cliente>();
			
			 for (int i = 0; i < allQuartos.size(); i++) {
				 Cliente quarto = allQuartos.get(i);
		     	  
		     	   if(quarto.getStatus().equals("Ativo")){
		        	   allAtivos.add(quarto);
		           }
		          
		       }
		        
		       return allAtivos;
		}
	  
		
		public void ativaInativa(){
			if(this.cliente.getStatus().equals("Ativo")){
			this.cliente.setStatus("Inativo");
			}else{
				this.cliente.setStatus("Ativo");
			}
		}
		
		public boolean isAtivaStatus(){
			if(this.cliente.getStatus().equals("Ativo")){
			return true;
			}else{
				return false;
			}
		}
	  
}
