package com.imaginea.gerritPlugin.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

public class FileDataRetrivalService {

	private static org.apache.log4j.Logger log = Logger.getLogger(FileDataRetrivalService.class);
	
	final private static int BUFFER_SIZE = 1024;

	public static String getFileDataStream(String url)
			throws MalformedURLException {
		log.debug("******************************" + url);
		URL urlObject = new URL(url);
		return getFileDataStream(urlObject);
	}

	public static String getFileDataStream(URL url) {

		if (url == null) {
			log.warn("arrgument:'url' cannot be null.");
			throw new IllegalArgumentException(
					"arrgument:'url' cannot be null.");
		}

		ByteArrayOutputStream byteArrayOutputStream = null;
		DataInputStream dis = null;
		ZipInputStream zis = null;
		HttpURLConnection urlConnection = null;
		InputStream inputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			// create the new connection, configure and connect
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.connect();

			inputStream = urlConnection.getInputStream();
			dis = new DataInputStream(new BufferedInputStream(inputStream));
			log.debug("Input connection established.");

			zis = new ZipInputStream(new BufferedInputStream(dis));

			int extracted = 0;
			ZipEntry entry = null;

			while ((entry = zis.getNextEntry()) != null) {

				// at-most one file is expected to present in the '.zip' file
				if (!entry.isDirectory() && extracted <= 1) {
					// modify buffer size according to the requirement
					byte[] buffer = new byte[BUFFER_SIZE];
					int count = 0;

					while ((count = zis.read(buffer)) != -1) {
						// write to stream
						byteArrayOutputStream.write(buffer, 0, count);
					}

					zis.closeEntry();
					extracted++;
				} else {
					String detail = "Unexpected number of entries present in the ZIP file retrieved from the provided Url.";
					log.error("Unexpected number of entries present in the ZIP file retrieved from the provided Url.");
					throw new UnexpectedException(detail);
				}
			}

		} catch (IOException e) {
			log.error(e);
		} finally {
			try {
				zis.close();
				dis.close();
				inputStream.close();
				byteArrayOutputStream.close();
				urlConnection.disconnect();
			} catch (IOException e) {
				log.error(e);
			}
		}

		return byteArrayOutputStream.toString();
	}
}
