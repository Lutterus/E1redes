package TCP;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sumatra
 */

import java.io.*;
import java.net.*;

class Client {
	private static Socket clientSocket;
	private static DataOutputStream outToServer;
	private static BufferedReader inFromServer;

	public static void main(String argv[]) throws Exception {
		createClientConnection();
		String text = readFile();
		sendMessage(text);

	}

	private static void sendMessage(String text) throws IOException {
		/*
		 * Envia para o servidor. Não esquecer do \n no final para permitir que o
		 * servidor use readLine
		 */
		outToServer.writeBytes(text + '\n');

		/* Lê mensagem de resposta do servidor */
		String echo = inFromServer.readLine();

		System.out.println("FROM SERVER: " + echo);

		/* Encerra conexão */
		clientSocket.close();

	}

	private static String readFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("texto.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			String everything = sb.toString();
			System.out.println("arquivo lido");
			return everything;
		} finally {
			br.close();
		}

	}

	private static void createClientConnection() throws UnknownHostException, IOException {
		/* Cria o socket cliente indicando o IP e porta de destino. */
		clientSocket = new Socket("127.0.0.1", 6790);

		/* Cria uma stream de saída para enviar dados para o servidor */
		outToServer = new DataOutputStream(clientSocket.getOutputStream());

		/* Cria uma stream de entrada para receber os dados do servidor */
		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

	}
}