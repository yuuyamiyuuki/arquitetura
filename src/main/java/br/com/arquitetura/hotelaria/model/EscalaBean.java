/*
 * package br.com.eptc.oper.view;
 * 
 * import java.io.File; import java.io.IOException; import java.io.Serializable;
 * import java.text.SimpleDateFormat; import java.util.ArrayList; import
 * java.util.Calendar; import java.util.Collections; import java.util.Date;
 * import java.util.GregorianCalendar; import java.util.List;
 * 
 * import javax.annotation.Resource; import javax.ejb.SessionContext; import
 * javax.ejb.Stateful; import javax.enterprise.context.Conversation; import
 * javax.enterprise.context.ConversationScoped; import
 * javax.faces.view.ViewScoped; import javax.faces.application.FacesMessage;
 * import javax.faces.component.UIComponent; import
 * javax.faces.context.FacesContext; import javax.faces.convert.Converter;
 * import javax.faces.event.AjaxBehaviorEvent; import javax.inject.Inject;
 * import javax.inject.Named; import javax.persistence.EntityManager; import
 * javax.persistence.PersistenceContext; import
 * javax.persistence.PersistenceContextType; import
 * javax.persistence.TypedQuery; import
 * javax.persistence.criteria.CriteriaBuilder; import
 * javax.persistence.criteria.CriteriaQuery; import
 * javax.persistence.criteria.Predicate; import javax.persistence.criteria.Root;
 * import javax.servlet.ServletContext;
 * 
 * import org.primefaces.component.export.ExcelOptions; import
 * org.primefaces.event.SelectEvent;
 * 
 * import com.lowagie.text.BadElementException; import com.lowagie.text.Chunk;
 * import com.lowagie.text.Document; import com.lowagie.text.DocumentException;
 * import com.lowagie.text.Element; import com.lowagie.text.Font; import
 * com.lowagie.text.Image; import com.lowagie.text.PageSize; import
 * com.lowagie.text.Paragraph; import com.lowagie.text.Phrase; import
 * com.lowagie.text.pdf.PdfPCell; import com.lowagie.text.pdf.PdfPTable; import
 * com.lowagie.text.pdf.PdfTable; import
 * com.lowagie.text.pdf.draw.DottedLineSeparator;
 * 
 * import br.com.eptc.oper.model.*; import br.com.eptc.oper.view.EscalaLogBean;
 * 
 *//**
	 * Backing bean for Escala entities.
	 * <p/>
	 * This class provides CRUD functionality for all Escala entities. It focuses
	 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
	 * state management, <tt>PersistenceContext</tt> for persistence,
	 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
	 * framework or custom base class.
	 *//*
		 * 
		 * @Named
		 * 
		 * @Stateful
		 * 
		 * @ViewScoped public class EscalaBean implements Serializable {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * 
		 * Support creating and retrieving Escala entities
		 * 
		 * 
		 * @Inject private EscalaLogBean escalaLogBean;
		 * 
		 * 
		 * @Inject private KeycloakBean keycloakBean;
		 * 
		 * private Long id; private Long matricula; private Date horaAgora;
		 * 
		 * public Long getId() { return this.id; }
		 * 
		 * public void setId(Long id) { this.id = id; }
		 * 
		 * private ExcelOptions excelOpt;
		 * 
		 * private Escala escala;
		 * 
		 * public Escala getEscala() { return this.escala; }
		 * 
		 * public void setEscala(Escala escala) { this.escala = escala; }
		 * 
		 * public Long getMatricula() { return matricula; }
		 * 
		 * public void setMatricula(Long matricula) { this.matricula = matricula; }
		 * 
		 * public ExcelOptions getExcelOpt() { return excelOpt; }
		 * 
		 * public void setExcelOpt(ExcelOptions excelOpt) { this.excelOpt = excelOpt; }
		 * 
		 * @Inject private Conversation conversation;
		 * 
		 * @PersistenceContext(unitName = "oper-persistence-unit", type =
		 * PersistenceContextType.EXTENDED) private EntityManager entityManager;
		 * 
		 * public String create() {
		 * 
		 * this.conversation.begin(); this.conversation.setTimeout(1800000L); return
		 * "create?faces-redirect=true"; }
		 * 
		 * public void retrieve() {
		 * 
		 * if (FacesContext.getCurrentInstance().isPostback()) { return; }
		 * 
		 * if (this.conversation.isTransient()) { this.conversation.begin();
		 * this.conversation.setTimeout(1800000L); }
		 * 
		 * if (this.id == null) { this.escala = this.example; } else { this.escala =
		 * findById(getId()); } }
		 * 
		 * public Escala findById(Long id) {
		 * 
		 * Escala escala = this.entityManager.find(Escala.class, id);
		 * 
		 * escala.setMatriculaAgenteSelecionado(escala.getMatriculaAgente());
		 * escala.setNomeAgenteSelecionado(escala.getNomeAgente());
		 * escala.setNomeGuerraSelecionado(escala.getNomeGuerra());
		 * escala.setSetorAgenteSelecionado(escala.getSetorAgente());
		 * escala.setPrefixoVeiculoSelecionado(escala.getPrefixoVeiculo());
		 * escala.setTemSelecionado(escala.getTem());
		 * escala.setRadioSelecionado(escala.getRadio());
		 * escala.setFuncionariosSelecionados(escala.getFuncionarios());
		 * 
		 * return escala;
		 * 
		 * }
		 * 
		 * 
		 * Support updating and deleting Escala entities
		 * 
		 * 
		 * public String update() {
		 * 
		 * 
		 * try {
		 * 
		 * this.escala.setNomeAgente(this.escala.getNomeAgenteSelecionado());
		 * this.escala.setMatriculaAgente(this.escala.getMatriculaAgenteSelecionado());
		 * this.escala.setNomeGuerra(this.escala.getNomeGuerraSelecionado());
		 * this.escala.setSetorAgente(this.escala.getSetorAgenteSelecionado());
		 * this.escala.setPrefixoVeiculo(this.escala.getPrefixoVeiculoSelecionado());
		 * this.escala.setTem(this.escala.getTemSelecionado());
		 * this.escala.setRadio(this.escala.getRadioSelecionado());
		 * this.escala.setTipoTurno(this.escala.getTurno().getTipoTurno().getDescricao()
		 * );
		 * System.out.println("Selecionado: "+this.escala.getSetorAgenteSelecionado());
		 * System.out.println("Setado: "+this.escala.getSetorAgente());
		 * this.escala.setFuncionarios(this.escala.getFuncionariosSelecionados());
		 * 
		 * int ano = this.escala.getDataEscala().getYear()+1900; int mes =
		 * this.escala.getDataEscala().getMonth()+1; int dia =
		 * this.escala.getDataEscala().getDate(); String data = dia+"/"+mes+"/"+ano;
		 * 
		 * 
		 * if (this.getComparaEscala(this.escala.getMatriculaAgente(),
		 * this.escala.getDataEscala()) == true) {
		 * FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!",
		 * "O agente "+this.escala.getNomeGuerra()
		 * +" já tem uma escala lançada para "+data+"."));
		 * 
		 * return null;
		 * 
		 * }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * if (this.id == null) { this.entityManager.persist(this.escala);
		 * escalaLogBean.geraLog(TipoOperacaoLog.INCLUSAO, this.escala);
		 * this.conversation.end(); return "create2?faces-redirect=true&id=" +
		 * this.escala.getId(); } else { this.entityManager.merge(this.escala);
		 * escalaLogBean.geraLog(TipoOperacaoLog.ALTERACAO, this.escala);
		 * this.conversation.end(); return "view?faces-redirect=true&id=" +
		 * this.escala.getId(); } } catch (Exception e) {
		 * FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(e.getMessage())); System.out.println(e); return null; } }
		 * 
		 * 
		 * public String updateForaEscala() {
		 * 
		 * try {
		 * 
		 * String nomes =""; String nomesGuerra =""; for (int i = 0; i <
		 * this.escala.getFuncionariosSelecionados().size(); i++) { Funcionario
		 * funcionario = this.escala.getFuncionariosSelecionados().get(i);
		 * 
		 * if (nomes.isEmpty()){ nomes = funcionario.getNomeGuerra() + " " +
		 * funcionario.getMatricula(); nomesGuerra = funcionario.getNomeGuerra();
		 * 
		 * }else{ nomes = nomes+" , "+funcionario.getNomeGuerra() + " " +
		 * funcionario.getMatricula(); nomesGuerra =
		 * nomesGuerra+" , "+funcionario.getNomeGuerra();
		 * 
		 * } }
		 * 
		 * this.escala.setNomeAgente(this.escala.getFuncionariosSelecionados().get(0).
		 * getNome());
		 * this.escala.setNomeGuerra(this.escala.getFuncionariosSelecionados().get(0).
		 * getNomeGuerra());
		 * this.escala.setMatriculaAgente(this.escala.getFuncionariosSelecionados().get(
		 * 0).getMatricula());
		 * this.escala.setFuncionarios(this.escala.getFuncionariosSelecionados());
		 * 
		 * this.escala.setAgentes(this.escala.getAgentesString());
		 * 
		 * 
		 * 
		 * 
		 * this.limpaAtiv();
		 * 
		 * if (this.id == null) { this.entityManager.persist(this.escala);
		 * escalaLogBean.geraLog(TipoOperacaoLog.INCLUSAO, this.escala);
		 * this.conversation.end(); return "view2?faces-redirect=true&id=" +
		 * this.escala.getId(); } else { this.entityManager.merge(this.escala);
		 * escalaLogBean.geraLog(TipoOperacaoLog.ALTERACAO, this.escala);
		 * this.conversation.end(); return "view2?faces-redirect=true&id=" +
		 * this.escala.getId(); } } catch (Exception e) {
		 * FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(e.getMessage())); System.out.println(e); return null; } } public
		 * String updateEscala(Escala escala) { try { if (escala.getId() == null) {
		 * this.entityManager.persist( escala); System.out.println("Escala "+
		 * escala.getId()+" para " + escala.getNomeGuerra() + " incluida com sucesso.");
		 * } else { this.entityManager.merge(escala); } return "ok"; } catch (Exception
		 * e) { FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(e.getMessage())); System.out.println(e); return null; }
		 * 
		 * }
		 * 
		 * 
		 * public String delete() { this.conversation.end();
		 * 
		 * try { Escala deletableEntity = findById(getId());
		 * 
		 * this.entityManager.remove(deletableEntity); this.entityManager.flush();
		 * return "search?faces-redirect=true"; } catch (Exception e) {
		 * FacesContext.getCurrentInstance().addMessage(null, new
		 * FacesMessage(e.getMessage())); return null; } }
		 * 
		 * 
		 * Support searching Escala entities with pagination
		 * 
		 * 
		 * private int page; private long count; private List<Escala> pageItems;
		 * 
		 * private Escala example = new Escala();
		 * 
		 * public int getPage() { return this.page; }
		 * 
		 * public void setPage(int page) { this.page = page; }
		 * 
		 * public int getPageSize() { return 10; }
		 * 
		 * public Escala getExample() { return this.example; }
		 * 
		 * public void setExample(Escala example) { this.example = example; }
		 * 
		 * public String search() { this.page = 0; return null; }
		 * 
		 * public void paginate() {
		 * 
		 * CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		 * 
		 * // Populate this.count
		 * 
		 * CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		 * Root<Escala> root = countCriteria.from(Escala.class); countCriteria =
		 * countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
		 * this.count = this.entityManager.createQuery(countCriteria).getSingleResult();
		 * 
		 * // Populate this.pageItems
		 * 
		 * CriteriaQuery<Escala> criteria = builder.createQuery(Escala.class); root =
		 * criteria.from(Escala.class); TypedQuery<Escala> query = this.entityManager
		 * .createQuery(criteria.select(root).where(getSearchPredicates(root)));
		 * 
		 * query.setFirstResult(this.page * getPageSize()).setMaxResults(
		 * getPageSize());
		 * 
		 * this.pageItems = query.getResultList(); }
		 * 
		 * private Predicate[] getSearchPredicates(Root<Escala> root) {
		 * 
		 * CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		 * List<Predicate> predicatesList = new ArrayList<Predicate>();
		 * 
		 * Long matriculaAgente = this.example.getMatriculaAgente(); if (matriculaAgente
		 * != null && matriculaAgente.intValue() != 0) {
		 * predicatesList.add(builder.equal(root.get("matriculaAgente"),
		 * matriculaAgente)); }
		 * 
		 * String prefixoVeiculo = this.example.getPrefixoVeiculo(); if (prefixoVeiculo
		 * != null && !"".equals(prefixoVeiculo)) {
		 * predicatesList.add(builder.like(builder.lower(root.<String>get(
		 * "prefixoVeiculo")), '%' + prefixoVeiculo.toLowerCase() + '%')); }
		 * 
		 * String tem = this.example.getTem(); if (tem != null && !"".equals(tem)) {
		 * predicatesList.add(builder.equal(root.get("tem"), tem)); }
		 * 
		 * 
		 * Turno turno = this.example.getTurno(); if (turno != null) {
		 * predicatesList.add(builder.equal(root.get("turno"), turno)); }
		 * 
		 * String tipoTurno = this.example.getTipoTurno(); if (tipoTurno != null) {
		 * predicatesList.add(builder.equal(root.get("tipoTurno"), tipoTurno)); }
		 * 
		 * String nomeAgente = this.example.getNomeAgente(); if (nomeAgente != null &&
		 * !"".equals(nomeAgente)) { predicatesList.add(
		 * builder.like(builder.lower(root.<String>get("nomeAgente")), '%' +
		 * nomeAgente.toUpperCase() + '%')); }
		 * 
		 * SituacaoEscala situacaoEscala = this.example.getSituacaoEscala(); if
		 * (situacaoEscala != null) {
		 * predicatesList.add(builder.equal(root.get("situacaoEscala"),
		 * situacaoEscala)); }
		 * 
		 * AtividadeEscala atividadeEscala = this.example.getAtividadeEscala(); if
		 * (atividadeEscala != null) {
		 * predicatesList.add(builder.equal(root.get("atividadeEscala"),
		 * atividadeEscala)); }
		 * 
		 * Date dataEscala = this.example.getDataEscala(); if (dataEscala != null) {
		 * predicatesList.add(builder.equal(root.get("dataEscala"), dataEscala)); }
		 * 
		 * String nomeGuerra = this.example.getNomeGuerra(); if (nomeGuerra != null &&
		 * !"".equals(nomeGuerra)) { predicatesList.add(
		 * builder.like(builder.lower(root.<String>get("nomeGuerra")), '%' +
		 * nomeGuerra.toLowerCase() + '%')); }
		 * 
		 * String setorAgente = this.example.getSetorAgente(); if (setorAgente != null
		 * && !"".equals(setorAgente)) { predicatesList.add(
		 * builder.like(builder.lower(root.<String>get("setorAgente")), '%' +
		 * setorAgente.toLowerCase() + '%')); }
		 * 
		 * Long radio = this.example.getRadio(); if (radio != null) {
		 * predicatesList.add(builder.equal(root.get("radio"), radio)); }
		 * 
		 * String setor = this.example.getSetor(); if (setor != null) {
		 * predicatesList.add(builder.equal(root.get("setor"), setor)); }
		 * 
		 * Date dataForaInicial = this.example.getDataForaInicial(); if (dataForaInicial
		 * != null) { predicatesList.add(builder.equal(root.get("dataForaInicial"),
		 * dataForaInicial)); }
		 * 
		 * Date dataForaFinal = this.example.getDataForaFinal(); if (dataForaFinal !=
		 * null) { predicatesList.add(builder.equal(root.get("dataForaFinal"),
		 * dataForaFinal)); }
		 * 
		 * String agentes = this.example.getAgentes(); if (agentes != null) {
		 * predicatesList.add(builder.equal(root.get("agentes"), agentes)); }
		 * 
		 * Date dtInicial = this.example.getDataInicialEscala(); Date dtFinal =
		 * this.example.getDataFinalEscala(); if (dtInicial != null && dtFinal != null)
		 * { predicatesList.add(builder.between(root.get("dataEscala"), dtInicial,
		 * dtFinal)); }
		 * 
		 * String ativoTodos = this.example.getAtivoTodos(); if (ativoTodos != null &&
		 * !"".equals(ativoTodos)) { if (ativoTodos.equals("Sim")) {
		 * predicatesList.add(builder.equal(root.get("ativo"),true)); } else if
		 * (ativoTodos.equals("Nao")) {
		 * predicatesList.add(builder.equal(root.get("ativo"),false)); } }
		 * 
		 * return predicatesList.toArray(new Predicate[predicatesList.size()]); }
		 * 
		 * public List<Escala> getPageItems() { return this.pageItems; }
		 * 
		 * public long getCount() { return this.count; }
		 * 
		 * 
		 * Support listing and POSTing back Escala entities (e.g. from inside an
		 * HtmlSelectOneMenu)
		 * 
		 * 
		 * public List<Escala> getAll() {
		 * 
		 * CriteriaQuery<Escala> criteria =
		 * this.entityManager.getCriteriaBuilder().createQuery(Escala.class); return
		 * this.entityManager.createQuery(criteria.select(criteria.from(Escala.class))).
		 * getResultList(); }
		 * 
		 * @Resource private SessionContext sessionContext;
		 * 
		 * public Converter getConverter() {
		 * 
		 * final EscalaBean ejbProxy =
		 * this.sessionContext.getBusinessObject(EscalaBean.class);
		 * 
		 * return new Converter() {
		 * 
		 * @Override public Object getAsObject(FacesContext context, UIComponent
		 * component, String value) {
		 * 
		 * return ejbProxy.findById(Long.valueOf(value)); }
		 * 
		 * @Override public String getAsString(FacesContext context, UIComponent
		 * component, Object value) {
		 * 
		 * if (value == null) { return ""; }
		 * 
		 * return String.valueOf(((Escala) value).getId()); } }; }
		 * 
		 * 
		 * Support adding children to bidirectional, one-to-many tables
		 * 
		 * 
		 * private Escala add = new Escala();
		 * 
		 * public Escala getAdd() { return this.add; }
		 * 
		 * public Escala getAdded() { Escala added = this.add; this.add = new Escala();
		 * return added; }
		 * 
		 * public void handleSelect(SelectEvent event) { Object value =
		 * event.getObject();
		 * 
		 * System.out.println("Escala: " + value); }
		 * 
		 * public void buscaMatricula(SelectEvent event) {
		 * 
		 * System.out.println("Matricula Escala:" + event.getObject()); CriteriaBuilder
		 * builder = this.entityManager.getCriteriaBuilder(); CriteriaQuery<Funcionario>
		 * query = builder.createQuery(Funcionario.class); Root<Funcionario> root =
		 * query.from(Funcionario.class);
		 * 
		 * Predicate predicateList = builder.and();
		 * 
		 * predicateList = builder.and(predicateList,
		 * builder.equal(root.get("matricula"), event.getObject()));
		 * 
		 * TypedQuery<Funcionario> typedQuery =
		 * entityManager.createQuery(query.select(root).where(predicateList));
		 * 
		 * Funcionario funcionario = typedQuery.getSingleResult();
		 * 
		 * System.out.println("Nome Escala:" + funcionario.getNome());
		 * System.out.println("Nome Guerra Escala:" + funcionario.getNomeGuerra());
		 * System.out.println("Nome Setor:" + funcionario.getSetorNome());
		 * 
		 * this.escala.setNomeAgenteSelecionado(funcionario.getNome());
		 * this.escala.setNomeGuerraSelecionado(funcionario.getNomeGuerra());
		 * this.escala.setMatriculaAgenteSelecionado(funcionario.getMatricula());
		 * this.escala.setSetorAgenteSelecionado(funcionario.getSetorNome());
		 * this.escala.setPrefixoVeiculoSelecionado(funcionario.getPrefixoVeiculo());
		 * this.escala.setTemSelecionado(funcionario.getTem());
		 * this.escala.setRadioSelecionado(funcionario.getRadio());
		 * this.escala.setColete(funcionario.getColete());
		 * this.escala.setAtividadeEscala(funcionario.getAtividadeEscala());
		 * this.escala.setTurno(funcionario.getTurno());
		 * this.escala.setSituacaoEscala(funcionario.getSituacaoEscala());
		 * this.escala.setTalaoInicio(funcionario.getTalaoInicio());
		 * this.escala.setTalaoFim(funcionario.getTalaoFim());
		 * 
		 * 
		 * }
		 * 
		 * public void atualizaVeiculo(SelectEvent event) {
		 * 
		 * String prefixoVeiculo = (String) event.getObject();
		 * System.out.println("Prefixo1:" + event.getObject());
		 * System.out.println("Prefixo1:" + prefixoVeiculo);
		 * 
		 * this.escala.setPrefixoVeiculoSelecionado(prefixoVeiculo);
		 * System.out.println("Prefixo selecionado:" +
		 * this.escala.getPrefixoVeiculoSelecionado());
		 * 
		 * }
		 * 
		 * public void atualizaTem(SelectEvent event) {
		 * 
		 * String codigoSit = (String) event.getObject(); System.out.println("Codigo1:"
		 * + event.getObject()); System.out.println("Codigo1:" + codigoSit);
		 * 
		 * this.escala.setTemSelecionado(codigoSit);
		 * System.out.println("TEM selecionado:" + this.escala.getTemSelecionado());
		 * 
		 * }
		 * 
		 * public void atualizaRadio(SelectEvent event) {
		 * 
		 * Long serie = (Long) event.getObject(); System.out.println("Serie1:" +
		 * event.getObject()); System.out.println("Serie1:" + serie);
		 * 
		 * this.escala.setRadioSelecionado(serie);
		 * System.out.println("Radio selecionado:" + this.escala.getRadioSelecionado());
		 * 
		 * }
		 * 
		 * public void ativaInativa() { if (escala.isAtivo() == true) {
		 * escala.setAtivo(false); escalaLogBean.geraLog(TipoOperacaoLog.DESATIVACAO,
		 * this.escala); } else { escala.setAtivo(true);
		 * escalaLogBean.geraLog(TipoOperacaoLog.REATIVACAO, this.escala); } }
		 * 
		 * public void calculaIntervalo(SelectEvent event) {
		 * 
		 * SimpleDateFormat format = new SimpleDateFormat("HH:mm"); Object value =
		 * format.format(event.getObject());
		 * 
		 * String inicioIntervalo = (String) value; String horaInicio =
		 * inicioIntervalo.substring(0, 2); String minutoInicio =
		 * inicioIntervalo.substring(3);
		 * 
		 * int hora = Integer.parseInt(horaInicio); int minuto =
		 * Integer.parseInt(minutoInicio);
		 * 
		 * GregorianCalendar gc = new GregorianCalendar(); gc.set(1970, 1, 1, hora,
		 * minuto); gc.add(Calendar.MINUTE, 15);
		 * 
		 * this.escala.setIntervaloFim(gc.getTime()); }
		 * 
		 * public void dataHoje() { if (this.id == null){ GregorianCalendar gc = new
		 * GregorianCalendar(); this.escala.setDataEscala(gc.getTime());
		 * System.out.println("Data2: " + gc.getTime()); System.out.println("Data3: " +
		 * this.escala.getDataEscala()); }
		 * 
		 * }
		 * 
		 * public void horaAtual() {
		 * 
		 * GregorianCalendar gc = new GregorianCalendar();
		 * this.escala.setIntervaloInicio(gc.getTime()); gc.add(Calendar.MINUTE, 15);
		 * 
		 * this.escala.setIntervaloFim(gc.getTime());
		 * System.out.println(this.escala.getIntervaloInicio()); if
		 * (this.escala.getIntervaloInicio() != null){ Calendar calendar =
		 * Calendar.getInstance(); calendar.setTime(this.escala.getIntervaloInicio());
		 * calendar.add(Calendar.MINUTE, 15);
		 * 
		 * this.escala.setIntervaloFim(calendar.getTime()); }
		 * 
		 * }
		 * 
		 * public List<Escala> buscaNomeGuerra(String nomeAgente) {
		 * 
		 * List<Escala> allEscalas = this.getAll(); List<Escala> filtroEscalas = new
		 * ArrayList<Escala>();
		 * 
		 * for (int i = 0; i < allEscalas.size(); i++) { Escala escala =
		 * allEscalas.get(i); System.out.println(escala.getNomeAgente());
		 * System.out.println(nomeAgente); if
		 * (escala.getNomeGuerra().toLowerCase().startsWith(nomeAgente)) {
		 * filtroEscalas.add(escala); }
		 * 
		 * if() { filtroPareceres.add(parecer); }
		 * 
		 * }
		 * 
		 * return filtroEscalas; }
		 * 
		 * public String getFileName() { Date dtEscala = this.example.getDataEscala();
		 * Turno turnoEscala = this.example.getTurno(); String fileName;
		 * 
		 * if (dtEscala != null) { if (turnoEscala != null){ fileName = ("Escalas_" +
		 * (dtEscala.getDate()) + ("-") + (dtEscala.getMonth()+1) + ("-") +
		 * (dtEscala.getYear() + 1900) +" Turno_" +
		 * turnoEscala.getDescricao().replace('Ã', 'A') ); } else{ fileName =
		 * ("Escalas_" + (dtEscala.getDate()) + ("-") + (dtEscala.getMonth()+1) + ("-")
		 * + (dtEscala.getYear() + 1900)); } } else { fileName = ("Escalas"); }
		 * 
		 * return fileName; }
		 * 
		 * public void customizationOptions() { excelOpt = new ExcelOptions(); //Headers
		 * excelOpt.setFacetBgColor("#000000"); excelOpt.setFacetFontSize("10");
		 * excelOpt.setFacetFontColor("#ffffff"); excelOpt.setFacetFontStyle("BOLD");
		 * //Dados excelOpt.setCellFontColor("#000000"); excelOpt.setCellFontSize("10");
		 * }
		 * 
		 * public void preProcessPDF(Object document) throws IOException,
		 * BadElementException, DocumentException { Document pdf = (Document) document;
		 * pdf.setPageSize(PageSize.A4.rotate()); pdf.setMargins(10, 10, 10, 10);
		 * pdf.open();
		 * 
		 * PdfPTable table = new PdfPTable(9);
		 * 
		 * table.setWidthPercentage(100); table.setSpacingBefore(0f);
		 * table.setSpacingAfter(0f);
		 * 
		 * Font catFont = new Font(Font.TIMES_ROMAN, 20, Font.BOLD); Paragraph p = new
		 * Paragraph("Relátorio Escalas", catFont); p.setSpacingAfter(10f);
		 * p.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * ServletContext servletContext = (ServletContext)
		 * FacesContext.getCurrentInstance().getExternalContext().getContext(); String
		 * logo = servletContext.getRealPath("") + File.separator + "template" +
		 * File.separator + "eptcLogoOld.png"; Image image = Image.getInstance(logo);
		 * image.setAlignment(Image.ALIGN_LEFT);
		 * 
		 * 
		 * PdfPCell cell = new PdfPCell(new Phrase("ESCALAS")); cell.setColspan(10);
		 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); cell.setPadding(5.0f);
		 * table.addCell(cell);
		 * 
		 * table.setTotalWidth(new float[]{ 72, 72 , 80, 72, 72 ,90 ,72 ,100 , 200});
		 * table.setLockedWidth(true);;
		 * 
		 * table.addCell("DATA"); table.addCell("TURNO"); table.addCell("MATRICULA");
		 * table.addCell("NOME GUERRA"); table.addCell("SITUACAO");
		 * table.addCell("ATIVIDADE"); table.addCell("VIATURA");
		 * table.addCell("AGENTE"); table.addCell("SETOR");
		 * 
		 * 
		 * 
		 * for (int i = 0; i < this.pageItems.size(); i++) { Date dtEscala =
		 * this.pageItems.get(i).getDataEscala();
		 * 
		 * table.addCell(dtEscala.getDate() + ("/") + (dtEscala.getMonth()+1) + ("/") +
		 * (dtEscala.getYear() + 1900));
		 * table.addCell(this.pageItems.get(i).getTurno().getDescricao());
		 * table.addCell(this.pageItems.get(i).getMatriculaAgente().toString());
		 * table.addCell(this.pageItems.get(i).getNomeGuerra());
		 * table.addCell(this.pageItems.get(i).getSituacaoEscala().getDescricao());
		 * table.addCell(this.pageItems.get(i).getAtividadeEscala().getDescricao());
		 * table.addCell(this.pageItems.get(i).getPrefixoVeiculo());
		 * table.addCell(this.pageItems.get(i).getNomeAgente());
		 * table.addCell(this.pageItems.get(i).getSetorAgente());
		 * 
		 * }
		 * 
		 * 
		 * pdf.add(image); pdf.add(p); pdf.add(table);;
		 * 
		 * } public void criaPdfLista(Object document) throws IOException,
		 * BadElementException, DocumentException { Document pdf = (Document) document;
		 * pdf.setPageSize(PageSize.A4); pdf.setMargins(10, 10, 10, 10); pdf.open();
		 * 
		 * List <Escala> lista = this.pageItems; for (int i = 0; i < lista.size(); i++)
		 * {
		 * 
		 * 
		 * ServletContext servletContext = (ServletContext)
		 * FacesContext.getCurrentInstance().getExternalContext().getContext(); String
		 * logo = servletContext.getRealPath("") + File.separator + "template" +
		 * File.separator + "eptcLogoOld.png"; Image image = Image.getInstance(logo);
		 * image.setAlignment(Image.ALIGN_LEFT);
		 * 
		 * Font catFont = new Font(Font.TIMES_ROMAN, 20, Font.BOLD); Font catFont2 = new
		 * Font(Font.TIMES_ROMAN, 10, Font.NORMAL); Font catFont3 = new
		 * Font(Font.TIMES_ROMAN, 10, Font.ITALIC);
		 * 
		 * 
		 * Date dtEscala = lista.get(i).getDataEscala(); Paragraph dadosEscala = new
		 * Paragraph(dtEscala.getDate() + ("/") + (dtEscala.getMonth()+1) + ("/") +
		 * (dtEscala.getYear() + 1900), catFont); dadosEscala.setSpacingAfter(10f);
		 * dadosEscala.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosEscala2 = new Paragraph("TURNO: " +
		 * lista.get(i).getTurno().getDescricao(), catFont2);
		 * dadosEscala2.setSpacingAfter(10f);
		 * dadosEscala2.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosEscala3 = new Paragraph("SITUAÇÃO: " +
		 * lista.get(i).getSituacaoEscala().getDescricao(), catFont2);
		 * dadosEscala3.setSpacingAfter(10f);
		 * dadosEscala3.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosEscala4 = new Paragraph("ATIVIDADE: " +
		 * lista.get(i).getAtividadeEscala().getDescricao(), catFont2);
		 * dadosEscala4.setSpacingAfter(10f);
		 * dadosEscala4.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosEscala5 = new Paragraph("AREA: " + lista.get(i).getArea(),
		 * catFont2); dadosEscala5.setSpacingAfter(10f);
		 * dadosEscala5.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosAgente = new Paragraph("Dados do Agente", catFont);
		 * dadosAgente.setSpacingAfter(10f);
		 * dadosAgente.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph equipamentosEscala = new Paragraph("Equipamentos da Escala",
		 * catFont); equipamentosEscala.setSpacingAfter(10f);
		 * equipamentosEscala.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * 
		 * 
		 * String para1 = "Matricula do Agente: " + lista.get(i).getMatriculaAgente();
		 * String para2 = "Nome do Agente: " + lista.get(i).getNomeAgente(); String
		 * para3 = "Nome de Guerra do Agente: " + lista.get(i).getNomeGuerra(); String
		 * para4 = "Nome do Setor: " + lista.get(i).getSetorAgente(); String para5 =
		 * "ínicio do Turno: " + lista.get(i).getTurno().getTurnoInicio(); String para6
		 * = "Fim do Turno: " + lista.get(i).getTurno().getTurnoFim(); String para7 =
		 * "ínicio do Intervalo: " + lista.get(i).getIntervaloInicio(); String para8 =
		 * "Fim do Intervalo: " + lista.get(i).getIntervaloFim();
		 * 
		 * 
		 * Paragraph paragraph1 = new Paragraph(para1); Paragraph paragraph2 = new
		 * Paragraph(para2); Paragraph paragraph3 = new Paragraph(para3); Paragraph
		 * paragraph4 = new Paragraph(para4); Paragraph paragraph5 = new
		 * Paragraph(para5); Paragraph paragraph6 = new Paragraph(para6); Paragraph
		 * paragraph7 = new Paragraph(para7); Paragraph paragraph8 = new
		 * Paragraph(para8);
		 * 
		 * 
		 * String para9 = "Prefixo do Veiculo: " + lista.get(i).getPrefixoVeiculo();
		 * String para10 = "Rádio: " + lista.get(i).getRadio(); String para11 = "TEM: "
		 * + lista.get(i).getTem(); String para12 = "Talão Manual Inicio: " +
		 * lista.get(i).getTalaoInicio(); String para13 = "Talão Manual Fim: " +
		 * lista.get(i).getTalaoFim(); String para14 = "Bateria: " +
		 * lista.get(i).getBateria();
		 * 
		 * Paragraph paragraph9 = new Paragraph(para9); Paragraph paragraph10 = new
		 * Paragraph(para10); Paragraph paragraph11 = new Paragraph(para11); Paragraph
		 * paragraph12 = new Paragraph(para12); Paragraph paragraph13= new
		 * Paragraph(para13); Paragraph paragraph14= new Paragraph(para14); Paragraph
		 * paragraph15 = new Paragraph("Assine aqui: "); paragraph15.add(new Chunk(new
		 * DottedLineSeparator())); paragraph15.add("(assinatura)");
		 * 
		 * 
		 * pdf.add(image); pdf.add(dadosEscala); pdf.add(dadosEscala2);
		 * pdf.add(dadosEscala3); pdf.add(dadosEscala4); pdf.add(dadosEscala5);
		 * pdf.add(dadosAgente); pdf.add(paragraph1); pdf.add(paragraph2);
		 * pdf.add(paragraph3); pdf.add(paragraph4); pdf.add(paragraph5);
		 * pdf.add(paragraph6); pdf.add(paragraph7); pdf.add(paragraph8);
		 * pdf.add(equipamentosEscala); if (lista.get(i).getPrefixoVeiculo() != null &&
		 * lista.get(i).getPrefixoVeiculo() != ""){ pdf.add(paragraph9); } if
		 * (lista.get(i).getRadio() != null){ pdf.add(paragraph10); } if
		 * (lista.get(i).getTem() != null){ pdf.add(paragraph11); } if
		 * (lista.get(i).getTalaoInicio() != null && lista.get(i).getTalaoInicio() !=
		 * ""){ pdf.add(paragraph12); } if (lista.get(i).getTalaoFim() != null &&
		 * lista.get(i).getTalaoFim() != ""){ pdf.add(paragraph13); } if
		 * (lista.get(i).getBateria() != null && lista.get(i).getBateria() != ""){
		 * pdf.add(paragraph14); } pdf.newPage(); }
		 * 
		 * 
		 * 
		 * 
		 * }
		 * 
		 * public void criaPdf(Object document) throws IOException, BadElementException,
		 * DocumentException { Document pdf = (Document) document;
		 * pdf.setPageSize(PageSize.A4); pdf.setMargins(10, 10, 10, 10); pdf.open();
		 * 
		 * Escala escala = this.escala;
		 * 
		 * 
		 * ServletContext servletContext = (ServletContext)
		 * FacesContext.getCurrentInstance().getExternalContext().getContext(); String
		 * logo = servletContext.getRealPath("") + File.separator + "template" +
		 * File.separator + "eptcLogoOld.png"; Image image = Image.getInstance(logo);
		 * image.setAlignment(Image.ALIGN_LEFT);
		 * 
		 * Font catFont = new Font(Font.TIMES_ROMAN, 20, Font.BOLD); Font catFont2 = new
		 * Font(Font.TIMES_ROMAN, 10, Font.NORMAL); Font catFont3 = new
		 * Font(Font.TIMES_ROMAN, 10, Font.ITALIC);
		 * 
		 * 
		 * Date dtEscala = escala.getDataEscala(); Paragraph dadosEscala = new
		 * Paragraph(dtEscala.getDate() + ("/") + (dtEscala.getMonth()+1) + ("/") +
		 * (dtEscala.getYear() + 1900), catFont); dadosEscala.setSpacingAfter(10f);
		 * dadosEscala.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosEscala2 = new Paragraph("TURNO: " +
		 * escala.getTurno().getDescricao(), catFont2);
		 * dadosEscala2.setSpacingAfter(10f);
		 * dadosEscala2.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosEscala3 = new Paragraph("SITUAÇÃO: " +
		 * escala.getSituacaoEscala().getDescricao(), catFont2);
		 * dadosEscala3.setSpacingAfter(10f);
		 * dadosEscala3.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosEscala4 = new Paragraph("ATIVIDADE: " +
		 * escala.getAtividadeEscala().getDescricao(), catFont2);
		 * dadosEscala4.setSpacingAfter(10f);
		 * dadosEscala4.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosEscala5 = new Paragraph("AREA: " + escala.getArea(),
		 * catFont2); dadosEscala5.setSpacingAfter(10f);
		 * dadosEscala5.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph dadosAgente = new Paragraph("Dados do Agente", catFont);
		 * dadosAgente.setSpacingAfter(10f);
		 * dadosAgente.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * Paragraph equipamentosEscala = new Paragraph("Equipamentos da Escala",
		 * catFont); equipamentosEscala.setSpacingAfter(10f);
		 * equipamentosEscala.setAlignment(Element.ALIGN_CENTER);
		 * 
		 * 
		 * 
		 * String para1 = "Matricula do Agente: " + escala.getMatriculaAgente(); String
		 * para2 = "Nome do Agente: " + escala.getNomeAgente(); String para3 =
		 * "Nome de Guerra do Agente: " + escala.getNomeGuerra(); String para4 =
		 * "Nome do Setor: " + escala.getSetorAgente(); String para5 =
		 * "ínicio do Turno: " + escala.getTurno().getTurnoInicio(); String para6 =
		 * "Fim do Turno: " + escala.getTurno().getTurnoFim(); String para7 =
		 * "ínicio do Intervalo: " + escala.getIntervaloInicio(); String para8 =
		 * "Fim do Intervalo: " + escala.getIntervaloFim();
		 * 
		 * 
		 * Paragraph paragraph1 = new Paragraph(para1); Paragraph paragraph2 = new
		 * Paragraph(para2); Paragraph paragraph3 = new Paragraph(para3); Paragraph
		 * paragraph4 = new Paragraph(para4); Paragraph paragraph5 = new
		 * Paragraph(para5); Paragraph paragraph6 = new Paragraph(para6); Paragraph
		 * paragraph7 = new Paragraph(para7); Paragraph paragraph8 = new
		 * Paragraph(para8);
		 * 
		 * 
		 * String para9 = "Prefixo do Veiculo: " + escala.getPrefixoVeiculo(); String
		 * para10 = "Rádio: " + escala.getRadio(); String para11 = "TEM: " +
		 * escala.getTem(); String para12 = "Talão Manual Inicio: " +
		 * escala.getTalaoInicio(); String para13 = "Talão Manual Fim: " +
		 * escala.getTalaoFim(); String para14 = "Bateria: " + escala.getBateria();
		 * 
		 * Paragraph paragraph9 = new Paragraph(para9); Paragraph paragraph10 = new
		 * Paragraph(para10); Paragraph paragraph11 = new Paragraph(para11); Paragraph
		 * paragraph12 = new Paragraph(para12); Paragraph paragraph13= new
		 * Paragraph(para13); Paragraph paragraph14= new Paragraph(para14);
		 * 
		 * pdf.add(image); pdf.add(dadosEscala); pdf.add(dadosEscala2);
		 * pdf.add(dadosEscala3); pdf.add(dadosEscala4); pdf.add(dadosEscala5);
		 * pdf.add(dadosAgente); pdf.add(paragraph1); pdf.add(paragraph2);
		 * pdf.add(paragraph3); pdf.add(paragraph4); pdf.add(paragraph5);
		 * pdf.add(paragraph6); pdf.add(paragraph7); pdf.add(paragraph8);
		 * pdf.add(equipamentosEscala); if (escala.getPrefixoVeiculo() != null &&
		 * escala.getPrefixoVeiculo() != ""){ pdf.add(paragraph9); } if
		 * (escala.getRadio() != null){ pdf.add(paragraph10); } if (escala.getTem() !=
		 * null){ pdf.add(paragraph11); } if (escala.getTalaoInicio() != null &&
		 * escala.getTalaoInicio() != ""){ pdf.add(paragraph12); } if
		 * (escala.getTalaoFim() != null && escala.getTalaoFim() != ""){
		 * pdf.add(paragraph13); } if (escala.getBateria() != null &&
		 * escala.getBateria() != ""){ pdf.add(paragraph14); }
		 * 
		 * pdf.newPage();
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 * 
		 * public List<Escala> getAllSetor() {
		 * 
		 * List<Escala> allPage= this.pageItems; List<Escala> allSetor = new
		 * ArrayList<Escala>(); String setor = keycloakBean.buscaSetor().toLowerCase();
		 * Date dataHj = new Date();
		 * 
		 * if (setor.contains("cot") || setor.contains("cos") || setor.contains("coe")
		 * || setor.contains("coc") || setor.contains("con") || setor.contains("col")){
		 * for (int i = 0; i < allPage.size(); i++) { Escala escala = allPage.get(i);
		 * 
		 * if(escala.getSetor().contains(this.example.getSetor()) ){
		 * System.out.print(escala.getSetor()); allSetor.add(escala); }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * else { int hjDia = dataHj.getDate(); int hjMes = dataHj.getMonth()+1; int
		 * hjAno = dataHj.getYear()+1900;
		 * 
		 * for (int i = 0; i < allPage.size(); i++) { Escala escala = allPage.get(i);
		 * 
		 * int escalaDia = escala.getDataEscala().getDate(); int escalaMes =
		 * escala.getDataEscala().getMonth()+1; int escalaAno =
		 * escala.getDataEscala().getYear() + 1900;
		 * 
		 * if(escalaDia == hjDia && escalaMes == hjMes && escalaAno == hjAno){
		 * allSetor.add(escala); }
		 * 
		 * } } return allSetor; }
		 * 
		 * 
		 * public void setaSetor() { if(this.id == null) {
		 * this.escala.setSetor(keycloakBean.buscaSetor()); } }
		 * 
		 * public void setorExample() { if(this.id == null) {
		 * this.example.setSetor(keycloakBean.buscaSetor()); } }
		 * 
		 * public void setaDataEx() { Date dataHj = new Date();
		 * 
		 * this.example.setDataEscala(dataHj); }
		 * 
		 * public Date getHoraAgora() { return horaAgora; }
		 * 
		 * public void setHoraAgora(Date horaAgora) { this.horaAgora = horaAgora; }
		 * 
		 * public String inclusaoEscala() {
		 * 
		 * return "view?faces-redirect=true&id=" + this.escala.getId(); }
		 * 
		 * public void clearViatura() { System.out.println("Viatura: " +
		 * this.escala.getPrefixoVeiculoSelecionado());
		 * 
		 * this.escala.setPrefixoVeiculoSelecionado(null);
		 * 
		 * System.out.println("Limpa" + this.escala.getPrefixoVeiculoSelecionado());
		 * 
		 * }
		 * 
		 * public void clearRadio() { System.out.println("Radio: " +
		 * this.escala.getRadioSelecionado());
		 * 
		 * this.escala.setRadioSelecionado(null);
		 * 
		 * System.out.println("Limpa: " + this.escala.getRadioSelecionado()); }
		 * 
		 * public void clearTem() { System.out.println("TEM: " +
		 * this.escala.getTemSelecionado());
		 * 
		 * this.escala.setTemSelecionado(null);
		 * 
		 * System.out.println("Limpa: " + this.escala.getTemSelecionado()); }
		 * 
		 * 
		 * public boolean isTestaSituacao() { if(this.escala.getSituacaoEscala() !=
		 * null){ System.out.println("Situacao: "+this.escala.getSituacaoEscala().
		 * getTipoSituacao()+" "+this.escala.getSituacaoEscala().getDescricao());
		 * if(this.escala.getSituacaoEscala().getTipoSituacao().
		 * contains("FORA DA ESCALA")) { return true; } else { return false; } } else {
		 * return false; }
		 * 
		 * }
		 * 
		 * 
		 * public void limpaAtiv() { if(this.isTestaSituacao()) { AtividadeEscala aE =
		 * new AtividadeEscala(); Turno turno = new Turno();
		 * 
		 * //oper/turno/view.xhtml?id=36 Long idT = new Long(36);
		 * 
		 * //oper/atividadeEscala/view.xhtml?id=15 Long idA = new Long(15);
		 * 
		 * turno.setId(idT); turno.setDescricao("FORA DA ESCALA");
		 * turno.setTipoTurno(TipoTurno.FOLGA);
		 * 
		 * aE.setId(idA); aE.setDescricao("FORA DA ESCALA");
		 * 
		 * 
		 * 
		 * 
		 * String descricao = "FORA DA ESCALA";
		 * 
		 * CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		 * CriteriaQuery<Turno> query = builder.createQuery(Turno.class); Root<Turno>
		 * root = query.from(Turno.class);
		 * 
		 * Predicate predicateList = builder.and(); if (descricao != null &&
		 * !"".equals(descricao)) { predicateList =
		 * builder.equal(builder.literal(descricao), root.get("descricao")); }
		 * 
		 * 
		 * TypedQuery<Turno> typedQuery = entityManager.createQuery(
		 * query.select(root).where(predicateList));
		 * 
		 * if(typedQuery.getResultList().size() > 0){
		 * 
		 * turno = typedQuery.getResultList().get(0); }
		 * 
		 * 
		 * CriteriaBuilder builder2 = this.entityManager.getCriteriaBuilder();
		 * CriteriaQuery<AtividadeEscala> query2 =
		 * builder2.createQuery(AtividadeEscala.class); Root<AtividadeEscala> root2 =
		 * query2.from(AtividadeEscala.class);
		 * 
		 * Predicate predicateList2 = builder2.and();
		 * 
		 * if (descricao != null && !"".equals(descricao)) { predicateList2 =
		 * builder2.equal(builder2.literal(descricao), root2.get("descricao")); }
		 * 
		 * 
		 * //Salva pessoas iguais existentes TypedQuery<AtividadeEscala> typedQuery2 =
		 * entityManager.createQuery( query2.select(root2).where(predicateList2));
		 * 
		 * if(typedQuery2.getResultList().size() > 0){
		 * 
		 * aE = typedQuery2.getResultList().get(0); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * System.out.println("Turno "+turno.getDescricao());
		 * System.out.println("AtividadeEscala "+aE.getDescricao());
		 * 
		 * this.escala.setAtividadeEscala(aE); this.escala.setTurno(turno);
		 * 
		 * }
		 * 
		 * 
		 * }
		 * 
		 * //Metodos de validação //Percorre o banco e retorna se existe uma escala ja
		 * incluida na mesma data com o mesmo funcionario , True = Se existe , False =
		 * Nao existe public boolean getComparaEscala(Long matricula, Date dataEscala){
		 * System.out.println("compara entrou teste"); CriteriaBuilder builder =
		 * this.entityManager.getCriteriaBuilder(); CriteriaQuery<Escala> query =
		 * builder.createQuery(Escala.class); Root<Escala> root =
		 * query.from(Escala.class);
		 * 
		 * 
		 * System.out.println("Matricula do agente " + matricula);
		 * 
		 * Predicate predicateList = builder.and(); //testa se a pessoa a ser inserida
		 * já tem carteira if (matricula != null && !"".equals(matricula)) {
		 * predicateList = builder.equal(builder.literal(matricula),
		 * root.get("matriculaAgente")); }
		 * 
		 * if (dataEscala != null && !"".equals(dataEscala)) { predicateList =
		 * builder.equal(builder.literal(dataEscala), root.get("dataEscala")); }
		 * 
		 * 
		 * //Salva escalas iguais existentes TypedQuery<Escala> typedQuery =
		 * entityManager.createQuery( query.select(root).where(predicateList));
		 * 
		 * List<Escala> array = typedQuery.getResultList(); List<Escala> arrayEscala =
		 * new ArrayList<Escala>();
		 * 
		 * 
		 * for (int i = 0 ; i < array.size(); i++) { Escala escala = array.get(i);
		 * 
		 * if (escala.getMatriculaAgente().equals(matricula) &&
		 * escala.getDataEscala().equals(dataEscala) ) { arrayEscala.add(escala);
		 * 
		 * } }
		 * 
		 * 
		 * if(arrayEscala.size() > 0 && !arrayEscala.get(0).getId().equals(this.id)){
		 * 
		 * System.out.println("Matricula query: " +
		 * arrayEscala.get(0).getMatriculaAgente());
		 * System.out.println("this Matricula: " + matricula);
		 * System.out.println("Equals? " +
		 * arrayEscala.get(0).getMatriculaAgente().equals(matricula));
		 * 
		 * 
		 * System.out.println("ID Achada: "+arrayEscala.get(0).getId());
		 * System.out.println("Esta ID: "+ this.id); System.out.println("!= ? " +
		 * !arrayEscala.get(0).getId().equals(this.id));
		 * 
		 * 
		 * return true; }else{ return false; } }
		 * 
		 * public List<Escala> getAllFora() {
		 * 
		 * List<Escala> allEscala= this.pageItems; List<Escala> allFora = new
		 * ArrayList<Escala>(); if(allEscala != null){ for (int i = 0; i <
		 * allEscala.size(); i++) { Escala escala = allEscala.get(i);
		 * 
		 * if(escala.getSituacaoEscala().getTipoSituacao().equals("FORA DA ESCALA") &&
		 * escala.getDataForaInicial() != null){ allFora.add(escala); }
		 * 
		 * } Collections.sort(allFora, Collections.reverseOrder());
		 * 
		 * return allFora; }
		 * 
		 * else { return null; } }
		 * 
		 * public List<Escala> getAllForaE() {
		 * 
		 * List<Escala> allEscala= this.getAll(); List<Escala> allFora = new
		 * ArrayList<Escala>(); if(allEscala != null){ for (int i = 0; i <
		 * allEscala.size(); i++) { Escala escala = allEscala.get(i);
		 * 
		 * if(escala.getSituacaoEscala().getTipoSituacao().equals("FORA DA ESCALA") &&
		 * escala.getDataForaInicial() != null){ allFora.add(escala); }
		 * 
		 * } Collections.sort(allFora, Collections.reverseOrder());
		 * 
		 * return allFora; }
		 * 
		 * else { return null; } }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 */