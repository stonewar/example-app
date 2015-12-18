package com.stonewar.appname.manager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.stonewar.appname.R;
import com.stonewar.appname.model.Track;
import com.stonewar.appname.util.Util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yandypiedra on 18.11.15.
 */
public class TrackManager {

    private static final String TAG = TrackManager.class.getName();

    public static final String TRACK_ID = MediaStore.Audio.Media._ID;
    public static final String TRACK_NO = MediaStore.Audio.Media.TRACK;
    public static final String TITLE = MediaStore.Audio.Media.TITLE;
    public static final String ARTIST = MediaStore.Audio.Media.ARTIST;
    public static final String DURATION = MediaStore.Audio.Media.DURATION;
    public static final String ALBUM = MediaStore.Audio.Media.ALBUM;
    public static final String ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID;
    public static final String COMPOSER = MediaStore.Audio.Media.COMPOSER;
    public static final String YEAR = MediaStore.Audio.Media.YEAR;
    public static final String PATH = MediaStore.Audio.Media.DATA;
    public static final Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    public static final String[] COLUMNS_TO_RETRIEVE = {TRACK_ID, TITLE, ARTIST,
            ALBUM, DURATION, ALBUM_ID};


    public static List<Track> findAllTracks(ContentResolver contentResolver, Context context) {
        final String selection = MediaStore.Audio.Media.IS_MUSIC + " = ?";
        final String[] selectionArg = {"1"};
        final String orderBy = MediaStore.Audio.Media.ARTIST;
        return findTracks(contentResolver, context, selection, selectionArg, orderBy);
    }

    public static List<Track> findAllTracksGroupedBy(ContentResolver contentResolver, Context context, String groupedBy, String orderBy) {
        final String selection = MediaStore.Audio.Media.IS_MUSIC + " = ?) GROUP BY (" + groupedBy;
        final String[] selectionArg = {"1"};
        return findTracks(contentResolver, context, selection, selectionArg, orderBy);
    }

    public static List<Track> findTracksByAlbum(ContentResolver contentResolver, Context context, String album, String artist, String orderBy) {
        final String selection = MediaStore.Audio.Media.IS_MUSIC + " = ? AND "+ALBUM+"=?";
        final String[] selectionArg = {"1", album};
        return findTracks(contentResolver, context, selection, selectionArg, orderBy);
    }

//    public static Track findSongById(ContentResolver contentResolver, Context context, Long id) {
//        Track song = null;
//        final String where = MediaStore.Audio.Media.IS_MUSIC + " = 1 AND " + MediaStore.Audio.Media._ID + " = " + id;
//        Cursor cursor = contentResolver.query(URI, null, where, null, null);
//
//        //TODO throw exception SongNotFoundException
//        if (cursor == null) {
//            Log.e(TAG, "The cursor is NULL");
//        } else if (!cursor.moveToFirst()) {
//            Log.i(TAG, "No media on the device");
//        } else {
//            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
//            int authorColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
//            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
//            song = getTrack(cursor, contentResolver, context, idColumn, titleColumn, authorColumn, albumColumn, albumIdColumn);
//        }
//
//        closeCursor(cursor);
//        return song;
//    }

    private static List<Track> findTracks(ContentResolver contentResolver, Context context, String selection, String[] selectionArg, String orderBy) {
        List<Track> foundSongs = new ArrayList<>();
        Cursor cursor = contentResolver.query(URI, COLUMNS_TO_RETRIEVE, selection, selectionArg, orderBy);

        //TODO throw exception EmptyPlayListException
        if (cursor == null) {
            Log.e(TAG, "The cursor is NULL");
        } else if (!cursor.moveToFirst()) {
            Log.i(TAG, "No media on the device");
        } else {

            int idColumn = cursor.getColumnIndex(TRACK_ID);
            int titleColumn = cursor.getColumnIndex(TITLE);
            int authorColumn = cursor.getColumnIndex(ARTIST);
            int albumColumn = cursor.getColumnIndex(ALBUM);
            int albumIdColumn = cursor.getColumnIndex(ALBUM_ID);
            int durationColumn = cursor.getColumnIndex(DURATION);

            do {
                Track song = getTrack(cursor, contentResolver, context, idColumn, titleColumn, authorColumn, albumColumn, albumIdColumn, durationColumn);
                foundSongs.add(song);
                // ...process entry...
            } while (cursor.moveToNext());
        }

        closeCursor(cursor);

        return foundSongs;
    }

    private static Track getTrack(Cursor cursor, ContentResolver contentResolver, Context context, int idColumn, int titleColumn, int authorColumn, int albumColumn, int albumIdColumn, int durationColumn) {
        Long id = cursor.getLong(idColumn);
        String title = cursor.getString(titleColumn);
        String author = cursor.getString(authorColumn);
        String albumTitle = cursor.getString(albumColumn);
        Long duration = cursor.getLong(durationColumn);

        Log.d(TAG, "Id: " + id);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Author: " + author);
        Log.d(TAG, "Duration: " + duration);
        Log.d(TAG, "Album Title: " + albumTitle);

        Bitmap album = null;
        try {
            Long albumId = cursor.getLong(albumIdColumn);
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uriAlbum = ContentUris.withAppendedId(sArtworkUri, albumId);
            InputStream in = contentResolver.openInputStream(uriAlbum);
            album = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            album = Util.getBitmap(context, R.mipmap.ic_launcher);
        }

        return new Track(id, album, title, author, albumTitle, duration);
    }


    private static void closeCursor(Cursor cursor) {
        if (cursor != null)
            cursor.close();
    }


    //TODO this method is not good
    public static Bitmap findArtworkById(ContentResolver contentResolver, Context context, Long id) {
        Bitmap album = null;
        final String where = MediaStore.Audio.Media.IS_MUSIC + " = 1 AND " + MediaStore.Audio.Media._ID + " = " + id;
        final String[] projection = {MediaStore.Audio.Media.ALBUM_ID};
        Cursor cursor = contentResolver.query(URI, projection, where, null, null);
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
                album = Util.getBitmap(context, R.mipmap.ic_launcher);
            } finally {
                closeCursor(cursor);
            }
        }

        return album;
    }

}
