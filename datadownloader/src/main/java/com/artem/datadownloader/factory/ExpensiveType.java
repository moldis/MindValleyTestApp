package com.artem.datadownloader.factory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExpensiveType {

    byte[] mData;

    public ExpensiveType(byte[] bytes){
        mData = bytes;
    }

    public byte[] asRawData(){
        return mData;
    }

    public JSONObject asJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject(new String(mData));
        return jsonObject;
    }

    public JSONArray asJSONArray() throws JSONException {
        JSONArray jsonArray = new JSONArray(new String(mData));
        return jsonArray;
    }

    public Bitmap asBitmap(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(mData, 0,mData.length);
        return bitmap;
    }

    public Object asPDF(){
        return null;
    }
}
