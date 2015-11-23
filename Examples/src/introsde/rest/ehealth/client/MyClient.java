package introsde.rest.ehealth.client;

import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class MyClient {
	static int first_person_id = 1;
	static int last_person_id = 2;
	static int to_delete_id_xml;
	static int to_delete_id_json;
	static ArrayList<MeasureDefinition> measures = new ArrayList<>();
	static Map<Integer, String> mids = new HashMap<>();

	public static void main(String[] args) {
		System.out.println(getBaseURI());
		try {
			request1();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.1\nIf you want to inspect the code check the method with name request1()");
		}
		System.out.println("\n#############################\n");
		try {
			request2();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.2\nIf you want to inspect the code check the method with name request2()");
		}
		System.out.println("\n#############################\n");
		try {
			request3();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.3\nIf you want to inspect the code check the method with name request3()");
		}
		System.out.println("\n#############################\n");
		try {
			request4();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.4\nIf you want to inspect the code check the method with name request4()");
		}
		System.out.println("\n#############################\n");
		try {
			request5();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.5\nIf you want to inspect the code check the method with name request5()");
		}
		System.out.println("\n#############################\n");
		try {
			request6();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.6\nIf you want to inspect the code check the method with name request6()");
		}
		System.out.println("\n#############################\n");
		try {
			request7();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.7\nIf you want to inspect the code check the method with name request7()");
		}
		System.out.println("\n#############################\n");
		try {
			request8();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.8\nIf you want to inspect the code check the method with name request8()");
		}
		System.out.println("\n#############################\n");
		try {
			request9();
		} catch (Exception e) {
			System.out.println(
					"Something wrong with step 3.9\nIf you want to inspect the code check the method with name request9()");
		}
		System.out.println("\n#############################\n");
	}

	private static void request1()
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
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
		out.println("Request #1: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
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
		out.println("Request #1: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
		response = service.path("person").request().accept(accept).get();
		resp = response.readEntity(String.class);

		resp = resp.replaceAll("firstname", "name");
		resp = resp.replaceAll("personID", "idPerson");
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

		Person[] navigationArray = gson.fromJson(resp, Person[].class);

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

	private static void request2()
			throws TransformerException, ParserConfigurationException, SAXException, IOException {
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
		out.println("Request #2: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
		response = service.path("person/" + first_person_id).request().accept(accept).get();
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
		out.println("Request #2: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
		response = service.path("person/" + first_person_id).request().accept(accept).get();
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

	private static void request3()
			throws TransformerException, ParserConfigurationException, SAXException, IOException {
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
		out.println("Request #3: PUT " + url + " Accept: " + accept + " Content-type: " + contentType);

		int randomNumberxml = new Random(System.currentTimeMillis()).nextInt(10000);
		String newNamexml = "newName#" + randomNumberxml;
		String bodyxml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> " + "<person> "
				+ "    <firstname>" + newNamexml + "</firstname> " + "    <lastname>Duck</lastname> "
				+ "    <birthdate>01/09/1978</birthdate> " + "    <healthProfile> " + "        <lifeStatus> "
				+ "            <measureDefinition> " + "                <measureName>weight</measureName> "
				+ "            </measureDefinition> " + "            <value>62.9</value> " + "        </lifeStatus> "
				+ "        <lifeStatus> " + "            <measureDefinition> "
				+ "                <measureName>height</measureName> " + "            </measureDefinition> "
				+ "            <value>171.5</value> " + "        </lifeStatus> " + "    </healthProfile> "
				+ "</person> ";
		response = service.path("person/" + first_person_id).request().accept(MediaType.APPLICATION_XML)
				.put(Entity.xml(bodyxml));
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
		out.println("Request #3: PUT " + url + " Accept: " + accept + " Content-type: " + contentType);

		int randomNumberj = new Random(System.currentTimeMillis()).nextInt(10000);
		String newNamejson = "newName#" + randomNumberj;
		String bodyj = "{ " + "  \"firstname\": \"" + newNamejson + "\", " + "  \"lastname\": \"Paperino\", "
				+ "  \"birthdate\": \"01/09/1978\", " + "  \"lifeStatus\": [ " + "    { "
				+ "      \"value\": \"168.7\", " + "      \"measureDefinition\": { "
				+ "        \"measureName\": \"height\" " + "      } " + "    }, " + "    { "
				+ "      \"value\": \"78.7\", " + "      \"measureDefinition\": { "
				+ "        \"measureName\": \"weight\" " + "      } " + "    } " + "  ] " + "} ";

		response = service.path("person/" + first_person_id).request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.json(bodyj));
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

	private static void request4()
			throws TransformerException, ParserConfigurationException, SAXException, IOException {
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
		out.println("Request #4: POST " + url + " Accept: " + accept + " Content-type: " + contentType);

		String bodyxml = "<person> " + "    <firstname>Chuck</firstname> " + "    <lastname>Norris</lastname> "
				+ "    <birthdate>01/01/1945</birthdate> " + "    <healthProfile> " + "        <lifeStatus> "
				+ "            <measureDefinition> " + "                <measureName>height</measureName> "
				+ "            </measureDefinition> " + "            <value>172</value> " + "        </lifeStatus> "
				+ "        <lifeStatus> " + "            <measureDefinition> "
				+ "                <measureName>weight</measureName> " + "            </measureDefinition> "
				+ "            <value>78.9</value> " + "        </lifeStatus> " + "    </healthProfile> "
				+ "</person> ";
		response = service.path("person").request().accept(accept).post(Entity.xml(bodyxml));
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
				if (node.getTextContent() != null && !node.getTextContent().equals("")) {
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
		out.println("Request #4: POST " + url + " Accept: " + accept + " Content-type: " + contentType);

		String bodyj = "{ " + "  \"firstname\": \"" + "Chuck" + "\", " + "  \"lastname\": \"Norris\", "
				+ "  \"birthdate\": \"01/01/1945\", " + "  \"lifeStatus\": [ " + "    { " + "      \"value\": \"172\", "
				+ "      \"measureDefinition\": { " + "        \"measureName\": \"height\" " + "      } " + "    }, "
				+ "    { " + "      \"value\": \"78.9\", " + "      \"measureDefinition\": { "
				+ "        \"measureName\": \"weight\" " + "      } " + "    } " + "  ] " + "} ";

		response = service.path("person").request().accept(accept).post(Entity.json(bodyj));
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

		// step 3.5 Accept XML
		url = getBaseURI() + "/person/" + to_delete_id_xml;
		accept = "";
		contentType = "";
		out.println("Request #5: DELETE " + url + " Accept: " + accept + " Content-type: " + contentType);

		response = service.path("person/" + to_delete_id_xml).request().accept(accept).delete();
		resp = response.readEntity(String.class);

		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);

		if (responseCode == 204) {
			url = getBaseURI() + "/person/" + to_delete_id_xml;
			accept = MediaType.APPLICATION_XML;
			contentType = "";
			out.println("Subsequent request: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
			response = service.path("person/" + to_delete_id_xml).request().accept(accept).get();
			resp = response.readEntity(String.class);
			responseCode = response.getStatus();
			out.println("=>Subsequent HTTP Status: " + responseCode);
			if (responseCode == 404) {
				result = "OK, person with id" + to_delete_id_xml + " is not present in the db";
			} else {
				result = "ERROR";
			}
			out.println("=> Result: " + result);
		}

		// step 3.5 Accept JSON
		url = getBaseURI() + "/person/" + to_delete_id_json;
		accept = "";
		contentType = "";
		out.println("Request #5: DELETE " + url + " Accept: " + accept + " Content-type: " + contentType);

		response = service.path("person/" + to_delete_id_json).request().accept(accept).delete();
		resp = response.readEntity(String.class);

		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);

		if (responseCode == 204) {
			url = getBaseURI() + "/person/" + to_delete_id_json;
			accept = MediaType.APPLICATION_JSON;
			contentType = "";
			out.println("Subsequent request: GET " + url + " Accept: " + accept + " Content-type: " + contentType);
			response = service.path("person/" + to_delete_id_json).request().accept(accept).get();
			resp = response.readEntity(String.class);
			responseCode = response.getStatus();
			out.println("=>Subsequent HTTP Status: " + responseCode);
			if (responseCode == 404) {
				result = "OK, person with id" + to_delete_id_json + " is not present in the db";
			} else {
				result = "ERROR";
			}
			out.println("=> Result: " + result);
		}

		out.close();
	}

	private static void request6()
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-6.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		Response response = null;

		// step 3.6 Accept XML
		url = getBaseURI() + "/measureTypes";
		accept = MediaType.APPLICATION_XML;
		contentType = "";
		out.println("Request #6: GET " + url + " Accept: " + accept + " Content-type: " + contentType);

		response = service.path("measureTypes").request().accept(accept).get();
		resp = response.readEntity(String.class);
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);

		NodeList measuretypes = doc.getElementsByTagName("measureDefinition");
		if (measuretypes.getLength() > 2) {
			result = "OK, more than 2 measureTypes";
		} else {
			result = "ERROR";
		}
		// for (int i = 0; i < measuretypes.getLength(); i++) {
		// Node n = measuretypes.item(i);
		// Node measurename = n.getFirstChild();
		// if (measurename.getNodeName().equals("measureName")) {
		// String measureName=measurename.getTextContent();
		// MeasureDefinition md=new MeasureDefinition();
		// md.setMeasureName(measureName);
		// measures.add(md);
		// }
		// }
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentXML(resp));

		// step 3.6 Accept JSON
		url = getBaseURI() + "/measureTypes";
		accept = MediaType.APPLICATION_JSON;
		contentType = "";
		out.println("Request #6: GET " + url + " Accept: " + accept + " Content-type: " + contentType);

		response = service.path("measureTypes").request().accept(accept).get();
		resp = response.readEntity(String.class);

		Gson gson = new Gson();
		MeasureDefinition[] mdlist = gson.fromJson(resp, MeasureDefinition[].class);
		measures.clear();
		measures.addAll(Arrays.asList(mdlist));
		if (measures.size() > 2) {
			result = "OK, more than 2 measureTypes";
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		responseCode = response.getStatus();
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentJSON(resp));

		out.close();
	}

	private static void request7() throws IOException, ParserConfigurationException, SAXException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-7.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		Response response = null;

		// step 3.7 Accept XML
		accept = MediaType.APPLICATION_XML;
		contentType = "";

		// xml and first person
		for (MeasureDefinition md : measures) {
			url = "person/" + first_person_id + "/" + md.getMeasureName();
			out.println("Request #7: GET " + getBaseURI() + "/" + url + " Accept: " + accept + " Content-type: "
					+ contentType);

			response = service.path(url).request().accept(accept).get();
			resp = response.readEntity(String.class);
			responseCode = response.getStatus();
			out.println("=> HTTP Status: " + responseCode);
			if (responseCode != 404) {
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(resp));
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				NodeList midlist = doc.getElementsByTagName("mid");
				mids.put(Integer.parseInt(midlist.item(0).getTextContent()), md.getMeasureName());
			}
			out.println(indentXML(resp));
		}

		// xml and last person
		for (MeasureDefinition md : measures) {
			url = "person/" + last_person_id + "/" + md.getMeasureName();
			out.println("Request #7: GET " + getBaseURI() + "/" + url + " Accept: " + accept + " Content-type: "
					+ contentType);
			response = service.path(url).request().accept(accept).get();
			resp = response.readEntity(String.class);
			responseCode = response.getStatus();
			out.println("=> HTTP Status: " + responseCode);
			if (responseCode != 404) {
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(resp));
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(is);
				NodeList midlist = doc.getElementsByTagName("mid");
			}
			out.println(indentXML(resp));
		}

		// step 3.7 Accept JSON
		accept = MediaType.APPLICATION_JSON;
		contentType = "";

		// json and first person
		for (MeasureDefinition md : measures) {
			url = "person/" + first_person_id + "/" + md.getMeasureName();
			out.println("Request #7: GET " + getBaseURI() + "/" + url + " Accept: " + accept + " Content-type: "
					+ contentType);

			response = service.path(url).request().accept(accept).get();
			resp = response.readEntity(String.class);
			responseCode = response.getStatus();
			out.println("=> HTTP Status: " + responseCode);
			if (responseCode != 404) {

				resp = resp.replaceAll("mid", "idMeasureHistory");
				resp = resp.replaceAll("created", "timestamp");

				ObjectMapper mapper = new ObjectMapper();
				HealthMeasureHistory[] hmlist = mapper.readValue(resp, HealthMeasureHistory[].class);
				for (HealthMeasureHistory hm : hmlist) {
					mids.put(hm.getIdMeasureHistory(), md.getMeasureName());
				}
			}
			out.println(indentJSON(resp));
		}

		// json and last person
		for (MeasureDefinition md : measures) {
			url = "person/" + last_person_id + "/" + md.getMeasureName();
			out.println("Request #7: GET " + getBaseURI() + "/" + url + " Accept: " + accept + " Content-type: "
					+ contentType);

			response = service.path(url).request().accept(accept).get();
			resp = response.readEntity(String.class);
			responseCode = response.getStatus();
			out.println("=> HTTP Status: " + responseCode);
			if (responseCode != 404) {
				resp = resp.replaceAll("mid", "idMeasureHistory");
				resp = resp.replaceAll("created", "timestamp");

				ObjectMapper mapper = new ObjectMapper();
				HealthMeasureHistory[] hmlist = mapper.readValue(resp, HealthMeasureHistory[].class);
			}
			out.println(indentJSON(resp));
		}
		if (mids.isEmpty()) {
			result = "ERROR";
		} else {
			result = "OK, there is at least an HealthMeasure record in the hisotry";
		}
		out.println("=> Result: " + result);
		out.close();
	}

	private static void request8() throws FileNotFoundException {
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-8.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		Response response = null;

		List<Integer> midlist = new ArrayList<>();
		midlist.addAll(mids.keySet());
		int mid = midlist.get(0);
		String measuretype = mids.get(mid);

		// step 3.8 Accept XML
		url = "person/" + first_person_id + "/" + measuretype + "/" + mid;
		accept = MediaType.APPLICATION_XML;
		contentType = "";
		out.println(
				"Request #8: GET " + getBaseURI() + "/" + url + " Accept: " + accept + " Content-type: " + contentType);

		response = service.path(url).request().accept(accept).get();
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();
		if (responseCode != 404) {
			result = "OK, HealthMeasure record with id=" + mid + " and type=" + measuretype + " found!";
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentXML(resp));

		// step 3.8 Accept JSON
		url = "person/" + first_person_id + "/" + measuretype + "/" + mid;
		accept = MediaType.APPLICATION_JSON;
		contentType = "";
		out.println(
				"Request #8: GET " + getBaseURI() + "/" + url + " Accept: " + accept + " Content-type: " + contentType);

		response = service.path(url).request().accept(accept).get();
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();
		if (responseCode != 404) {
			result = "OK, HealthMeasure record with id=" + mid + " and type=" + measuretype + " found!";
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentJSON(resp));

		out.close();
	}

	private static void request9() throws JsonParseException, JsonMappingException, IOException, JAXBException {

		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		WebTarget service = client.target(getBaseURI());

		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-9.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		Response response = null;
		int counterBefore = 0;

		accept = MediaType.APPLICATION_JSON;
		contentType = "";
		url = "person/" + first_person_id + "/weight";
		// out.println("Request #7: GET " + getBaseURI() + "/" + url
		// + " Accept: " + accept + " Content-type: " + contentType);
		response = service.path(url).request().accept(accept).get();
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();
		if (responseCode != 404) {
			resp = resp.replaceAll("mid", "idMeasureHistory");
			resp = resp.replaceAll("created", "timestamp");
			ObjectMapper mapper = new ObjectMapper();
			HealthMeasureHistory[] hmlist = mapper.readValue(resp, HealthMeasureHistory[].class);
			counterBefore = hmlist.length;
		}

		// step 3.9 accept xml
		accept = MediaType.APPLICATION_XML;
		contentType = MediaType.APPLICATION_XML;
		url = "person/" + first_person_id + "/weight";
		out.println("Request #9: POST " + getBaseURI() + "/" + url + " Accept: " + accept + " Content-type: "
				+ contentType);

		LifeStatus lifestatus = new LifeStatus();
		lifestatus.setValue("72");
		JAXBContext jc = JAXBContext.newInstance(LifeStatus.class);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		m.marshal(lifestatus, new StreamResult(new OutputStreamWriter(outputStream, "UTF-8")));
		String bodyxml = outputStream.toString();
		response = service.path(url).request().accept(accept).post(Entity.xml(bodyxml));
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();

		// check if one more
		accept = MediaType.APPLICATION_JSON;
		contentType = "";
		url = "person/" + first_person_id + "/weight";
		// out.println("Request #7: GET " + getBaseURI() + "/" + url
		// + " Accept: " + accept + " Content-type: " + contentType);
		response = service.path(url).request().accept(accept).get();
		String resp2 = response.readEntity(String.class);
		int responseCode2 = response.getStatus();
		if (responseCode2 != 404) {
			resp2 = resp2.replaceAll("mid", "idMeasureHistory");
			resp2 = resp2.replaceAll("created", "timestamp");
			ObjectMapper mapper = new ObjectMapper();
			HealthMeasureHistory[] hmlist = mapper.readValue(resp2, HealthMeasureHistory[].class);
			int counterAfter = hmlist.length;
			if (counterAfter == (counterBefore + 1)) {
				result = "OK, one more record!";
			} else {
				result = "ERROR";
			}
		}

		out.println("=> Result: " + result);
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentXML(resp));

		// JSON
		accept = MediaType.APPLICATION_JSON;
		contentType = "";
		url = "person/" + first_person_id + "/height";
		// out.println("Request #7: GET " + getBaseURI() + "/" + url
		// + " Accept: " + accept + " Content-type: " + contentType);
		response = service.path(url).request().accept(accept).get();
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();
		if (responseCode != 404) {
			resp = resp.replaceAll("mid", "idMeasureHistory");
			resp = resp.replaceAll("created", "timestamp");
			ObjectMapper mapper = new ObjectMapper();
			HealthMeasureHistory[] hmlist = mapper.readValue(resp, HealthMeasureHistory[].class);
			counterBefore = hmlist.length;
		}

		// step 3.9 accept json
		accept = MediaType.APPLICATION_JSON;
		contentType = MediaType.APPLICATION_JSON;
		url = "person/" + first_person_id + "/height";
		out.println("Request #9: POST " + getBaseURI() + "/" + url + " Accept: " + accept + " Content-type: "
				+ contentType);

		LifeStatus lifestatusj = new LifeStatus();
		lifestatusj.setValue("172");
		ObjectMapper mapperj = new ObjectMapper();
		JaxbAnnotationModule module = new JaxbAnnotationModule();
		mapperj.registerModule(module);
		mapperj.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapperj.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		String bodyjson = mapperj.writeValueAsString(lifestatusj);

		response = service.path(url).request().accept(accept).post(Entity.json(bodyjson));
		resp = response.readEntity(String.class);
		responseCode = response.getStatus();

		// check if one more
		accept = MediaType.APPLICATION_JSON;
		contentType = "";
		url = "person/" + first_person_id + "/height";
		// out.println("Request #7: GET " + getBaseURI() + "/" + url
		// + " Accept: " + accept + " Content-type: " + contentType);
		response = service.path(url).request().accept(accept).get();
		String resp3 = response.readEntity(String.class);
		int responseCode3 = response.getStatus();
		if (responseCode3 != 404) {
			resp3 = resp3.replaceAll("mid", "idMeasureHistory");
			resp3 = resp3.replaceAll("created", "timestamp");
			ObjectMapper mapper = new ObjectMapper();
			HealthMeasureHistory[] hmlist = mapper.readValue(resp3, HealthMeasureHistory[].class);
			int counterAfter = hmlist.length;
			if (counterAfter == (counterBefore + 1)) {
				result = "OK, one more record!";
			} else {
				result = "ERROR";
			}
		}

		out.println("=> Result: " + result);
		out.println("=> HTTP Status: " + responseCode);
		out.println(indentXML(resp));

	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://lorebzassignment2.herokuapp.com/sdelab").build();
		// "http://localhost:5700/sdelab").build();
	}

	private static String indentXML(String resp) {
		try {
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(resp));

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(is);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
			// "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(outputStream, "UTF-8")));

			return outputStream.toString();
		} catch (Exception e) {
			return resp;
		}

	}

	private static String indentJSON(String toIndent) {
		try {
			String retval = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(toIndent));
			return retval;
		} catch (Exception e) {
			return toIndent;
		}

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
