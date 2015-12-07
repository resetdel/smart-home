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
 * Model for structure state snapshot.
 */
public class StructureSnapshot {
	private final String name;
	private final String state;

	public StructureSnapshot(String name, String state) {
		this.name = name;
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}
}
