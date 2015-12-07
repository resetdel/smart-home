/*
 * This is proprietary work of svitla.com. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to svitla.com.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package com.smart.home.model;

/**
 * Model for device state snapshot.
 */
public class DeviceSnapshot {
	private final String structure;
	private final String deviceType;
	private final String location;
	private final String statusType;
	private final String status;

	public DeviceSnapshot(String structure, String deviceType, String location, String statusType, String status) {
		this.structure = structure;
		this.deviceType = deviceType;
		this.location = location;
		this.statusType = statusType;
		this.status = status;
	}

	public String getStructure() {
		return structure;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public String getLocation() {
		return location;
	}

	public String getStatusType() {
		return statusType;
	}

	public String getStatus() {
		return status;
	}

}
