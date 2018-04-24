package com.cnpc.pms.base.dao.core;
import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.util.StrUtil;
import java.util.Collections;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.hql.QueryTranslator;
import org.hibernate.hql.ast.QueryTranslatorImpl;

// Referenced classes of package com.cnpc.pms.base.dao.core:
//            HQLHelper

public class DistinctHelper extends HQLHelper
{

    public DistinctHelper(Class clazz, String alias, IFilter filter, ISort sort, IPage page, String distinct)
    {
        this.filter = filter;
        this.sort = sort;
        this.page = page;
        init(clazz, alias);
        StringBuffer select = new StringBuffer();
        select.append("SELECT DISTINCT new ").append(clazz.getCanonicalName()).append("(");
        String arr$[] = distinct.split(",");
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String field = arr$[i$];
            select.append(this.alias).append(".").append(field.trim()).append(",");
        }

        select.deleteCharAt(select.length() - 1);
        select.append(")");
        selectDistinct = select.toString();
    }

    protected String getSelectClause(String selectString)
    {
        if(StrUtil.isNotBlank(selectString))
        {
            StringBuffer selectItems = new StringBuffer();
            selectItems.append("SELECT ");
            if(selectString.indexOf("$") > -1)
            {
                selectItems.append(selectString.replaceAll("\\$", (new StringBuilder()).append(alias).append(".").toString()));
            } else
            {
                String items[] = selectString.split(",");
                for(int i = 0; i < items.length; i++)
                {
                    selectItems.append(alias).append(".").append(items[i].trim());
                    if(i < items.length - 1)
                        selectItems.append(",");
                }

            }
            return selectItems.toString();
        } else
        {
            return selectDistinct;
        }
    }

    public Integer getCount(Session session)
    {
        StringBuffer countSb = new StringBuffer();
        countSb.append(selectDistinct).append(fromAndWhere);
        QueryTranslator queryTranslator = new QueryTranslatorImpl(countSb.toString(), countSb.toString(), Collections.EMPTY_MAP, (SessionFactoryImplementor)session.getSessionFactory());
        queryTranslator.compile(Collections.EMPTY_MAP, false);
        String sqlFromAndWhere = queryTranslator.getSQLString();
        StringBuffer sqlCount = new StringBuffer("SELECT COUNT(*) FROM (");
        sqlCount.append(sqlFromAndWhere);
        //sqlCount.append(")");
        //Change for MYSQL
        //Edit by Liu Junsong 2015/10/09
        sqlCount.append(") t");
        Query count = session.createSQLQuery(sqlCount.toString());
        setParameters(count);
        Number size = (Number)count.uniqueResult();
        return Integer.valueOf(size.intValue());
    }

    private String selectDistinct;
}


