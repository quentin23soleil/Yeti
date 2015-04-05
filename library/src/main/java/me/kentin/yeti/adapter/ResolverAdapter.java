package me.kentin.yeti.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import me.kentin.yeti.R;
import me.kentin.yeti.utils.ChooserHistory;

public class ResolverAdapter extends RecyclerView.Adapter<ResolverAdapter.ViewHolder> {

    private static final String TAG = ResolverAdapter.class.getSimpleName();

    private final ResolverComparator comparator;

    private Intent shareIntent;

    private final PackageManager packageManager;

    private final int iconSize;

    private ArrayList<Intent> intents;

    private List<DisplayResolveInfoWithIntent> items;

    private OnItemClickedListener onItemClickedListener;

    private HashMap<String, Integer> priorities;

    private ChooserHistory history;

    public ResolverAdapter(Context context, Intent shareIntent, OnItemClickedListener listener) {
        packageManager = context.getPackageManager();
        iconSize = context.getResources().getDimensionPixelSize(R.dimen.item_share_icon);
        this.shareIntent = shareIntent;
        comparator = new ResolverComparator(context);
        setOnItemClickedListener(listener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.r_application, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ensureListBuilt();

        final DisplayResolveInfoWithIntent item = items.get(position);

        holder.text.setText(item.getLabel(packageManager));
        holder.text.setCompoundDrawables(null, null, null, null);

        new IconLoaderTask(item, holder.text).execute();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickedListener.onItemClicked(item.intent, item.resolveInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        ensureListBuilt();
        return items.size();
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        onItemClickedListener = listener;
    }

    private void rebuildItems() {
        items = null;
        notifyDataSetChanged();
    }

    private void ensureListBuilt() {
        if (items == null) {
            buildList();
        }
    }

    private void buildList() {
        if (intents != null) {
            items = new ArrayList<>(intents.size());
            for (Intent intent : intents) {
                // new Intent to get a normal intent in case of a LabeledIntent
                ActivityInfo ai = new Intent(intent)
                        .resolveActivityInfo(packageManager, PackageManager.GET_ACTIVITIES);
                if (ai == null) {
                    Log.w(TAG, "No activity found for " + intent);
                    continue;
                }
                ResolveInfo resolveInfo = new ResolveInfo();
                resolveInfo.activityInfo = ai;
                if (intent instanceof LabeledIntent) {
                    LabeledIntent labeledIntent = (LabeledIntent) intent;
                    resolveInfo.resolvePackageName = labeledIntent.getSourcePackage();
                    resolveInfo.labelRes = labeledIntent.getLabelResource();
                    resolveInfo.nonLocalizedLabel = labeledIntent.getNonLocalizedLabel();
                    resolveInfo.icon = labeledIntent.getIconResource();
                }

                if (resolveInfo.icon == 0 || resolveInfo.labelRes == 0) {
                    try {
                        ApplicationInfo info = packageManager.getApplicationInfo(
                                intent.getPackage(), 0);
                        if (resolveInfo.icon == 0) {
                            resolveInfo.icon = info.icon;
                        }
                        if (resolveInfo.labelRes == 0) {
                            resolveInfo.labelRes = info.labelRes;
                        }
                    } catch (PackageManager.NameNotFoundException ignored) {
                    }
                }

                items.add(new DisplayResolveInfoWithIntent(resolveInfo, intent));
            }
        } else {
            List<ResolveInfo> resolveInfos = packageManager
                    .queryIntentActivities(shareIntent, PackageManager.GET_ACTIVITIES);
            items = new ArrayList<>(resolveInfos.size());
            for (ResolveInfo resolveInfo : resolveInfos) {
                Intent intent = new Intent(shareIntent);
                intent.setClassName(resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                items.add(new DisplayResolveInfoWithIntent(resolveInfo, intent));
            }
        }

        Collections.sort(items, comparator);
    }

    private class DisplayResolveInfoWithIntent {

        private final Intent intent;

        private ResolveInfo resolveInfo;

        private CharSequence label;

        private Drawable icon;

        public DisplayResolveInfoWithIntent(ResolveInfo resolveInfo, Intent intent) {
            this.resolveInfo = resolveInfo;
            this.intent = intent;
        }

        public CharSequence getLabel(PackageManager pm) {
            if (label == null) {
                label = resolveInfo.loadLabel(pm);
            }
            return label;
        }

        public Drawable getIcon(PackageManager pm) {
            if (icon == null) {
                icon = resolveInfo.loadIcon(pm);
            }
            return icon;
        }
    }

    class ResolverComparator implements Comparator<DisplayResolveInfoWithIntent> {

        private final Collator collator;

        public ResolverComparator(Context context) {
            collator = Collator.getInstance(context.getResources().getConfiguration().locale);
        }

        @Override
        public int compare(DisplayResolveInfoWithIntent lhs, DisplayResolveInfoWithIntent rhs) {
            if (history != null) {
                int leftCount = history.get(lhs.resolveInfo.activityInfo.packageName);
                int rightCount = history.get(rhs.resolveInfo.activityInfo.packageName);
                if (leftCount != rightCount) {
                    return rightCount - leftCount;
                }
            }

            if (priorities != null) {
                int leftPriority = getPriority(lhs);
                int rightPriority = getPriority(rhs);
                if (leftPriority != rightPriority) {
                    return rightPriority - leftPriority;
                }
            }

            CharSequence sa = lhs.getLabel(packageManager);
            if (sa == null) {
                sa = lhs.resolveInfo.activityInfo.name;
            }
            CharSequence sb = rhs.getLabel(packageManager);
            if (sb == null) {
                sb = rhs.resolveInfo.activityInfo.name;
            }

            return collator.compare(sa.toString(), sb.toString());
        }

        private int getPriority(DisplayResolveInfoWithIntent lhs) {
            Integer integer = priorities.get(lhs.resolveInfo.activityInfo.packageName);
            return integer != null ? integer : 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public interface OnItemClickedListener {

        void onItemClicked(Intent intent, ResolveInfo resolveInfo);
    }

    private class IconLoaderTask extends AsyncTask<Void, Void, Drawable> {

        private final WeakReference<TextView> textView;

        private final DisplayResolveInfoWithIntent info;

        public IconLoaderTask(DisplayResolveInfoWithIntent info, TextView textView) {
            textView.setTag(R.id.resolver_icon, this);
            this.textView = new WeakReference<>(textView);
            this.info = info;
        }

        @Override
        protected Drawable doInBackground(Void... params) {
            Drawable icon = info.getIcon(packageManager);
            icon.setBounds(0, 0, iconSize,
                    (int) (((float) icon.getIntrinsicHeight()) / icon.getIntrinsicWidth()
                            * iconSize));
            return icon;
        }

        @Override
        protected void onPostExecute(Drawable icon) {
            super.onPostExecute(icon);
            TextView textView = this.textView.get();
            if (textView != null && textView.getTag(R.id.resolver_icon) == this) {
                textView.setCompoundDrawables(icon, null, null, null);
            }
        }
    }}
