# Yeti - Control what you share [![Build Status](https://travis-ci.org/dommerq/Yeti.svg?branch=master)](https://travis-ci.org/dommerq/Yeti) [![Build version](https://bintray.com/dommerq/Maven/Yeti/_latestVersion)](https://bintray.com/dommerq/Maven/Yeti/_latestVersion)

Do you want to customize what you share, depending on what the user wants to share it with?

Let's say you want to add a "via @theTwitterAccountOfYourProduct" if the content is being shared with a Twitter client.


###**BOOM! That's what Yeti does.**


## Add to your project  [![Build version](https://bintray.com/dommerq/Maven/Yeti/_latestVersion)](https://bintray.com/dommerq/Maven/Yeti/_latestVersion)

**Gradle (jcenter)**
```groovy
compile 'me.kentin:yeti:1.0.0'
```
**Maven**

```
Who uses that? Seriously.
```

Or you can clone it and import `yeti` as a module.


## Usage

Two options. Because there are 2 ways to share on Android.

  - using a shareActionProvider (the menu thing)
  - using a normal intent, with ACTION_SEND.

For both you're going to need an Intent anyway.


```java
shareIntent = new Intent();
shareIntent.setAction(Intent.ACTION_SEND);
shareIntent.setType("text/plain");
shareIntent.putExtra(Intent.EXTRA_TEXT, "This is the text BEFORE you change it bro. (if you want huh)");
```


### ShareActionProvider

Add the action provider to your menu xml.
```xml
<item
  android:id="@+id/menu_item_share"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  app:showAsAction="ifRoom"
  android:title="Share"
  app:actionProviderClass="me.kentin.yeti.YetiActionProvider" />

```

And set the intent in your activity:

```java

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar/toolbar if it is present.
    getMenuInflater().inflate(R.menu.menu_actionprovideractivity, menu);

    // Locate MenuItem with YetiActionProvider
    MenuItem item = menu.findItem(R.id.menu_item_share);

    // Fetch and store ShareActionProvider
    yetiActionProvider = (YetiActionProvider) MenuItemCompat.getActionProvider(item);

    // You don't have to Override everything though, just what you want
    yetiActionProvider.setOnShareListener(shareListener);
    yetiActionProvider.setShareIntent(shareIntent);

    return true;
}

```

### Normal way

Create the **Yeti intent** and then, `startActivityForResult` with it.

```java
Yeti yeti = Yeti.with(activity);
Intent intent = yeti.share(yourShareIntent)
startActivityForResult(yeti.share(shareIntent), REQUEST_CODE_YETI);
```

Override `onActivityResult` and use **THE SAME INSTANCE OF YETI** with the data.


```java

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_YETI) {
        yeti.result(data, shareListener);
    }
}
```
`shareListener` here is just a [OnShareListener](https://github.com/dommerq/Yeti/blob/master/library/src/main/java/me/kentin/yeti/listener/OnShareListener.java).


## Todo
- "I want to share with Twitter, and Facebook only. Filter the apps pretty please."
- Want something? Tell me. :)

## License

Yeti is licensed under the Apache 2 license (you can use it in commercial and open source projects).

```
Copyright 2015 Quentin Dommerc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
Anyway no one read this, so why would I care having meaningful License.
See the License for the specific language governing permissions and
limitations under the License.
```
