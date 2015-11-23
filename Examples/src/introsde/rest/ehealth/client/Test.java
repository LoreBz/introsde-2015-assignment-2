package introsde.rest.ehealth.client;

import introsde.rest.ehealth.model.LifeStatus;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;

import org.glassfish.jersey.client.ClientConfig;

public class Test {
	public static void main(String[] args) throws JAXBException,
			UnsupportedEncodingException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());
		Response response = null;
		String resp = "buro";
		int responseCode;
		String url = "";
		String accept = "";
		String contentType = "";
		String result = "";
		int counterBefore = 0;
		System.out.println("start");

		accept = MediaType.APPLICATION_XML;
		contentType = MediaType.APPLICATION_XML;
		url = "person/" + "1" + "/weight";
		System.out.println("Request #9: POST " + getBaseURI() + "/" + url
				+ " Accept: " + accept + " Content-type: " + contentType);

		LifeStatus lifestatus = new LifeStatus();
		lifestatus.setValue("1900");
		JAXBContext jc = JAXBContext.newInstance(LifeStatus.class);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		m.marshal(lifestatus, new StreamResult(new OutputStreamWriter(
				outputStream, "UTF-8")));
		String bodyxml = outputStream.toString();
		response = service.path("person").request().accept(accept)
				.post(Entity.xml(bodyxml));
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();

		System.out.println(resp);
		System.out.println(responseCode);
		// // Get plain text

		System.out.println("end");
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
		// "http://lorebzassignment2.herokuapp.com/sdelab").build();
				"http://localhost:5700/sdelab").build();
	}
}
