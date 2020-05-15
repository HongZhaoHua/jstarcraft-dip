package com.github.kilianB;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

/**
 * Utility methods not belonging in any other specific category.
 * 
 * @author Kilian
 *
 */
public class MiscUtil {

	/**
	 * Retrieve the OS type this program is executed on.
	 * 
	 * <dl>
	 * <dt>os.arch x86_64
	 * <dd>Operating System Architecture
	 * <dt>os.version 10.12.6
	 * <dd>Operating System Version
	 * </dl>
	 * 
	 * @author Kilian
	 * @since 1.0.0
	 */
	public enum OS {
	    // lower case identifier
		WINDOWS("windows"), LINUX("linux"), SOLARIS("sunos", "solaris"), MAC_OS("darwin", "mac", "macos", "mac os x"), UNKNOWN("generic"), OTHER(""); // Will always return true

		/**
		 * Return the OS type this program is executed on
		 * 
		 * @return the OS used to launch this JVM
		 */
		public static OS getOS() {
			return currentOS == null ? retrieveInfo() : currentOS;
		}

		public String toString() {
			String osPretty = currentOS.name().toLowerCase().replace("_", " ");
			osPretty = osPretty.substring(0, 1).toUpperCase() + osPretty.substring(1);

			int index = 0;
			while ((index = osPretty.indexOf(" ", index + 1)) != -1) {
				osPretty = osPretty.substring(0, 1).toUpperCase() + osPretty.substring(1);
			}
			return osPretty + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version");
		}

		private static OS retrieveInfo() {
			String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH); // turkey
			for (OS os : OS.values()) {
				for (String identifier : os.osIdentifier) {
					if (osName.contains(identifier)) {
						currentOS = os;
						return currentOS;
					}
				}
			}
			// Never happens
			return OTHER;
		}

		/**
		 * 
		 */
		private static OS currentOS = null;

		private String[] osIdentifier;

		private OS(String... haystack) {
			osIdentifier = haystack;
		}
	}

	/***
	 * <p>
	 * Restart the java application by registering a shutdown hook at the very end
	 * of the life cycle. Tested under JRE 8 Windows 10.
	 * 
	 * <p style="color:red;">
	 * <b>This method should be used with caution and testing is needed to be done
	 * before relying on it's functionality!. JVM specific security managers, and
	 * overall behaviour might impact this method</b>
	 * </p>
	 * 
	 * 
	 * <p>
	 * This method "injects" it's own runnable at the very end of the lifetime of
	 * the program after all shutdown hooks have been executed by accessing non
	 * private methods via reflection. This might break if the implementation of
	 * java will change therefore again be cautious!
	 * 
	 * Guard yourself against constant restarting due to uninitialized conditions.
	 * 
	 * @since 1.0.0
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void restartApp() {

		Runnable restartApp = new Runnable() {
			// Runnable mostly from here
			// http://lewisleo.blogspot.de/2012/08/programmatically-restart-java.html
			@Override
			public void run() {
				// java binary
				String java = System.getProperty("java.home") + "/bin/java";
				// vm arguments
				List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
				StringBuffer vmArgsOneLine = new StringBuffer();
				for (String arg : vmArguments) {
					// if it's the agent argument : we ignore it otherwise the
					// address of the old application and the new one will be in conflict
					if (!arg.contains("-agentlib")) {
						vmArgsOneLine.append(arg);
						vmArgsOneLine.append(" ");
					}
				}
				// init the command to execute, add the vm args
				final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);
				// program main and program arguments (be careful a sun property. might not be
				// supported by all JVM)
				String[] mainCommand = System.getProperty("sun.java.command").split(" ");
				// program main is a jar
				if (mainCommand[0].endsWith(".jar")) {
					// if it's a jar, add -jar mainJar
					cmd.append("-jar " + new File(mainCommand[0]).getPath());
				} else {
					// else it's a .class, add the classpath and mainClass
					cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
				}
				// finally add program arguments
				for (int i = 1; i < mainCommand.length; i++) {
					cmd.append(" ");
					cmd.append(mainCommand[i]);
				}
				try {
					Runtime.getRuntime().exec(cmd.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		// Modified heavily from here

		/*
		 * We want all shutdown hooks to finish before re starting our application.
		 * Registering a normal shutdown hook is out of question. Give it the lowest
		 * priority
		 * 
		 * (0) Console restore hook (1) Application hooks (2) DeleteOnExit hook
		 * 
		 * so far I only came across the 3 slots being used. If we register a slot which
		 * is supposed to be used by another java feature we are in trouble so be
		 * careful!.
		 */

		// Register low priority shutdown hook.
		try {
			Class shutdownClass = Class.forName("java.lang.Shutdown");

			// Do other classes
			Method registerShutdownSlot = shutdownClass.getDeclaredMethod("add", Integer.TYPE, Boolean.TYPE, Runnable.class);

			// The JVM has 10 shutdown hook priorities Maybe start from 10 and work our way
			// up?
			for (int i = 3; i < 10; i++) {
				try {
					registerShutdownSlot.setAccessible(true);
					registerShutdownSlot.invoke(null, i, false, restartApp);
					break;
				} catch (IllegalStateException e) {
				} finally {
					registerShutdownSlot.setAccessible(false);
				}
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Error registering shutdown hook " + e.getMessage() + e);
		}
		// Invoke shutdown hooks
		System.exit(0);
	}

	/**
	 * Compute a consistent hashcode for enum values
	 * 
	 * @param e the enum to compute the hashcode for
	 * @return the hashcode
	 */
	public static int consistentHashCode(@SuppressWarnings("rawtypes") Enum e) {
		return e.name().hashCode() * 31 + e.ordinal() ^ e.getClass().getName().hashCode();

	}
}
