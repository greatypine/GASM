/**
 * gaobaolei
 */
package com.cnpc.pms.defaultConfig.dto;

/**
 * @author gaobaolei
 *
 */
public class DefaultConfigDTO {
	private static final long serialVersionUID = 1330456392965059187L;
	private Long id;
	private String employee_no;
	private Integer default_system;//0:crm;1:数据平台
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public Integer getDefault_system() {
		return default_system;
	}
	public void setDefault_system(Integer default_system) {
		this.default_system = default_system;
	}
	
	
}
