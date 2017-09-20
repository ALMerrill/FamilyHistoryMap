package handler;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;

import access.Access;
import access.AuthTokenDao;
import access.IDao;

import static com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler.BUFFER_SIZE;

/**
 * Created by Andrew1 on 5/27/17.
 */

public class BaseHandler {
    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }


    public void writeString(String str, BufferedOutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    public void deleteExpiredAuthTokens(int expire) {
        AuthTokenDao atDao = new AuthTokenDao();
        Access access = new Access();
        Connection connection = access.connect();
        boolean cleared = true;
        try {
            if(!atDao.clearExpiredAuthTokens(expire, connection))
                cleared = false;
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        access.commitRoll(cleared);
    }
}
