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

class Server {
	private static Socket connectionSocket;
	private static BufferedReader inFromClient;
	private static DataOutputStream outToClient;

	public static void main(String argv[]) throws Exception {

		/* Cria socket do servidor */
		ServerSocket welcomeSocket = new ServerSocket(6790);

		while (true) {
			createClientConnection(welcomeSocket);
			String echo = getMessage();
			boolean test = testMessage(echo);
			answerClient(test);
			createLocalFileandWriteContent(echo);

		}
	}

	private static void createLocalFileandWriteContent(String echo) throws IOException {
		File yourFile = new File("score.txt");
		yourFile.createNewFile(); // if file already exists will do nothing
		FileOutputStream oFile = new FileOutputStream(yourFile, false);

		try (FileWriter writer = new FileWriter(yourFile); BufferedWriter bw = new BufferedWriter(writer)) {

			bw.write(echo);

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

	}

	private static void answerClient(boolean test) throws IOException {
		String answer;
		if (test == true) {
			answer = "texto recebido corretamente";
			System.out.println(answer);
		} else {
			answer = "texto NÃO recebido corretamente";
			System.out.println(answer);
		}

		answer = answer + '\n';
		/* Envia mensagem para o cliente */
		outToClient.writeBytes(answer);

		/* Encerra socket do cliente */
		connectionSocket.close();

	}

	private static boolean testMessage(String echo) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("texto.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			String everything = sb.toString();
			everything = everything + '\n';
			if (everything.contentEquals(echo)) {
				return true;
			} else {
				return false;
			}

		} finally {
			br.close();
		}

	}

	private static String getMessage() throws IOException {
		/*
		 * Aguarda o envio de uma mensagem do cliente. Esta mensagem deve ser terminada
		 * em \n ou \r Neste exemplo espera-se que a mensagem seja textual (string).
		 * Para ler dados não textuais tente a chamada read()
		 */
		String clientSentence = inFromClient.readLine();

		/* Determina o IP e Porta de origem */
		InetAddress IPAddress = connectionSocket.getInetAddress();
		int port = connectionSocket.getPort();

		/* Exibe, IP:port => msg */
		System.out.println(IPAddress.getHostAddress() + ":" + port + " => " + clientSentence);

		/* Adiciona o \n para que o cliente também possa ler usando readLine() */
		return clientSentence + '\n';

	}

	private static void createClientConnection(ServerSocket welcomeSocket) throws IOException {
		/*
		 * Aguarda o recebimento de uma conexão. O servidor fica aguardando neste ponto
		 * até que nova conexão seja aceita.
		 */
		connectionSocket = welcomeSocket.accept();

		/* Cria uma stream de entrada para receber os dados do cliente */
		inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

		/* Cria uma stream de saída para enviar dados para o cliente */
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());

	}
}