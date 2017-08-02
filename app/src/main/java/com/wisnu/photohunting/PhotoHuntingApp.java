package com.wisnu.photohunting;/*
 * Copyright (C) 2015 PT.Gamatechno Indonesia.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

public class PhotoHuntingApp extends Application {
    public static final String USER_ID     = "USER_ID";
    public static final String USER_NAME   = "USER_NAME";
    public static final String USER_MAIL   = "USER_MAIL";
    public static final String USER_PHOTO  = "USER_PHOTO";
    public static final String USER_STATUS = "USER_STATUS";

    @Override
    public void onCreate() {
        super.onCreate();

        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build(new HawkBuilder.Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Hawk success initializing");
                    }

                    @Override
                    public void onFail(Exception e) {
                        System.out.println("Hawk failed");
                    }
                });
    }
}
