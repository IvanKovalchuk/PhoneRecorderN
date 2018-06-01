package phonerecorder.kivsw.com.faithphonerecorder.os;

import javax.inject.Singleton;

import dagger.Component;
import phonerecorder.kivsw.com.faithphonerecorder.model.CloudCacheModule;
import phonerecorder.kivsw.com.faithphonerecorder.model.DiskRepresentativeModule;
import phonerecorder.kivsw.com.faithphonerecorder.model.error_processor.ErrorProcessorModule;
import phonerecorder.kivsw.com.faithphonerecorder.model.persistent_data.PersistentDataModule;
import phonerecorder.kivsw.com.faithphonerecorder.model.player.AndroidPlayer;
import phonerecorder.kivsw.com.faithphonerecorder.model.player.AndroidPlayerModule;
import phonerecorder.kivsw.com.faithphonerecorder.model.settings.SettingsModule;
import phonerecorder.kivsw.com.faithphonerecorder.model.task_executor.TaskExecutorModule;
import phonerecorder.kivsw.com.faithphonerecorder.model.tasks.CallRecorder;
import phonerecorder.kivsw.com.faithphonerecorder.model.tasks.RecordSender;
import phonerecorder.kivsw.com.faithphonerecorder.model.tasks.SmsReader;
import phonerecorder.kivsw.com.faithphonerecorder.model.tasks.TaskModule;
import phonerecorder.kivsw.com.faithphonerecorder.ui.ErrorMessage.MvpErrorMessageBuilderModule;
import phonerecorder.kivsw.com.faithphonerecorder.ui.main_activity.MainActivity;
import phonerecorder.kivsw.com.faithphonerecorder.ui.main_activity.MainActivityModule;
import phonerecorder.kivsw.com.faithphonerecorder.ui.notification.NotificationShowerModule;
import phonerecorder.kivsw.com.faithphonerecorder.ui.player.PlayerPresenter;
import phonerecorder.kivsw.com.faithphonerecorder.ui.player.PlayerPresenterModule;
import phonerecorder.kivsw.com.faithphonerecorder.ui.record_list.RecordListFragment;
import phonerecorder.kivsw.com.faithphonerecorder.ui.record_list.RecordListPresenterModule;
import phonerecorder.kivsw.com.faithphonerecorder.ui.settings.SettingsFragment;
import phonerecorder.kivsw.com.faithphonerecorder.ui.settings.SettingsPresenterModule;

/**
 * Created by ivan on 3/21/18.
 */

@Component(modules={SettingsPresenterModule.class, RecordListPresenterModule.class, SettingsModule.class,
           ApplicationModule.class, DiskRepresentativeModule.class, AndroidPlayerModule.class, PlayerPresenterModule.class,
           CloudCacheModule.class, PersistentDataModule.class, TaskExecutorModule.class,
           TaskModule.class, NotificationShowerModule.class, ErrorProcessorModule.class, MainActivityModule.class,
           MvpErrorMessageBuilderModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(MainActivity activity);
    void inject(SettingsFragment fragment);
    void inject(RecordListFragment fragment);
    void inject(AppReceiver receiver);
    void inject(AppService service);
    void inject(MyApplication app);

    CallRecorder getCallRecorder();
    RecordSender getRecordSender();
    SmsReader getSmsReader();
    PlayerPresenter getInnerPlayer();
    AndroidPlayer getAndroidPlayer();


}
