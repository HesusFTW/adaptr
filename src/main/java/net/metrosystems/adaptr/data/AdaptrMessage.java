package net.metrosystems.adaptr.data;

import java.io.Serializable;
import java.util.HashMap;

public class AdaptrMessage implements Serializable {
	private HashMap<String, String> headers;
	private HashMap<String, String> properties;
	private byte[] payload;

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "AdaptrMessage{" +
				"headers=" + headers +
				'}';
	}
}
