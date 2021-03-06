package com.floatvideo.popup;

import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.floatvideo.popup.extractor.NewPipe;
import com.floatvideo.popup.util.Constants;
import com.floatvideo.popup.util.PermissionHelper;
import com.floatvideo.popup.extractor.StreamingService;
import com.floatvideo.popup.player.PopupVideoPlayer;

/**
 * Get the url from the intent and open a popup player
 */
public class RouterPopupActivity extends RouterActivity {

    @Override
    protected void handleUrl(String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !PermissionHelper.checkSystemAlertWindowPermission(this)) {
            Toast.makeText(this, R.string.msg_popup_permission, Toast.LENGTH_LONG).show();
            return;
        }
        StreamingService service = NewPipe.getServiceByUrl(url);
        if (service == null) {
            Toast.makeText(this, R.string.url_not_supported_toast, Toast.LENGTH_LONG).show();
            return;
        }

        Intent callIntent = new Intent(this, PopupVideoPlayer.class);
        switch (service.getLinkTypeByUrl(url)) {
            case STREAM:
                break;
            default:
                Toast.makeText(this, R.string.url_not_supported_toast, Toast.LENGTH_LONG).show();
                return;
        }

        callIntent.putExtra(Constants.KEY_URL, url);
        callIntent.putExtra(Constants.KEY_SERVICE_ID, service.getServiceId());
        startService(callIntent);
    }
}
