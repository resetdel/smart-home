/*
 * This is proprietary work of svitla.com. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to svitla.com.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package com.smart.home.service;

import com.smart.home.model.DeviceSnapshot;
import com.smart.home.model.StructureSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Twitter service class post the twitter updates.
 */
public class TwitterService {

	private static final Logger LOG = LoggerFactory.getLogger(TwitterService.class);

	private final Twitter twitter;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	/**
	 * Constructor for twitter service
	 *
	 * @param apiKey      twitter API key
	 * @param apiSecret   twitter API secret key
	 * @param token       twitter access token
	 * @param tokenSecret twitter access token secret
	 */
	public TwitterService(String apiKey, String apiSecret, String token, String tokenSecret) {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(apiKey)
				.setOAuthConsumerSecret(apiSecret)
				.setOAuthAccessToken(token)
				.setOAuthAccessTokenSecret(tokenSecret);
		TwitterFactory factory = new TwitterFactory(cb.build());
		twitter = factory.getInstance();
		twitter.directMessages();
	}

	/**
	 * Requires for twitter messages.
	 * Note: twitter prevents duplication of messages.
	 *
	 * @return formatted time.
	 */
	private String getTime() {
		return dateFormat.format(new Date());
	}

	/**
	 * Update for structure snapshot
	 *
	 * @param structureSnapshot structure snapshot
	 * @throws TwitterException {@link TwitterException}
	 */
	public void update(StructureSnapshot structureSnapshot) throws TwitterException {
		LOG.info("updating status on state change");
		processHomeEvent(structureSnapshot);
	}

	/**
	 * Update for device snapshot
	 *
	 * @param deviceSnapshot device snapshot
	 * @throws TwitterException {@link TwitterException}
	 */
	public void update(DeviceSnapshot deviceSnapshot) throws TwitterException {
		LOG.info("Updating twitter statusType [{}] on {}", deviceSnapshot.getStatusType(), deviceSnapshot.getStatus());
		switch (deviceSnapshot.getStatusType()) {
			case "online":
				processOnlineEvent(deviceSnapshot);
				break;
			case "target_temp":
				processTargetTemperatureEvent(deviceSnapshot);
				break;
		}
	}

	/**
	 * Home event
	 *
	 * @param structureSnapshot structure snapshot
	 * @throws TwitterException {@link TwitterException}
	 */
	private void processHomeEvent(StructureSnapshot structureSnapshot) throws TwitterException {
		String message = String.format("Set state to '%s' at location %s at %s.",
				structureSnapshot.getState(),
				structureSnapshot.getName(),
				getTime());
		LOG.info(message);
		twitter.updateStatus(message);
	}

	/**
	 * Online event
	 *
	 * @param deviceSnapshot device snapshot
	 * @throws TwitterException {@link TwitterException}
	 */
	private void processOnlineEvent(DeviceSnapshot deviceSnapshot) throws TwitterException {
		String message;
		if (deviceSnapshot.getStatus().equals("true")) {
			message = "Device %s at location %s@%s came back online at %s.";
		} else {
			message = "Device %s at location %s@%s went offline at %s.";
		}

		message = String.format(message,
				deviceSnapshot.getDeviceType(),
				deviceSnapshot.getStructure(),
				deviceSnapshot.getLocation(),
				getTime());
		LOG.info(message);

		twitter.updateStatus(message);
	}

	/**
	 * Target temperature event
	 *
	 * @param deviceSnapshot device snapshot
	 * @throws TwitterException {@link TwitterException}
	 */
	private void processTargetTemperatureEvent(DeviceSnapshot deviceSnapshot) throws TwitterException {
		String message = String.format("Thermostat at location %s@%s set a target temperature %s at %s.",
				deviceSnapshot.getStructure(),
				deviceSnapshot.getLocation(),
				deviceSnapshot.getStatus(),
				getTime());
		LOG.info(message);

		twitter.updateStatus(message);
	}

}
