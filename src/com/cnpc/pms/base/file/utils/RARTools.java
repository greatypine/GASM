package com.cnpc.pms.base.file.utils;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by cyh(275923233@qq.com) on 2015/9/16.
 */
public class RARTools {
    public static void unRar(String zipPath,String descDir){
        try {
            unRar(new File(zipPath), new File(descDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void unRar(File sourceRar, File destDir) throws Exception {
        Archive archive = null;
        FileOutputStream fos = null;
        System.out.println("Starting...");
        try {
            archive = new Archive(sourceRar);
            FileHeader fh = archive.nextFileHeader();
            int count = 0;
            File destFileName = null;
            while (fh != null) {
                System.out.println((++count) + ") " + fh.getFileNameString());
                String compressFileName = fh.getFileNameString().trim();
                destFileName = new File(destDir.getAbsolutePath() + "/" + compressFileName);
                if (fh.isDirectory()) {
                    if (!destFileName.exists()) {
                        destFileName.mkdirs();
                    }
                    fh = archive.nextFileHeader();
                    continue;
                }
                if (!destFileName.getParentFile().exists()) {
                    destFileName.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(destFileName);
                archive.extractFile(fh, fos);
                fos.close();
                fos = null;
                fh = archive.nextFileHeader();
            }

            archive.close();
            archive = null;
            System.out.println("Finished !");
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (Exception e) {
                    //……
                }
            }
            if (archive != null) {
                try {
                    archive.close();
                    archive = null;
                } catch (Exception e) {
                    //……
                }
            }
        }
    }
}