package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import jersey.repackaged.com.google.common.collect.Lists;

@Stateless
// only used if the the application is deployed in a Java EE container
@LocalBean
// only used if the the application is deployed in a Java EE container
public class MeasureHistoryResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int id;
	String measureType;

	EntityManager entityManager; // only used if the application is deployed in
									// a Java EE container

	public MeasureHistoryResource(UriInfo uriInfo, Request request, int id,
			String measureType, EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.measureType = measureType;
		this.entityManager = em;
	}

	public MeasureHistoryResource(UriInfo uriInfo, Request request, int id,
			String measureType) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.measureType = measureType;
	}

	// Application integration
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getHealthMeasureHistory(
	/*
	 * @QueryParam("before") String before,
	 * 
	 * @QueryParam("after") String after
	 */) {
		System.out.println("Request #6: GET /person/{id}/{measureType}");
		List<HealthMeasureHistory> measureHistory = this
				.getHMhistoryByUserAndType(id, measureType);
		// if (before == null || after == null) {
		// System.out
		// .println("GET person/{personID}/{measureType} aka request 6");
		// if (measureHistory == null)
		// throw new RuntimeException(
		// "Get: HealthMeasureHistory of person with " + id
		// + " for measuretype of type" + measureTypeId
		// + " not found");
		// return measureHistory;
		// } else {
		// Date Dbefore, Dafter;
		// try {
		// long numb_before = Long.parseLong(before);
		// long numb_after = Long.parseLong(after);
		//
		// Dbefore = new Date(numb_before);
		// Dafter = new Date(numb_after);
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw new RuntimeException(
		// "Get: HealthMeasureHistory of person with "
		// + id
		// + " for measuretype of type"
		// + measureTypeId
		// + "into selected date interval"
		// +
		// " not possible, probably you passed invalid before and after arguments");
		// }
		// System.out
		// .println("GET person/{personID}/{measureType}?before={beforeDate}&after={afterDate} aka request 11");
		// if (Dbefore.compareTo(Dafter) <= 0) {
		// throw new RuntimeException(
		// "Get: HealthMeasureHistory of person with "
		// + id
		// + " for measuretype of type"
		// + measureTypeId
		// + "into selected date interval"
		// +
		// " not possible, probably you passed after param bigger than before");
		// }
		// for (HealthMeasureHistory HMh : measureHistory) {
		// if (HMh.getTimestamp().compareTo(Dbefore) > 0
		// || HMh.getTimestamp().compareTo(Dafter) < 0) {
		// measureHistory.remove(HMh);
		// System.out.println("removing a date from the set \n"
		// + HMh.toString());
		// }
		// }
		// // bug here
		// return measureHistory;
		//
		// }
		// System.out.println("GET person/{personID}/{measureType} aka request 6");
		if (!measureHistory.isEmpty()) {
			System.out.println("we got something");
		} else {
			return Response
					.status(404)
					.entity("Get: HealthMeasureHistory for person with " + id
							+ " and measuretype " + measureType + " not found")
					.build();
		}
		GenericEntity<List<HealthMeasureHistory>> entity = new GenericEntity<List<HealthMeasureHistory>>(
				Lists.newArrayList(measureHistory)) {};
		return Response.ok(entity).build();

	}

	private List<HealthMeasureHistory> getHMhistoryByUserAndType(int id2,
			String measureType) {
		System.out.println("Reading HMhistory from DB for person with id: "
				+ id2 + " and measuretype of type=" + measureType);

		// this will work within a Java EE container, where not DAO will be
		// needed
		// Person person = entityManager.find(Person.class, personId);

		List<HealthMeasureHistory> hMhistoryList = HealthMeasureHistory
				.getAll();
		System.out.println("Got " + hMhistoryList.size() + " records");
		List<HealthMeasureHistory> retval = new ArrayList<>();
		for (HealthMeasureHistory hm : hMhistoryList) {
			if (hm.getPerson() != null) {
				if (hm.getPerson().getName() != null) {
					System.out.println(hm.getPerson().getName());
				}
				int idperson = hm.getPerson().getIdPerson();
				if (id2 == idperson) {
					if (hm.getMeasureDefinition() != null) {
						String measurename = hm.getMeasureDefinition()
								.getMeasureName();
						if (measurename.equals(measureType)) {
							retval.add(hm);
							System.out.println(hm.getPerson().getName()
									+ " "
									+ hm.getValue()
									+ " "
									+ hm.getMeasureDefinition()
											.getMeasureName());
						}
					} else {
						System.out.println("no measurdef! for this record");
					}
				}
			}

		}
		return retval;
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response newLifeStatus(LifeStatus record) throws IOException {
		// we should create a new Lifestatus and archive the old one
		System.out.println("POST /person/{id}/{measureType} aka request 8!");
		Person existingPerson = Person.getPersonById(id);
		if (existingPerson == null) {
			return Response.status(404).entity("You are trying to post a measure related to a non existing Person!").build();
		}
		System.out.println("personID valid");
		System.out
				.println("getting measuredefinition with type=" + measureType);
		MeasureDefinition md = null;
		try {
			List<MeasureDefinition> mdlist = MeasureDefinition.getAll();
			for (MeasureDefinition measureDefinition : mdlist) {
				if (measureDefinition.getMeasureName().equals(measureType)) {
					md = measureDefinition;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("errore nel prendere la measuredef");
		}
		if (record.getMeasureDefinition() != null) {
			if (!record.getMeasureDefinition().getMeasureName()
					.equals(md.getMeasureName())) {
				System.out.println("You set a path with measuretypeid="
						+ measureType + " that means=" + md.getMeasureName()
						+ "\nbut you set in the body a measuredef="
						+ record.getMeasureDefinition().getMeasureName());
			}
		}
		// to update lifestatus we update the lifestatus with the same
		// measuretype
		LifeStatus existingLS = this.getLifestatusByPersonIdAndMeasureType(id,
				md.getMeasureName());
		if (existingLS != null) {
			System.out.println("removing old lifestatus");
			LifeStatus.removeLifeStatus(existingLS);
		}
		System.out.println("preparing new lifestatus");
		LifeStatus toSave = new LifeStatus();
		toSave.setMeasureDefinition(md);
		toSave.setPerson(existingPerson);
		toSave.setValue(record.getValue());
		LifeStatus.saveLifeStatus(toSave);

		// and put the record in the history too
		HealthMeasureHistory hm = new HealthMeasureHistory();
		hm.setPerson(existingPerson);
		hm.setMeasureDefinition(md);
		hm.setValue(record.getValue());
		hm.setTimestamp(new Date(System.currentTimeMillis()));
		System.out.println("saving new lifestatus");
		HealthMeasureHistory.saveHealthMeasureHistory(hm);
		System.out.println("end");

		return Response.ok(toSave).build();
	}

	LifeStatus getLifestatusByPersonIdAndMeasureType(int personID,
			String measurename) {
		List<LifeStatus> list = LifeStatus.getAll();
		for (LifeStatus ls : list) {
			if (ls.getPerson().getIdPerson() == personID
					&& ls.getMeasureDefinition().getMeasureName()
							.equals(measurename)) {
				return ls;
			}
		}
		return null;
	}

	@Path("{mid}")
	public TypedMeasureResource getTypedMeasureById(
			@PathParam("mid") int measureId) {
		return new TypedMeasureResource(uriInfo, request, id, measureType,
				measureId);
	}

}
