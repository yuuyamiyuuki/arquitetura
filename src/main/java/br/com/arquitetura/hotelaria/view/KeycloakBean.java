package br.com.arquitetura.hotelaria.view;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.*;


/**
 * Backing bean for Demanda entities.
 * <p/>
 * This class provides CRUD functionality for all Demanda entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class KeycloakBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nomeLogado;
	
	private String role;

	public String getNomeLogado() {
		return nomeLogado;
	}

	public void setNomeLogado(String nomeLogado) {
		this.nomeLogado = nomeLogado;
	}
	
	
	public void buscaUser(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		KeycloakSecurityContext session = (KeycloakSecurityContext) ((HttpServletRequest) context.getExternalContext()
						.getRequest()).getAttribute(KeycloakSecurityContext.class.getName());
		
		this.nomeLogado = session.getIdToken().getName();

		if (session.getToken().getResourceAccess().get("HOTELARIA") != null){
			
			Set<String> roles = session.getToken().getResourceAccess().get("HOTELARIA").getRoles();
			for(String role : roles)
			{
				this.role = role;
				System.out.println("Role: " + role); 
			}
			
		}else{
			this.role = "semRole";
			System.out.println("Role: " + role); 
		}
		
		
		
	}
	
	


	public String buscaNomeUsuario(){
		
		FacesContext context = FacesContext.getCurrentInstance();
		KeycloakSecurityContext session = (KeycloakSecurityContext) ((HttpServletRequest) context.getExternalContext()
						.getRequest()).getAttribute(KeycloakSecurityContext.class.getName());
		
		
		String nomeUsuario = session.getIdToken().getPreferredUsername();
		
		System.out.println("nomeUsuario: " + nomeUsuario);
		return nomeUsuario;
		
	}
	
	  public String efetuarLogout() {
		  
		  FacesContext fc = FacesContext.getCurrentInstance();
		  HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		  session.invalidate();
	       
		//  FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	        return "/index.xhtml";
	        
	        
	    }
	  
	  public String buscaRole(){
		  
			
			FacesContext context = FacesContext.getCurrentInstance();
			KeycloakSecurityContext session = (KeycloakSecurityContext) ((HttpServletRequest) context.getExternalContext()
			.getRequest()).getAttribute(KeycloakSecurityContext.class.getName());
		
			
			//sce Homologação
			/*if (session.getToken().getResourceAccess().get("SIEWEB_Homologacao") != null){
				
				Set<String> roles = session.getToken().getResourceAccess().get("SIEWEB_Homologacao").getRoles();
				for(String role : roles)
				{
					return role;
				}
				
			} */
			
			//sce Produção
			if (session.getToken().getResourceAccess().get("HOTELARIA") != null){
				
				Set<String> roles = session.getToken().getResourceAccess().get("HOTELARIA").getRoles();
				for(String role : roles)
				{
					return role;
				}
				
			}
			
			return null;
	  }

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
