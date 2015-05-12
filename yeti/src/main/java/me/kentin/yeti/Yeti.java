package me.kentin.yeti;

import android.content.Context;
import android.content.Intent;

import java.util.Arrays;
import java.util.List;

import me.kentin.yeti.activity.YetiShareActivity;
import me.kentin.yeti.listener.OnShareListener;
import me.kentin.yeti.utils.SharePackageHelper;

public class Yeti {

    private Context context;

    private List<SharePackageHelper.ApplicationType> acceptableTypes;

    Yeti(Context context) {
        this.context = context;
    }

    public static Yeti with(Context context) {
        return new Yeti(context);
    }

    public Yeti only(SharePackageHelper.ApplicationType... types) {
        this.acceptableTypes = Arrays.asList(types);
        return this;
    }

    public Intent share(Intent shareIntent) {
        return YetiShareActivity.createIntent(context, shareIntent, acceptableTypes);
    }

    public void result(Intent intent, OnShareListener onShareListener) {
        if (onShareListener != null) {

            SharePackageHelper.ApplicationType applicationType = new SharePackageHelper().getApplicationType(intent);

            if (applicationType == SharePackageHelper.ApplicationType.Twitter) {
                if (!onShareListener.shareWithTwitter(intent)) {
                    context.startActivity(intent);
                }
            } else if (applicationType == SharePackageHelper.ApplicationType.Facebook) {
                if (!onShareListener.shareWithFacebook(intent)) {
                    context.startActivity(intent);
                }
            } else if (applicationType == SharePackageHelper.ApplicationType.GooglePlus) {
                if (!onShareListener.shareWithGooglePlus(intent)) {
                    context.startActivity(intent);
                }
            } else if (applicationType == SharePackageHelper.ApplicationType.Email) {
                if (!onShareListener.shareWithEmail(intent)) {
                    context.startActivity(intent);
                }
            } else if (applicationType == SharePackageHelper.ApplicationType.Sms) {
                if (!onShareListener.shareWithSms(intent)) {
                    context.startActivity(intent);
                }
            } else {
                if (!onShareListener.shareWithOther(intent)) {
                    context.startActivity(intent);
                }
            }
        }
    }
}
