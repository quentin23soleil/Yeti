package me.kentin.yeti;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ActionProvider;
import android.support.v7.internal.widget.ActivityChooserModel;
import android.support.v7.internal.widget.ActivityChooserModel.OnChooseActivityListener;
import android.support.v7.internal.widget.ActivityChooserView;
import android.support.v7.internal.widget.TintManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

import me.kentin.yeti.listener.OnShareListener;
import me.kentin.yeti.utils.SharePackageHelper;

public class YetiActionProvider extends ActionProvider {

    public YetiActionProvider(Context context) {
        super(context);
        this.context = context;

    }

    /**
     * The default for the maximal number of activities shown in the sub-menu.
     */
    private static final int DEFAULT_INITIAL_ACTIVITY_COUNT = 4;

    /**
     * The the maximum number activities shown in the sub-menu.
     */
    private int maxShownActivityCount = DEFAULT_INITIAL_ACTIVITY_COUNT;

    /**
     * Listener for handling menu item clicks.
     */
    private final ShareMenuItemOnMenuItemClickListener shareMenuItemOnMenuItemClickListener =
            new ShareMenuItemOnMenuItemClickListener();

    /**
     * The default name for storing share history.
     */
    public static final String DEFAULT_SHARE_HISTORY_FILE_NAME = "share_history.xml";

    /**
     * Context for accessing resources.
     */
    private final Context context;

    /**
     * The name of the file with share history data.
     */
    private String shareHistoryFileName = DEFAULT_SHARE_HISTORY_FILE_NAME;

    private OnChooseActivityListener onChooseActivityListener;

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateActionView() {
        // Create the view and set its data model.
        ActivityChooserModel dataModel = ActivityChooserModel.get(context, shareHistoryFileName);
        ActivityChooserView activityChooserView = new ActivityChooserView(context);
        activityChooserView.setActivityChooserModel(dataModel);

        // Lookup and set the expand action icon.
        TypedValue outTypedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.actionModeShareDrawable, outTypedValue, true);
        Drawable drawable = TintManager.getDrawable(context, outTypedValue.resourceId);
        activityChooserView.setExpandActivityOverflowButtonDrawable(drawable);
        activityChooserView.setProvider(this);

        // Set content description.
        activityChooserView.setDefaultActionButtonContentDescription(
                R.string.abc_shareactionprovider_share_with_application);
        activityChooserView.setExpandActivityOverflowButtonContentDescription(
                R.string.abc_shareactionprovider_share_with);

        return activityChooserView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSubMenu() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        // Clear since the order of items may change.
        subMenu.clear();

        ActivityChooserModel dataModel = ActivityChooserModel.get(context, shareHistoryFileName);
        PackageManager packageManager = context.getPackageManager();

        final int expandedActivityCount = dataModel.getActivityCount();
        final int collapsedActivityCount = Math.min(expandedActivityCount, maxShownActivityCount);

        // Populate the sub-menu with a sub set of the activities.
        for (int i = 0; i < collapsedActivityCount; i++) {
            ResolveInfo activity = dataModel.getActivity(i);
            subMenu.add(0, i, i, activity.loadLabel(packageManager))
                    .setIcon(activity.loadIcon(packageManager))
                    .setOnMenuItemClickListener(shareMenuItemOnMenuItemClickListener);
        }

