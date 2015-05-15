package me.kentin.yeti.utils;

import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

public class SharePackageHelper {

    String[] twitter = new String[] {
            "com.klinker.android.twitter_l",
            "org.mariotaku.twidere",
            "com.levelup.touiteur",
            "jp.r246.twicca",
            "com.jv.materialfalcon",
            "com.twitter.android",
            "com.handmark.tweetcaster",
            "it.mvilla.android.fenix",
            "com.handmark.tweetcaster.premium",
            "com.twidroid",
            "com.echofon",
            "com.dwdesign.tweetings",
            "com.levelup.touiteurpremium",
            "com.jv.falcon",
            "com.saulmm.tweetwear",
            "com.seesmic",
            "net.sinproject.android.tweecha",
            "de.vanita5.twittnuker",
            "com.happydroid.tweetline.donate",
            "com.happydroid.tweetline",
            "com.handlerexploit.tweedle",
            "me.kentin.sharetest"
    };

    String[] facebook = new String[] {
            "com.facebook.katana",
            "app.fastfacebook.com",
            "com.androdb.fastlitefb",
            "com.danvelazco.fbwrapper"
    };

    String[] email = new String[] {
            "com.my.mail",
            "com.mail.emails",
            "com.yahoo.mobile.client.android.mail",
            "com.trtf.blue",
            "com.cloudmagic.mail",
            "com.email.email",
            "com.sonyericsson.extras.liveware.extension.mail",
            "com.google.android.gm",
            "com.mailboxapp",
            "com.fsck.k9",
            "com.google.android.apps.inbox",
            "com.boxer.email",
            "com.clearhub.wl",
            "com.google.android.email"
    };

    String[] googlePlus = new String[] {
            "com.google.android.apps.plus"
    };

    String[] sms = new String[] {
            "com.textra",
            "com.jb.gosms",
            "com.handcent.nextsms",
            "com.hellotext.hello",
            "com.p1.chompsms",
            "com.google.android.apps.messaging",
            "com.klinker.android.evolve_sms",
            "fr.slvn.mms",
            "com.android.mms",
            "com.google.android.talk",
            "com.facebook.orca",
            "com.whatsapp",
            "com.tencent.mm",
            "com.viber.voip"
    };

    public ApplicationType getApplicationType(Intent intent) {
        String packageName = intent.getComponent().getPackageName();

        for (String twitterApp : twitter) {
            if (twitterApp.equals(packageName)) {
                return ApplicationType.Twitter;
            }
        }
        for (String facebookApp : facebook) {
            if (facebookApp.equals(packageName)) {
                return ApplicationType.Facebook;
            }
        }
        for (String emailApp : email) {
            if (emailApp.equals(packageName)) {
                return ApplicationType.Email;
            }
        }
        for (String googlePlusApp : googlePlus) {
            if (googlePlusApp.equals(packageName)) {
                return ApplicationType.GooglePlus;
            }
        }
        for (String smsApp : sms) {
            if (smsApp.equals(packageName)) {
                return ApplicationType.Sms;
            }
        }
        return ApplicationType.Other;
    }

    public boolean isIntentAcceptable(ResolveInfo resolveInfo, List<ApplicationType> acceptableTypes) {
        if (acceptableTypes == null) {
            return true;
        }
        for (ApplicationType type : acceptableTypes) {
            String[] packageNames = getPackageNamesByType(type);
            if (packageNames != null) {
                for (String packageName : packageNames) {
                    if (resolveInfo.activityInfo.packageName.equals(packageName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private String[] getPackageNamesByType(ApplicationType type) {
        switch (type) {
            case Twitter:
                return twitter;
            case Facebook:
                return facebook;
            case Email:
                return email;
            case GooglePlus:
                return googlePlus;
            case Sms:
                return sms;
            default:
                return null;
        }
    }

    public enum ApplicationType {
        Twitter,
        Facebook,
        Email,
        GooglePlus,
        Sms,
        Other

    }

}
