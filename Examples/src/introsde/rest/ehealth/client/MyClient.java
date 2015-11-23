package introsde.rest.ehealth.client;

import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.myutil.MultiPrintStream;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class MyClient {
	static int first_person_id;
	static int last_person_id;
	static int to_delete_id_xml;
	static int to_delete_id_json;

	public static void main(String[] args) throws IOException, JAXBException,
			SAXException, TransformerException, ParserConfigurationException,
			XPathExpressionException {
		System.out.println(getBaseURI());
		request1();
		request2();
		request3();
		request4();
		request5();
	}

	private static void request1() throws ParserConfigurationException,
			SAXException, IOException, TransformerException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-1.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		int counterPerson = 0;
		Response response = null;

		// step 3.1 Accept XML
		url = getBaseURI() + "/person";
		accept = MediaType.APPLICATION_XML;
		contentType = "";
		out.println("Request #1: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		response = service.path("person").request().accept(accept).get();
		resp = response.readEntity(String.class);

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		NodeList nodes = doc.getElementsByTagName("person");

		// first_person_id
		Node first_node = nodes.item(0);

		NodeList fn_children = first_node.getChildNodes();
		for (int i = 0; i < fn_children.getLength(); i++) {
			Node item = fn_children.item(i);
			if (!(item.getNodeName() == "#text")) {
				if (item.getNodeName().equals("personID")) {
					first_person_id = Integer.parseInt(item.getTextContent());
				}
			}
		}

		// last_person_id
		Node last_node = nodes.item(nodes.getLength() - 1);

		NodeList ln_children = last_node.getChildNodes();
		for (int i = 0; i < ln_children.getLength(); i++) {
			Node item = ln_children.item(i);
			if (!(item.getNodeName() == "#text")) {
				if (item.getNodeName().equals("personID")) {
					last_person_id = Integer.parseInt(item.getTextContent());
				}
			}
		}
		counterPerson = nodes.getLength();
		if (counterPerson < 3) {
			result = "ERROR (less than 3 persons)";
		} else {
			result = "OK (#person=" + counterPerson + ")";
		}
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentXML(resp));

		// step 3.1 Accept json
		url = getBaseURI() + "/person";

		url = getBaseURI() + "/person";
		accept = MediaType.APPLICATION_JSON;
		contentType = "";
		out.println("Request #1: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		response = service.path("person").request().accept(accept).get();
		resp = response.readEntity(String.class);

		resp = resp.replaceAll("firstname", "name");
		resp = resp.replaceAll("personID", "idPerson");
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

		Person[] navigationArray = gson.fromJson(resp, Person[].class);
		for (Person p : navigationArray) {
			System.out.println(p.getName());
			System.out.println(p.getBirthdate());
			System.out.println(p.getIdPerson());
		}

		counterPerson = MyClient.countSubStringOccur(resp, "idPerson");
		if (counterPerson < 3) {
			result = "ERROR (less than 3 persons)";
		} else {
			result = "OK (#person=" + counterPerson + ")";
		}
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentJSON(resp));

		out.close();

	}

	private static void request2() throws TransformerException,
			ParserConfigurationException, SAXException, IOException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-2.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		Response response = null;

		// step 3.2 Accept XML
		url = getBaseURI() + "/person/" + first_person_id;
		accept = MediaType.APPLICATION_XML;
		contentType = "";
		out.println("Request #2: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		response = service.path("person/"+first_person_id).request().accept(accept).get();
		resp = response.readEntity(String.class);

		responseCode = response.getStatus();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK, status is fine";
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentXML(resp));

		// step 3.1 Accept json
		url = getBaseURI() + "/person/" + first_person_id;
		accept = MediaType.APPLICATION_JSON;
		contentType = "";
		out.println("Request #2: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		response = service.path("person/"+first_person_id).request().accept(accept).get();
		resp = response.readEntity(String.class);

		responseCode = response.getStatus();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK, status is fine";
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentJSON(resp));

		out.close();

	}

	private static void request3() throws TransformerException,
			ParserConfigurationException, SAXException, IOException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-3.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		Response response = null;

		// step 3.2 Accept XML
		url = getBaseURI() + "/person/" + first_person_id;
		accept = MediaType.APPLICATION_XML;
		contentType = MediaType.APPLICATION_XML;
		out.println("Request #3: PUT " + url + " Accept: " + accept
				+ " Content-type: " + contentType);

		int randomNumberxml = new Random(System.currentTimeMillis())
				.nextInt(10000);
		String newNamexml = "newName#" + randomNumberxml;
		String bodyxml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> "
				+ "<person> "
				+ "    <firstname>"
				+ newNamexml
				+ "</firstname> "
				+ "    <lastname>Duck</lastname> "
				+ "    <birthdate>01/09/1978</birthdate> "
				+ "    <healthProfile> "
				+ "        <lifeStatus> "
				+ "            <measureDefinition> "
				+ "                <measureName>weight</measureName> "
				+ "            </measureDefinition> "
				+ "            <value>62.9</value> "
				+ "        </lifeStatus> "
				+ "        <lifeStatus> "
				+ "            <measureDefinition> "
				+ "                <measureName>height</measureName> "
				+ "            </measureDefinition> "
				+ "            <value>171.5</value> "
				+ "        </lifeStatus> "
				+ "    </healthProfile> "
				+ "</person> ";
		response = service.path("person/" + first_person_id).request()
				.accept(MediaType.APPLICATION_XML).put(Entity.xml(bodyxml));
		resp = response.readEntity(String.class);

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		NodeList nodes = doc.getElementsByTagName("firstname");
		// check if name is changed
		if (nodes.getLength() == 1) {
			Node node = nodes.item(0);
			if (node.getTextContent().equals(newNamexml)) {
				result = "OK, name changed";
			} else {
				result = "ERROR";
			}
		}
		responseCode = response.getStatus();
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentXML(resp));

		// step 3.1 Accept json
		url = getBaseURI() + "/person/" + first_person_id;
		accept = MediaType.APPLICATION_JSON;
		contentType = MediaType.APPLICATION_JSON;
		out.println("Request #3: PUT " + url + " Accept: " + accept
				+ " Content-type: " + contentType);

		int randomNumberj = new Random(System.currentTimeMillis())
				.nextInt(10000);
		String newNamejson = "newName#" + randomNumberj;
		String bodyj = "{ " + "  \"firstname\": \"" + newNamejson + "\", "
				+ "  \"lastname\": \"Paperino\", "
				+ "  \"birthdate\": \"01/09/1978\", " + "  \"lifeStatus\": [ "
				+ "    { " + "      \"value\": \"168.7\", "
				+ "      \"measureDefinition\": { "
				+ "        \"measureName\": \"height\" " + "      } "
				+ "    }, " + "    { " + "      \"value\": \"78.7\", "
				+ "      \"measureDefinition\": { "
				+ "        \"measureName\": \"weight\" " + "      } "
				+ "    } " + "  ] " + "} ";

		response = service.path("person/" + first_person_id).request()
				.accept(MediaType.APPLICATION_JSON).put(Entity.json(bodyj));
		resp = response.readEntity(String.class);

		resp = resp.replaceAll("firstname", "name");
		resp = resp.replaceAll("personID", "idPerson");
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		Person person = gson.fromJson(resp, Person.class);

		if (person.getName().equals(newNamejson)) {
			result = "OK, name changed";
		} else {
			result = "ERROR";
		}

		responseCode = response.getStatus();

		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentJSON(resp));

		out.close();

	}

	private static void request4() throws TransformerException,
			ParserConfigurationException, SAXException, IOException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-4.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		Response response = null;

		// step 3.4 Accept XML
		url = getBaseURI() + "/person";
		accept = MediaType.APPLICATION_XML;
		contentType = MediaType.APPLICATION_XML;
		out.println("Request #4: POST " + url + " Accept: " + accept
				+ " Content-type: " + contentType);

		String bodyxml = "<person> " + "    <firstname>Chuck</firstname> "
				+ "    <lastname>Norris</lastname> "
				+ "    <birthdate>01/01/1945</birthdate> "
				+ "    <healthProfile> " + "        <lifeStatus> "
				+ "            <measureDefinition> "
				+ "                <measureName>height</measureName> "
				+ "            </measureDefinition> "
				+ "            <value>172</value> " + "        </lifeStatus> "
				+ "        <lifeStatus> " + "            <measureDefinition> "
				+ "                <measureName>weight</measureName> "
				+ "            </measureDefinition> "
				+ "            <value>78.9</value> " + "        </lifeStatus> "
				+ "    </healthProfile> " + "</person> ";
		response = service.path("person").request()
				.accept(accept).post(Entity.xml(bodyxml));
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		NodeList nodes = doc.getElementsByTagName("personID");
		// check if person is created with and ID
		if (responseCode == 200 || responseCode == 201 || responseCode == 202) {
			if (nodes.getLength() == 1) {
				Node node = nodes.item(0);
				if (node.getTextContent() != null
						&& !node.getTextContent().equals("")) {
					result = "OK, person saved with ID" + node.getTextContent();
					to_delete_id_xml = Integer.parseInt(node.getTextContent());
				}
			}
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentXML(resp));

		// step 3.4 Accept json
		url = getBaseURI() + "/person";
		accept = MediaType.APPLICATION_JSON;
		contentType = MediaType.APPLICATION_JSON;
		out.println("Request #4: POST " + url + " Accept: " + accept
				+ " Content-type: " + contentType);

		String bodyj = "{ " + "  \"firstname\": \"" + "Chuck" + "\", "
				+ "  \"lastname\": \"Norris\", "
				+ "  \"birthdate\": \"01/01/1945\", " + "  \"lifeStatus\": [ "
				+ "    { " + "      \"value\": \"172\", "
				+ "      \"measureDefinition\": { "
				+ "        \"measureName\": \"height\" " + "      } "
				+ "    }, " + "    { " + "      \"value\": \"78.9\", "
				+ "      \"measureDefinition\": { "
				+ "        \"measureName\": \"weight\" " + "      } "
				+ "    } " + "  ] " + "} ";

		response = service.path("person").request()
				.accept(accept).post(Entity.json(bodyj));
		resp = response.readEntity(String.class);

		resp = resp.replaceAll("firstname", "name");
		resp = resp.replaceAll("personID", "idPerson");
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		Person person = gson.fromJson(resp, Person.class);

		responseCode = response.getStatus();

		if (responseCode == 200 || responseCode == 201 || responseCode == 202) {
			result = "OK, person saved with ID" + person.getIdPerson();
			to_delete_id_json = person.getIdPerson();
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentJSON(resp));

		out.close();

	}

	private static void request5() throws FileNotFoundException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-5.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		Response response = null;

		// step 3.4 Accept XML
		url = getBaseURI() + "/person/"+to_delete_id_xml;
		accept = "";
		contentType = "";
		out.println("Request #5: DELETE " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		
		response = service.path("person/"+to_delete_id_xml).request()
				.accept(accept).delete();
		resp = response.readEntity(String.class);
		
		
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		
		if (responseCode==204) {
			url = getBaseURI() + "/person/" + to_delete_id_xml;
			accept = "";
			contentType = "";
			out.println("Subsequent request: GET " + url + " Accept: " + accept
					+ " Content-type: " + contentType);
			response = service.path("person/"+to_delete_id_xml).request().accept(accept).get();
			resp = response.readEntity(String.class);
			responseCode = response.getStatus();
			out.println("=>Subsequent HTTP Status: " + responseCode);
			if (responseCode==404) {
				result="OK, person with id"+to_delete_id_xml+" is not present in the db";
			} else {
				result="ERROR";
			}
			out.println("=> Result: " + result);
		}
		
		

	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://lorebzassignment2.herokuapp.com/sdelab").build();
		// "http://lorebzassignment2.herokuapp.com/sdelab"
	}

	private static String indentXML(String resp) throws TransformerException,
			ParserConfigurationException, SAXException, IOException {
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		transformer.transform(new DOMSource(doc), new StreamResult(
				new OutputStreamWriter(outputStream, "UTF-8")));

		return outputStream.toString();
	}

	private static String indentJSON(String toIndent) {
		String retval = new GsonBuilder().setPrettyPrinting().create()
				.toJson(new JsonParser().parse(toIndent));
		return retval;
	}

	private static int countSubStringOccur(String str, String findStr) {
		int lastIndex = 0;
		int count = 0;

		while (lastIndex != -1) {

			lastIndex = str.indexOf(findStr, lastIndex);

			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}

}
