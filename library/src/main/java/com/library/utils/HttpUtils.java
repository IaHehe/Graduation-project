package com.library.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_FORM_DATA = MediaType.parse("multipart/form-data;charset=utf-8");
    private static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpeg");

    private static OkHttpClient client = null;

    private HttpUtils() {}

    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (HttpUtils.class) {
                if (client == null)
                    client = new OkHttpClient.Builder()
                            .connectTimeout(180, TimeUnit.SECONDS) // 连接超时
                            .writeTimeout(120, TimeUnit.SECONDS) // 写入超时
                            .readTimeout(120, TimeUnit.SECONDS) // 读取超时
                            .build();
            }
        }

        return client;
    }

    /**
     * Get请求
     *
     * @param url
     * @param callback
     */
    public static void doGet(String url, Callback callback) {
        LogUtil.i("请求地址："+url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * Post请求发送键值对数据
     *
     * @param url
     * @param mapParams
     * @param callback
     */
    public static void doPost(String url, Map<String, String> mapParams, Callback callback) {
        LogUtil.i("请求地址："+url);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : mapParams.keySet()) {
            builder.add(key, mapParams.get(key));
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * Post请求发送JSON数据
     *
     * @param url
     * @param jsonParams
     * @param callback
     */
    public static void doPost(String url, String jsonParams, Callback callback) {
        LogUtil.i("请求地址："+url);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * 批量上传文件
     * @param url
     * @param mapParams
     * @param fileKey 上传文件的key，服务端要求的参数名
     * @param filesPath  上传文件的路径
     * @param callback
     */
    public static void doPostForm(String url, Map<String, String> mapParams, String fileKey, List<String> filesPath, Callback callback) {
        LogUtil.i("请求地址："+url);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(int i = 0; i < filesPath.size(); i ++) {
            File f = new File(filesPath.get(i));
            if(null != f) {
                builder.addFormDataPart(fileKey,f.getName(),
                        RequestBody.create(MediaType.parse(judgeType(f.getPath())), f));
            }
        }

        for (String key : mapParams.keySet()) {
            builder.addFormDataPart(key, mapParams.get(key));
        }

        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(builder.build())//添加请求体
                .build();

        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * 上传byte流文件
     * @param url
     * @param mapParams
     * @param fileKey
     * @param bytes
     * @param callback
     */
    public static void doPostFromByStream(String url, Map<String, String> mapParams,
                                          String fileKey, byte[] bytes, Callback callback) {

        LogUtil.i(url);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart(fileKey, System.currentTimeMillis()+".jpg",
                RequestBody.create(MEDIA_TYPE_IMAGE, bytes));

        for (String key : mapParams.keySet()) {
            builder.addFormDataPart(key, mapParams.get(key));
        }

        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(builder.build())//添加请求体
                .build();

        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param pathName
     * @param fileName
     * @param callback
     */
    public static void doFile(String url, String pathName, String fileName, Callback callback) {
        LogUtil.i("请求地址："+url);
        //判断文件类型
        MediaType MEDIA_TYPE = MediaType.parse(judgeType(pathName));
        //创建文件参数
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(MEDIA_TYPE.type(), fileName,
                        RequestBody.create(MEDIA_TYPE, new File(pathName)));
        //发出请求参数
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "9199fdef135c122")
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);

    }

    /**
     * 根据文件路径判断MediaType
     *
     * @param path
     * @return
     */
    private static String judgeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 下载文件
     * @param url
     * @param fileDir
     * @param fileName
     */
    public static void downFile(String url, final String fileDir, final String fileName) {
        LogUtil.i("请求地址："+url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(fileDir, fileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                }
            }
        });
    }
}
