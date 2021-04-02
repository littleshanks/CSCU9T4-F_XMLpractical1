import java.io.*;               // import input-output
import java.text.DecimalFormat;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;         // import parsers
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.*;           // import XPath
import javax.xml.validation.*;      // import validators
import javax.xml.transform.*;       // import DOM source classes

//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import org.w3c.dom.*;               // import DOM

/**
  DOM handler to read XML information, to create this, and to print it.

  @author   CSCU9T4, University of Stirling
  @version  11/03/20
*/
public class DOMMenu {

  /** Document builder */
  private static DocumentBuilder builder = null;

  /** XML document */
  private static Document document = null;

  /** XPath expression */
  private static XPath path = null;

  /** XML Schema for validation */
  private static Schema schema = null;

  /*----------------------------- General Methods ----------------------------*/

  /**
    Main program to call DOM parser.

    @param args         command-line arguments
  */
  public static void main(String[] args)  {
    // load XML file into "document"
    loadDocument(args[0]);
    validateDocument(args[1]);
    // print staff.xml using DOM methods and XPath queries
    printNodes();   
  
   
  }

  /**
    Set global document by reading the given file.

    @param filename     XML file to read
  */
  private static void loadDocument(String filename) {
    try {
      // create a document builder
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builder = builderFactory.newDocumentBuilder();

      // create an XPath expression
      XPathFactory xpathFactory = XPathFactory.newInstance();
      path = xpathFactory.newXPath();

      // parse the document for later searching
      document = builder.parse(new File(filename));
    }
    catch (Exception exception) {
      System.err.println("could not load document " + exception);
    }
  }

  /*-------------------------- DOM and XPath Methods -------------------------*/
  /**
   Validate the document given a schema file
   @param filename XSD file to read
  */
  private static Boolean validateDocument(String filename)  {
    try {
      String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      SchemaFactory factory = SchemaFactory.newInstance(language);
      schema = factory.newSchema(new File(filename));
      Validator validator = schema.newValidator();
      validator.validate(new DOMSource(document));
      return true;
    } catch (Exception e){
      e.printStackTrace();
      return false;
    }
  }
  /**
    Print nodes using DOM methods and XPath queries.
  */
  private static void printNodes() {
    
	NodeList nodeList = document.getElementsByTagName("item");
	
	for (int i = 0; i < nodeList.getLength(); i++) {
		Node menuItem = nodeList.item(i);
		if (menuItem.getNodeType() == Node.ELEMENT_NODE)
		{
			Element eElement = (Element) menuItem;
			String itemName = String.format("%-15s", eElement.getElementsByTagName("name").item(0).getTextContent());
			String itemPrice = String.format("£%-10s",eElement.getElementsByTagName("price").item(0).getTextContent());
			String itemDescription = String.format("%-60s",eElement.getElementsByTagName("description").item(0).getTextContent());
			
			String menuLine = itemName + itemPrice + itemDescription;
					
			System.out.println(menuLine);
		}
	}
	System.out.println("\n\nCombos");
	
	String SaladName = query("//item[2]/name");
	double SaladPrice = Double.parseDouble(query("//item[2]/price"));
	
	String SoupName = query("//item[3]/name");
	double SoupPrice = Double.parseDouble(query("//item[3]/price"));
	
	String BurgerName = query("//item[4]/name");
	double BurgerPrice = Double.parseDouble(query("//item[4]/price"));
	
	String PastaName = query("//item[6]/name");
	double PastaPrice = Double.parseDouble(query("//item[6]/price"));
	
	DecimalFormat df = new DecimalFormat("#.##");
	System.out.println(String.format("%-26s", (SaladName + " + " + BurgerName)) + "£" + df.format(SaladPrice + BurgerPrice));
	System.out.println(String.format("%-26s", (SoupName + " + " + BurgerName)) + "£" + df.format(SoupPrice + BurgerPrice));
	System.out.println(String.format("%-26s", (SaladName + " + " + PastaName)) + "£" + df.format(SaladPrice + PastaPrice));
	
  }

  /**
    Get result of XPath query.

    @param query        XPath query
    @return         result of query
  */
  private static String query(String query) {
    String result = "";
    try {
      result = path.evaluate(query, document);
    }
    catch (Exception exception) {
      System.err.println("could not perform query - " + exception);
    }
    return(result);
  }
}
