package me.kentin.yeti;

import android.content.Intent;

public abstract class OnShareListener {

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param source The source of the notification.
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithFacebook(YetiActionProvider source, Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param source The source of the notification.
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithTwitter(YetiActionProvider source, Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param source The source of the notification.
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithGooglePlus(YetiActionProvider source, Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param source The source of the notification.
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithEmail(YetiActionProvider source, Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param source The source of the notification.
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithSms(YetiActionProvider source, Intent intent);

    /**
     * Called when a share target has been selected. The client can
     * decide whether to perform some action before the sharing is
     * actually performed OR handle the action itself.
     *
     * @param source The source of the notification.
     * @param intent The intent for launching the chosen share target.
     * @return Return true if you have handled the intent.
     */
    public abstract boolean shareWithOther(YetiActionProvider source, Intent intent);
}
