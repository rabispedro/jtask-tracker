package com.tracker.jtask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String FILE_PATH = Path.of("tasks.json").toString();

    private static final List<Task> tasks = new LinkedList<>();

    public static void main(String[] args) {
        System.out.println("Welcome to JTask Tracker!!!");

        var setuped = setupTask();

        if (setuped && args.length > 0) {
            switch (args[0]) {
                case "add" -> addTask(getDescription(args));
                case "list" -> listTasks((args.length > 1 ? args[1] : ""));
                case "update" -> System.err.println("teste");
                default -> System.err.println("Invalid argument: " + args[0]);
            }
        }

        System.out.println("Goodbye!!!");
    }

    private static boolean setupTask() {
        if (!Path.of("tasks.json").toFile().exists()) {
            try {
                Path.of("tasks.json").toFile().createNewFile();
            } catch (IOException ioe) {
                System.err.println("Exception: " + ioe.getLocalizedMessage());
                return false;
            }
        }

        try (var file = new FileInputStream(FILE_PATH)) {
            
            var size = file.available();
            while (size > 0) {
                var readed = file.readAllBytes();

                System.out.println(size);

                Map<String, String> decodedData = extractDataFromJsonBytes(readed);
                tasks.add(new Task(decodedData));
                file.skip(size);
            }

            return true;
        } catch (IOException ioe) {
            System.err.println("Exception: " + ioe.getLocalizedMessage());
            return false;
        }
    }

    private static void addTask(String description) {
        System.out.println("Adding task '" + description + "'");

        Task task = new Task(description);

        try (var file = new FileOutputStream(Path.of("tasks.json").toFile(), true)) {
            file.write((task.toString() + ",\n").getBytes());
        } catch (IOException ioe) {
            System.err.println("Exception: " + ioe.getLocalizedMessage());
        }
    }

    private static void listTasks(String status) {
        switch (status.toLowerCase()) {
            case "todo", "in-progress", "done" -> listTasksWithStatus(status);
            default -> listAllTasks();
        }
    }

    private static void listAllTasks() {
        System.out.println("Listing all tasks");
        tasks.stream()
                .forEach(task -> System.out.println(task));
    }

    private static void listTasksWithStatus(String status) {
        System.out.println("Listing all tasks with status '" + status + "'");
        tasks.stream()
                .filter(task -> task.getStatus().equals(Task.Status.valueOf(status.toUpperCase())))
                .forEach(task -> System.out.println(task));
    }

    private static String getDescription(String[] args) {
        var stringBuilder = new StringBuilder("");

        for (int i = 1; i < args.length - 1; i++) {
            stringBuilder.append(args[i]);
            stringBuilder.append(" ");
        }
        stringBuilder.append(args[args.length - 1]);

        return stringBuilder.toString();
    }

    private static Map<String, String> extractDataFromJsonBytes(byte[] bytes) {
        Map<String, String> extractedData = new HashMap<>();

        var decoded = new String(bytes);
        for (var line : decoded.split(", ")) {
            var pair = line.split("\":");
            var key = pair[0].replaceAll("\"", "").replaceAll(",", "").strip();
            var value = pair[1].replaceAll("\"", "").replaceAll("}", "").replaceAll(",", "").strip();
            extractedData.put(key, value);
        }

        return extractedData;
    }
}