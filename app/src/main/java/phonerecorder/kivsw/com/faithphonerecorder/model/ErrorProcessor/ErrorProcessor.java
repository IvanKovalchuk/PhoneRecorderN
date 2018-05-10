package phonerecorder.kivsw.com.faithphonerecorder.model.ErrorProcessor;

import java.util.List;

import io.reactivex.exceptions.CompositeException;
import phonerecorder.kivsw.com.faithphonerecorder.model.persistent_data.IJournal;
import phonerecorder.kivsw.com.faithphonerecorder.ui.main_activity.MainActivityContract;

/**
 * Created by ivan on 5/7/18.
 */

public class ErrorProcessor implements IErrorProcessor {

    private IJournal persistentData;
    private MainActivityContract.IMainActivityPresenter mainActivityPresenter;

    ErrorProcessor(IJournal persistentData, MainActivityContract.IMainActivityPresenter mainActivityPresenter)
    {
        this.persistentData = persistentData;
        this.mainActivityPresenter = mainActivityPresenter;
    }

    @Override
    public void onError(Throwable exception)
    {
        onError(exception, true);
    };
    @Override
    public void onError(Throwable exception, boolean writeToJournal)
    {
        if(exception instanceof CompositeException)
        {
            List<Throwable> exceptions=((CompositeException)exception).getExceptions();
            for(Throwable t:exceptions)
                onError(t);
            return;
        }

        if(writeToJournal)
           persistentData.journalAdd(exception);

        mainActivityPresenter.showErrorMessage(exception.getMessage());
    };
}
