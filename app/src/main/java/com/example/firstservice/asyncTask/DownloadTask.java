package com.example.firstservice.asyncTask;

import android.os.AsyncTask;
import android.os.Environment;

import com.example.firstservice.listener.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    //文课件下载状态
    private static final int TYPE_SUCCEED=0;
    private static final int TYPE_FAILED=1;
    private static final int TYPE_CANCELED=3;
    private static final int TYPE_PASUED=2;

    private DownloadListener downloadListener;
    private boolean isCanceled=false;
    private boolean isPasued=false;

    private int lastProgress;

    public DownloadTask(DownloadListener downloadListener){
        this.downloadListener=downloadListener;
    }
    @Override
    protected Integer doInBackground(String... strings) {  //前台传递数据
        RandomAccessFile saveFile=null;
        InputStream is=null;
        long downloadLength=0;
        File file=null;
        try {
            String downloadUrl=strings[0];
            String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));//获取file名字
            String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file=new File(directory+fileName);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if(file.exists()){
                downloadLength= file.length();
            }
            long contentLength= 0;
            contentLength = getContentLength(downloadUrl);
            if(contentLength==0){
                return TYPE_FAILED;
            }else if(contentLength==downloadLength){
                return TYPE_SUCCEED;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(downloadUrl)
                    .addHeader("RANGE", "bytes=" + downloadLength)
                    .build();
            Response response = client.newCall(request).execute();
            if (response!=null){
                is = response.body().byteStream();
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(downloadLength);
                byte[] bytes = new byte[1024];
                int total=0;
                int len=0;
                while ((len=is.read())!=-1){
                    if (isCanceled){
                        return  TYPE_CANCELED;
                    }else if(isPasued){
                        return  TYPE_PASUED;
                    }else {
                        total+=len;
                    }
                    saveFile.write(bytes,0,len);
                    int progress=(int) ((total+downloadLength)*100/contentLength);
                    publishProgress(progress);
                }
            }
            response.body().close();
            return TYPE_SUCCEED;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
                try {
                  if (is!=null){
                    is.close();
                  }
                  if(saveFile!=null){
                    saveFile.close();
                  }
                  if (isCanceled&&file!=null){
                      file.delete();
                  }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status){
            case TYPE_SUCCEED:
                downloadListener.onSuccess();
                break;
            case TYPE_FAILED:
                downloadListener.onFailed();
                break;
            case TYPE_PASUED:
                downloadListener.onPaused();
                break;
            case TYPE_CANCELED:
                downloadListener.onCanceled();
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress=values[0];
        if(progress>lastProgress){
            downloadListener.onProgress(progress);
            lastProgress=progress;
        }
    }

    public void pauseDownload(){
        isPasued=true;

    }
    public void cancleDownload(){
        isCanceled=true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = client.newCall(request).execute();
        if(response!=null&&response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
