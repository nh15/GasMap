package com.nh.gasmap;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpAsyncTaskLoader extends AsyncTaskLoader<List<GasStation>> {
    private static final String TAG = "HttpAsyncTaskLoader";

    private String mUrl = null; //WebAPIのURL

    public HttpAsyncTaskLoader(Context context, String url) {
        super(context);
        Log.d(TAG, "create");
        mUrl = url;
    }

    // WebAPIの呼び出し(HTTP通信)を行う
    @Override
    public List<GasStation> loadInBackground() {
        Log.d(TAG, "loadInBackground()");

        List<GasStation> gasStationList = null;
        HttpURLConnection connection = null;
        URL url = null;
        String body = null;

        try {
            url = new URL(mUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // リダイレクトを自動で許可しない設定
            connection.setInstanceFollowRedirects(false);

            // URL接続からデータを読み取る
            connection.setDoInput(true);

            // 接続開始。connect()が実行された時点で、サーバーからレスポンスが返ってくる
            connection.connect();

            // 本文の取得
            InputStream inputStream = connection.getInputStream();
            //body = InputStreamToString(inputStream);

            /************************************************
             取得した情報(body)からどうやって必要な情報(緯度/経度)だけを取り出すの？
             ************************************************/
            gasStationList = parseXml(inputStream);

            inputStream.close();

        } catch (MalformedURLException e) {
            Log.e(TAG, "Catch the MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Catch the IOException");
            e.printStackTrace();
        }

        return gasStationList;
    }

    private String InputStreamToString(InputStream inputStream) throws IOException {
        Log.d(TAG, "InputStreamToString()");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }

    // xmlのデータを解析してgasStationにデータを詰め込む
    private List<GasStation> parseXml(InputStream inputStream) {
        Log.d(TAG, "parseXml()");

        GasStation gasStation = new GasStation();
        List<GasStation> gasStationList = new ArrayList<>();
        XmlPullParser xmlPullParser = Xml.newPullParser();
        boolean tagFlag = false;

        try {
            xmlPullParser.setInput(inputStream, "UTF-8");

            int eventType;
            eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG, "parseXml() Start document");

                        break;

                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parseXml() Start tag");
                        //TAGの名前を取得する
                        String startTagName = xmlPullParser.getName();
                        Log.d(TAG, "startTagName: " + startTagName);

                        //欲しい情報のTAGの場合、flagを立てる
                        ///////////テスト用////////////////
                        if (startTagName.equals("Version")) {
                            tagFlag = true;
                        }
                        ///////////ここまで////////////////
                        break;

                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parseXml() End tag");
                        String endTagName = xmlPullParser.getName();

                        //1件分の情報取得が完了したら(TAGで判定)、リストに追加し、gasStationをリセットする
                        ///////////テスト用////////////////
                        if (endTagName.equals("Version")) {
                            gasStationList.add(gasStation);
                            gasStation = new GasStation();
                        }
                        ///////////ここまで////////////////

                        break;

                    case XmlPullParser.TEXT:
                        Log.d(TAG, "parseXml() Text: " + xmlPullParser.getText());

                        // flagが立っていたら情報を取得し、flagを戻す
                        if (tagFlag) {
                            //////テスト用//////
                            Log.d(TAG, "parseXml() setBrand");
                            gasStation.setBrand(xmlPullParser.getText());
                            /////ここまで//////
                            tagFlag = false;
                        }
                        break;

                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            Log.d(TAG, "parseXml() Catch the Exception.");
            e.printStackTrace();
        }

        return gasStationList;
    }
}
