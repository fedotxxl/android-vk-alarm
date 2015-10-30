package io.belov.vk.alarm.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by fbelov on 30.10.15.
 */
public class IoUtils {

    @Nullable
    public static File download(String fileUrl, File fileTo) {
        int count;
        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
            // Output stream to write file in SD card
            OutputStream output = new FileOutputStream(fileTo);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // Write data to file
                output.write(data, 0, count);
            }
            // Flush output
            output.flush();
            // Close streams
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return null;
    }

    public static void downloadAsync(final String fileUrl, final File fileTo, final FileDownloadedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = IoUtils.download(fileUrl, fileTo);

                if (file != null && file.exists()) {
                    listener.on(fileUrl, file);
                }
            }
        }).start();
    }

    public interface FileDownloadedListener {

        void on(String url, File file);

    }
}
