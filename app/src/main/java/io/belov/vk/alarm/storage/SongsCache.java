package io.belov.vk.alarm.storage;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by fbelov on 02.11.15.
 */
public class SongsCache implements SongsCacheI {

    private static final String TAG = "SongsCache";

    private Context context;
    private File root;

    public SongsCache(Context context) {
        this.context = context;
        this.root = new File(context.getFilesDir(), "songs");
        this.root.mkdirs();
    }


    @Override
    public void save(int key, File file, Importance importance) {
        try {
            FileUtils.copyFile(file, getSongFile(key));
        } catch (IOException e) {
            Log.e(TAG, "save", e);
        }
    }

    @Override
    public void touchAndSetImportance(int key, Importance importance) {

    }

    @Override
    public FileWithData get(int key) {
        return new FileWithData(key, getSongFile(key), Importance.LARGE);
    }

    @Override
    public List<FileWithData> getAll() {
        return null;
    }

    @Override
    public void clear() {
        try {
            FileUtils.cleanDirectory(root);
        } catch (IOException e) {
            Log.e(TAG, "save", e);
        }
    }

    private File getSongFile(int key) {
        return new File(root, "song-" + key);
    }
}
