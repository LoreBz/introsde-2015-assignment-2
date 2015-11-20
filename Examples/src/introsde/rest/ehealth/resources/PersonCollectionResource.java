package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.Person;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Stateless
// will work only inside a Java EE application
@LocalBean
// will work only inside a Java EE application
@Path("/person")
public class PersonCollectionResource {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	// will work only inside a Java EE application
	@PersistenceUnit(unitName = "introsde-jpa")
	EntityManager entityManager;

	// will work only inside a Java EE application
	@PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
	private EntityManagerFactory entityManagerFactory;

	// Return the list of people to the user in the browser
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML })
	public List<Person> getPersonsBrowser() {
		System.out.println("Request #1: GET /person");
		List<Person> people = null;
		try {
			people = Person.getAll();
		} catch (Exception e) {
			System.out.println("fottuto");
			e.printStackTrace();
		}

		return people;
	}

	// retuns the number of people
	// to get the total number of records
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		System.out.println("Getting count...");
		List<Person> people = Person.getAll();
		int count = people.size();
		return String.valueOf(count);
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Person newPerson(Person person) throws IOException, ParseException {
		System.out.println("Request #4: POST /person");
		System.out.println("Request #4: POST /person");
		Person p = new Person();
		p.setName(person.getName());
		p.setLastname(person.getLastname());
		p.setBirthdate(person.getBirthdate());
		p.setEmail(person.getEmail());
		List<LifeStatus> lfList = new ArrayList<>();
		for (LifeStatus lf : person.getLifeStatus()) {
			LifeStatus nls = new LifeStatus();
			MeasureDefinition md = lf.getMeasureDefinition();
			List<MeasureDefinition> mdlist = MeasureDefinition.getAll();
			for (MeasureDefinition rmd : mdlist) {
				if (rmd.getMeasureName().equals(md.getMeasureName())) {
					nls.setMeasureDefinition(rmd);
				}
			}
			nls.setValue(lf.getValue());
			nls.setPerson(p);
			lfList.add(nls);
		}
		p.setLifeStatus(lfList);
		Person retval = Person.savePerson(p);
		for (LifeStatus ls : lfList) {
			// and add this record in history too
			HealthMeasureHistory record = new HealthMeasureHistory();
			record.setMeasureDefinition(ls.getMeasureDefinition());
			record.setPerson(retval);
			record.setTimestamp(new Date(System.currentTimeMillis()));
			record.setValue(ls.getValue());
			HealthMeasureHistory.saveHealthMeasureHistory(record);
		}

		return retval;
	}

	// Defines that the next path parameter after the base url is
	// treated as a parameter and passed to the PersonResources
	// Allows to type http://localhost:599/base_url/1
	// 1 will be treaded as parameter todo and passed to PersonResource
	@Path("{personId}")
	public PersonResource getPerson(@PathParam("personId") int id) {
		return new PersonResource(uriInfo, request, id);
	}
}