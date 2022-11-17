package org.example;/*Este progrma esta pensado para guardar un máximo de 999 posiciones, más posible duplicados*/

import java.io.*;
import java.util.Scanner;

public class RegVideojuegos{

    public static Scanner tec = new Scanner(System.in);
    public static int tamString = 20;
    public static int tam = (4+(tamString*2)+8); //int+string+double

    public static String registroGuardado = "regJuegos.dat";
    public static String registroDuplicado = "duplicados.dat";

    public static boolean dupEncontrado = false;
    public static String nombreEncontrado;
    public static String nombreEncontradoDup;
    public static int idBuscado;
    public static int idEncontrado;
    public static double precioEncontrado;

    public static int cont;

    public static void main(String[] args){
        File file = new File(registroGuardado);
        if(!file.exists())
            inicio();

        String userInput = "";

        System.out.println("**************\n* BIENVENIDO *\n**************\n");

        while(true){

            //MENU
            System.out.println("\nElige una opción pulsando un número del 0-5\n1- Registrar videojuego.    2- Ver lista videojuegos.\n3- Buscar videojuego.       4- Borrar videojuego\n5- Borrar todos y salir.    0- Salir.");
            userInput = tec.nextLine();

            switch(userInput){
                case "1": registrar(); break;
                case "2": lista(); break;
                case "3": buscar(); break;
                case "4": eliminar(); break;
                case "5": borraTodo(); break;
                case "0": salir(); break;
                default: System.out.println("Opción incorrecta. Pulsa 1, 2, 3, 4, 5, 0 y después pulsa Intro.");
            }
        }
    }

    /**********************************************************************************/
    /*                                    SALIDAS                                     */
    /**********************************************************************************/
    public static void salir(){
        System.out.println("¿Quieres salir? s/n");
        String userInput = tec.nextLine();

        if(userInput.equals("s")){
            System.out.println("\n*********\n* ADIOS *\n*********\n"); 
            System.exit(0);
        }

    }
    public static void borraTodo(){
        System.out.println("ATENCION: ¿Quieres BORRAR todos los datos guardados y salir? s/n");
        String userInput = tec.nextLine();

        if(userInput.equals("s")){
            File guardado = new File(registroGuardado);
            guardado.delete();
            File dup = new File(registroDuplicado);
            dup.delete();
            System.exit(0);
        }

    }

    /**********************************************************************************/
    /*                                    REGISTRAR                                   */
    /**********************************************************************************/
    public static void registrar(){
        int	id = 0;

        //para evitar que puedan meter letras o cualquier cosa y falle el programa
        while(true){
            try{
                System.out.print("\nIntroduce:\n\nID: ");
                id = Integer.parseInt(tec.nextLine());
            }catch(Exception o){
                System.err.print("Cantidad incorrecta, vuelva a insertarla.");
                continue;
            }

            if(id>999){
                System.out.print("Has introducido mas de tres digitos. Por favor vuelve a intentarlo.");
                continue;
            }

            break;
        }

        System.out.print("\nNombre: ");
        String nombre = tec.nextLine();

        double precio = 0;
        //para evitar que puedan meter letras o cualquier cosa y falle el programa
        while(true){
            try{
                System.out.print("\nPrecio: ");
                precio = Double.parseDouble(tec.nextLine());
                break;
            }catch(Exception i){
                System.err.print("Cantidad incorrecta, vuelva a insertarla.");
                continue;
            }
        }


        try(
            RandomAccessFile raf = new RandomAccessFile(registroGuardado,"rw");
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(registroDuplicado,true));
        ){
            raf.seek(tam*(id-1));

            if(id!=raf.readInt()){ //si el registro está libre
                raf.seek(tam*(id-1));
                raf.writeInt(id);

                //limitamos el string del nombre a 10 caracteres
                if(nombre.length()>tamString)
                    raf.writeUTF(nombre.substring(0,tamString));
                else
                    raf.writeUTF(nombre);

                raf.writeDouble(precio);
            }else{	//si esta duplicado
                dos.writeInt(id);
                dos.writeUTF(nombre);
                dos.writeDouble(precio);
            }
        }catch(IOException e){
            System.err.println(e.getMessage());
        }

