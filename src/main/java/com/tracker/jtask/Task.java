package com.tracker.jtask;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable {
	private static Long serialId = 0L;

	private Long id;
	private String description;
	private Status status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Task() {
		id = serialId++;
		this.status = Status.TODO;
		createdAt = LocalDateTime.now();
	}

	public Task(String extractedData) {
		for (var pair : extractedData.split(", ")) {
			var key = pair.split(": ")[0];
			var value = pair.split(": ")[1];

			switch (key) {
				case "id" -> this.id = Long.decode(value);
				case "description" -> this.description = value;
				case "status" -> this.status = Status.valueOf(value);
				case "createdAt" -> this.createdAt = LocalDateTime.parse(value);
			}

			if (key.equals("updatedAt") && !value.equals("null")) {
				this.updatedAt = LocalDateTime.parse(value);
			}
		}
	}

	public Task(Long id, String description, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public enum Status {
		TODO, IN_PROGRESS, DONE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		updatedAt = LocalDateTime.now();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		updatedAt = LocalDateTime.now();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		updatedAt = LocalDateTime.now();
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (status != other.status)
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (updatedAt == null) {
			if (other.updatedAt != null)
				return false;
		} else if (!updatedAt.equals(other.updatedAt))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{ \"id\": \"" + id +
				"\", \"description\": \"" + description +
				"\", \"status\": \"" + status.toString().replace("_", "-") +
				"\", \"createdAt\": \"" + createdAt +
				"\", \"updatedAt\": \"" + updatedAt + "\" }";
	}
}
