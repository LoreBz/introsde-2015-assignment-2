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
		Person person = this.getPersonById(id);
		if (person == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		return person;
	}

	// for the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Person getPersonHTML() {
		Person person = this.getPersonById(id);
		if (person == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		System.out.println("Returning person... " + person.getIdPerson());
		return person;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putPerson(Person person) throws ParseException {
		System.out.println("--> Updating Person... " + this.id);
		System.out.println("--> " + person.toString());
		System.out.println("Request #3: PUT /person/{id}");
		Response res;
		Person existing = getPersonById(this.id);

		if (existing == null) {
			res = Response.noContent().build();
		} else {

			res = Response.created(uriInfo.getAbsolutePath()).build();

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

			Person.updatePerson(p);

		}
		return res;
	}

	@DELETE
	public void deletePerson() {
		Person c = getPersonById(id);
		if (c == null)
			throw new RuntimeException("Delete: Person with " + id
					+ " not found");
		Person.removePerson(c);
	}

	@Path("{measureType}")
	public MeasureHistoryResource getMeasureHistory(
			@PathParam("measureType") int measureTypeId) {
		return new MeasureHistoryResource(uriInfo, request, id, measureTypeId);
	}

	public Person getPersonById(int personId) {
		System.out.println("Reading person from DB with id: " + personId);

		// this will work within a Java EE container, where not DAO will be
		// needed
		// Person person = entityManager.find(Person.class, personId);

		Person person = Person.getPersonById(personId);
		System.out.println("Person: " + person.toString());
		return person;
	}
}