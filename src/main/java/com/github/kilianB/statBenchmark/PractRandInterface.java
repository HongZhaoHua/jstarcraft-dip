package com.github.kilianB.statBenchmark;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.kilianB.pcg.fast.PcgRSFast;

public class PractRandInterface {

	public static void main(String[] args) throws IOException {

		/*
		 * Settings
		 */

		PcgRSFast rngToTest = new PcgRSFast(0, 0);

		// Pract rand settings

		int bitsPerData = 32; // Integer are 32 bits
		int bytesToTest = 36; // 2^n -> 2^36 bytes = 64 gb data
		int transformFolding = 2; // Transform the input for additional statistical tests

		File practRandExecutable = new File("benchmark/win_binary/PractRand_094/RNG_test");

		// Terminal command on windows -> Output | RNG_test [Parameters]
		ProcessBuilder p = new ProcessBuilder(practRandExecutable.getAbsolutePath().toString(), "stdin" + bitsPerData, "-a", "-tf", Integer.toString(transformFolding), "-tlmax", Integer.toString(bytesToTest));

		Process practRandProcess = p.start();

		// Handle the output returned by the test
		Thread t = new Thread(() -> {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(practRandProcess.getInputStream()))) {
				String line;
				while ((line = br.readLine()) != null) {

					// Print it to the console ... and additionally to a file?
					System.out.println(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		t.start();

		// Pipe input data to the process
		DataOutputStream out = new DataOutputStream(practRandProcess.getOutputStream());

		try {
			// Shall we trust brench prediction? We should but pull if outside
			// of the loop just in case ...
			if (bitsPerData == 64) {
				while (true) {
					out.writeLong(rngToTest.nextLong());
				}
			} else if (bitsPerData == 32) {
				while (true) {
					out.writeInt(rngToTest.nextInt());
				}
			} else if (bitsPerData == 16) {
				while (true) {
					// Widening cast will take place followed by bitshift
					out.writeShort(rngToTest.nextShort());
				}
			} else if (bitsPerData == 8) {
				while (true) {
					// Same out.write takes an int...
					out.write(rngToTest.nextByte());
				}
			}
		} catch (IOException io) {
			// If practrand collected enough data it will close the pipe on it's side.
			// Ignore this error and cease execution
			if (!io.getMessage().contains("The pipe has been ended")) {
				io.printStackTrace();
			}
		}

		// Should run 30 min ...
	}

}
