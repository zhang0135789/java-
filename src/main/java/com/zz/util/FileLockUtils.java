package com.zz.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/**
 * Created by Clare lau (Clarelau61803@gmail.com) on 2018/7/22 0022.
 */
public class FileLockUtils {
    private FileLock fileLock = null;

    private File file = null;

    private RandomAccessFile randomAccessFile = null;

    public FileLockUtils(String fileName) {
        this.file = new File(fileName);
        File dir = new File(fileName.substring(0, fileName.lastIndexOf(File.separator)));
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    public FileLockUtils(File file) {
        this.file = file;
    }

    /**
     * 文件加锁并创建文件
     *
     * @return
     * @throws IOException
     */
    public boolean Lock() throws IOException {
        if (!this.file.exists()) {
            this.file.createNewFile();
        }
        this.randomAccessFile = new RandomAccessFile(this.file, "rw");
        this.fileLock = this.randomAccessFile.getChannel().tryLock();
        try {
            return this.fileLock.isValid();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解锁并删除文件
     *
     * @return
     * @throws IOException
     */
    public boolean unLock() throws IOException {
        if (!this.file.exists()) {
            return true;
        } else {
            if (this.fileLock != null) {
                this.fileLock.release();
            }
            if (this.randomAccessFile != null) {
                this.randomAccessFile.close();
            }
            if (this.file.delete()) {
                return true;
            } else {
                return false;
            }

        }

    }

    /**
     * @return Returns the fileLock.
     */
    public FileLock getFileLock() {
        return this.fileLock;
    }

    /**
     * @param fileLock The fileLock to set.
     */
    public void setFileLock(FileLock fileLock) {
        this.fileLock = fileLock;
    }
}
