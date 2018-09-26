package main;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class App {

    public static void watchDir(Path path, WatchService watcher) {
        WatchKey key;

        try {
            key = watcher.take();
        } catch (InterruptedException e) {
            return;
        }

        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();

            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                System.out.println("Created: " + event.context().toString());
            }

            if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                System.out.println("Deleted: " + event.context().toString());
            }

            if (kind == OVERFLOW) {
                continue;
            }

            boolean valid = key.reset();
            if (!valid) {
                System.out.println("Path doesn't exist " + path);
                break;
            }
        }
        key.reset();
    }

    public static void main(String[] args) throws Exception {

        String pathToDirectory = "C:\\001";
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(pathToDirectory);
        WatchKey key = null;

        try {
            key = path.register(watcher,
                    ENTRY_CREATE,
                    ENTRY_MODIFY,
                    ENTRY_DELETE);

            System.out.println("Monitoring directory for changes...");
            System.out.println();
        } catch (IOException e) {
            System.err.println(e);
        }

        while (true) {
            watchDir(path, watcher);
        }

    }
}
