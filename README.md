# ReadSMSAndroid
Android Read SMS using Google API


Google has restricted Read SMS from users mobile. But they given an API to get OTP and auto filling OTP. Here we go and see the steps.

#### Add the dependency in your app level ```build.gradle``` file

       // Add at app level gradle file
    implementation 'com.google.android.gms:play-services-base:16.0.1'
    implementation 'com.google.android.gms:play-services-identity:16.0.0'
    implementation 'com.google.android.gms:play-services-auth:16.0.1'
    implementation 'com.google.android.gms:play-services-auth-api-phone:16.0.0'
    
    
#### In your project level ```build.gradle``` add below dependancy
      // Add at project level gradle file
      classpath 'com.google.gms:google-services:4.3.0'


#### Add ```receiver``` in your ```AndroidManifest.xml``` file

              <receiver
                android:name=".util.SMSReceiver"
                android:exported="true">
                  <intent-filter>
                    <action android:name="com.google.android.gms.auth.api.phone.SMS_RETRIEVED"/>
                 </intent-filter>
              </receiver>

#### Get Hash key with below line
     
      private fun getHashKey() {
         Log.d("APP_HASH_KEY ", "Apps Hash Key: " + AppSignatureHashHelper(this).appSignatures[0])
     }

#### Your Backend has to set the SMS template with this above generated Hash Key. Like below
      
        <#> Your ExampleApp code is: 123ABC78 
        FA+9qCX9VSP

#### Start listening
 
         private fun startSMSListener() {
        try {
            smsReceiver = SMSReceiver()
            smsReceiver.setOTPListener(this)

            val intentFilter = IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter)

            val client = SmsRetriever.getClient(this)

            val task = client.startSmsRetriever();
            task.addOnSuccessListener {

            }

            task.addOnFailureListener {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
