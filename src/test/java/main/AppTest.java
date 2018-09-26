package main;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static org.junit.Assert.*;

public class AppTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private WatchKey key;

    @Before
    public void setUp() throws Exception {
        temporaryFolder.create();
        String pathToDir = temporaryFolder.getRoot().toString();

        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(pathToDir);

        key = path.register(watcher,
                ENTRY_CREATE,
                ENTRY_DELETE);
    }

    @Test
    public void testWatchDirectory() throws Exception {

        System.out.println("Monitoring directory for changes...");
        System.out.println();

        temporaryFolder.newFile("text.txt");

        for (WatchEvent<?> event : key.pollEvents()) {
            System.out.println(event.kind() + " for file " + "\'" + event.context().toString() + "\'");
            assertEquals(event.kind(), StandardWatchEventKinds.ENTRY_CREATE);
            assertEquals(event.context().toString(), "text.txt");
        }

        temporaryFolder.delete();
        for (WatchEvent<?> event : key.pollEvents()) {
            System.out.println(event.kind() + " for file " + "\'" + event.context().toString() + "\'");
            assertEquals(event.kind(), StandardWatchEventKinds.ENTRY_DELETE);
            assertEquals(event.context().toString(), "text.txt");
        }






    }
}