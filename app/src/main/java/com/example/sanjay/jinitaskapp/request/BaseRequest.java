package com.example.sanjay.jinitaskapp.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;




public class BaseRequest extends Request {
    private static final String TAG = BaseRequest.class.getSimpleName();
    private final Gson gson = new Gson();


    private final Response.Listener listener;

    public BaseRequest(String url, Response.Listener responseListener, Response.ErrorListener listener) {
        super(Request.Method.GET, url, listener);
        Log.e(TAG, "Requesting url : " + url);
        this.listener = responseListener;
    }
    public BaseRequest(int method, String url, Response.Listener responseListener, Response.ErrorListener listener) {
        super(method, url, listener);
        Log.e(TAG, "Requesting url : " + url);
        this.listener = responseListener;
    }

    @Override
    public Response parseNetworkResponse(NetworkResponse response) {

        try {
            String json = null;
            json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Log.e(TAG, "JSON : "+ json);

            return Response.success(json,HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return Response.error(new ParseError(e));

        } catch (JsonSyntaxException e) {
            e.printStackTrace();

            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        volleyError.printStackTrace();
        return volleyError;
    }

    @Override
    protected void deliverResponse(Object response) {
        listener.onResponse(response);
    }


}
