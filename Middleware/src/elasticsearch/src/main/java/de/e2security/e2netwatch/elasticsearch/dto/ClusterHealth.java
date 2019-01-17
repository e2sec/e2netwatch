package de.e2security.e2netwatch.elasticsearch.dto;

public class ClusterHealth {
	
	private String name;
	private boolean isAvailable;
	private String version;
	
	public ClusterHealth(String name, boolean isAvailable, String version) {
		this.name = name;
		this.isAvailable = isAvailable;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
