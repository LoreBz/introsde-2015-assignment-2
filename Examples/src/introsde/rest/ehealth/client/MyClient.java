package introsde.rest.ehealth.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.glassfish.jersey.client.ClientConfig;
import org.xml.sax.SAXException;

public class MyClient {
	public static void main(String[] args) throws IOException, JAXBException, SAXException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());
		System.out.println(getBaseURI());
		// // GET BASEURL/rest/salutation
		// // Accept: text/plain

		String url = getBaseURI() + "/person";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		// con.setRequestProperty("User-Agent", USER_AGENT);
		System.out.println("\nSending 'GET' request to URL : " + url);
		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);

		System.out.println("Response output:\n" + MyClient.getConnectionOutput(con));

		JAXBContext jaxbContext = JAXBContext.newInstance("");
		Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = schemaFactory.newSchema(new File("catalog.xsd"));
		unMarshaller.setSchema(schema);

		// // // Get plain text
		// System.out.println(
		// service.path("salutation").request().accept(MediaType.TEXT_PLAIN).get().readEntity(String.class));
		// // Get XML
		// System.out.println(
		// service.path("salutation").request().accept(MediaType.TEXT_XML).get().readEntity(String.class));
		// // // The HTML
		// System.out.println(
		// service.path("salutation").request().accept(MediaType.TEXT_HTML).get().readEntity(String.class));

	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://lorebzassignment2.herokuapp.com/sdelab").build();
	}

	public static String getConnectionOutput(HttpURLConnection con) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		return response.toString();
	}
}
