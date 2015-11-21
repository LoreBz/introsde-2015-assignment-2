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

		// // GET BASEURL/rest/salutation
		// // Accept: text/plain
		int randomNumberj = new Random(System.currentTimeMillis())
				.nextInt(10000);
		String newNamej = "newName#" + randomNumberj;
		String bodyj = "{ " + "  \"firstname\": \"" + newNamej + "\", "
				+ "  \"lastname\": \"Paperino\", "
				+ "  \"birthdate\": \"01/09/1978\", " + "  \"lifeStatus\": [ "
				+ "    { " + "      \"value\": \"168.7\", "
				+ "      \"measureDefinition\": { "
				+ "        \"measureName\": \"height\" " + "      } "
				+ "    }, " + "    { " + "      \"value\": \"78.7\", "
				+ "      \"measureDefinition\": { "
				+ "        \"measureName\": \"weight\" " + "      } "
				+ "    } " + "  ] " + "} ";
		Response response = service.path("person/1").request()
				.accept(MediaType.TEXT_XML).put(Entity.json(bodyj));
		String retval = response.readEntity(String.class);
		System.out.println(retval);
		// // Get plain text

	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://lorebzassignment2.herokuapp.com/sdelab").build();
	}
}
