package com.example.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.iv_result);
    }

    public void doGet(View view) {
        //1.拿到OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        //2.构造Request
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .build();

        //3.将Request封装为Call
        Call call = client.newCall(request);

        try {
            //4.执行Call
            Response response = call.execute();
            L.e(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException("MainActivity doGet IOException " + e.getMessage());
        }

//         final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
//
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .cookieJar(new CookieJar() {
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                        cookieStore.put(url.host(), cookies);
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        List<Cookie> cookies = cookieStore.get(url.host());
//                        return cookies != null ? cookies : new ArrayList<Cookie>();
//                    }
//                })
//                .build();

    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void doPost(View view) {
        //1.拿到OkHttpClient对象
        OkHttpClient client = new OkHttpClient();

        //2.构造Request
        //2.1构造RequestBody
        RequestBody body = RequestBody.create(JSON, "");

//        RequestBody body=new FormBody.Builder()
//                .addEncoded("application/json","charset=utf-8")
//                .build();

        Request request = new Request.Builder()
                .url("http://www.imooc.com")
                .post(body)
                .build();

        //3.将Request封装为Call
        Call call = client.newCall(request);

        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e(response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //操作UI
                    }
                });
            }
        });

    }

    public static final MediaType JSON2 = MediaType.parse("text/plain; charset=utf-8");

    public void doPostString(View view) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(JSON2, "{username:chen,password:123}");

        Request request = new Request.Builder()
                .url("http://www.imooc.com")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e(response.body().string());
            }
        });
    }

    public static final MediaType JSON3 = MediaType.parse("application/ctet-stream; charset=utf-8");

    public void doPostFile(View view) {
        OkHttpClient client = new OkHttpClient();

        File file = new File(Environment.getExternalStorageDirectory(), "banner2.jpg");

        if (!file.exists()) {
            L.e(Environment.getExternalStorageDirectory() + "is not exists");
            return;
        }

        RequestBody requestBody = RequestBody.create(JSON3, file);

        Request request = new Request.Builder()
                .url("http://www.imooc.com")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public void doUpload(View view) {
        OkHttpClient client = new OkHttpClient();

        File file = new File(Environment.getExternalStorageDirectory(), "banner2.jpg");

        if (!file.exists()) {
            L.e(Environment.getExternalStorageDirectory() + "is not exists");
            return;
        }

        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addFormDataPart("files",
                        null,
                        new MultipartBody.Builder("BbC04y")
                                .addPart(Headers.of("Content-Disposition", "form-data; filename=\"img.png\""),
                                        RequestBody.create(MediaType.parse("image/png"), file)).build())
                .build();

        Request request = new Request.Builder()
                .url("http://www.imooc.com/upload")
                .post(body)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public void doDownload(View view) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                int len = 0;
                File file = new File(Environment.getExternalStorageDirectory() + "chendong.jpg");
                byte[] buf = new byte[128];
                FileOutputStream fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                is.close();
                L.e("download success");

            }
        });

    }

    public void doDownloadImage(View view) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });

            }
        });
    }


    /**
     * POST提交键值对
     * <p>
     * OkHttp也可以通过POST方式把键值对数据传送到服务器
     */
    public void post(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        /**
         * OkHttpClient client = new OkHttpClient();
         String post(String url, String json) throws IOException {
         RequestBody formBody = new FormEncodingBuilder()
         .add("platform", "android")
         .add("name", "bug")
         .add("subject", "XXXXXXXXXXXXXXX")
         .build();

         Request request = new Request.Builder()
         .url(url)
         .post(body)
         .build();

         Response response = client.newCall(request).execute();
         if (response.isSuccessful()) {
         return response.body().string();
         } else {
         throw new IOException("Unexpected code " + response);
         }
         }
         */

    }

    /**
     * Post方式提交流
     */

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    public void postIO() {
        final OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new RequestBody() {

            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Post方式提交表单
     */
    public void postFrom() {
        final OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();

        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Post方式提交分块请求，可以上传文件
     */

    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public void postFromPart() {
        final OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "logo-square.png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * HTTP头部的设置和读取
     */

    public void headerSet() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://github.com")
                .header("User-Agent", "My super agent")
                .addHeader("Accept", "text/html")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }

            System.out.println(response.header("Server"));
            System.out.println(response.headers("Set-Cookie"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Synchronous Get（同步Get）
     */
    public void Synchronous() {

        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        try {
            Response response = client.newCall(request).execute();//同步
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                L.e(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            L.e(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Asynchronous Get（异步Get）
     */
    public void Asynchronous() {

        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        client.newCall(request).enqueue(new Callback() {//异步，需要设置一个回调接口
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                System.out.println(response.body().string());
            }
        });

    }


}
