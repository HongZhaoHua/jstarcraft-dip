package com.github.kilianB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;

public class NetworkUtil {
	/**
	 * Resolves the first found link local address of the current machine
	 * 192.168.xxx.xxx. This is a fix for
	 * <code>InetAddress.getLocalHost().getHostAddress();</code> which in some cases
	 * will resolve to a loopback address (127.0.01).
	 * <p>
	 * Site local addresses 192.168.xxx.xxx are available inside the same network.
	 * Same counts for 10.xxx.xxx.xxx addresses, and 172.16.xxx.xxx through
	 * 172.31.xxx.xxx
	 * <p>
	 * Link local addresses 169.254.xxx.xxx are for a single network segment
	 * <p>
	 * Addresses in the range 224.xxx.xxx.xxx through 239.xxx.xxx.xxx are multicast
	 * addresses.
	 * <p>
	 * Broadcast address 255.255.255.255.
	 * <p>
	 * Loopback addresses 127.xxx.xxx.xxx
	 * 
	 * @return link local address of the machine or InetAddress.getLocalHost() if no
	 *         address can be found.
	 * @throws IOException if address can not be resolved
	 * @since 1.0.0
	 */
	public static InetAddress resolveSiteLocalAddress() throws IOException {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

		while (interfaces.hasMoreElements()) {
			NetworkInterface curInterface = interfaces.nextElement();
			Enumeration<InetAddress> addresses = curInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress curInetAddress = addresses.nextElement();
				if (curInetAddress.isSiteLocalAddress()) {
					return curInetAddress;
				}
			}
		}
		return InetAddress.getLocalHost();
	}

	/**
	 * Resolves the public facing ip address of this network. The public ip address
	 * is not the ip address assigned by the router, but rather the ip address
	 * exposed during web requests. External hosts trying to access this device will
	 * use this ip.
	 * 
	 * @return the public ip address of this network.
	 * @throws IOException if the ip can not be resolved
	 * @since 1.5.9
	 */
	public static InetAddress resolvePublicAddress() throws IOException {

		// Java has no means to resolve the address itself. Utilize web services instead
		try {
			return InetAddress.getByName(readLineFromURL("http://checkip.amazonaws.com"));
		} catch (Exception e) {
		}
		// amazon failed fallback to a different service
		try {
			return InetAddress.getByName(readLineFromURL("https://api.ipify.org"));
		} catch (Exception e) {
			throw new IOException("Could not resolve ip address. Check internet connection ", e);
		}
	}

	/**
	 * Read a single line from a given url
	 * 
	 * @param url the url to read a line from
	 * @return the content of the first line as returned by the url
	 * @throws IOException if an io error occurs
	 * @since 1.5.9
	 */
	private static String readLineFromURL(String url) throws IOException {
		URL urlU = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlU.openStream()));

		String line = in.readLine(); // you get the IP as a String
		in.close();
		return line;
	}

	/**
	 * Collect all content available in the reader and return it as a string
	 * 
	 * @param br Buffered Reader input source
	 * @return Content of the reader as string
	 * @throws IOException Exception thrown during read operation.
	 * @since 1.0.0
	 */
	public static String dumpReader(BufferedReader br) throws IOException {
		StringBuilder response = new StringBuilder();
		String temp;
		while ((temp = br.readLine()) != null) {
			response.append(temp).append(System.lineSeparator());
		}
		return response.toString();
	}

	/**
	 * Read all the content available to be read by the socket and return the
	 * content as a String. The socket is closed after reading is done.
	 * 
	 * @param s The socket
	 * @return the content of the sockets input stream as string
	 * @throws IOException if an IOError occurs
	 * @since 1.0.0
	 */
	public static String collectSocketAndClose(Socket s) throws IOException {
		String result = collectSocket(s);
		s.close();
		return result;
	}

	/**
	 * Read all the content available to be read by the socket and return the
	 * content as a String.
	 * 
	 * @param s The socket
	 * @return the content of the sockets input stream as string
	 * @throws IOException if an IOError occurs
	 * @since 1.0.0
	 */
	public static String collectSocket(Socket s) throws IOException {

		InputStream is = s.getInputStream();

		StringBuilder sb = new StringBuilder();

		int b;

		while (is.available() > 0) {
			b = is.read();
			sb.append((char) b);
		}
		return sb.toString();
	}

	/**
	 * Read all the content available to be read by the socket and return the
	 * content as a String.
	 * 
	 * @param s         The socket
	 * @param msTimeout the time elapsed after the last character is read.
	 * @return the content of the sockets input stream as string
	 * @throws IOException if an IOError occurs
	 * @since 1.0.0
	 */
	public static String collectSocket(Socket s, int msTimeout) throws IOException {

		int oldTimeout = s.getSoTimeout();

		s.setSoTimeout(msTimeout);

		InputStream is = s.getInputStream();

		StringBuilder sb = new StringBuilder();

		try {
			while (!s.isClosed()) {
				sb.append((char) is.read());
			}
			// TODO expensive each read an exception is thrown
		} catch (java.net.SocketTimeoutException io) {
		}

		s.setSoTimeout(oldTimeout);

		return sb.toString();
	}

}