import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.io.File.*;

public class ej3{
    public static int tamString = 20;
    public static int tam = (4+(tamString*2)+8);
	public static Videojuego videojuego;
	public static ArrayList<Videojuego> arrayVideojuegos = new ArrayList<>();
	public static File file = new File("regJuegos.dat");

    public static void main(String[] args){
    	Element nodoRaiz;
    	Element nodoJuego;
    	Element nodoDato;
    	Text text;

    	DocumentBuilder db;
    	Document doc;

        System.out.println("Generando archivo XML...");

    	try{
    		db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    		doc = db.getDOMImplementation().createDocument(null, "xml", null);
    	}catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }


        //empezamos a "escribir" el xml, definiendo sus elementos y su estructura
        nodoRaiz = doc.createElement("videojuegos");
        doc.getDocumentElement().appendChild(nodoRaiz);

    	creaArrayJuegos();
    	for(Videojuego objetoJuego : arrayVideojuegos){
    		nodoJuego = doc.createElement("videojuego");
    		nodoRaiz.appendChild(nodoJuego);

    		nodoDato = doc.createElement("id");
    		nodoJuego.appendChild(nodoDato);
    		text = doc.createTextNode(String.valueOf(objetoJuego.getId()));
    		nodoDato.appendChild(text);

    		nodoDato = doc.createElement("nombre");
    		nodoJuego.appendChild(nodoDato);
    		text = doc.createTextNode(String.valueOf(objetoJuego.getNom()));
    		nodoDato.appendChild(text);

    		nodoDato = doc.createElement("precio");
    		nodoJuego.appendChild(nodoDato);
    		text = doc.createTextNode(String.valueOf(objetoJuego.getPrecio()));
    		nodoDato.appendChild(text);
    	}

    	Source source = new DOMSource(doc);
    	Result result = new StreamResult(new File("regJuegos.xml"));
    	Transformer trans;
    	try{
    		trans = TransformerFactory.newInstance().newTransformer();
    		trans.setOutputProperty(OutputKeys.INDENT,"yes");
    		trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    		trans.transform(source,result);
    	} catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Archivo XML generado con éxito.");
    }

    //sacamos los datos del RAF y los metemos en una array, para luego crear con esos datos el xml
    public static void creaArrayJuegos(){
        int id;
        String nombre = "";
        double precio;
        int posicion = 0;

        if(file.exists()){
            try(
                RandomAccessFile raf = new RandomAccessFile(file,"r");
            ){
                //nos colocamos al principio
                raf.seek(0);
                while(posicion<1000){
                    //leemos el id
                    id = raf.readInt();
                    //si existe, lo escribimos, si no existe (id==0) lo pasamos por alto sin escribir
                    if(id!=0){
                        //el readChar lee caracter a caracter así que para leerlo todo usamos un bucle
                    	nombre = raf.readUTF();
                        precio = raf.readDouble();
                        videojuego = new Videojuego(id, nombre, precio);
                        arrayVideojuegos.add(videojuego);
                        posicion++;
                        raf.seek(tam*posicion);
                    }else{
                        posicion++;
                        raf.seek(tam*posicion);
                    }
                }

            }catch(EOFException o){
                //no ponemos nada para que no salte el null ni deje una linea en blanco extra.
            }catch(IOException e){
                System.err.println(e.getMessage());
            }
        }
    }

}
