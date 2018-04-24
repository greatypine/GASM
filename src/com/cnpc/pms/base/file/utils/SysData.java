package com.cnpc.pms.base.file.utils;

/**
 * Created by zhangjy on 2015/9/15.
 */
public class SysData {
    public static class UserAdmin{
        public final static long ID = 1l;
        public final static String USERNAME = "admin";
        public final static String WEICHULI = "/home/dicai/weichuli";
        public final static String YICHULI = "/home/dicai/yichuli";
        public final static String CHULICUOWU = "/home/dicai/chulicuowu";
       // public final static String WEICHULIWINDOWS = "D:\\MyWorkspace\\dataImport\\地采\\未处理";
       // public final static String YICHULIWINDOWS = "D:\\MyWorkspace\\dataImport\\地采\\已处理";
       // public final static String CHULICUOWUWINDOWS = "D:\\MyWorkspace\\dataImport\\地采\\处理错误";
        private final static String PROJECT_ROOT = "/Users/shuhuadai/Documents/workspace/dc-import";
//        public final static String WEICHULIWINDOWS = "F:\\新建文件夹 (2)\\66_代码\\dataImport\\地采\\未处理";
//        public final static String YICHULIWINDOWS = "F:\\新建文件夹 (2)\\66_代码\\dataImport\\地采\\已处理";
//        public final static String CHULICUOWUWINDOWS = "F:\\新建文件夹 (2)\\66_代码\\dataImport\\地采\\处理错误";
        public final static String WEICHULIWINDOWS = PROJECT_ROOT + "/地采/未处理";
        public final static String YICHULIWINDOWS = PROJECT_ROOT + "/地采/已处理";
        public final static String CHULICUOWUWINDOWS = PROJECT_ROOT + "/地采/处理错误";
    }
}
