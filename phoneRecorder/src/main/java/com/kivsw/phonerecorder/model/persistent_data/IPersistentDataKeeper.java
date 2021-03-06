package com.kivsw.phonerecorder.model.persistent_data;

/**
 * Created by ivan on 5/8/18.
 */

public interface IPersistentDataKeeper {
    class CallInfo
    {
        public String number;
        public boolean isIncome;
        public long elapsed_ms;
        public String toString(){return String.format("%s %s time:%d", number, isIncome?"in":"out", elapsed_ms/1000);};
    };

    void setCallInfo(String number, boolean isIncome);
    CallInfo getCallInfo();

    long getLastIncomeSms();
    void setLastIncomeSms(long v);
    long getLastOutgoingSms();
    void setLastOutgoingSms(long v);

    long getLastTimeOfReadingAddrBook();
    void setLastTimeOfReadingAddrBook(long time);
}
