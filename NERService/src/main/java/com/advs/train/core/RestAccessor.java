package com.advs.train.core;

import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class RestAccessor<T> {
    private RestRequest<T> mRequest;
    private Logger mLog = Logger.getLogger(RestAccessor.class);

    /**
     * Takes the request bean and makes the call to the server.
     *
     * @param pRequest The request bean.
     */
    public RestAccessor(RestRequest<T> pRequest) {
        mRequest = pRequest;
    }

    /**
     * Make the rest call and get back the data in the format chosen.
     *
     * @return The data from the server in the generic format.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public T makeCall() throws Exception {
        T returnVal = null;
        if (isMarshallable(mRequest.getResponseClass())) {
            returnVal = unmarshallObject();
        } else if (mRequest.getResponseClass() == String.class) {
            returnVal = (T) makeStringCall();
        } else if (mRequest.getResponseClass() == Byte.class || mRequest.getResponseClass() == Byte[].class || mRequest.getResponseClass() == byte.class || mRequest.getResponseClass() == byte[].class) {
            returnVal = (T) getBytes();
        } else {
            throw new MalformedURLException("Specified return type not supported. The RestAccessor can only return String, DOMDocument, Byte, or MarshalledObject");
        }
        return returnVal;
    }

    public byte[] makeBytesCall(Logger pLog) throws MalformedURLException, IOException {
        return getBytes(pLog);
    }

    /**
     * Check to see if the given class is a child of MarshalledObject.
     *
     * @param pClass The class to check.
     * @return Whether it is a child of MarshalledObject.
     */
    @SuppressWarnings("rawtypes")
    private boolean isMarshallable(Class pClass) {
        boolean hasMethod = false;
        Method[] methods = pClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals("unmarshalFromString")) {
                hasMethod = true;
                break;
            }
        }
        return hasMethod;
    }

    /**
     * Make the call based on the request bean and return the string
     * response.
     *
     * @return The raw string from the response.
     * @throws Exception
     */
    public String makeStringCall() throws MalformedURLException, IOException, NoSuchAlgorithmException, KeyManagementException {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);


        StringBuilder sb = new StringBuilder();
        HttpURLConnection connection = null;
        InputStream in = null;
        InputStreamReader reader = null;
        try {
            URL url = constructURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(mRequest.getMethod());
            setContentType(connection);
            addHeaders(connection);
            ensureAuthenticator(connection);
            sendOutput(connection);
            connection.connect();
            in = connection.getInputStream();
            if (connection.getContentEncoding() != null) {
                reader = new InputStreamReader(in, connection.getContentEncoding());
            } else {
                reader = new InputStreamReader(in);
            }
            char[] buffer = new char[4096];
            int currentBytes;
            while ((currentBytes = reader.read(buffer, 0, buffer.length)) > 0) {
                for (int x = 0; x < currentBytes; x++) {
                    sb.append(buffer[x]);
                }
            }
        } catch (Exception e) {
            mLog.error(e.getMessage(), e);
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (in != null) {
                in.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return sb.toString();
    }

    /**
     * Post the body to the endpoint if a post body is defined.
     *
     * @param pConnection The URL connection.
     * @throws IOException
     */
    private void sendOutput(HttpURLConnection pConnection) throws IOException {
        String data = "";
        if ((data = mRequest.getPostBody()) != null) {
            DataOutputStream writer = null;
            try {
                mLog.debug("Sending post data to endpoint.");
                pConnection.setDoOutput(true);
                writer = new DataOutputStream(pConnection.getOutputStream());
                writer.writeBytes(data);
                writer.flush();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } else {
            mLog.debug("No post data was defined, not sending anything.");
        }
    }

    private void setContentType(HttpURLConnection pConnection) {
        if (mRequest.getContentType() != null) {
            pConnection.setRequestProperty("Content-Type", mRequest.getContentType());
        }
    }

    /**
     * Add the headers from the request into this connection.
     *
     * @param pConnection The connection.
     */
    private void addHeaders(HttpURLConnection pConnection) {
        for (String key : mRequest.getHeaders().keySet()) {
            pConnection.setRequestProperty(key, mRequest.getHeaders().get(key));
        }
    }

    /**
     * Add the query to the URL if the url doesn't already have one.
     *
     * @return
     * @throws MalformedURLException
     */
    private URL constructURL() throws MalformedURLException {
        String urlString = mRequest.getUrl();
        String query = mRequest.getQuery();
        if (query != null && query.length() > 0 && query.charAt(0) == '?') {
            query = query.substring(1);
        }
        if (!urlString.contains("?") && mRequest.getQuery() != null) {
            urlString = urlString + "?" + query;
        } else if (query != null) {
            urlString = urlString + "&" + query;
        }
        mLog.debug("url  --> " + urlString);
        return new URL(urlString);
    }

    /**
     * Get the raw bytes from the transaction.
     *
     * @return The bytes from the transaction.
     * @throws Exception
     */
    private byte[] getBytes() throws MalformedURLException, IOException {
        List<Byte> bytes = new LinkedList<Byte>();
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            URL url = constructURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(mRequest.getMethod());
            addHeaders(connection);
            ensureAuthenticator(connection);
            connection.connect();
            in = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int currentBytes;
            while ((currentBytes = in.read(buffer, 0, buffer.length)) > 0) {
                for (int x = 0; x < currentBytes; x++) {
                    bytes.add(buffer[x]);
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        byte[] byteArray = new byte[bytes.size()];
        int x = 0;
        for (Byte b : bytes) {
            byteArray[x] = b.byteValue();
            x++;
        }
        return byteArray;
    }

    /**
     * Get the raw bytes from the transaction.
     *
     * @return The bytes from the transaction.
     * @throws Exception
     */
    private byte[] getBytes(Logger pLog) throws MalformedURLException, IOException {
        List<Byte> bytes = new LinkedList<Byte>();
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            URL url = constructURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(mRequest.getMethod());
            addHeaders(connection);
            ensureAuthenticator(connection);
            connection.connect();
            pLog.debug("ILBytes (Response Code): " + connection.getResponseCode());
            pLog.debug("ILBytes (Content Length): " + connection.getContentLength());
            in = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int currentBytes;
            while ((currentBytes = in.read(buffer, 0, buffer.length)) > 0) {
                for (int x = 0; x < currentBytes; x++) {
                    bytes.add(buffer[x]);
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        byte[] byteArray = new byte[bytes.size()];
        int x = 0;
        for (Byte b : bytes) {
            byteArray[x] = b.byteValue();
            x++;
        }
        return byteArray;
    }

    /**
     * Unmarshall castor object from the rest call.
     *
     * @return The specified castor object.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private T unmarshallObject() throws MalformedURLException, IOException {
        T t = null;
        InputStreamReader reader = null;
        HttpURLConnection connection = null;
        InputStream in = null;
        try {
            URL url = constructURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(mRequest.getMethod());
            addHeaders(connection);
            ensureAuthenticator(connection);
            connection.connect();
            in = connection.getInputStream();
            reader = new InputStreamReader(in);
            if (reader != null) {
//                t = (T)Unmarshaller.unmarshal(mRequest.getResponseClass(), reader);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return t;
    }

    /**
     * Add authentication information if any has been specified.
     */
    private void ensureAuthenticator(HttpURLConnection pConnection) {
        String header = mRequest.getUsername().trim() + ":" + mRequest.getPassword().trim();
        String encoded = new String(Base64.getEncoder().encode(header.getBytes()));
//        pConnection.setRequestProperty("Authorization", "Basic " + encoded);
        pConnection.setRequestProperty("username", mRequest.getUsername().trim());
        pConnection.setRequestProperty("password", mRequest.getPassword().trim());


        encoded = Base64.getEncoder().encodeToString((mRequest.getUsername().trim()+":"+mRequest.getPassword().trim()).getBytes(StandardCharsets.UTF_8));  //Java 8
        pConnection.setRequestProperty("Authorization", "Basic "+encoded);

    }
}
