package com.recognizer.common.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.library.pojo.BasicEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by DELL on 2017/4/1.
 * 自定义异步任务类-处理网络上传任务
 */

/**
 *  public class MyTask extends AsyncTask<Params, Progrss, Result>
 *      Params: 调用异步任务时传入的类型 ;

        Progress : 字面意思上说是进度条, 实际上就是动态的由子线程向主线程publish数据的类型

        Result : 返回结果的类型
 * @param <T>
 */

public class NetWorkerAsync<T> extends AsyncTask<NetWorkerAsync.NetWorkerAsyncCallback<T>, Void, Object> {

    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_FORM_DATA = MediaType.parse("multipart/form-data;charset=utf-8");
    private static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/jpeg");

    private NetWorkerAsync.NetWorkerAsyncCallback<T> callback;
    private Class<T> t;
    private Bitmap bitmap;
    private String url;

    public NetWorkerAsync(Class<T> t, Bitmap bitmap, String url) {
        this.t = t;
        this.bitmap = bitmap;
        this.url = url;
    }

    public void setOnNetWorkerAsyncCallback(NetWorkerAsync.NetWorkerAsyncCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(NetWorkerAsync.NetWorkerAsyncCallback<T>... params) {
        callback = params[0];

        try {
            ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrOutStream);

            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();

            RequestBody requestBodyContent = RequestBody.create(MEDIA_TYPE_IMAGE
                    , byteArrOutStream.toByteArray());
            // 构建请求体
            RequestBody requestBody = new MultipartBody
                    .Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", "邹东军")
                    .addFormDataPart("number", "2013440020")
                    .addFormDataPart("password", "123")
                    .addFormDataPart("college.collegeCode", "5001")
                    .addFormDataPart("department.departmentCode", "50010")
                    .addFormDataPart("classes.classesCode", "500100")
                    .addFormDataPart("file", "2013444277.jpg", requestBodyContent)
                    .build();

            Request request = new Request
                    .Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            if(!response.isSuccessful()) {
                BasicEntity b = new BasicEntity();
                b.setMessage("服务器错误");
                return b.toString();
            }else {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(o instanceof String) {
            String responseBody = (String) o;
            Gson gson = new Gson();
            T t = gson.fromJson(responseBody, this.t);
            callback.onNetWorkerAsyncSuccess(t);
        }
        if(o instanceof Exception) {
            Exception e = (Exception) o;
            this.callback.onNetWorkerAsyncFailed(e);
        }

    }

    public interface NetWorkerAsyncCallback<S> {
        void onNetWorkerAsyncSuccess(S t);
        void onNetWorkerAsyncFailed(Exception e);
    }
}
