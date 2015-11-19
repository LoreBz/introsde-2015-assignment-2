package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.MeasureDefinition;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Stateless
// will work only inside a Java EE application
@LocalBean
// will work only inside a Java EE application
@Path("/measureTypes")
public class MeasureTypesResource {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	// will work only inside a Java EE application
	@PersistenceUnit(unitName = "introsde-jpa")
	EntityManager entityManager;

	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML })
	public List<MeasureDefinition> getPersonsBrowser() {
		System.out.println("GET /measureTypes aka request 9");
		List<MeasureDefinition> measureTypes = MeasureDefinition.getAll();
		return measureTypes;
	}

}