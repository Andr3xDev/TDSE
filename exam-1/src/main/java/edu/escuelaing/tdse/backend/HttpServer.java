package edu.escuelaing.tdse.backend;

import java.net.*;
import java.io.*;

/**
 * Main class to initializate main backend server.
 */
public class HttpServer {
	public static void main(String[] args) throws IOException, URISyntaxException {
		ServerSocket serverSocket = null;
		String ruta = "src/main/resourses/public/";
		try {
			serverSocket = new ServerSocket(45000);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 45000.");
			System.exit(1);
		}

		Socket clientSocket = null;
		try {
			System.out.println("Listo para recibir ...");
			clientSocket = serverSocket.accept();
			RequestHandler requestHandler = new RequestHandler(clientSocket, ruta);
            requestHandler.requestHandler();
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}
		PrintWriter out = new PrintWriter(
				clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));
		String inputLine, outputLine;
		while ((inputLine = in.readLine()) != null) {
			System.out.println("Recibí: " + inputLine);
			if (!in.ready()) {
				break;
			}
		}
		outputLine = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html\r\n"
				+ "\r\n"
				+ "<!DOCTYPE html>\n"
				+ "<html>\n"
				+ "<head>\n"
				+ "<meta charset=\"UTF-8\">\n"
				+ "<title>Title of the document</title>\n"
				+ "</head>\n"
				+ "<body>\n"
				+ "<h1>Mi propio mensaje</h1>\n"
				+ "</body>\n"
				+ "</html>\n";
		out.println(outputLine);
		out.close();
		in.close();
		clientSocket.close();
		serverSocket.close();
	}
}