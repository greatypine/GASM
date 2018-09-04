// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SpringHelper.java

package com.cnpc.pms.base.util;

import com.cnpc.pms.base.common.context.PMSApplicationContext;
import com.cnpc.pms.base.configuration.ConventionApplicationContext;
import com.cnpc.pms.base.configuration.PMSConfigHandlerJunior;
import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.EntityHelper;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.security.*;
import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;
import com.google.inject.internal.cglib.proxy.Enhancer;
import java.beans.Introspector;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.ClassUtils;

// Referenced classes of package com.cnpc.pms.base.util:
//            Slf4jConfigurer, StrUtil

public class SpringHelper
{

    public SpringHelper()
    {
    }

    public static ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public static Object getBean(String beanName)
    {
        if(applicationContext != null)
            return applicationContext.getBean(beanName);
        else
            return null;
    }

    public static IManager getManagerByObject(Object o)
    {
        if(o != null)
            return getManagerByClass(o.getClass());
        else
            return null;
    }

    public static IManager getManagerByClass(Class clazz)
    {
        return getManagerByClass(getOriginalClass(clazz).getName());
    }

    public static IManager getManagerByClass(String beanClassName)
    {
        String managerBeanName = getShortNameAsProperty(beanClassName);
        try
        {
            return (IManager)applicationContext.getBean(managerBeanName);
        }
        catch(BeansException e)
        {
            log.error("Fail to get manager bean [{}], as type {}", managerBeanName, beanClassName);
            e.printStackTrace();
        }
        catch(ClassCastException e)
        {
            log.error("bean name [{}] is not a Manager", managerBeanName);
        }
        return null;
    }

    public static String getMessage(String msgKey)
    {
        UserSession userSession = SessionManager.getUserSession();
        Locale locale;
        if(userSession != null)
            locale = userSession.getLocale();
        else
            locale = Locale.getDefault();
        Object args[] = new Object[0];
        return getApplicationContext().getParent().getMessage(msgKey, args, locale);
    }

    public static String getMessage(String msgKey, Object args[])
    {
        UserSession userSession = SessionManager.getUserSession();
        Locale locale;
        if(userSession != null)
            locale = userSession.getLocale();
        else
            locale = Locale.getDefault();
        return getApplicationContext().getParent().getMessage(msgKey, args, locale);
    }

    public static Class getOriginalClass(Class clazz)
    {
        Class ret;
        for(ret = clazz; Enhancer.isEnhanced(ret); ret = ret.getSuperclass());
        return ret;
    }

    public static SecurityMgr getSecurityManager()
    {
        return (SecurityMgr)getBean("com.cnpc.pms.base.security.SecurityMgr");
    }

    public static SessionManager getSessionManager()
    {
        return (SessionManager)getBean("com.cnpc.pms.base.security.SessionManager");
    }

    public static void init(String contextFiles[])
    {
        Slf4jConfigurer.initLogging();
        ApplicationContext ctx = new ClassPathXmlApplicationContext(contextFiles);
        setRootContext(ctx);
        PMSApplicationContext appContext = PMSApplicationContext.getInstance();
        appContext.initialize();
    }

    public static void setRootContext(ApplicationContext ctx)
    {
        try
        {
            ConfigurableApplicationContext childBean = (ConfigurableApplicationContext)BeanFactoryUtils.beanOfType(ctx, AUTO_REGISTER_APPLICATION_CONTEXT);
            childBean.setParent(ctx);
            childBean.refresh();
            setApplicationContext(childBean);
        }
        catch(Exception e)
        {
            log.info("###################### Original Mode.######################");
            GenericApplicationContext genericWac = new GenericApplicationContext();
            genericWac.setParent(ctx);
            setApplicationContext(genericWac);
            log.debug("Start to auto register spring beans ===>");
            PMSConfigHandlerJunior.initialPMSConfig();
            log.debug("<=== End of auto register spring beans:");
        }
    }

    private static void setApplicationContext(ApplicationContext ctx)
    {
        applicationContext = ctx;
    }

    public static String getShortNameAsProperty(String className)
    {
        className = ClassUtils.getShortName(className);
        return Introspector.decapitalize(className);
    }

