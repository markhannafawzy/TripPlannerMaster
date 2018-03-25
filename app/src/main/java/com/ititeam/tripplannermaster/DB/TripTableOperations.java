package com.ititeam.tripplannermaster.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import com.ititeam.tripplannermaster.model.*;

/**
 * Created by MARK on 3/18/2018.
 */

public class TripTableOperations {

    Context context;

    public TripTableOperations(Context context)
    {
        this.context = context;
    }

    public ArrayList<Trip> selectAllTrips ()
    {
        String [] result_columns = {AdapterDba.DbOpenHelper.TRIP_ID,
                AdapterDba.DbOpenHelper.TRIP_NAME,
                AdapterDba.DbOpenHelper.TRIP_START_POINT,
                AdapterDba.DbOpenHelper.TRIP_END_POINT,
                AdapterDba.DbOpenHelper.TRIP_DATE,
                AdapterDba.DbOpenHelper.TRIP_TIME,
                AdapterDba.DbOpenHelper.TRIP_STATUS,
                AdapterDba.DbOpenHelper.TRIP_DIRECTION,
                AdapterDba.DbOpenHelper.TRIP_DESCRIPTION,
                AdapterDba.DbOpenHelper.TRIP_REPITITION,
                AdapterDba.DbOpenHelper.TRIP_CATEGORY,
                AdapterDba.DbOpenHelper.USER_ID};
        String whereClause = null;
        String [] selectArgs = null;
        String groupBy  = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = AdapterDba.getAdapterDbaInstance(context)._select(AdapterDba.DbOpenHelper.TRIP_TABLE ,result_columns , whereClause, selectArgs, groupBy , having , orderBy);
        ArrayList<Trip> returnedData = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Trip trip = new Trip();
            trip.setTripId(cursor.getInt(0));
            trip.setTripName(cursor.getString(1));
            trip.setTripStartPoint(cursor.getString(2));
            trip.setTripEndPoint(cursor.getString(3));
            trip.setTripDate(cursor.getString(4));
            trip.setTripTime(cursor.getString(5));
            trip.setTripStatus(cursor.getString(6));
            trip.setTripDirection(cursor.getString(7));
            trip.setTripDescription(cursor.getString(8));
            trip.setTripRepetition(cursor.getString(9));
            trip.setTripCategory(cursor.getString(10));
            trip.setUserId(cursor.getInt(11));
            returnedData.add(trip);
        }
        return returnedData;
    }

    public Trip selectAllTripsForGettingLastId ()
    {
        String [] result_columns = {AdapterDba.DbOpenHelper.TRIP_ID,};
        String whereClause = null;
        String [] selectArgs = null;
        String groupBy  = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = AdapterDba.getAdapterDbaInstance(context)._select(AdapterDba.DbOpenHelper.TRIP_TABLE ,result_columns , whereClause, selectArgs, groupBy , having , orderBy);
        Trip trip = new Trip();
        while (cursor.moveToNext())
        {
            trip.setTripId(cursor.getInt(0));
        }
        return trip;
    }

    public Trip selectSingleTrips (String id)
    {
        String [] result_columns = {AdapterDba.DbOpenHelper.TRIP_ID,
                AdapterDba.DbOpenHelper.TRIP_NAME,
                AdapterDba.DbOpenHelper.TRIP_START_POINT,
                AdapterDba.DbOpenHelper.TRIP_END_POINT,
                AdapterDba.DbOpenHelper.TRIP_DATE,
                AdapterDba.DbOpenHelper.TRIP_TIME,
                AdapterDba.DbOpenHelper.TRIP_STATUS,
                AdapterDba.DbOpenHelper.TRIP_DIRECTION,
                AdapterDba.DbOpenHelper.TRIP_DESCRIPTION,
                AdapterDba.DbOpenHelper.TRIP_REPITITION,
                AdapterDba.DbOpenHelper.TRIP_CATEGORY,
                AdapterDba.DbOpenHelper.USER_ID};
        String whereClause = AdapterDba.DbOpenHelper.TRIP_ID+"=?";
        String [] selectArgs = {id};
        String groupBy  = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = AdapterDba.getAdapterDbaInstance(context)._select(AdapterDba.DbOpenHelper.TRIP_TABLE ,result_columns , whereClause, selectArgs, groupBy , having , orderBy);

        Trip trip = new Trip();
        if (cursor.moveToNext())
        {
            trip.setTripId(cursor.getInt(0));
            trip.setTripName(cursor.getString(1));
            trip.setTripStartPoint(cursor.getString(2));
            trip.setTripEndPoint(cursor.getString(3));
            trip.setTripDate(cursor.getString(4));
            trip.setTripTime(cursor.getString(5));
            trip.setTripStatus(cursor.getString(6));
            trip.setTripDirection(cursor.getString(7));
            trip.setTripDescription(cursor.getString(8));
            trip.setTripCategory(cursor.getString(10));
            trip.setUserId(cursor.getInt(11));

        }

        ArrayList<Note> notes =  new NoteTableOperations(context).selectNoteWithTripFk(trip.getTripId()+"");
        for (Note note : notes)
        {
            trip.getTripNotes().add(note);
        }

        return trip;
    }

    public boolean insertTrip (Trip trip)
    {
        boolean flag = false;
        ContentValues newValues = new ContentValues();
        newValues.put(AdapterDba.DbOpenHelper.TRIP_NAME, trip.getTripName());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_START_POINT, trip.getTripStartPoint());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_END_POINT, trip.getTripEndPoint());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_DATE, trip.getTripDate());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_TIME, trip.getTripTime());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_STATUS, trip.getTripStatus());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_DIRECTION, trip.getTripDirection());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_DESCRIPTION, trip.getTripDescription());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_REPITITION, trip.getTripRepetition());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_CATEGORY, trip.getTripCategory());
        newValues.put(AdapterDba.DbOpenHelper.USER_ID, trip.getUserId());
        AdapterDba.getAdapterDbaInstance(context)._insert(AdapterDba.DbOpenHelper.TRIP_TABLE , newValues);
        Trip  tripWithId=  this.selectAllTripsForGettingLastId();

        for (Note note : trip.getTripNotes())
        {
            note.setTripIdFk(tripWithId.getTripId());
            new NoteTableOperations(context).insertNote(note);
            flag = true;
        }
        return flag;
    }

    boolean updateTrip (Trip trip)
    {
        boolean flag = false;
        String whereClause = AdapterDba.DbOpenHelper.TRIP_ID+"=?";
        String [] whereArgs = {trip.getTripId()+""};
        ContentValues newValues = new ContentValues();
        newValues.put(AdapterDba.DbOpenHelper.TRIP_START_POINT, trip.getTripStartPoint());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_END_POINT, trip.getTripEndPoint());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_DATE, trip.getTripDate());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_TIME, trip.getTripName());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_STATUS, trip.getTripStatus());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_DIRECTION, trip.getTripDirection());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_DESCRIPTION, trip.getTripDescription());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_REPITITION, trip.getTripRepetition());
        newValues.put(AdapterDba.DbOpenHelper.TRIP_CATEGORY, trip.getTripCategory());
        newValues.put(AdapterDba.DbOpenHelper.USER_ID, trip.getUserId());
        AdapterDba.getAdapterDbaInstance(context)._update(AdapterDba.DbOpenHelper.TRIP_TABLE ,whereClause ,whereArgs , newValues);

        for (Note note : trip.getTripNotes())
        {
            new NoteTableOperations(context).updateNote(note);
            flag = true;
        }
        return flag;
    }

    public void deleteTrip (String id)
    {
        String whereClause = AdapterDba.DbOpenHelper.TRIP_ID+"=?";
        String [] whereArgs = {id};
        AdapterDba.getAdapterDbaInstance(context)._delete(AdapterDba.DbOpenHelper.TRIP_TABLE ,whereClause ,whereArgs);
    }
}