package com.gsk.www.call_log;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "CallLog";
    private static final int URL_LOADER = 1;

    private TextView callLogsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        Log.d(TAG, "onCreate()");//Logcat MESSAGE
        setContentView (R.layout.activity_main);
        initialize();

    }

    private void initialize() {
        Log.d(TAG, "initialize()");

        Button btnCallLog = (Button) findViewById(R.id.btn_call_log);

        btnCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "initialize() >> initialise loader");
                Toast.makeText (getApplicationContext (),"Please Wait Your Call Log Details are loading......",Toast.LENGTH_LONG).show ();

                getLoaderManager().initLoader(URL_LOADER, null, MainActivity.this);
            }
        });

        callLogsTextView = (TextView) findViewById(R.id.call_logs);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.d(TAG, "onCreateLoader() >> loaderID : " + i);
        //Toast.makeText (getApplicationContext (),"Please Wait Sometimes Your Call Log Details are Loading...",Toast.LENGTH_LONG).show ();


        switch (i) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader (
                        this,   // Parent activity context
                        CallLog.Calls.CONTENT_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                return null;
        }

    }


    @Override
    public void onLoadFinished(Loader <Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished()");

        StringBuilder sb = new StringBuilder();
       // Toast.makeText (getApplicationContext (),"Your Call Log Details are Loaded Successfuly",Toast.LENGTH_LONG).show ();

        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);


        sb.append("<h4>Call Log Details <h4>");
        sb.append("\n");
        sb.append("\n");

        sb.append("<table>");

        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date (Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;

            int callTypeCode = Integer.parseInt(callType);
            switch (callTypeCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Outgoing";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Incoming";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed";
                    break;
            }

            sb.append("<tr>")
                    .append("<td>Phone Number: </td>")
                    .append("<td><strong>")
                    .append(phNumber)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Call Type:</td>")
                    .append("<td><strong>")
                    .append(dir)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Date & Time:</td>")
                    .append("<td><strong>")
                    .append(callDayTime)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<tr>")
                    .append("<td>Call Duration (Seconds):</td>")
                    .append("<td><strong>")
                    .append(callDuration)
                    .append("</strong></td>");
            sb.append("</tr>");
            sb.append("<br/>");
            sb.append("<br/>");
        }
        sb.append("</table>");

        cursor.close();

        callLogsTextView.setText(Html.fromHtml(sb.toString()));

    }



    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
        //Toast.makeText (getApplicationContext (),"Please Wait Sometimes Your Call Log Details are Loading...",Toast.LENGTH_LONG).show ();

    }
}