    public static String getManagerNameByEntity(String entityName)
    {
        return (new StringBuilder()).append(getShortNameAsProperty(entityName)).append("Manager").toString();
    }

    public static String getEntityNameByManager(String managerName)
    {
        String str = ClassUtils.getShortName(managerName);
        return str.substring(0, str.length() - BUSINESS_LOGIC_CUT_OFF);
    }

    public static String getDaoNameByManager(String managerName)
    {
        return (new StringBuilder()).append(getEntityNameByManager(managerName)).append("DAO").toString();
    }

    public static String getEntityNameByDAO(String daoName)
    {
        String str = ClassUtils.getShortName(daoName);
        return str.substring(0, str.length() - DATA_ACCESS_CUT_OFF);
    }

    public static String getDecoratedNameBySource(String originalName, String dataSourceKey)
    {
        if(StrUtil.isBlank(dataSourceKey))
            return originalName;
        else
            return (new StringBuilder()).append(originalName).append("_").append(dataSourceKey).toString();
    }

    public static String getDecoratedNameByEntity(String originalName, String entityName)
    {
        if(StrUtil.isEmpty(entityName))
        {
            return originalName;
        } else
        {
            String dataSourceKey = getDataSourceSuffix(entityName);
            return getDecoratedNameBySource(originalName, dataSourceKey);
        }
    }

    private static String getDataSourceSuffix(String entityName)
    {
        String suffix = null;
        try
        {
            Class entityClass = EntityHelper.getClass(entityName);
            AlternativeDS ds = (AlternativeDS)entityClass.getAnnotation(AlternativeDS.class);
            if(!DataSourceConfigurer.isMainDataSource(ds))
                suffix = ds.source();
        }
        catch(ClassNotFoundException ex1)
        {
        	String errorEntity="BaseFile,Excel,ExcelUpload,EshopPurchase,MassOrder,UserProfile,AppDownLoadLog,Attachments,EshopPurchase,MassOrder,SyncData,UserProfile,ChartStat,Dynamic,TurnoverStat,"
        			+ "UserOperationStat,UserOperationStat,DfCustomerMonthOrder,GAXDriveRecode,Inter,MessageNew,MongoDB,OrderHeat,MapBasicData,NxQuery,QueryConfig";
        	if(errorEntity.indexOf(entityName)==-1) {
        		log.error("Fail to find the Entity: [{}].", entityName);
        	}
        }
        catch(Exception e)
        {
            log.error("Fail to get AlternativeDS Entity[{}]'s TargetDS.", entityName, e);
        }
        return suffix;
    }

    public static void printParentBeans()
    {
        printBeans(applicationContext.getParent());
    }

    public static void printBeans()
    {
        printBeans(applicationContext);
    }

    public static void printAll()
    {
        ApplicationContext ac = applicationContext;
        int i = 0;
        do
        {
            log.info("[{}]======================", Integer.valueOf(i++));
            printBeans(ac);
            ac = ac.getParent();
        } while(ac != null);
    }

    private static void printBeans(ApplicationContext appContext)
    {
        int i = 1;
        String arr$[] = appContext.getBeanDefinitionNames();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String beanName = arr$[i$];
            Object bean;
            try
            {
                bean = appContext.getBean(beanName);
            }
            catch(Exception e)
            {
                bean = e.toString();
            }
            log.info("|-{}, {} = {}", new Object[] {
                Integer.valueOf(i++), beanName, bean
            });
        }

        log.info("\\- There are {} Beans in {}", Integer.valueOf(appContext.getBeanDefinitionCount()), appContext);
    }

    public static final String BUSINESS_LOGIC_BEAN_POSTFIX = "Manager";
    public static final int BUSINESS_LOGIC_CUT_OFF = "Manager".length();
    public static final String DATA_ACCESS_OBJECT_POSTFIX = "DAO";
    public static final int DATA_ACCESS_CUT_OFF = "DAO".length();
    public static final Class AUTO_REGISTER_APPLICATION_CONTEXT = ConventionApplicationContext.class;
    private static ApplicationContext applicationContext;
    protected static final Logger log = LoggerFactory.getLogger(SpringHelper.class);
    private static final String CONFIG_JOINER = "_";

}
