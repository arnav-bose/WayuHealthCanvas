package com.example.arnav.wayuhealth.SQLiteDatabase;

/**
 * Created by Arnav on 07/06/2016.
 */
public class ImageUploadStructure {

    int _id;
    String _memID;
    String _blobKey;
    String _servingURL;

    public ImageUploadStructure(){}

    public ImageUploadStructure(String _memID, String _blobKey, String _servingURL){
        this._memID = _memID;
        this._blobKey = _blobKey;
        this._servingURL = _servingURL;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_memID() {
        return _memID;
    }

    public void set_memID(String _memID) {
        this._memID = _memID;
    }

    public String get_blobKey() {
        return _blobKey;
    }

    public void set_blobKey(String _blobKey) {
        this._blobKey = _blobKey;
    }

    public String get_servingURL() {
        return _servingURL;
    }

    public void set_servingURL(String _servingURL) {
        this._servingURL = _servingURL;
    }
}
