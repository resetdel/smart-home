/*
 * This is proprietary work of svitla.com. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to svitla.com.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package com.smart.home.config;

import com.smart.home.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Contains credentials for twitter and nest API.
 */
public class PropertiesHolder {

	private static final Logger LOG = LoggerFactory.getLogger(PropertiesHolder.class);

	private static final String APPLICATION_PROPERTIES = "application.properties";

	private static final Properties properties = new Properties();

	static {
		try (InputStream in = Application.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES)) {

			properties.load(in);

		} catch (IOException e) {
			LOG.error("Unable to load {} file: {}", APPLICATION_PROPERTIES, e.getMessage());
		}
	}

	private PropertiesHolder() {
	}

	public static String getFirebaseURL() {
		return getProperty("firebase-url");
	}

	public static String getTwitterAccessToken() {
		return getProperty("twitter-access-token");
	}

	public static String getTwitterAccessTokenSecret() {
		return getProperty("twitter-access-token-secret");
	}

	public static String getTwitterApiKey() {
		return getProperty("twitter-api-key");
	}

	public static String getTwitterApiSecret() {
		return getProperty("twitter-api-secret");
	}

	public static String getNestToken() {
		return getProperty("nest-token");
	}

	private static String getProperty(String key) {
		return properties.getProperty(key);
	}
}