        System.out.println("-----------------Exito al registrar-----------------\n\n");
    }


    /**********************************************************************************/
    /*                                    LISTA                                       */
    /**********************************************************************************/

    public static void lista(){
        System.out.println("\n--------------------------------------------------");
        File file = new File(registroGuardado);
        File dupFile = new File(registroDuplicado);
        int id;
        String nombre = "";
        double precio;
        int posicion = 1;

        if(file.exists()){
            try(
                RandomAccessFile raf = new RandomAccessFile(file,"rw");
            ){
                //nos colocamos al principio
                raf.seek(0);
                System.out.println("\nLISTA VIDEOJUEGOS\n---------------");

                while(posicion<1000){
                    //leemos el id
                    id = raf.readInt();

                    //si existe, lo escribimos, si no existe (id==0) lo pasamos por alto sin escribir
                    if(id!=0){
                        nombre = raf.readUTF();
                        precio = raf.readDouble();
                        System.out.println("\n   ID: "+id+"\n   Nombre: "+nombre+"\n   Precio: "+precio);
                        //nos vamos a la siguiente posicion.
                        posicion++;
                        raf.seek(tam*(posicion-1));
                    }else{
                        //nos vamos a la siguiente posicion, sin escribir nada
                        posicion++;
                        raf.seek(tam*(posicion-1));
                    }

                }
            }catch(IOException e){
                System.err.println(e.getMessage());
            }


            //duplicados
            if(dupFile.exists()){
	            try(
	                DataInputStream dis = new DataInputStream(new FileInputStream(registroDuplicado));
	            ){
	                System.out.println("\nDUPLICADOS\n--------------");

	                while(true){
	                    System.out.println("\n   ID: "+dis.readInt()+"\n   Nombre: "+dis.readUTF()+"\n   Precio: "+dis.readDouble());
	                }

	            }catch(EOFException e){
	                //no pongo nada para que no salte el null ni deje una linea en blanco.
	            }catch(IOException i){
	                System.err.println(i.getMessage());
	            }
            }

        }
        System.out.println("\n--------------------------------------------------");
    }



    /**********************************************************************************/
    /*                                    BUSCAR                                      */
    /**********************************************************************************/

    public static void buscar(){
    	cont = 0;
        File file = new File(registroGuardado);
        File dupFile = new File(registroDuplicado);

        //para evitar que puedan meter letras o cualquier cosa y falle el programa
        while(true){
            try{
                System.out.print("\nIntroduce:\n\nID (tres digitos): ");
                idBuscado = Integer.parseInt(tec.nextLine());
            }catch(Exception o){
                System.err.print("Cantidad incorrecta, vuelva a insertarla.");
                continue;
            }

            if(idBuscado >999){
                System.out.print("Has introducido mas de tres digitos. Por favor vuelve a intentarlo.");
                continue;
            }

            break;
        }

        idEncontrado = 0;
        nombreEncontrado = "";
        precioEncontrado = 0;
        System.out.println("\nVIDEOJUEGOS ENCONTRADOS CON ESE ID\n---------------------------------");

        if(file.exists()){
        	//buscamos primero en el registro normal
            try(
                RandomAccessFile raf = new RandomAccessFile(registroGuardado,"rw");
            ){
                raf.seek(tam*(idBuscado -1));
                idEncontrado = raf.readInt();
                nombreEncontrado = raf.readUTF();
                precioEncontrado = raf.readDouble();

                if(idEncontrado == 0)
                    System.out.println("Videojuego no encontrado.");
                else{
                	cont++;
                    System.out.println(cont+"------------------------------------------\n   ID: "+ idEncontrado +"\n   Nombre: "+nombreEncontrado+"\n   Precio: "+ precioEncontrado);
                }

            }catch(EOFException o){
                System.out.println("Videojuego no encontrado.");
            }catch(IOException e){
                System.err.println(e.getMessage());
            }

            //busca también en el registro de duplicados.
            if(dupFile.exists()){	            
	            dupEncontrado = true;
	            nombreEncontradoDup = "";
	            try(
	                DataInputStream dis = new DataInputStream(new FileInputStream(registroDuplicado));
	            ){
	                idEncontrado = dis.readInt();
	                nombreEncontradoDup = dis.readUTF();
	                precioEncontrado = dis.readDouble();

	                while(idEncontrado != idBuscado){
	                    idEncontrado = dis.readInt();
	                    nombreEncontradoDup = dis.readUTF();
	                    precioEncontrado = dis.readDouble();
	                }

	            }catch(EOFException i){
	                //recogemos la exception y cambiamos la booleana para que muestre el mensaje adecuado.
	                dupEncontrado = false;
	            }catch(IOException e){
	                System.err.println(e.getMessage());
	            }

	            //mensaje de duplicado encontrado
	            if(dupEncontrado){
	            	cont++;
	                System.out.println(cont+"------------------------------------------\n   ID: "+ idEncontrado +"\n   Nombre: "+nombreEncontradoDup+"\n   Precio: "+ precioEncontrado);
	            }
            }

            	
        }else{
            System.out.println("\nAun no hay registros.\n");
        }

        System.out.println("\n--------------------------------------------------");
    }



    /**********************************************************************************/
    /*                                    ELIMINAR                                    */
    /**********************************************************************************/

    public static void eliminar(){

        buscar();
        if(dupEncontrado){
            System.out.println("¿Cual quieres borrar, el primero (escribe 1) o el duplicado (escibe 2)?");
            int eleccionBorrar = tec.nextInt();
            tec.nextLine();

			//Si elige la opcion 1 borramos en el RAF
            if(eleccionBorrar==1){
	            try(
	            	RandomAccessFile raf = new RandomAccessFile(registroGuardado,"rw");
	            ){
	            	raf.seek(tam*(idBuscado-1));
	                raf.writeInt(0);
                    raf.writeUTF("");
	                raf.writeDouble(0);
	            }catch(IOException e){

	            }

            //Si elige la opción 2 borramos el duplicado
            }else{
            	File escritura = new File("temp.dat");
            	File lectura = new File(registroDuplicado);

	           	try(
	                DataOutputStream dos = new DataOutputStream(new FileOutputStream(escritura,true));
	                DataInputStream dis = new DataInputStream(new FileInputStream(lectura));
	        	){
	        		int id;

	           		while(true){
	           			id = dis.readInt();
	           			if(idBuscado!=id){
				            dos.writeInt(id);
				            dos.writeUTF(dis.readUTF());
				            dos.writeDouble(dis.readDouble());
	           			}else{
	           				dis.readUTF();
	           				dis.readDouble();
	           			}
	           		}

		        }catch(EOFException e){
                    System.err.println(e.getMessage());
                }catch(IOException e){
		            System.err.println(e.getMessage());
		        }finally{
                    lectura.delete();
                    escritura.renameTo(lectura);
                }
            }

        }else{
            System.out.println("¿Quieres borrar este registro? (s/n)");
            String op = tec.nextLine();

            if (op.equals("s")){
                try(
                    RandomAccessFile raf = new RandomAccessFile(registroGuardado,"rw");
                ){
                    raf.seek(tam*(idBuscado -1));
                    raf.writeInt(0);
                    //limitamos el string del nombre a 10 caracteres
                    raf.writeUTF("");
                    raf.writeDouble(0);
                    idEncontrado = raf.readInt();

                }catch(EOFException o){

                }catch(IOException e){

                }

                //busca también en el registro de duplicados.
                dupEncontrado = true;
                nombreEncontradoDup = "";
                try(
                    DataInputStream dis = new DataInputStream(new FileInputStream(registroDuplicado));
                ){
                    idEncontrado = dis.readInt();
                    nombreEncontradoDup = dis.readUTF();
                    precioEncontrado = dis.readDouble();

                    while(idEncontrado != idBuscado){
                        idEncontrado = dis.readInt();
                        nombreEncontradoDup = dis.readUTF();
                        precioEncontrado = dis.readDouble();
                    }

                }catch(EOFException i){
                    //recogemos la exception y cambiamos la booleana para que muestre el mensaje adecuado.
                    dupEncontrado = false;
                }catch(IOException e){
                    System.err.println(e.getMessage());
                }

                if(dupEncontrado)
                    System.out.println("\n   ID: "+ idEncontrado +"\n   Nombre: "+nombreEncontrado+"\n   Precio: "+ precioEncontrado);

            } else if (op.equals("n")) {
                System.out.println("El registro no ha sido borrado.");
            }

        }

    }


    public static void inicio(){
        try(
            RandomAccessFile raf = new RandomAccessFile(registroGuardado,"rw");
        ){
            raf.setLength(1000*tam);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}