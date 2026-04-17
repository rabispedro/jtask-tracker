package com.tracker.jtask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static final String FILE_PATH = Path.of("src/main/resources/tasks.json").toString();

    private static final Set<Task> tasks = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("\t\tWelcome to JTask Tracker!!!\n");

        var start = System.nanoTime();
        var setuped = setupTask();

        if (setuped && args.length > 0) {
            switch (args[0]) {
                case "add" -> addTask(getDescription(args));
                case "list" -> listTasks((args.length > 1 ? args[1] : ""));
                case "update" -> System.err.println("teste");
                default -> System.err.println("Invalid argument: " + args[0]);
            }
        }
        var end = System.nanoTime();
        System.out.println("\n\tDone in " + ((end - start) / 1_000_000.0) + " ms. Goodbye!!!");
    }

    private static boolean setupTask() {
        if (!Path.of(FILE_PATH).toFile().exists()) {
            try {
                Path.of(FILE_PATH).toFile().createNewFile();
                hydrateTaskFile();
            } catch (IOException ioe) {
                System.err.println("Setup Path Exists IOException: " + ioe.getLocalizedMessage());
                return false;
            }
        }

        try (var file = new FileInputStream(FILE_PATH)) {
            var readed = file.readAllBytes();
            List<String> decodedData = extractDataFromJsonBytes(readed);

            decodedData
                    .forEach(data -> tasks.add(new Task(data)));

            return true;
        } catch (IOException ioe) {
            System.err.println("Setup File Input Stream IOException: " + ioe.getLocalizedMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Setup File Input Stream Exception: " + e.getLocalizedMessage());
            return false;
        }
    }

    private static void hydrateTaskFile() {
        String body = """
                {
                    \"todo\": [
                    ],
                    \"in-progress\": [
                    ],
                    \"done\": [
                    ]
                }
                """;

        try (var file = new FileOutputStream(FILE_PATH)) {
            file.write(body.getBytes());
            file.close();
        } catch (IOException ioe) {
            System.err.println("Hydrate Task File IOException: " + ioe.getLocalizedMessage());
        } catch (Exception e) {
            System.err.println("Hydrate Task File Exception: " + e.getLocalizedMessage());
        }
    }

    private static void addTask(String description) {
        System.out.println("Adding task '" + description + "'");

        Task task = new Task();
        task.setDescription(description);

        try (var file = new FileOutputStream(Path.of(FILE_PATH).toFile(), true)) {
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
                .filter(task -> task.getStatus().equals(Task.Status.valueOf(status.replace("-", "_").toUpperCase())))
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

    private static List<String> extractDataFromJsonBytes(byte[] bytes) {
        Set<String> junkStrings = Set.of("\"todo\": [", "\"in-progress\": [", "\"done\": [", "{", "}", "],", "]", "");
        List<String> extractedData = new ArrayList<>();

        var decoded = new String(bytes);
        for (var line : decoded.split("\n")) {
            line = line.strip();

            if (!junkStrings.contains(line)) {
                extractedData.add(line.replace("{ ", "").replace(" },", "").replace(" }", "").replaceAll("\"", ""));
            }
        }

        return extractedData;
    }
}