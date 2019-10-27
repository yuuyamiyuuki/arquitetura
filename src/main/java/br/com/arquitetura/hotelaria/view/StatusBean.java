package br.com.arquitetura.hotelaria.view;

import java.io.Serializable;
import java.util.Arrays;
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

import br.com.arquitetura.hotelaria.model.Status;





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
public class StatusBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Status> enumStatus;

	public List<Status> getEnumStatus() {
		enumStatus = Arrays.asList(Status.values());
		return enumStatus;
	}

	public void setEnumStatus(List<Status> enumStatus) {
		this.enumStatus = enumStatus;
	}
	


	/*public void ListaStatus(){
		enumStatus = Arrays.asList(Status.values());
	}

	public List<Status> getEnumTipoLei() {
	    return enumStatus;*/
	
	
	
}
