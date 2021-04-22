package com.advs.train.core;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class RestRequest<T> {
    private String mUrl;
    private String mMethod;
    private String mQuery;
    private String mRole;
    private String mPostData;
    private String mUsername;
    private String mPassword;
    private String mContentType;
    private HashMap<String, String> mHeaders;
    private Class<T> mReturnType;
    private boolean mTamaleHeader = false;

    public RestRequest(Class<T> pReturnType)
    {
        mHeaders = new HashMap<>();
        mReturnType = pReturnType;
    }

    /**
     * Get the url of the request.
     *
     * @return The url.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Set the url of the request.
     *
     * @param url The url for the request.
     */
    public void setUrl(String url) {
        mUrl = url;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    /**
     * Gets the REST method (GET, POST, etc).
     *
     * @return The REST method.
     */
    public String getMethod() {
        return mMethod;
    }

    /**
     * Set the method for the request.
     *
     * @param method The method of the request (GET, POST, etc).
     */
    public void setMethod(String method) {
        mMethod = method;
    }

    /**
     * Set the content type of the request.
     *
     * @param pContentType
     */
    public void setContentType(String pContentType){
        mContentType = pContentType;
    }

    /**
     * Get the content Type of the request.
     *
     * @return Teh content type.
     */
    public String getContentType(){
        return mContentType;
    }

    /**
     * Get the query string.
     *
     * @return Get the query string.
     */
    public String getQuery() {
        return mQuery;
    }

    /**
     * Set the query string. This does not have to be
     * separate from the url; add the query string in either
     * place.
     *
     * @param query The query string.
     */
    public void setQuery(String query) {
        mQuery = query;
    }

    /**
     * Set the post body to send with the request.
     *
     * @param pBody The string to post.
     */
    public void setPostBody(String pBody){
        mPostData = pBody;
    }

    /**
     * Get the post data on the request.
     *
     * @return The String that will be posted.
     */
    public String getPostBody(){
        return mPostData;
    }

    /**
     * Get the header collection.
     *
     * @return The header collection.
     */
    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    /**
     * Set the header collection.
     *
     * @param headers The header collection.
     */
    public void setHeaders(HashMap<String, String> headers) {
        mHeaders = headers;
    }

    /**
     * Get the class specified for the response data.
     *
     * @return The genericized class.
     */
    public Class<T> getResponseClass() {
        return mReturnType;
    }

    /**
     * Get the authentication security role.
     *
     * @return The security role.
     */
    public String getRole() {
        return mRole;
    }

    /**
     * Get the authentication security role.
     *
     * @param role The security role.
     */
    public void setRole(String role) {
        mRole = role;
    }

    /**
     * Should this connection have the custom tamale
     * header.
     *
     * @return Whether to add a tamale header.
     */
    public boolean isTamaleHeader() {
        return mTamaleHeader;
    }

    /**
     * Set whether the tamale custom authentication
     * header should be added.
     *
     * @param tamaleHeader True if the header should be added.
     */
    public void setTamaleHeader(boolean tamaleHeader) {
        mTamaleHeader = tamaleHeader;
    }


}
