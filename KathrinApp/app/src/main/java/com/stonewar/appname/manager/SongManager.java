package com.stonewar.appname.manager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.stonewar.appname.model.Song;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yandypiedra on 18.11.15.
 */
public class SongManager {

    private static final String TAG = SongManager.class.getName();


    public static List<Song> findAllSongs(ContentResolver contentResolver, Uri uri) {
        final String where = MediaStore.Audio.Media.IS_MUSIC + " = 1";
        final String orderBy = MediaStore.Audio.Media.ARTIST;
        return findSongs(contentResolver, uri, where, orderBy);
    }

    public static List<Song> findAllSongGroupedByArtists(ContentResolver contentResolver, Uri uri) {
        final String where = MediaStore.Audio.Media.IS_MUSIC + " = 1) GROUP BY ("+MediaStore.Audio.Media.ARTIST;
        final String orderBy = MediaStore.Audio.Media.ARTIST;
        return findSongs(contentResolver, uri, where, orderBy);
    }

    public static Song findSongById(ContentResolver contentResolver, Uri uri, Long id) {
        Song song = null;
        final String where = MediaStore.Audio.Media.IS_MUSIC + " = 1 AND " + MediaStore.Audio.Media._ID + " = " + id;
        Cursor cursor = contentResolver.query(uri, null, where, null, null);

        //TODO throw exception SongNotFoundException
        if (cursor == null) {
            Log.e(TAG, "The cursor is NULL");
        } else if (!cursor.moveToFirst()) {
            Log.i(TAG, "No media on the device");
        } else {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int authorColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            song = getSong(cursor, contentResolver, idColumn, titleColumn, authorColumn, albumIdColumn);
        }

        closeCursor(cursor);
        return song;
    }

    public static Bitmap findArtworkById(ContentResolver contentResolver, Uri uri, Long id) {
        Bitmap album = null;
        final String where = MediaStore.Audio.Media.IS_MUSIC + " = 1 AND " + MediaStore.Audio.Media._ID + " = " + id;
        final String[] projection = {MediaStore.Audio.Media.ALBUM_ID};
        Cursor cursor = contentResolver.query(uri, projection, where, null, null);
        if (cursor == null) {
            Log.e(TAG, "The cursor is NULL");
        } else if (!cursor.moveToFirst()) {
            Log.i(TAG, "No media on the device");
        } else {
            try {
                int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                Long albumId = cursor.getLong(albumIdColumn);
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri uriAlbum = ContentUris.withAppendedId(sArtworkUri, albumId);
                InputStream in = contentResolver.openInputStream(uriAlbum);
                album = BitmapFactory.decodeStream(in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                closeCursor(cursor);
            }
        }

        return album;
    }

    private static List<Song> findSongs(ContentResolver contentResolver, Uri uri, String where, String orderBy){
        List<Song> foundSongs = new ArrayList<>();
        Cursor cursor = contentResolver.query(uri, null, where, null, orderBy);

        //TODO throw exception EmptyPlayListException
        if (cursor == null) {
            Log.e(TAG, "The cursor is NULL");
        } else if (!cursor.moveToFirst()) {
            Log.i(TAG, "No media on the device");
        } else {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int authorColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            do {
                Song song = getSong(cursor, contentResolver, idColumn, titleColumn, authorColumn, albumIdColumn);
                foundSongs.add(song);
                // ...process entry...
            } while (cursor.moveToNext());
        }

        closeCursor(cursor);

        return foundSongs;
    }

    private static Song getSong(Cursor cursor, ContentResolver contentResolver, int idColumn, int titleColumn, int authorColumn, int albumIdColumn) {
        Long id = cursor.getLong(idColumn);
        String title = cursor.getString(titleColumn);
        String author = cursor.getString(authorColumn);
        Log.d(TAG, "Id: " + id);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Author: " + author);

        Bitmap album = null;

        try {
            Long albumId = cursor.getLong(albumIdColumn);
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uriAlbum = ContentUris.withAppendedId(sArtworkUri, albumId);
            InputStream in = contentResolver.openInputStream(uriAlbum);
            album = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Song(id, album, title, author);
    }

    private static void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }

}
