package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ejLeeXML{
	public static void main(String[] args){
		File archivoLectura = new File ("regJuegos.xml");
		DocumentBuilder db = null;
		Document doc = null;
		Element ele;
		Node nodo;
		try{
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(archivoLectura);
		} catch (ParserConfigurationException | IOException | SAXException  e) {
            throw new RuntimeException(e);
        }

        NodeList list = doc.getElementsByTagName("videojuego");
        //for(Node nodo : list){
        for(int i=0 ; i<list.getLength() ; i++){
        	nodo = list.item(i);
        	ele = (Element) nodo;
        	System.out.println("ID: "+ele.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());
        	System.out.println("NOMBRE: "+ele.getElementsByTagName("nombre").item(0).getChildNodes().item(0).getNodeValue());
        	System.out.println("PRECIO: "+ele.getElementsByTagName("precio").item(0).getChildNodes().item(0).getNodeValue());
        }
	}
}