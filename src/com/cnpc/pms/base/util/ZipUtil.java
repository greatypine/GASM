// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ZipUtil.java

package com.cnpc.pms.base.util;

import java.io.*;
import java.util.Stack;
import java.util.zip.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtil {

	public ZipUtil() {
	}

	private void createNewFile(File fFile) throws Exception {
		if (fFile.isDirectory()) {
			createNewFolder(fFile);
		} else {
			File fParent = fFile.getParentFile();
			createNewFolder(fParent);
			boolean b = false;
			b = fFile.createNewFile();
			LOG.debug((new StringBuilder()).append(fFile.getAbsolutePath())
					.append("file is already existed:").append(b).toString());
		}
	}

	private void createNewFolder(File fObj) throws Exception {
		Stack fList = new Stack();
		if (fObj.exists())
			return;
		searchNewFolder(fObj, fList);
		boolean b;
		for (; fList.size() > 0; LOG.debug((new StringBuilder())
				.append("folder created:").append(b).toString())) {
			File fTemp = (File) fList.pop();
			b = false;
			b = fTemp.mkdir();
		}

	}

	private void searchNewFolder(File fObj, Stack allFolder) throws Exception {
		if (fObj.exists()) {
			return;
		} else {
			allFolder.push(fObj);
			File fParent = fObj.getParentFile();
			searchNewFolder(fParent, allFolder);
			return;
		}
	}

	public void unzip(String zipFileName, String outputDirectory)
			throws Exception {
		ZipInputStream in;
		in = null;
		FileOutputStream out = null;
		FileInputStream fileIn = null;
		try {
			FileInputStream fileIn1 = new FileInputStream(zipFileName);
			in = new ZipInputStream(fileIn1);
			ZipEntry z;
			while ((z = in.getNextEntry()) != null) {
				LOG.debug((new StringBuilder()).append("unziping ")
						.append(z.getName()).toString());
				if (z.isDirectory()) {
					String name = z.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File((new StringBuilder())
							.append(outputDirectory).append(File.separator)
							.append(name).toString());
					boolean b = false;
					b = f.mkdir();
					LOG.debug((new StringBuilder()).append("file is created:")
							.append(b).toString());
				} else {
					File f = new File((new StringBuilder())
							.append(outputDirectory).append(File.separator)
							.append(z.getName()).toString());
					createNewFile(f);
					FileOutputStream out1 = new FileOutputStream(f);
					int b;
					while ((b = in.read()) != -1)
						out1.write(b);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				in.close();
			} catch (Exception ee) {

			}
		}

	}

	public void zip(String zipFileName, File inputFile) throws Exception {
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			zip(out, inputFile, "");
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				out.close();
			} catch (Exception ee) {

			}
		}

	}

	public void zip(String zipFileName, String inputFile) throws Exception {
		zip(zipFileName, new File(inputFile));
	}

	public void zip(ZipOutputStream out, File f, String base) throws Exception {
		FileInputStream in;
		in = null;
		LOG.debug((new StringBuilder()).append("Zipping  ").append(f.getName())
				.toString());
		if (f.isDirectory()) {
			File fl[] = f.listFiles();
			out.putNextEntry(new ZipEntry((new StringBuilder()).append(base)
					.append("/").toString()));
			base = base.length() != 0 ? (new StringBuilder()).append(base)
					.append("/").toString() : "";
			for (int i = 0; i < fl.length; i++) {
				File fff = fl[i];
				zip(out, fff,
						(new StringBuilder()).append(base)
								.append(fff.getName()).toString());
			}

		} else if (base == null || "".equals(base))
			base = f.getName();
		out.putNextEntry(new ZipEntry(base));
		try {
			in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1)
				out.write(b);
		} catch (FileNotFoundException exTemp) {
			throw exTemp;
		} finally {
			in.close();
		}

	}

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
