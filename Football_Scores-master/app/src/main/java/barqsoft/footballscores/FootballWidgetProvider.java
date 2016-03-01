package barqsoft.footballscores;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.FootballWidgetService;

/**
 * Created by esguti on 29.02.16.
 */
public class FootballWidgetProvider extends AppWidgetProvider {

    @SuppressLint("NewApi")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, FootballWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.football_appwidget);

            remoteViews.setRemoteAdapter(widgetId, R.id.widget_scores_list, intent);
            remoteViews.setEmptyView(R.id.widget_scores_list, R.id.widget_scores_empty);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
