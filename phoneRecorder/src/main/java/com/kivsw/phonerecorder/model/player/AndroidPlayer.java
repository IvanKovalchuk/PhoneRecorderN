package com.kivsw.phonerecorder.model.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.kivsw.phonerecorder.model.utils.RecordFileNameData;

import java.io.File;

import phonerecorder.kivsw.com.phonerecorder.R;

/**
 * play a record with another app
 */
// https://developer.android.com/reference/android/support/v4/content/FileProvider.html
public class AndroidPlayer implements IPlayer{

    public AndroidPlayer() {

    }

    private Uri createURI(Context context, String filePath)
    {
        try {
            File file = new File(filePath);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            return uri;
        }catch(Exception e)
        {
            return Uri.parse("file://"+filePath);
        }

    }
    @Override
    public void play(Context activity, String filePath) {
        Intent i= new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = createURI(activity, filePath);//Uri.parse("file://"+filePath);
        i.setDataAndType (uri, "audio/3gp");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ComponentName ri=i.resolveActivity(activity.getPackageManager());
        if ( ri!= null)
            activity.startActivity(i);
    }

    @Override
    public void playItemWithChooser(Context activity, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = createURI(activity, filePath);//Uri.parse("file://"+filePath);

        i.setDataAndType (uri, "audio/3gp");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        String title = activity.getResources().getString(R.string.chooser_title);
        // Create intent to show chooser
        Intent chooser = Intent.createChooser(i, title);
        if (i.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivity(chooser);

    }
    @Override
    public void setUiLabels(String callerName, RecordFileNameData recordFileNameData)
    {

    };
}
