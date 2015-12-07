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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.smart.home.listener.NestEventListener;
import com.smart.home.model.DeviceSnapshot;
import com.smart.home.model.StructureSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Nest service class works with nest client and notify about firebase events.
 */
public class NestService {

	private static final Logger LOG = LoggerFactory.getLogger(NestService.class);
	private static final String N_A = "n/a";

	private final Firebase firebase;

	private final Map<String, Map<String, String>> onlineStates = new HashMap<>();

	private final Map<String, Map<String, String>> targetTemperatureStates = new HashMap<>();

	private final Map<String, String> structureStates = new HashMap<>();

	private final Map<String, String> devicesMap = new HashMap<>();

	private final NestEventListener nestEventListener;

	/**
	 * Constructor for nest service
	 *
	 * @param nestToken         nest token API
	 * @param firebaseURL       firebase url
	 * @param nestEventListener event listener for nest
	 */
	public NestService(String nestToken, String firebaseURL, NestEventListener nestEventListener) {
		this.nestEventListener = nestEventListener;
		firebase = new Firebase(firebaseURL);
		init(nestToken);
	}

	private void init(String nestToken) {
		firebase.auth(nestToken, new Firebase.AuthListener() {

			@Override
			public void onAuthError(FirebaseError firebaseError) {
				LOG.error("Firebase authentication error: {}.", firebaseError.getMessage());
			}

			@Override
			public void onAuthSuccess(Object o) {

				LOG.info("Firebase authentication success");

				firebase.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						LOG.info("Received Firebase data snapshot {}", dataSnapshot.toString());
						processNestEvent(dataSnapshot);
					}

					@Override
					public void onCancelled(FirebaseError firebaseError) {
						LOG.error("Firebase error: {}", firebaseError.toString());
					}
				});
			}

			@Override
			public void onAuthRevoked(FirebaseError firebaseError) {
				LOG.info("Firebase authentication revoked: {}.", firebaseError.getMessage());
			}
		});
	}

	private void processNestEvent(DataSnapshot dataSnapshot) {
		try {
			devicesMap.clear();

			DataSnapshot structures = dataSnapshot.child("structures");

			if (structures != null && structures.getChildren() != null) {

				structures.getChildren().forEach(struct -> {

					String structName = struct.child("name").getValue().toString();
					devicesMap.put(struct.getName(), structName);
					String structState = struct.child("away").getValue().toString();
					String oldState = Optional.ofNullable(structureStates.get(structName)).orElse(N_A);
					structureStates.put(structName, structState);

					if (!N_A.equals(oldState) && !oldState.equals(structState)) {
						nestEventListener.update(new StructureSnapshot(structName, structState));
					}
				});

			} else {
				LOG.error("Unable to parse data for child [structures].");
			}

			DataSnapshot thermostats = dataSnapshot.child("devices").child("thermostats");

			if (thermostats != null && thermostats.getChildren() != null) {
				thermostats.getChildren().forEach(thermostat -> {

					String thermId = thermostat.getName();
					String location = thermostat.child("name").getValue().toString();
					String structId = Optional.ofNullable(thermostat.child("structure_id").getValue()).orElse(N_A).toString();
					String targetTemp = Optional.ofNullable(thermostat.child("target_temperature_f").getValue()).orElse(N_A).toString();
					String onlineStatus = thermostat.child("is_online").getValue().toString();

					compareAndNotify(targetTemperatureStates, "target_temp", targetTemp, structId, thermId, location);
					compareAndNotify(onlineStates, "online", onlineStatus, structId, thermId, location);

				});
			}
		} catch (Exception e) {
			LOG.error("Got an exception during processing event from Nest, exception: " + e.getMessage(), e);
		}
	}

	private void compareAndNotify(Map<String, Map<String, String>> stateMap, String statusType, String status, String structId, String thermId, String location) {
		if (!stateMap.containsKey(structId)) {
			stateMap.put(structId, new HashMap<>());
		}

		String oldState = Optional.ofNullable(stateMap.get(structId).get(thermId)).orElse(N_A);

		if (!N_A.equals(oldState) && !oldState.equals(status)) {
			nestEventListener.update(new DeviceSnapshot(devicesMap.get(structId), "Thermostat", location, statusType, status));
		}

		stateMap.get(structId).put(thermId, status);
	}

}
