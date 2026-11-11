
package com.dreamhome.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.OutputStream;

public class MainActivity extends Activity {

    private WebView webView;
    private ValueCallback<Uri[]> filePathCallback;

    private static final int FILE_CHOOSER_REQUEST = 1001;
    private static final int SAVE_FILE_REQUEST = 1002;

    private byte[] pendingFileData;
    private String pendingFileName;
    private String pendingMimeType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.addJavascriptInterface(new AndroidFileBridge(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view, WebResourceRequest request) {

                Uri uri = request.getUrl();
                String scheme = uri.getScheme();

                return !("https".equalsIgnoreCase(scheme)
                        || "http".equalsIgnoreCase(scheme)
                        || "file".equalsIgnoreCase(scheme)
                        || "about".equalsIgnoreCase(scheme));
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(
                    WebView view,
                    ValueCallback<Uri[]> callback,
                    FileChooserParams params) {

                if (filePathCallback != null) {
                    filePathCallback.onReceiveValue(null);
                }

                filePathCallback = callback;

                Intent intent = params.createIntent();

                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST);
                } catch (Exception e) {
                    filePathCallback = null;
                    Toast.makeText(MainActivity.this,
                            "File picker unavailable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            }
        });

        webView.loadUrl("file:///android_asset/index.html");
    }

    private class AndroidFileBridge {

        @JavascriptInterface
        public void saveFile(String fileName, String mimeType, String base64Data) {

            runOnUiThread(() -> {
                try {
                    pendingFileName = fileName;
                    pendingMimeType = mimeType;
                    pendingFileData = Base64.decode(base64Data, Base64.DEFAULT);

                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType(mimeType);
                    intent.putExtra(Intent.EXTRA_TITLE, fileName);

                    startActivityForResult(intent, SAVE_FILE_REQUEST);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,
                            "Unable to open save location",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_REQUEST) {
            if (filePathCallback != null) {
                Uri[] results = WebChromeClient.FileChooserParams
                        .parseResult(resultCode, data);

                filePathCallback.onReceiveValue(results);
                filePathCallback = null;
            }
        }

        if (requestCode == SAVE_FILE_REQUEST) {

            if (resultCode == RESULT_OK && data != null
                    && data.getData() != null && pendingFileData != null) {

                try {
                    Uri uri = data.getData();

                    OutputStream outputStream =
                            getContentResolver().openOutputStream(uri);

                    if (outputStream == null) {
                        throw new Exception("Cannot open file");
                    }

                    outputStream.write(pendingFileData);
                    outputStream.flush();
                    outputStream.close();

                    Toast.makeText(this,
                            "File saved successfully!",
                            Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(this,
                            "File saving failed!",
                            Toast.LENGTH_LONG).show();
                }
            }

            pendingFileData = null;
            pendingFileName = null;
            pendingMimeType = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
