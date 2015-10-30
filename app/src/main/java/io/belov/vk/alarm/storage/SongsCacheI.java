package io.belov.vk.alarm.storage;

import java.io.File;
import java.util.List;

/**
 * Created by fbelov on 30.10.15.
 */
public interface SongsCacheI {

    void save(int id, File file);
    File get(int id);
    List<FileWithKey> getAll();
    void clear();

    class FileWithKey {

        private int id;
        private File file;

    }

}
