package controlador;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.bson.Document;
import org.hibernate.Query;
import org.hibernate.Session;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import modelo.Vuelo;

public class MongoManager {

	static Scanner teclado;

	public MongoManager(Scanner sc) {
		teclado = sc;
	}

	public void ejecucion() {

		String codigo, asiento, dni, apellido, nombre, dniP, tarjeta, codV, newDni;

		System.out.println("Prueba conexión MongoDB");
		MongoClient mongo = crearConexion();

		System.out.println("-----------------------------------------");
		System.out.println("Menú BD");
		System.out.println("0. Salir de la aplicación");
		System.out.println("1. COMPRAR VUELO");
		System.out.println("2. CANCELAR COMPRAVUELO");
		System.out.println("3. MODIFICAR VUELO COMPRADO");
		System.out.println("-----------------------------------------");

		int num = teclado.nextInt();

		if (mongo != null) {

			switch (num) {

			case 0:

				break;

			case 1: {

				mostrar(mongo);

				System.out.println("Introduzca el codigo de vuelo a comprar: ");
				codigo = teclado.next();
				System.out.println("Introduzca el asiento: ");
				asiento = teclado.next();
				System.out.println("Introduzca su DNI: ");
				dni = teclado.next();
				System.out.println("Introduzca su apellido: ");
				apellido = teclado.next();
				System.out.println("Introduzca su nombre: ");
				nombre = teclado.next();
				System.out.println("Introduzca el DNI del pagador: ");
				dniP = teclado.next();
				System.out.println("Introduzca su tarjeta: ");
				tarjeta = teclado.next();

				comprar(mongo, codigo, asiento, dni, apellido, nombre, dniP, tarjeta);

				break;

			}

			case 2: {

				mostrar(mongo);

				System.out.println("Introduzca el codigo del vuelo: ");
				codigo = teclado.next();
				System.out.println("Introduzca su DNI: ");
				dni = teclado.next();
				System.out.println("Introduzca su codigo de venta: ");
				codV = teclado.next();

				borrar(mongo, codigo, dni, codV);

				break;

			}

			case 3: {

				System.out.println("Introduzca el codigo del vuelo: ");
				codigo = teclado.next();
				System.out.println("Introduzca su DNI: ");
				dni = teclado.next();
				System.out.println("Introduzca su codigo de venta: ");
				codV = teclado.next();

				System.out.println("Introduzca el asiento: ");
				asiento = teclado.next();
				System.out.println("Introduzca su DNI: ");
				newDni = teclado.next();
				System.out.println("Introduzca su apellido: ");
				apellido = teclado.next();
				System.out.println("Introduzca su nombre: ");
				nombre = teclado.next();
				System.out.println("Introduzca el DNI del pagador: ");
				dniP = teclado.next();
				System.out.println("Introduzca su tarjeta: ");
				tarjeta = teclado.next();

				modificar(mongo, codigo, dni, codV, newDni, asiento, apellido, nombre, dniP, tarjeta);

				break;

			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + num);
			}

		} else {
			System.out.println("Error: Conexión no establecida");
		}
	}

	/**
	 * Clase para crear una conexión a MongoDB.
	 * 
	 * @return MongoClient conexión
	 */
	private static MongoClient crearConexion() {
		MongoClient mongo = null;
		mongo = new MongoClient("localhost", 27017);

		return mongo;
	}

	private static void mostrar(MongoClient mongo) {
        MongoDatabase db = mongo.getDatabase("vuelos2_0");

        MongoCollection coleccionVuelos = db.getCollection("vuelos");

        FindIterable fi = coleccionVuelos.find();

        MongoCursor cur = fi.cursor();

        while (cur.hasNext()) {
            Document obj = (Document) cur.next();
            String vuelos = "Codigo: " + obj.get("codigo") + " | Origen: " + obj.get("origen") + " | Destino: "
                    + obj.get("destino") + " | Fecha: " + obj.get("fecha") + " | Hora: " + obj.get("hora")
                    + " | Plazas totales: " + obj.get("plazas_totales") + " | Plazas disponibles: "
                    + obj.get("plazas_disponibles");
            System.out.println(vuelos);
        }
        System.out.println();
    }

