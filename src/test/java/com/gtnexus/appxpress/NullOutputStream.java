package com.gtnexus.appxpress;

import java.io.IOException;
import java.io.OutputStream;

/**
 * You can think of me as writing to /dev/null.
 *
 * Created by jjdonov on 10/15/14.
 */
public class NullOutputStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b) throws IOException {
	super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
	super.write(b, off, len);
    }
}
