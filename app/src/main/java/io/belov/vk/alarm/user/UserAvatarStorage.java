package io.belov.vk.alarm.user;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.belov.vk.alarm.storage.SongsCacheI;

/**
 * Created by fbelov on 06.11.15.
 */
public class UserAvatarStorage {

    private static final String TAG = "UserAvatarStorage";

    public static final int CURRENT_USER_ID = -1;

    private File root;

    public UserAvatarStorage(Context context) {
        this.root = new File(context.getFilesDir(), "avatars");
        this.root.mkdirs();
    }


    public void save(int userId, File file) {
        try {
            FileUtils.copyFile(file, getAvatarFile(userId));
        } catch (IOException e) {
            Log.e(TAG, "save", e);
        }
    }


    public File get(int userId) {
        return getAvatarFile(userId);
    }

    public void clear() {
        try {
            FileUtils.cleanDirectory(root);
        } catch (IOException e) {
            Log.e(TAG, "save", e);
        }
    }

    private File getAvatarFile(int userId) {
        return new File(root, "avatar-" + userId);
    }

}
