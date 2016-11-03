package br.com.rsicarelli.espressointents.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.rsicarelli.espressointents.R;

public class FileUtil {

    public static File getStubFile(Context context) throws IOException {
        InputStream inputStream = context.getResources()
                .openRawResource(R.raw.img_test);

        File imageFile = File.createTempFile("temporary-file", ".jpg");
        OutputStream out = new FileOutputStream(imageFile);

        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.flush();
        out.close();

        inputStream.close();
        return imageFile;
    }
}
