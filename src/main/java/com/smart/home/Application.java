/*
 * This is proprietary work of svitla.com. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to svitla.com.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package com.smart.home;

import com.smart.home.config.PropertiesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Main class for smart home application.
 */
public class Application {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {

		new SmartHomeAdapter(PropertiesHolder.getFirebaseURL(),
				PropertiesHolder.getTwitterApiKey(),
				PropertiesHolder.getTwitterApiSecret(),
				PropertiesHolder.getTwitterAccessToken(),
				PropertiesHolder.getTwitterAccessTokenSecret(),
				PropertiesHolder.getNestToken());

		LOG.info("Press 'enter' key to exit . . . ");

		new Scanner(System.in).nextLine();
	}

}