        if (collapsedActivityCount < expandedActivityCount) {
            // Add a sub-menu for showing all activities as a list item.
            SubMenu expandedSubMenu = subMenu.addSubMenu(Menu.NONE, collapsedActivityCount,
                    collapsedActivityCount,
                    context.getString(R.string.abc_activity_chooser_view_see_all));
            for (int i = 0; i < expandedActivityCount; i++) {
                ResolveInfo activity = dataModel.getActivity(i);
                expandedSubMenu.add(0, i, i, activity.loadLabel(packageManager))
                        .setIcon(activity.loadIcon(packageManager))
                        .setOnMenuItemClickListener(shareMenuItemOnMenuItemClickListener);
            }
        }
    }

    /**
     * Sets the file name of a file for persisting the share history which
     * history will be used for ordering share targets. This file will be used
     * for all view created by {@link #onCreateActionView()}. Defaults to
     * {@link #DEFAULT_SHARE_HISTORY_FILE_NAME}. Set to <code>null</code>
     * if share history should not be persisted between sessions.
     * <p/>
     * <strong>Note:</strong> The history file name can be set any time, however
     * only the action views created by {@link #onCreateActionView()} after setting
     * the file name will be backed by the provided file. Therefore, if you want to
     * use different history files for sharing specific types of content, every time
     * you change the history file {@link #setShareHistoryFileName(String)} you must
     * call {@link android.app.Activity#invalidateOptionsMenu()} to recreate the
     * action view. You should <strong>not</strong> call
     * {@link android.app.Activity#invalidateOptionsMenu()} from
     * {@link android.app.Activity#onCreateOptionsMenu(Menu)}."
     * <p/>
     * <code>
     * private void doShare(Intent intent) {
     * if (IMAGE.equals(intent.getMimeType())) {
     * mShareActionProvider.setHistoryFileName(SHARE_IMAGE_HISTORY_FILE_NAME);
     * } else if (TEXT.equals(intent.getMimeType())) {
     * mShareActionProvider.setHistoryFileName(SHARE_TEXT_HISTORY_FILE_NAME);
     * }
     * mShareActionProvider.setIntent(intent);
     * invalidateOptionsMenu();
     * }
     * <code>
     *
     * @param shareHistoryFile The share history file name.
     */
    public void setShareHistoryFileName(String shareHistoryFile) {
        shareHistoryFileName = shareHistoryFile;
        setActivityChooserPolicyIfNeeded();
    }

    /**
     * Sets an intent with information about the share action. Here is a
     * sample for constructing a share intent:
     * <p>
     * <pre>
     * <code>
     *  Intent shareIntent = new Intent(Intent.ACTION_SEND);
     *  shareIntent.setType("image/*");
     *  Uri uri = Uri.fromFile(new File(getFilesDir(), "foo.jpg"));
     *  shareIntent.putExtra(Intent.EXTRA_STREAM, uri.toString());
     * </pre>
     * </code>
     * </p>
     *
     * @param shareIntent The share intent.
     * @see Intent#ACTION_SEND
     * @see Intent#ACTION_SEND_MULTIPLE
     */
    public void setShareIntent(Intent shareIntent) {
        ActivityChooserModel dataModel = ActivityChooserModel.get(context,
                shareHistoryFileName);
        dataModel.setIntent(shareIntent);
    }

    /**
     * Reusable listener for handling share item clicks.
     */
    private class ShareMenuItemOnMenuItemClickListener implements OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            ActivityChooserModel dataModel = ActivityChooserModel.get(context,
                    shareHistoryFileName);
            final int itemId = item.getItemId();
            Intent launchIntent = dataModel.chooseActivity(itemId);
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                context.startActivity(launchIntent);
            }
            return true;
        }
    }

    /**
     * Set the activity chooser policy of the model backed by the current
     * share history file if needed which is if there is a registered callback.
     */
    private void setActivityChooserPolicyIfNeeded() {
        if (onShareListener == null) {
            return;
        }
        if (onChooseActivityListener == null) {
            onChooseActivityListener = new ShareActivityChooserModelPolicy();
        }
        ActivityChooserModel dataModel = ActivityChooserModel.get(context, shareHistoryFileName);
        dataModel.setOnChooseActivityListener(onChooseActivityListener);
    }

    private class ShareActivityChooserModelPolicy implements ActivityChooserModel.OnChooseActivityListener {
        @Override
        public boolean onChooseActivity(ActivityChooserModel host, Intent intent) {

            if (onShareListener != null) {
                SharePackageHelper.ApplicationType applicationType = new SharePackageHelper().getApplicationType(intent);

                if (applicationType == SharePackageHelper.ApplicationType.Twitter) {
                    return onShareListener.shareWithTwitter(intent);
                } else if (applicationType == SharePackageHelper.ApplicationType.Facebook) {
                    return onShareListener.shareWithFacebook(intent);
                } else if (applicationType == SharePackageHelper.ApplicationType.GooglePlus) {
                    return onShareListener.shareWithGooglePlus(intent);
                } else if (applicationType == SharePackageHelper.ApplicationType.Email) {
                    return onShareListener.shareWithEmail(intent);
                } else if (applicationType == SharePackageHelper.ApplicationType.Sms) {
                    return onShareListener.shareWithSms(intent);
                } else {
                    return onShareListener.shareWithOther(intent);
                }
            }
            return false;
        }
    }

    private OnShareListener onShareListener; //also need to add getter and setter

    public OnShareListener getOnShareListener() {
        return onShareListener;
    }

    public void setOnShareListener(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
        setActivityChooserPolicyIfNeeded();
    }

}
