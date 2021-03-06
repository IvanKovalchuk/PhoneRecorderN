package com.kivsw.phonerecorder.os;

import com.kivsw.phonerecorder.model.CloudCacheModule;
import com.kivsw.phonerecorder.model.DiskRepresentativeModule;
import com.kivsw.phonerecorder.model.addrbook.AddrBookModule;
import com.kivsw.phonerecorder.model.error_processor.ErrorProcessorModule;
import com.kivsw.phonerecorder.model.internal_filelist.InternalFilesModule;
import com.kivsw.phonerecorder.model.metrica.IMetrica;
import com.kivsw.phonerecorder.model.metrica.MetricaModule;
import com.kivsw.phonerecorder.model.persistent_data.PersistentDataModule;
import com.kivsw.phonerecorder.model.player.AndroidPlayer;
import com.kivsw.phonerecorder.model.player.AndroidPlayerModule;
import com.kivsw.phonerecorder.model.settings.SettingsModule;
import com.kivsw.phonerecorder.model.task_executor.TaskExecutorModule;
import com.kivsw.phonerecorder.model.task_executor.tasks.TaskModule;
import com.kivsw.phonerecorder.os.jobs.AppService;
import com.kivsw.phonerecorder.ui.ErrorMessage.MvpErrorMessageBuilderModule;
import com.kivsw.phonerecorder.ui.main_activity.MainActivity;
import com.kivsw.phonerecorder.ui.main_activity.MainActivityModule;
import com.kivsw.phonerecorder.ui.notification.NotificationShowerModule;
import com.kivsw.phonerecorder.ui.player.PlayerPresenter;
import com.kivsw.phonerecorder.ui.player.PlayerPresenterModule;
import com.kivsw.phonerecorder.ui.record_list.RecordListFragment;
import com.kivsw.phonerecorder.ui.record_list.RecordListPresenterModule;
import com.kivsw.phonerecorder.ui.record_list.operations.OperationsModule;
import com.kivsw.phonerecorder.ui.settings.SettingsFragment;
import com.kivsw.phonerecorder.ui.settings.SettingsPresenterModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ivan on 3/21/18.
 */

@Component(modules={SettingsPresenterModule.class, RecordListPresenterModule.class, SettingsModule.class,
           ApplicationModule.class, DiskRepresentativeModule.class, AndroidPlayerModule.class, PlayerPresenterModule.class,
           CloudCacheModule.class, PersistentDataModule.class, TaskExecutorModule.class, InternalFilesModule.class,
           MvpErrorMessageBuilderModule.class, MetricaModule.class, OperationsModule.class, AddrBookModule.class,
           TaskModule.class, NotificationShowerModule.class, ErrorProcessorModule.class, MainActivityModule.class,
           })
@Singleton
public interface ApplicationComponent {
    void inject(MainActivity activity);
    void inject(SettingsFragment fragment);
    void inject(RecordListFragment fragment);
    void inject(AppReceiver receiver);
    void inject(AppService service);
    //void inject(AppJobService service);
    void inject(MyApplication app);

    PlayerPresenter getInnerPlayer();
    AndroidPlayer getAndroidPlayer();

    IMetrica getMetrica();


}
