package introsde.rest.ehealth.client;

import introsde.rest.ehealth.myutil.MultiPrintStream;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class MyClient {
	static int first_person_id;
	static int last_person_id;

	public static void main(String[] args) throws IOException, JAXBException,
			SAXException, TransformerException, ParserConfigurationException,
			XPathExpressionException {
		System.out.println(getBaseURI());
		request1();
		request2();
		request3();

	}

	private static void request1() throws IOException, JAXBException,
			SAXException, TransformerException, ParserConfigurationException,
			XPathExpressionException {
		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-1.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);

		String url = "";
		URL obj = null;
		HttpURLConnection con;
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		int counterPerson = 0;
		// step 3.1 Accept XML
		url = getBaseURI() + "/person";

		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		accept = "application/xml";
		con.setRequestProperty("Accept", accept);
		// contentType = "application/xml";
		// con.setRequestProperty(" Content-type", contentType);

		out.println("Request #1: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		resp = MyClient.getConnectionOutputXML(con);

		// setting first/last_person_id
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
					out.println("personID=" + item.getTextContent() + " found!");
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
					out.println("personID=" + item.getTextContent() + " found!");
					last_person_id = Integer.parseInt(item.getTextContent());
				}
			}
		}

		// counterPerson = MyClient.countSubStringOccur(resp, "<person");
		counterPerson = nodes.getLength();
		if (counterPerson < 3) {
			result = "ERROR (less than 3 persons)";
		} else {
			result = "OK (#person=" + counterPerson + ")";
		}
		out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		out.println("=> HTTP Status: " + responseCode);
		out.println(resp);

		// step 3.1 Accept json
		url = getBaseURI() + "/person";

		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		accept = "application/json";
		con.setRequestProperty("Accept", accept);
		// contentType = "application/json";
		// con.setRequestProperty(" Content-type", contentType);

		out.println("Request #1: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);

		resp = MyClient.getConnectionOutputJSON(con);

		counterPerson = MyClient.countSubStringOccur(resp, "name");
		if (counterPerson < 3) {
			result = "ERROR (less than 3 persons)";
		} else {
			result = "OK (#person=" + counterPerson + ")";
		}
		out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		out.println("=> HTTP Status: " + responseCode);
		out.println(resp);

		out.close();
	}

	private static void request2() throws IOException, JAXBException,
			SAXException, TransformerException, ParserConfigurationException {
		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-2.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);
		String url = "";
		URL obj = null;
		HttpURLConnection con;
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";
		int counterPerson = 0;

		// step 3.2 Accept XML
		url = getBaseURI() + "/person/" + first_person_id;

		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		accept = "application/xml";
		con.setRequestProperty("Accept", accept);
		// contentType = "application/xml";
		// con.setRequestProperty(" Content-type", contentType);

		out.println("Request #2: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		resp = MyClient.getConnectionOutputXML(con);

		responseCode = con.getResponseCode();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK, status is fine";
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		out.println("=> HTTP Status: " + responseCode);
		out.println(resp);

		// step 3.2 Accept JSON
		url = getBaseURI() + "/person/" + first_person_id;

		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		accept = "application/json";
		con.setRequestProperty("Accept", accept);
		// contentType = "application/xml";
		// con.setRequestProperty(" Content-type", contentType);

		out.println("Request #2: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		resp = MyClient.getConnectionOutputJSON(con);

		responseCode = con.getResponseCode();
		if (responseCode == 200 || responseCode == 202) {
			result = "OK, status is fine";
		} else {
			result = "ERROR";
		}
		out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		out.println("=> HTTP Status: " + responseCode);
		out.println(resp);

	}

	private static void request3() throws IOException, JAXBException,
			SAXException, TransformerException, ParserConfigurationException {
		List<PrintStream> streams = new ArrayList<>();
		streams.add(System.out);
		FileOutputStream fileWriter = new FileOutputStream("step_3-3.txt");
		streams.add(new PrintStream(fileWriter));
		MultiPrintStream out = new MultiPrintStream(streams);
		String url = "";
		URL obj = null;
		HttpURLConnection con;
		int responseCode = -1;
		String resp = "";
		String accept = "";
		String contentType = "";
		String result = "";

		// step 3.3 Accept XML
		url = getBaseURI() + "/person/" + first_person_id;

		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();
		con.setDoOutput(true);

		con.setRequestMethod("PUT");
		accept = "application/xml";
		con.setRequestProperty("Accept", accept);
		contentType = "application/xml";
		con.setRequestProperty(" Content-type", contentType);

		out.println("Request #2: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		// prepare body
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

		int randomNumber = new Random(System.currentTimeMillis())
				.nextInt(10000);
		String newName = "newName#" + randomNumber;
		String xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> "
				+ "<person> "
				+ "    <firstname>"
				+ newName
				+ "</firstname> "
				+ "    <lastname>Paperino</lastname> "
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
		wr.write(xmlBody);
		// wr.close();
		con.getOutputStream().flush();
		resp = MyClient.getConnectionOutputXML(con);

		// check name in the response
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(resp));

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		NodeList nodes = doc.getElementsByTagName("firstname");
		Node node = nodes.item(0);
		if (node.getTextContent().equals(newName)) {
			result = "OK";
		} else {
			result = "ERROR";
		}

		responseCode = con.getResponseCode();

		out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		out.println("=> HTTP Status: " + responseCode);
		out.println(resp);

		// step 3.2 Accept JSON
		url = getBaseURI() + "/person/" + first_person_id;

		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();
		con.setDoOutput(true);

		con.setRequestMethod("PUT");
		accept = "application/json";
		con.setRequestProperty("Accept", accept);
		contentType = "application/json";
		con.setRequestProperty(" Content-type", contentType);

		out.println("Request #2: GET " + url + " Accept: " + accept
				+ " Content-type: " + contentType);
		// prepare body
		OutputStreamWriter wrj = new OutputStreamWriter(con.getOutputStream());

		int randomNumberj = new Random(System.currentTimeMillis())
				.nextInt(10000);
		String newNamej = "newName#" + randomNumberj;
		String xmlBodyj = "{ " + "  \"firstname\": \"" + newNamej + "\", "
				+ "  \"lastname\": \"Paperino\", "
				+ "  \"birthdate\": \"01/09/1978\", " + "  \"lifeStatus\": [ "
				+ "    { " + "      \"value\": \"168.7\", "
				+ "      \"measureDefinition\": { "
				+ "        \"measureName\": \"height\" " + "      } "
				+ "    }, " + "    { " + "      \"value\": \"78.7\", "
				+ "      \"measureDefinition\": { "
				+ "        \"measureName\": \"weight\" " + "      } "
				+ "    } " + "  ] " + "} ";
		wrj.write(xmlBodyj);
		wrj.close();

		resp = MyClient.getConnectionOutputXML(con);

		// check name in the response
		InputSource isj = new InputSource();
		isj.setCharacterStream(new StringReader(resp));

		DocumentBuilderFactory dbFactoryj = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder dBuilderj = dbFactoryj.newDocumentBuilder();
		Document docj = dBuilderj.parse(isj);
		NodeList nodesj = docj.getElementsByTagName("firstname");
		Node nodej = nodesj.item(0);
		if (nodej.getTextContent().equals(newNamej)) {
			result = "OK";
		} else {
			result = "ERROR";
		}

		responseCode = con.getResponseCode();

		out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		out.println("=> HTTP Status: " + responseCode);
		out.println(resp);

	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://lorebzassignment2.herokuapp.com/sdelab").build();
	}

	private static String getConnectionOutputXML(HttpURLConnection con)
			throws IOException, TransformerException,
			ParserConfigurationException, SAXException {
		PrintWriter writer = new PrintWriter("out.xml", "UTF-8");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			writer.println(inputLine);
		}
		in.close();
		writer.close();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse("out.xml");
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

	private static String getConnectionOutputJSON(HttpURLConnection con)
			throws IOException, TransformerException,
			ParserConfigurationException, SAXException {
		PrintWriter writer = new PrintWriter("out.json", "UTF-8");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		String retval = new GsonBuilder().setPrettyPrinting().create()
				.toJson(new JsonParser().parse(response.toString()));
		writer.write(retval);
		writer.close();
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

	private static void printNode(Node node) throws XPathExpressionException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		System.out.println(node.getNodeName());
		NodeList childNodes = node.getChildNodes();
		XPathExpression expr = xpath.compile("/*");
		NodeList evaluate = (NodeList) expr.evaluate(node, XPathConstants.NODE);
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (!(item.getNodeName() == "#text")) {
				System.out.println(item.getNodeName() + ": "
						+ item.getTextContent());
			}
		}
	}
}
