package io.belov.vk.alarm.audio;

import android.net.Uri;

import java.io.File;

import io.belov.vk.alarm.vk.VkSongWithFile;

/**
 * Created by fbelov on 05.11.15.
 */
public class PlayableSong {

    private String title;
    private String artist;
    private Uri uri;
    private File file;

    public PlayableSong(Uri uri, String title, String artist) {
        this.uri = uri;
        this.title = title;
        this.artist = artist;
    }

    public PlayableSong(File file, String title, String artist) {
        this.file = file;
        this.title = title;
        this.artist = artist;
    }

    public boolean hasFile() {
        return file != null;
    }

    public boolean hasUri() {
        return uri != null;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Uri getUri() {
        return uri;
    }

    public File getFile() {
        return file;
    }

    public static PlayableSong from(VkSongWithFile songWithFile) {
        return new PlayableSong(songWithFile.getFile(), songWithFile.getTitle(), songWithFile.getArtist());
    }
}
