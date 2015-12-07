/*
 * This is proprietary work of svitla.com. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to svitla.com.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
package com.smart.home.listener;

import com.smart.home.model.DeviceSnapshot;
import com.smart.home.model.StructureSnapshot;

/**
 * Event listener for nest data snapshots.
 */
public interface NestEventListener {

	/**
	 * Update for structure.
	 *
	 * @param structureSnapshot structure snapshot
	 */
	void update(final StructureSnapshot structureSnapshot);

	/**
	 * Update for device.
	 *
	 * @param deviceSnapshot device snapshot
	 */
	void update(final DeviceSnapshot deviceSnapshot);

}
