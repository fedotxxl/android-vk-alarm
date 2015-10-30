package io.belov.vk.alarm.vk;

import java.io.File;

/**
 * Created by fbelov on 30.10.15.
 */
public class VkSongWithFile {

    private VkSong song;
    private File file;

    public VkSongWithFile(VkSong song, File file) {
        this.song = song;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public int getId() {
        return song.getId();
    }

    public int getOwnerId() {
        return song.getOwnerId();
    }

    public String getArtist() {
        return song.getArtist();
    }

    public String getTitle() {
        return song.getTitle();
    }

    public long getDate() {
        return song.getDate();
    }

    public String getUrl() {
        return song.getUrl();
    }

    public boolean isFileExists() {
        return file.exists();
    }
}
