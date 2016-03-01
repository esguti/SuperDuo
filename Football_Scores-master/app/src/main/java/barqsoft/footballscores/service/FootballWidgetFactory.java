package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.sql.Date;
import java.text.SimpleDateFormat;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresDBHelper;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FootballWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor m_cursor;
    private int m_AppWidgetId;


    public FootballWidgetFactory(Context context, Intent intent) {
        mContext = context;
        m_AppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        // In onCreate() you setup any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
    }

    @Override
    public void onDataSetChanged() {

        // Refresh the cursor
        if (m_cursor != null) {
            m_cursor.close();
        }

        Thread thread = new Thread() {
            public void run() {
                String[] date = new String[1];
                Date filterDate = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                date[0] = format.format(filterDate);
                try {
                    m_cursor = mContext.getContentResolver().query(
                            DatabaseContract.BASE_CONTENT_URI, null,
                            null,
                            date,
                            null
                    );

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }

    }

    @Override
    public void onDestroy() { if (m_cursor != null) { m_cursor.close(); } }

    @Override
    public int getCount() { return (m_cursor != null)? m_cursor.getCount() : 0; }

    public RemoteViews getViewAt(int position) {

        String home, away, homeGoal, awayGoal, league;
        home = away = homeGoal = awayGoal = league = "";

        if(m_cursor.moveToPosition(position)){
            home = m_cursor.getString(m_cursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
            away = m_cursor.getString(m_cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
            homeGoal = String.valueOf(m_cursor.getInt(m_cursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL)));
            homeGoal = (homeGoal.equals("-1"))? " ": homeGoal;
            awayGoal = String.valueOf(m_cursor.getInt(m_cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL)));
            awayGoal = (awayGoal.equals("-1"))? " ": awayGoal;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.football_appwidget_item);
        rv.setTextViewText(R.id.home_name, home);
        rv.setTextViewText(R.id.away_name, away);
        rv.setTextViewText(R.id.score_textview, homeGoal + " - " + awayGoal);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() { return 1; }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}