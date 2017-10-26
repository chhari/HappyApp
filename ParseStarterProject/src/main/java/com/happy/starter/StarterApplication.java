/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.happy.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;


public class  StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("641831b6ecb3ee37ef6d60b214c0bd3f3304a0b0")
            .clientKey("b541c0ae5de49231563524f931538677f8cadc5e")
            .server("http://ec2-54-202-49-47.us-west-2.compute.amazonaws.com:80/parse/")
            .build()
    );



  /*  ParseObject object = new ParseObject("Images");
    object.put("ourImages", "https://scontent.fbed1-1.fna.fbcdn.net/v/t31.0-8/18121898_1495985220451681_4532810255471297730_o.jpg?oh=03545d4d826a8705522566fc2533c546&oe=598ADCC0");

    object.saveInBackground(new SaveCallback () {
      @Override
      public void done(ParseException ex) {
        if (ex == null) {
          Log.i("Parse Result", "Successful!");
        } else {
          Log.i("Parse Result", "Failed" + ex.toString());
        }
      }
    });

    */

    //ParseUser.enableAutomaticUser();

    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);



  }
}
