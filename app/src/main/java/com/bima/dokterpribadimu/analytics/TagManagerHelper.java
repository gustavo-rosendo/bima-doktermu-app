package com.bima.dokterpribadimu.analytics;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.bima.dokterpribadimu.R;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by gustavo.santos on 7/22/2016.
 */
public class TagManagerHelper {
    private static final String TAG = TagManagerHelper.class.getSimpleName();

    private static final long TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS = 2000;
    private static final String CONTAINER_ID = "GTM-PS46H8";

    public static void initializeTagManager(Context context) {
        TagManager tagManager = TagManager.getInstance(context);

        // Modify the log level of the logger to print out not only
        // warning and error messages, but also verbose, debug, info messages.
        tagManager.setVerboseLoggingEnabled(true);

        PendingResult<ContainerHolder> pending =
                tagManager.loadContainerPreferFresh(CONTAINER_ID, -1);

        // The onResult method will be called as soon as one of the following happens:
        //     1. a saved container is loaded
        //     2. if there is no saved container, a network container is loaded
        //     3. the 2-second timeout occurs
        pending.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(ContainerHolder containerHolder) {
                ContainerHolderSingleton.setContainerHolder(containerHolder);
                Container container = containerHolder.getContainer();
                if (!containerHolder.getStatus().isSuccess()) {
                    Log.e(TAG, "setResultCallback()->onResult(): failure loading container");
                    return;
                }
                ContainerLoadedCallback.registerCallbacksForContainer(container);
                containerHolder.setContainerAvailableListener(new ContainerLoadedCallback());
            }
        }, TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS, TimeUnit.MILLISECONDS);
    }


    private static class ContainerLoadedCallback implements ContainerHolder.ContainerAvailableListener {
        @Override
        public void onContainerAvailable(ContainerHolder containerHolder, String containerVersion) {
            // We load each container when it becomes available.
            Container container = containerHolder.getContainer();
            registerCallbacksForContainer(container);
        }

        public static void registerCallbacksForContainer(Container container) {
//            // Register two custom function call macros to the container.
//            container.registerFunctionCallMacroCallback("increment", new CustomMacroCallback());
//            container.registerFunctionCallMacroCallback("mod", new CustomMacroCallback());

            // Register a custom function call tag to the container.
            container.registerFunctionCallTagCallback("New Registration by Email", new CustomTagCallback());
        }
    }

//    private static class CustomMacroCallback implements Container.FunctionCallMacroCallback {
//        private int numCalls;
//
//        @Override
//        public Object getValue(String name, Map<String, Object> parameters) {
//            if ("increment".equals(name)) {
//                return ++numCalls;
//            } else if ("mod".equals(name)) {
//                return (Long) parameters.get("key1") % Integer.valueOf((String) parameters.get("key2"));
//            } else {
//                throw new IllegalArgumentException("Custom macro name: " + name + " is not supported.");
//            }
//        }
//    }

    private static class CustomTagCallback implements Container.FunctionCallTagCallback {
        @Override
        public void execute(String tagName, Map<String, Object> parameters) {
            // The code for firing this custom tag.
            Log.i(TAG, "Custom function call tag :" + tagName + " is fired.");
        }
    }

}
