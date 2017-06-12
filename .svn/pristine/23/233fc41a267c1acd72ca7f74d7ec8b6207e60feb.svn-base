/*
 *
 * @author Echo
 * @created 2016.5.29
 * @email bairu.xu@speedatagroup.com
 * @version $version
 *
 */

package common.http;



import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import common.base.App;
import common.utils.LogUtil;

import static common.utils.LogUtil.DEGUG_MODE;


public class MySSLProtocolSocketFactory implements ProtocolSocketFactory {
    private static final String TAG = DEGUG_MODE ? "MySSLProtocolSocketFactory" : MySSLProtocolSocketFactory.class.getSimpleName();
    private static final boolean debug = true;
    private SSLContext sslcontext = null;

    private SSLContext createSSLContext() {
        SSLContext sslcontext = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            LogUtil.i(debug, TAG, "【MySSLProtocolSocketFactory.createSSLContext()】【keyStore=" + keyStore + "】");
            InputStream instream = App.getInstance().getResources().getAssets().open("client.p12");
            LogUtil.i(debug, TAG, "【MySSLProtocolSocketFactory.createSSLContext()】【instream=" + instream + "】");
            keyStore.load(instream, "dabai521".toCharArray());
            instream.close();
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            LogUtil.i(debug, TAG, "【MySSLProtocolSocketFactory.createSSLContext()】【kmf=" + kmf + "】");
            kmf.init(keyStore, "dabai521".toCharArray());
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(kmf.getKeyManagers(), new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslcontext;
    }

    private SSLContext getSSLContext() {
        if (this.sslcontext == null) {
            this.sslcontext = createSSLContext();
        }
        return this.sslcontext;
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
                socket,
                host,
                port,
                autoClose
        );
    }

    public Socket createSocket(String host, int port) throws IOException,
            UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(
                host,
                port
        );
    }


    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(String host, int port, InetAddress localAddress,
                               int localPort, HttpConnectionParams params) throws IOException,
            UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        } else {
            Socket socket = socketfactory.createSocket();
            SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
            return socket;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }


}