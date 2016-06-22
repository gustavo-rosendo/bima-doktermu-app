package com.bima.dokterpribadimu.data.servertime;

import com.bima.dokterpribadimu.utils.Constants;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by apradanas.
 */
public class ServerTimeClient {

    private static final String TIME_SERVER = "time.nist.gov"; // server list: http://tf.nist.gov/tf-cgi/servers.cgi
    private static final int REQUEST_TIME_OUT_MILLIS = 30000; // 30 seconds

    /**
     * ServerTimeClient implementation to get SNTP Client
     * @return Observable<BaseResponse>
     */
    public Observable<SntpClient> getSntpClient() {
        return Observable.create(new Observable.OnSubscribe<SntpClient>() {
            @Override
            public void call(final Subscriber<? super SntpClient> subscriber) {
                SntpClient sntpClient = new SntpClient();
                try {
                    sntpClient.requestTime(TIME_SERVER, REQUEST_TIME_OUT_MILLIS);

                    subscriber.onNext(sntpClient);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e.toString().contains(Constants.SOCKET_TIMEOUT_EXCEPTION)) {
                        subscriber.onNext(null);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }
}
