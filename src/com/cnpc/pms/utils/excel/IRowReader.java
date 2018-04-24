package com.cnpc.pms.utils.excel;

import java.util.List;
import java.util.Map;

/**
 * 读取Excel接口
 * Created by liu on 2016/7/28 0028.
 */
public interface IRowReader {

    /**业务逻辑实现方法
     * @param sheetIndex
     * @param curRow
     * @param rowlist
     */
    void getRows(int sheetIndex,int curRow, Map<Integer,String> rowlist);
}
