package introsde.rest.ehealth.client;

import java.net.URI;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class Test {
	public static void main(String[] args) {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());
		Response response;
		String resp;
		int responseCode;
		System.out.println("start");

		String bodyxml = "<person> " + 
				 "    <firstname>bau</firstname> " + 
				 "    <lastname>bau</lastname> " + 
				 "    <birthdate>01/09/1978</birthdate> " + 
				 "    <healthProfile> " + 
				 "        <lifeStatus> " + 
				 "            <measureDefinition> " + 
				 "                <measureName>height</measureName> " + 
				 "            </measureDefinition> " + 
				 "            <value>168.7</value> " + 
				 "        </lifeStatus> " + 
				 "        <lifeStatus> " + 
				 "            <measureDefinition> " + 
				 "                <measureName>weight</measureName> " + 
				 "            </measureDefinition> " + 
				 "            <value>78.7</value> " + 
				 "        </lifeStatus> " + 
				 "    </healthProfile> " + 
				 "</person> ";
		response = service.path("person").request()
				.accept(MediaType.APPLICATION_XML).post(Entity.xml(bodyxml));
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();

		System.out.println(resp);
		// // Get plain text

		System.out.println("end");
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://lorebzassignment2.herokuapp.com/sdelab").build();
	}
}
