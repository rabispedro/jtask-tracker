package com.tracker.jtask;

import java.time.LocalDateTime;
import java.time.Month;

public class TaskTests {
	private Task task;

	void runAll() {
		givenNoArguments_whenCreatingTask_thenCreateWithEmptyValues();
		givenValidDescription_whenCreatingTask_thenCreateWithDefaults();
		givenAllValidArguments_whenCreatingTask_thenCreateWithAllArguments();
	}

	void givenNoArguments_whenCreatingTask_thenCreateWithEmptyValues() {
		task = new Task();

		assert(task != null);
		assert(task.getStatus().equals(Task.Status.TODO));
	}

	void givenValidDescription_whenCreatingTask_thenCreateWithDefaults() {
		var validDescription = "Valid description";
		
		task = new Task(validDescription);

		assert(task != null);
		assert(task.getDescription().equals(validDescription));
		assert(task.getStatus().equals(Task.Status.TODO));
	}

	void givenAllValidArguments_whenCreatingTask_thenCreateWithAllArguments() {
		var validId = 10L;
		var validDescription = "Valid description";
		var validStatus = Task.Status.IN_PROGRESS;
		var validCreatedAt = LocalDateTime.of(2000, Month.DECEMBER, 31, 10, 10);
		var validUpdatedAt = LocalDateTime.now();

		
		task = new Task(validId, validDescription, validStatus, validCreatedAt, validUpdatedAt);

		assert(task != null);
		assert(task.getId().equals(validId));
		assert(task.getDescription().equals(validDescription));
		assert(task.getStatus().equals(validStatus));
		assert(task.getCreatedAt().equals(validCreatedAt));
		assert(task.getUpdatedAt().equals(validUpdatedAt));
	}

	void givenValidMappedData_whenCreatingTask_thenCreateWithAllMappedArguments() {
		String mappedData = "id: 10, description: teste 1 dois três, status: TODO, createdAt: 2025-12-02T13:51:53.000000000, updatedAt: null";

		task = new Task(mappedData);

		assert(task != null);
		assert(task.getId().equals(10L));
		assert(task.getDescription().equals("teste 1 dois três"));
		assert(task.getStatus().equals(Task.Status.TODO));
		assert(task.getCreatedAt().equals(LocalDateTime.of(2025, 12, 02, 13, 51, 53)));
		assert(task.getUpdatedAt().equals(null));
	}
}
