package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViewsService;

/**
 * Created by esguti on 01.03.16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FootballWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FootballWidgetFactory(getApplicationContext(), intent);
    }
}
