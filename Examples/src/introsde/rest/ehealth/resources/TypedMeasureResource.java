package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.HealthMeasureHistory;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class TypedMeasureResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	int id;
	String measureType;
	int measureId;
	EntityManager entityManager; // only used if the application is deployed in

	// a Java EE container

	public TypedMeasureResource(UriInfo uriInfo, Request request, int id,
			String measureType, int measureId, EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.measureType = measureType;
		this.measureId = measureId;
		this.entityManager = em;
	}

	public TypedMeasureResource(UriInfo uriInfo, Request request, int id,
			String measureType, int measureId) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.measureType = measureType;
		this.measureId = measureId;
	}

	// Application integration
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public HealthMeasureHistory getTypedMeasureById() {
		System.out
				.println("GET person/{personID}/{measureType}/{mid} aka request 7");
		System.out.println("GET person/" + id + "/" + measureType + "/"
				+ measureId + " aka request 7");
		HealthMeasureHistory record = HealthMeasureHistory
				.getHealthMeasureHistoryById(measureId);
		System.out.println("record@memLoc: " + record);
		System.out.println("record measuredef.ID="
				+ record.getMeasureDefinition().getIdMeasureDef());
		System.out.println(record.getMeasureDefinition().getMeasureName());

		if (record == null
				|| !record.getMeasureDefinition().getMeasureName().equals(measureType)) {

			throw new RuntimeException(
					"Get: HealthMeasureHistory of person with " + id
							+ " for measuretype of type" + measureType
							+ " not found");

		} else {
			return record;
		}
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putPerson(HealthMeasureHistory record) {
		System.out
				.println("PUT /person/{id}/{measureType}/{mid} aka request 10");
		System.out.println("--> Updating MeasureRecord... " + this.measureId);
		System.out.println("--> " + record.toString());
		Response res;
		HealthMeasureHistory existing = HealthMeasureHistory
				.getHealthMeasureHistoryById(this.measureId);

		if (existing == null) {
			res = Response.noContent().build();
		} else {
			System.out.println("processing request 10, updating measure...");
			res = Response.created(uriInfo.getAbsolutePath()).build();
			record.setIdMeasureHistory(existing.getIdMeasureHistory());
			record.setPerson(existing.getPerson());
			record.setMeasureDefinition(existing.getMeasureDefinition());
			record.setTimestamp(existing.getTimestamp());
			// if the client has not selected a timestamp value we set it to the
			// current time
			// if (record.getTimestamp() == null) {
			// record.setTimestamp(new Date(System.currentTimeMillis()));
			// }
			// we assume the client has passed us a correct value, so
			// record.getValue()!=null
			if (record.getValue() != null) {
				HealthMeasureHistory.updateHealthMeasureHistory(record);
			}
		}
		return res;
	}

}
