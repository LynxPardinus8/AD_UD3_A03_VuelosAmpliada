package controlador;

import java.io.IOException;
import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {

		try {

			System.out.println("Inicio Ejecucion - Gestor de Vuelos");
			Scanner miScanner = new Scanner(System.in); // Para leer las opciones de teclado

			MongoManager miControlador = new MongoManager(miScanner);

			System.out.println("-----------------------------------------");
			System.out.println("Menú Gestor Vuelos");
			System.out.println("0. Salir");
			System.out.println("1. Entrar");
			System.out.println("-----------------------------------------");

			int num = miScanner.nextInt();

			switch (num) {

			case 0:

				break;

			case 1:

				miControlador.ejecucion();

				break;

			default:

				System.out.println("ERROR");

				break;

			}

			System.out.println("\nFin Ejecucion - Gestor Vuelos");
			System.exit(0);

		} catch (Exception e) {

			System.out.println("Se ha producido una excepción, es posible que el fichero de mapeo no esté bien");
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			System.exit(-1);

		}
	}

}
