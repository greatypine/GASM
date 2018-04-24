package com.cnpc.pms.base.query.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

import com.cnpc.pms.base.entity.EntityHelper;
import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.util.StrUtil;

/**
 * PMS Application Query Object <br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
@ObjectCreate(pattern = "pmsquery/query")
public class PMSQuery {

	/** The id. */
	@SetProperty(attributeName = "id", pattern = "pmsquery/query")
	private String id;

	/** The base class. */
	@SetProperty(attributeName = "baseclass", pattern = "pmsquery/query")
	private String baseClass;

	/** The dto class. */
	@SetProperty(attributeName = "dtoclass", pattern = "pmsquery/query")
	private String dtoClass;

	/** The pagesize. */
	@SetProperty(attributeName = "pagesize", pattern = "pmsquery/query")
	private int pagesize = IPage.DEFAULT_RECORDS_PER_PAGE;

	/** The header. */
	@SetProperty(attributeName = "header", pattern = "pmsquery/query")
	private String header;

	/** The filter. */
	@SetProperty(attributeName = "filter", pattern = "pmsquery/query")
	private String filter;

	/** The order. */
	@SetProperty(attributeName = "order", pattern = "pmsquery/query")
	private String order;
	
	/** The metadataMethod. */
	@SetProperty(attributeName = "metadataMethod", pattern = "pmsquery/query")
	private String metadataMethod;
	
	/** The method. */
	@SetProperty(attributeName = "method", pattern = "pmsquery/query")
	private String method;
	
	/** The businessId. */
	@SetProperty(attributeName = "businessId", pattern = "pmsquery/query")
	private String businessId;

	/** The distinct. */
	@SetProperty(attributeName = "distinct", pattern = "pmsquery/query")
	private String distinct;
	
	/** The distinct. */
	@SetProperty(attributeName = "width", pattern = "pmsquery/query")
	private String width = "1024";
	
	/** The uniondatakey. */
	@SetProperty(attributeName = "uniondatakey", pattern = "pmsquery/query")
	private String uniondatakey = null;
	
	public String getUniondatakey() {
		return uniondatakey;
	}

	public void setUniondatakey(String uniondatakey) {
		this.uniondatakey = uniondatakey;
	}

	/** The columns. */
	private final List<PMSColumn> columns = new ArrayList<PMSColumn>();


	private Class<?> targetClass;
	private final Map<String, PMSColumn> columnsMap = new LinkedHashMap<String, PMSColumn>();
	private final StringBuffer statString = new StringBuffer();
	private final List<String> statItems = new ArrayList<String>();
	private final List<String> statFormat = new ArrayList<String>();
	private Sort sort;
    private final List join = new ArrayList();
    private final List joinAlias = new ArrayList();
	public List getJoin() {
		return join;
	}

	public List getJoinAlias() {
		return joinAlias;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public Sort getSort() {
		return sort;
	}

	/**
	 * Adds the column.
	 * 
	 * @param column
	 *            the column
	 */
	@SetNext
	public void addColumn(PMSColumn column) {
		this.columns.add(column);
		this.columnsMap.put(column.getKey(), column);
		if (StrUtil.isNotBlank(column.getStat())) {
			String header = column.getHeader();
			for (String o : column.getStat().split(",")) {
				if (statItems.size() > 0) {
					statString.append(',');
				}
				// NOTE 可以对不同的度量增加不同的前期处理，coalesce
				o = o.trim();
				String label;
				if (o.indexOf('/') > 0) {
					String[] s = o.split("/");
					label = s[0];
					statString.append(s[1].replace("?", column.getKey()));
				} else {
					label = o;
					statString.append(o).append('(').append(column.getKey()).append(')');
				}
				String format = column.getFormat();
				if (label.equals("count") == true) {
					format = null;
				}
				statFormat.add(format);
				statItems.add(column.getKey() + "${query.statistisc." + label + "}: ");//2012-12-13 haochengjie header tochange key
			}
		}
	}

	/**
	 * Gets the base class.
	 * 
	 * @return the baseClass
	 */
	public String getBaseClass() {
		return baseClass;
	}

	/**
	 * Gets the columns.
	 * 
	 * @return the columns
	 */
	public List<PMSColumn> getColumns() {
		return columns;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public String getHeader() {
		if (header != null) {
			return header;
		} else {
			return id;
		}
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the pagesize.
	 * 
	 * @return the pagesize
	 */
	public int getPagesize() {
		return pagesize;
	}

	/**
	 * Sets the base class.
	 * 
	 * @param baseClass
	 *            the baseClass to set
	 * @throws ClassNotFoundException
	 */
	public void setBaseClass(String baseClass) throws ClassNotFoundException {
		this.baseClass = baseClass;
		this.targetClass = EntityHelper.getClass(baseClass);
	}

	/**
	 * Sets the header.
	 * 
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the pagesize.
	 * 
	 * @param pagesize
	 *            the pagesize to set
	 */
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	/**
	 * Gets the dto class.
	 * 
	 * @return the dto class
	 */
	public String getDtoClass() {
		return dtoClass;
	}

	/**
	 * Sets the dto class.
	 * 
	 * @param dtoClass
	 *            the new dto class
	 */
	public void setDtoClass(String dtoClass) {
		this.dtoClass = dtoClass;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public PMSColumn getColumn(String key) {
		return columnsMap.get(key);
	}

	public Map<String, PMSColumn> getColumnsMap() {
		return columnsMap;
	}

	public String getStatString() {
		return statString.toString();
	}

	public List<String> getStatItems() {
		return statItems;
	}

	public boolean hasStatistics() {
		return statItems.size() > 0;
	}

	public List<String> getStatFormat() {
		return statFormat;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getOrder() {
		return order;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getDistinct() {
		return distinct;
	}

	public void setDistinct(String distinct) {
		this.distinct = distinct;
	}
	
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	

	public String getMetadataMethod() {
		return metadataMethod;
	}

	public void setMetadataMethod(String metadataMethod) {
		this.metadataMethod = metadataMethod;
	}

	public void setOrder(String order) {
		this.order = order;
		if (StrUtil.isNotBlank(order)) {
			sort = null;
			String[] orders = order.split(",");
			for (String o : orders) {
				String[] field = o.trim().split(" ");
				int type = ISort.ASC;
				if (o.toUpperCase().indexOf("DESC") > 0) {
					type = ISort.DESC;
				}
				if (sort == null) {
					sort = new Sort(field[0], type);
				} else {
					sort.appendSort(new Sort(field[0], type));
				}
			}
		}
	}

}
