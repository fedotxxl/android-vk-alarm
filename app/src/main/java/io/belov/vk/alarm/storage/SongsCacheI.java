package io.belov.vk.alarm.storage;

import java.io.File;
import java.util.List;

/**
 * Created by fbelov on 30.10.15.
 */
public interface SongsCacheI {

    void save(int key, File file, Importance importance);
    void touchAndSetImportance(int key, Importance importance);
    FileWithData get(int key);
    List<FileWithData> getAll();
    void clear();

    class FileWithData {
        private int key;
        private File file;
        private Importance importance;

        public int getKey() {
            return key;
        }

        public File getFile() {
            return file;
        }

        public Importance getImportance() {
            return importance;
        }
    }

    enum Importance {
        SMALL, LARGE
    }
}
