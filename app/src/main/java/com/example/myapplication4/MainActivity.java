package com.example.myapplication4;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView pTextView;
    Button   pButton;
    String htmlPageUrl = "http://jyj.jje.hs.kr/jyj/0303/schedule?section=2"; //파싱할 홈페이지의 URL 주소
        String contentString = " ";
    Document doc = null;
    Elements contents;

    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pTextView =  (TextView) findViewById(R.id.TextView);
        pTextView.setMovementMethod(new ScrollingMovementMethod()); // 스크롤 가능한 텍스트 뷰로 만들기

        pButton =  (Button) findViewById(R.id.Button3);
        pButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DownloadFilesTask().execute();
            }
        });
    }

    private class DownloadFilesTask extends AsyncTask <Void, Integer, Void> {
        @Override protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                doc = Jsoup.connect(htmlPageUrl).get();
                //Log.d(">>>>>>>>>>>",doc.toString());
                contents = doc.select("dl.event");
                //Log.d(">>>>>>>>>>>",contents.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(Element e: contents) {
                Elements second = e.children();
                //Log.d(">>>1111>>>>", second.html());

                Elements third  = second.select("ul li");
                //Log.d(">>>2222>>>>", third.html());

                for (Element e1 : third) {
                    Elements links = e1.getElementsByTag("a");

                    Log.d(">>>>3333>>>>",  "\n");
                    for (Element link : links) {
                        cnt++;
                        String linkHref = link.attr("onclick");
                        Log.d(cnt+":" ,  " " + linkHref);
                    }
                }
            //    cnt++;
                //contentString += cnt +". "+ element.text() + "\n";
                //if(cnt == 10)//10위까지 파싱하므로
                //    break;
            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            contentString += htmlPageUrl + "\n";


            Log.i("TAG",""+contentString);
            pTextView.setText(contentString);
        }
    }
}


