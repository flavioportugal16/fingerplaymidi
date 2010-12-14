package com.flat20.fingerplay.config.parsers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TouchOSCParser {

	public TouchOSCParser(File file) {

		Enumeration<ZipEntry> entries;
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(file);

			entries = (Enumeration<ZipEntry>) zipFile.entries();

			while(entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry)entries.nextElement();

				if( entry.isDirectory() ) {
					// Assume directories are stored parents first then children.
					//System.err.println("Extracting directory: " + entry.getName());
					// This is not robust, just for demonstration purposes.
					//(new File(entry.getName())).mkdir();
					//continue;
				}

				System.out.println("Found: " + entry.getName());
				//copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entry.getName())));
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}
	}

	public final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}
}
