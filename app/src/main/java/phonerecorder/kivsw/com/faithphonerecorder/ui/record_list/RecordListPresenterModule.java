package phonerecorder.kivsw.com.faithphonerecorder.ui.record_list;

import android.content.Context;

import com.kivsw.cloud.DiskContainer;
import com.kivsw.cloudcache.CloudCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;
import phonerecorder.kivsw.com.faithphonerecorder.model.ErrorProcessor.IErrorProcessor;
import phonerecorder.kivsw.com.faithphonerecorder.model.player.IPlayer;
import phonerecorder.kivsw.com.faithphonerecorder.model.settings.ISettings;
import phonerecorder.kivsw.com.faithphonerecorder.model.tasks.RecordSender;

/**
 * Created by ivan on 3/27/18.
 */
@Module
public class RecordListPresenterModule {
    @Provides
    @NonNull
    @Singleton
    public RecordListContract.IRecordListPresenter providePresenter(Context appContext, ISettings settings, IPlayer player, DiskContainer disks, CloudCache cloudCache, IErrorProcessor errorProcessor, RecordSender recordSender)
    {
        return new RecordListPresenter(appContext, settings, player,  disks, cloudCache,  errorProcessor, recordSender);
    }
}
