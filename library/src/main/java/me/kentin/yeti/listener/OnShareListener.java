package me.kentin.yeti.listener;

import android.content.Intent;

public abstract class OnShareListener {

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithFacebook(Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithTwitter(Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithGooglePlus(Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithEmail(Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithSms(Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithOther(Intent intent);
}