	public static void comprar(MongoClient mongo, String codigo, String asiento, String dni, String apellido,
            String nombre, String dniP, String tarjeta) {

        MongoDatabase db = mongo.getDatabase("vuelos2_0");

        MongoCollection coleccionVuelos = db.getCollection("vuelos");

        int restarPlazas = -1;

        int plazasDisponibles;

        FindIterable fi = coleccionVuelos.find();

        MongoCursor cur = fi.cursor();

        Document pd = (Document) cur.next();
        plazasDisponibles = pd.getInteger("plazas_disponibles");

        String codV = codAleatorio().toUpperCase();

        Document quienCambio = new Document("codigo", codigo);
        Document cambios = new Document();

        cambios.append("asiento", asiento);
        cambios.append("dni", dni);
        cambios.append("apellido", apellido);
        cambios.append("nombre", nombre);
        cambios.append("dniPagador", dniP);
        cambios.append("tarjeta", tarjeta);
        cambios.append("codigoVenta", codV);

        System.out.println("Su codigo de venta es: " + codV);

        Document auxSet1 = new Document("vendidos", cambios);
        Document auxSet2 = new Document("plazas_disponibles", restarPlazas);
        Document auxSet3 = new Document("$push", auxSet1);
        Document auxSet4 = new Document("$inc", auxSet2);

        if (plazasDisponibles == 0) {
            System.out.println("No hay plazas disponibles en estos momentos");
        } else {
            coleccionVuelos.updateOne(quienCambio, auxSet3);
            coleccionVuelos.updateOne(quienCambio, auxSet4);
        }

        System.out.println("Billete comprado con exito");

    }

	public static void borrar(MongoClient mongo, String codigo, String dni, String codV) {

		MongoDatabase db = mongo.getDatabase("vuelos2_0");

		MongoCollection coleccionVuelos = db.getCollection("vuelos");

		int plazasDisponibles = +1;

		Document quienCambio = new Document("codigo", codigo);
		Document cambios = new Document();

		cambios.append("dni", dni);
		cambios.append("codigoVenta", codV);

		Document auxSet1 = new Document("vendidos", cambios);
		Document auxSet2 = new Document("plazas_disponibles", plazasDisponibles);
		Document auxSet3 = new Document("$pull", auxSet1);
		Document auxSet4 = new Document("$inc", auxSet2);

		coleccionVuelos.updateOne(quienCambio, auxSet3);
		coleccionVuelos.updateOne(quienCambio, auxSet4);

		System.out.println("Billete cancelado con exito");

	}

	public static void modificar(MongoClient mongo, String codigo, String dni, String codV, String newDni,
			String asiento, String apellido, String nombre, String dniP, String tarjeta) {

		MongoDatabase db = mongo.getDatabase("vuelos2_0");

		MongoCollection coleccionVuelos = db.getCollection("vuelos");

		Document quienCambio = new Document("codigo", codigo);
		Document cambios = new Document();

		cambios.append("dni", dni);
		cambios.append("codigoVenta", codV);
		
		Document auxSet1 = new Document("vendidos", cambios);
		Document auxSet2 = new Document("$pull", auxSet1);
		coleccionVuelos.updateOne(quienCambio, auxSet2);
		
		Document cambios2 = new Document();
		cambios2.append("asiento", asiento);
		cambios2.append("dni", newDni);
		cambios2.append("apellido", apellido);
		cambios2.append("nombre", nombre);
		cambios2.append("dniPagador", dniP);
		cambios2.append("tarjeta", tarjeta);
		cambios2.append("codigoVenta", codAleatorio().toUpperCase());
		
		System.out.println("Su nuevo codigo de venta es: " + codV);

		Document quienCambio2 = new Document("codigo", codigo);
		Document auxSet3 = new Document("vendidos", cambios2);
		Document auxSet4 = new Document("$push", auxSet3);
		coleccionVuelos.updateOne(quienCambio2, auxSet4);

		System.out.println("Billete modificado con exito");

	}

	public static String codAleatorio() {
		String zeros = "000000000";
		Random rnd = new Random();
		String s = Integer.toString(rnd.nextInt(0X1000000), 16);
		s = zeros.substring(s.length()) + s;
		return s;
	}
}
