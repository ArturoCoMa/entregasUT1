package org.example;/*1 - Programa que se ejecute de la forma

    java programa ficheroTextoOrigen ficheroTextoDestino numLinea

de tal forma que añada al fichero de destino, manteniendo el contenido anterior, la línea indicada como último parámetro.*/

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ej1{
	public static void main(String[] args){
		if(args.length==3){
			Path rutaOrigen = FileSystems.getDefault().getPath(args[0]); 
			Path rutaDestino = FileSystems.getDefault().getPath(args[1]);
			int numLinea = (Integer.parseInt(args[2]))-1;

			List<String> lista = null;
			try{
				lista = Files.readAllLines(rutaOrigen, StandardCharsets.UTF_8);
				Files.writeString(rutaDestino,"\n"+lista.get(numLinea),StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			}catch(IOException e){
				throw new RuntimeException(e);
			}
			
		}else{
			System.out.println("La forma de uso conrrecta es:\njava programa ficheroTextoOrigen ficheroTextoDestino numLinea");
		}
	}
}