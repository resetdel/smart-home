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

import com.smart.home.listener.NestEventListener;
import com.smart.home.model.DeviceSnapshot;
import com.smart.home.model.StructureSnapshot;
import com.smart.home.service.NestService;
import com.smart.home.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterException;

/**
 * Smart home adapter which connects nest and twitter services.
 * If you wish to add few more social services and post updates from nest see {@link NestEventListener}.
 */
public class SmartHomeAdapter implements NestEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(SmartHomeAdapter.class);

	private final TwitterService twitterService;

	/**
	 * Constructor for smart home adapter.
	 *
	 * @param firebaseURL firebase url
	 * @param apiKey      twitter API key
	 * @param apiSecret   twitter API secret key
	 * @param token       twitter access token
	 * @param tokenSecret twitter access token secret
	 * @param nestToken   nest token
	 */
	public SmartHomeAdapter(String firebaseURL, String apiKey, String apiSecret, String token, String tokenSecret, String nestToken) {

		twitterService = new TwitterService(apiKey, apiSecret, token, tokenSecret);

		//subscribe on NEST events for 'Structure' and 'DeviceState'
		new NestService(nestToken, firebaseURL, this);
	}

	@Override
	public void update(final StructureSnapshot structureSnapshot) {
		try {
			twitterService.update(structureSnapshot);
		} catch (TwitterException e) {
			LOG.error("Got TwitterException for structure event: {}", e.getMessage());
		}
	}

	@Override
	public void update(final DeviceSnapshot deviceSnapshot) {
		try {
			twitterService.update(deviceSnapshot);
		} catch (TwitterException e) {
			LOG.error("Got TwitterException for device state event: {}", e.getMessage());
		}
	}

}
