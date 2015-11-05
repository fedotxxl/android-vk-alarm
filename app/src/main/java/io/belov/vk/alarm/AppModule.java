package io.belov.vk.alarm;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.belov.vk.alarm.alarm.AlarmManager;
import io.belov.vk.alarm.alert.AlarmAlertScheduler;
import io.belov.vk.alarm.audio.PlayerBackupProvider;
import io.belov.vk.alarm.audio.PlayerQueue;
import io.belov.vk.alarm.audio.PlayerUtils;
import io.belov.vk.alarm.persistence.AlarmDaoI;
import io.belov.vk.alarm.preferences.PreferencesManager;
import io.belov.vk.alarm.song.SongDownloader;
import io.belov.vk.alarm.song.SongStorage;
import io.belov.vk.alarm.storage.SongsCache;
import io.belov.vk.alarm.storage.SongsCacheI;
import io.belov.vk.alarm.user.UserDao;
import io.belov.vk.alarm.user.UserDaoI;
import io.belov.vk.alarm.user.UserManager;
import io.belov.vk.alarm.utils.FileDownloader;
import io.belov.vk.alarm.vk.VkManager;
import io.belov.vk.alarm.vk.VkSongManager;

@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton
    public Context provideContext() {
        return app;
    }

    @Provides @Singleton
    public AlarmAlertScheduler provideScheduler(Context context) {
        return new AlarmAlertScheduler(context);
    }

    @Provides @Singleton
    public VkSongManager provideVkSongManager() {
        return new VkSongManager();
    }

    @Provides @Singleton
    public AlarmManager provideAlarmManager(Bus bus, AlarmDaoI dao, AlarmAlertScheduler alertScheduler) {
        return new AlarmManager(bus, dao, alertScheduler);
    }

    @Provides @Singleton
    public VkManager provideVkManager() {
        return new VkManager();
    }

    @Provides @Singleton
    public UserDaoI provideUserDao(Context context) {
        return new UserDao(context);
    }

    @Provides @Singleton
    public UserManager provideUserManager(UserDaoI dao, VkManager vkManager, Bus bus) {
        return new UserManager(dao, vkManager, bus);
    }

    @Provides @Singleton
    public PreferencesManager providePreferencesManager() {
        return new PreferencesManager();
    }

    @Provides @Singleton
    public PlayerBackupProvider providePlayerBackupProvider() {
        return new PlayerBackupProvider();
    }

    @Provides @Singleton
    public SongsCacheI provideSongsCache(Context context) {
        return new SongsCache(context);
    }

    @Provides @Singleton
    public FileDownloader provideFileDownloader() {
        return new FileDownloader();
    }

    @Provides @Singleton
    public SongDownloader provideSongDownloader(Context context, SongsCacheI songsCache, FileDownloader fileDownloader) {
        return new SongDownloader(context, songsCache, fileDownloader);
    }

    @Provides @Singleton
    public SongStorage provideSongStorage(SongsCacheI songsCache) {
        return new SongStorage(songsCache);
    }
    
    @Provides @Singleton
    public PlayerQueue.Dependencies providePlayerQueueDependencies(PreferencesManager preferencesManager, PlayerBackupProvider playerBackupProvider, SongDownloader songDownloader, SongStorage songStorage) {
        return new PlayerQueue.Dependencies(preferencesManager, playerBackupProvider, songDownloader, songStorage);
    }

    @Provides @Singleton
    public PlayerUtils providePlayerUtils(Context context) {
        return new PlayerUtils(context);
    }
}
