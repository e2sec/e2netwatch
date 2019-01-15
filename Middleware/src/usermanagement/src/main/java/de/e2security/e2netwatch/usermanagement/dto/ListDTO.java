package de.e2security.e2netwatch.usermanagement.dto;

import java.util.List;

public class ListDTO<T> {
	
	private int total;
	private int retrieved;
	private List<T> content;
	
	public ListDTO(List<T> content, Integer total) {
		this.content = content;
		this.retrieved = content.size();
		if (total != null)
			this.total = total;
		else
			this.total = content.size();
	}

	public int getTotal() {
		return total;
	}

	public int getRetrieved() {
		return retrieved;
	}

	public List<T> getContent() {
		return content;
	}

}
