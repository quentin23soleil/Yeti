package me.kentin.yeti.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.kentin.yeti.R;
import me.kentin.yeti.adapter.ResolverAdapter;
import me.kentin.yeti.view.ResolverDrawerLayout;

import static me.kentin.yeti.utils.SharePackageHelper.ApplicationType;

public class YetiShareActivity extends Activity implements ResolverAdapter.OnItemClickedListener {

    public static final String EXTRA_SHARE_INTENT = "shareIntent";

    private static List<ApplicationType> acceptableTypes;

    private Intent shareIntent;

    public static Intent createIntent(Context context, Intent shareIntent, List<ApplicationType> acceptableTypes) {
        YetiShareActivity.acceptableTypes = acceptableTypes;
        Intent intent = new Intent(context, YetiShareActivity.class);
        intent.putExtra(EXTRA_SHARE_INTENT, shareIntent);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeti_resolver_list);

        ResolverDrawerLayout resolverDrawerLayout = (ResolverDrawerLayout) findViewById(R.id.resolver_contentPanel);
        TextView title = (TextView) findViewById(R.id.resolver_title);
        resolverDrawerLayout.setOnClickOutsideListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.resolver_recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ResolverAdapter adapter = createAdapter();

        recyclerview.setAdapter(adapter);

        resolverDrawerLayout.setScrollableChildView(recyclerview);

        title.setText(getString(R.string.whichSendApplication));
    }

    private ResolverAdapter createAdapter() {
        Intent intent = getIntent();
        shareIntent = intent.getParcelableExtra(EXTRA_SHARE_INTENT);
        return new ResolverAdapter(this, shareIntent, this, acceptableTypes);
    }

    @Override
    public void onItemClicked(Intent intent, ResolveInfo resolveInfo) {
        setResult(RESULT_OK, intent);
        finish();
    }
}
