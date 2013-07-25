package com.imaginea.gerritPlugin.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

public class FileDataRetrivalServiceTest {

	@Test
	public void testWithNullURLReferences() {
		
		/**
		 * TEST CASE :IllegalArgumentException is expected when a 'null' URL is passed.
		 */
		try {
			FileDataRetrivalService.getFileDataStream((URL)null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		/**
		 * TEST CASE :IOException is expected when an invalid URL is passed.
		 */
		try {
			FileDataRetrivalService.getFileDataStream(new URL(null));
			fail();
		} catch (IOException e) {
			assertTrue(true);
		}

	}

	@Test
	public void testWithInvalidURLReferences() {
		/**
		 * TEST CASE :IOException is expected when an invalid URL is passed.
		 */
		try {
			FileDataRetrivalService.getFileDataStream(new URL(""));
			fail();
		} catch (IOException e) {
			assertTrue(true);
		}

		/**
		 * TEST CASE :IOException is expected when an invalid URL is passed.
		 */
		try {
			FileDataRetrivalService.getFileDataStream(new URL("*"));
			fail();
		} catch (IOException e) {
			assertTrue(true);
		}

		/**
		 * TEST CASE :IOException is expected when an invalid URL is passed.
		 */
		try {
			FileDataRetrivalService.getFileDataStream(new URL("*"));
			fail();
		} catch (IOException e) {
			assertTrue(true);
		}

	}

	@Test
	public void testWithValidURLReferencesButInvalidZIPReferences() {

		/**
		 * TEST CASE :IOException is expected when an valid URL is passed which doesn't have a ZIP.
		 */
		try {
			String out = FileDataRetrivalService.getFileDataStream(new URL("https://www.facebook.com/"));
			assertTrue("On passing a valid url with that doesn't have ZIP returns null", out != null);
		} catch (IOException e) {
			fail();
		}

		/**
		 * TEST CASE :IOException is expected when an valid URL is passed which does have a ZIP that contain multiple entries.
		 */
		try {
			FileDataRetrivalService.getFileDataStream(new URL("http://www.vbaccelerator.com/home/VB/Code/Libraries/Windows/Creating_New_Desktops/VB6_New_Desktop_Sample.zip"));
		} catch (IOException e) {
			assertTrue(true);
		}
	}

}
