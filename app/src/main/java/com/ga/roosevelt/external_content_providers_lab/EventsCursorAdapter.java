package com.ga.roosevelt.external_content_providers_lab;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by roosevelt on 8/11/16.
 */
public class EventsCursorAdapter extends CursorAdapter {
    public EventsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.event_layout, parent, false);

    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView txtEventName = (TextView) view.findViewById(R.id.txtEventName);
        TextView txtEventDate = (TextView) view.findViewById(R.id.txtEventDate);

        final long id = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events._ID));
        String eventName = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
        String eventDate = getDate(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));

        txtEventName.setText(eventName);
        txtEventDate.setText(eventDate);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Toast.makeText(view.getContext(), "gonna delete this " + id, Toast.LENGTH_SHORT).show();
                final View myView = view;

                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setMessage("Are you sure you want to delete this event?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        myView.getContext().getContentResolver().delete(
                                ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id),
                                null,
                                null);
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.show();

                return false;
            }
        });

    }

    public static String getDate(long milliSeconds)
    {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(milliSeconds);  //here your time in miliseconds
        return  "" + cl.get(Calendar.DAY_OF_MONTH) + "/" + cl.get(Calendar.MONTH) + "/" + cl.get(Calendar.YEAR);

    }
}
