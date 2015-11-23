package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.Person;

import java.text.ParseException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless
// only used if the the application is deployed in a Java EE container
@LocalBean
// only used if the the application is deployed in a Java EE container
public class PersonResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int id;

	EntityManager entityManager; // only used if the application is deployed in
									// a Java EE container

	public PersonResource(UriInfo uriInfo, Request request, int id,
			EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.entityManager = em;
	}

	public PersonResource(UriInfo uriInfo, Request request, int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	// Application integration
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person getPerson() {
		System.out.println("Request #2: GET /person/{id}");
		Person person = Person.getPersonById(id);
		if (person == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		return person;
	}

	// for the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Person getPersonHTML() {
		Person person = Person.getPersonById(id);
		if (person == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		System.out.println("Returning person... " + person.getIdPerson());
		return person;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person putPerson(Person person) throws ParseException {
		System.out.println("--> Updating Person... " + this.id);
		System.out.println("--> " + person.toString());
		System.out.println("Request #3: PUT /person/{id}");
		Person existing = Person.getPersonById(this.id);
		Person retval = null;

		if (existing == null) {
			throw new RuntimeException("PUT: Person with " + id + " not found");
		} else {
			System.out.println("preparing update..");
			Person p = new Person();
			p.setIdPerson(this.id);
			p.setName(person.getName());
			p.setLastname(person.getLastname());
			String toUpdate = person.getBirthdate();
			try {
				if (toUpdate != null) {
					System.out.println("Ttryng to  set this new birthdate\t"
							+ toUpdate);
					p.setBirthdate(toUpdate);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// p.setBirthdate(existing.getBirthdate());
			p.setLifeStatus(existing.getLifeStatus());

			retval = Person.updatePerson(p);

		}
		return retval;
	}

	@DELETE
	public Response deletePerson() {
		Person c = Person.getPersonById(id);
		if (c == null) {
			return Response
					.status(404)
					.entity("Person with the id " + id
							+ " is not present in the database").build();
		} else {
			Person.removePerson(c);
			return Response.status(204).build();
		}
	}

	@Path("{measureType}")
	public MeasureHistoryResource getMeasureHistory(
			@PathParam("measureType") int measureTypeId) {
		return new MeasureHistoryResource(uriInfo, request, id, measureTypeId);
	}

}