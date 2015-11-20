package introsde.rest.ehealth.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

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

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class MyClient {
	int first_person_id;
	int last_person_id;

	public static void main(String[] args) throws IOException, JAXBException,
			SAXException, TransformerException, ParserConfigurationException {
		// ClientConfig clientConfig = new ClientConfig();
		// Client client = ClientBuilder.newClient(clientConfig);
		// WebTarget service = client.target(getBaseURI());
		request1();
	}

	private static void request1() throws IOException, JAXBException,
			SAXException, TransformerException, ParserConfigurationException {
		System.out.println(getBaseURI());
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
		contentType = "application/xml";
		con.setRequestProperty("Content-Type", contentType);

		System.out.println("Request #1: GET " + url + " Accept: " + accept
				+ "Content-type: " + contentType);
		resp = MyClient.getConnectionOutputXML(con);

		counterPerson = MyClient.countSubStringOccur(resp, "<person");
		if (counterPerson < 3) {
			result = "ERROR (less than 3 persons)";
		} else {
			result = "OK (#person=" + counterPerson + ")";
		}
		System.out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);

		// step 3.1 Accept json
		url = getBaseURI() + "/person";

		obj = new URL(url);
		con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		accept = "application/json";
		con.setRequestProperty("Accept", accept);
		contentType = "application/json";
		con.setRequestProperty("Content-Type", contentType);

		System.out.println("Request #1: GET " + url + " Accept: " + accept
				+ "Content-type: " + contentType);
		// responseCode = con.getResponseCode();
		// System.out.println("Response Code : " + responseCode);

		resp = MyClient.getConnectionOutputJSON(con);

		counterPerson = MyClient.countSubStringOccur(resp, "name");
		if (counterPerson < 3) {
			result = "ERROR (less than 3 persons)";
		} else {
			result = "OK (#person=" + counterPerson + ")";
		}
		System.out.println("=> Result: " + result);
		responseCode = con.getResponseCode();
		System.out.println("=> HTTP Status: " + responseCode);
		System.out.println(resp);
	}

	// private static void request2() throws IOException, JAXBException,
	// SAXException, TransformerException, ParserConfigurationException {
	// // to do
	// }

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
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
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
}
