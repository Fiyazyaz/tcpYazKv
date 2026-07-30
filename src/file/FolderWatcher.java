package file;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class FolderWatcher {

    public static void main(String[] args) throws Exception {

        WatchService watchService = FileSystems.getDefault().newWatchService();

        Path folderA = Paths.get("FolderA");
        Path folderB = Paths.get("FolderB");

        WatchKey keyA = folderA.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
        );

        WatchKey keyB = folderB.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
        );

        Map<WatchKey, Path> watchMap = new HashMap<>();
        watchMap.put(keyA, folderA);
        watchMap.put(keyB, folderB);

        System.out.println("Watching FolderA and FolderB...");

        while (true) {

            // Wait until any folder changes
            WatchKey key = watchService.take();

            // Which folder generated this event?
            Path currentFolder = watchMap.get(key);

            // Decide destination folder
            Path destinationFolder =
                    currentFolder.equals(folderA) ? folderB : folderA;

            for (WatchEvent<?> event : key.pollEvents()) {

                WatchEvent.Kind<?> kind = event.kind();

                Path fileName = (Path) event.context();

                Path source = currentFolder.resolve(fileName);

                Path destination = destinationFolder.resolve(fileName);

                try {

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {

                        Files.copy(
                                source,
                                destination,
                                StandardCopyOption.REPLACE_EXISTING
                        );

                        System.out.println("CREATE : " + fileName);

                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {

                        Files.copy(
                                source,
                                destination,
                                StandardCopyOption.REPLACE_EXISTING
                        );

                        System.out.println("MODIFY : " + fileName);

                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {

                        Files.deleteIfExists(destination);

                        System.out.println("DELETE : " + fileName);
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            key.reset();
        }
    }
}