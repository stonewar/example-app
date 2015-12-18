package com.stonewar.appname.manager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.stonewar.appname.model.Album;
import com.stonewar.appname.model.Track;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yandypiedra on 17.12.15.
 */
public class AlbumManager {

    private static final String TAG = AlbumManager.class.getName();

    public static final String ID = MediaStore.Audio.Albums._ID;
    public static final String ALBUM_NAME = MediaStore.Audio.Albums.ALBUM;
    public static final String ARTIST = MediaStore.Audio.Albums.ARTIST;
    public static final Uri URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    public static final String[] COLUMNS_TO_RETRIEVE = {ID, ALBUM_NAME, ARTIST};


    public static List<Album> findAllAlbums(Context context, ContentResolver contentResolver, String selection) {
        Cursor cursor = contentResolver.query(URI, COLUMNS_TO_RETRIEVE, selection, null, ARTIST);
        List<Album> foundAlbums = new ArrayList<>();
        if (cursor == null) {
            Log.e(TAG, "The cursor is NULL");
        } else if (!cursor.moveToFirst()) {
            Log.i(TAG, "No Albums on the device"); //TODO throw exception
        }
        else {
            int idColumn = cursor.getColumnIndex(ID);
            int titleColumn = cursor.getColumnIndex(ALBUM_NAME);
            int artistColumn =  cursor.getColumnIndex(ARTIST);

            do {
                foundAlbums.add(getAlbum(context, contentResolver, cursor, idColumn, titleColumn, artistColumn));
                // ...process entry...
            } while (cursor.moveToNext());
        }

        closeCursor(cursor);

        return foundAlbums;
    }

    private static Album getAlbum(Context context, ContentResolver contentResolver, Cursor cursor, int idColumn, int titleColumn, int artistColumn){
        Long id = cursor.getLong(idColumn);
        String title = cursor.getString(titleColumn);
        String artist = cursor.getString(artistColumn);
        Log.d(TAG, "Id: " + id);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Author: " + artist);
        List<Track> tracks = TrackManager.findTracksByAlbum(contentResolver, context, title, artist, TrackManager.ARTIST);
        Album album = new Album(tracks, id, title, artist);
        return  album;
    }

    private static void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }
}
