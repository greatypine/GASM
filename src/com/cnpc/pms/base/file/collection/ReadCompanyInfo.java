package com.cnpc.pms.base.file.collection;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.file.comm.utils.StringUtils;
import com.cnpc.pms.base.file.utils.ReadExcelToDB;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.Attachment;
import com.cnpc.pms.personal.entity.BusinessInfo;
import com.cnpc.pms.personal.entity.BusinessType;
import com.cnpc.pms.personal.entity.Company;
import com.cnpc.pms.personal.entity.Office;
import com.cnpc.pms.personal.entity.Town;
import com.cnpc.pms.personal.entity.Village;
import com.cnpc.pms.personal.manager.AttachmentsManager;
import com.cnpc.pms.personal.manager.BusinessInfoManager;
import com.cnpc.pms.personal.manager.BusinessTypeManager;
import com.cnpc.pms.personal.manager.CompanyManager;
import com.cnpc.pms.personal.manager.OfficeManager;
import com.cnpc.pms.personal.manager.TownManager;
import com.cnpc.pms.personal.manager.VillageManager;

public class ReadCompanyInfo extends BizBaseCommonManager {
	// 读取上传后的文件
	public void readExcel(List<File> listfile,String ip) {
		for (File f : listfile) {
			// 遇到目录，打开目录取文件
			if (f.isDirectory()) {
				System.out.println("这是一个文件夹");
			} else {
				// 文件是Excel
				if (f.getName().indexOf("xlsx") > 0 || f.getName().indexOf("xls") > 0
						|| f.getName().indexOf("xlsm") > 0) {
					ReadExcelToDB readExcelToDB = new ReadExcelToDB();
					StringBuffer stringBuffer = readExcelToDB.getVerification(f.getPath(), f.getName());
					if (stringBuffer == null) {
						String village_gb_code = null;
						Long village_id = null;
						Long town_id = null;
						Company company = null;
						Office office = null;
						Integer office_id = null;// 写字楼id
						String office_name_ert = null;// 写字楼名称
						CompanyManager companyManager = (CompanyManager) SpringHelper.getBean("companyManager");
						OfficeManager officeManager = (OfficeManager) SpringHelper.getBean("officeManager");
						// 查找社区
						List<Map<String, String>> list = readExcelToDB.getDataFormExcel(f.getPath(), f.getName());
						for (Map<String, String> map : list) {
							String gb_code = map.get("village_gb_code");
							System.out.println(map.get("office_name"));
							System.out.println(gb_code);
							if (village_gb_code != gb_code) {
								System.out.println(gb_code);
								Village village = getVillage(gb_code);
								if (village != null) {
									village_id = village.getId();
									village_gb_code = village.getGb_code();
									town_id = village.getTown_id();
								}else if(gb_code!=null&&!gb_code.equals("0")){
									throw new RuntimeException("社区不存在");
								}  else {
									TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
									Town town = townManager.getTownByGb_code_vir(map.get("town_gb_code"));
									if(town!=null){
										town_id=town.getId();
										village_gb_code=null;
										village_id=null;
									}else{
										throw new RuntimeException("街道不存在;gb_code为:"+map.get("town_gb_code"));
									}
									
								}
							}
							String office_company = map.get("office_company");
							String office_floor_of_company = map.get("office_floor_of_company");
							String office_address = map.get("office_address");
							String office_area = map.get("office_area");
							String office_type = map.get("office_type");
							String office_floor = map.get("office_floor");
							String office_parking = map.get("office_parking");
							String office_name = map.get("office_name");
							if (office_name_ert !=office_name) {
								Office office2 = getOffice(office_name, village_id);
								if (office2 == null) {
									office = new Office();
									office.setOffice_address(office_address);
									office.setOffice_area(office_area);
									office.setOffice_floor(office_floor);
									office.setOffice_name(office_name);
									office.setOffice_parking(office_parking);
									office.setOffice_type(office_type);
									office.setVillage_id(village_id);
									office.setTown_id(town_id);
									preObject(office);
									officeManager.save(office);
									office_id = office.getOffice_id();
								} else {
									office_id = office2.getOffice_id();
								}
							}
							if (!"无信息".equals(office_company)) {
								Company company2 = getCompany(office_company, office_floor_of_company, office_id);
								if (company2 == null) {
									company = new Company();
									company.setOffice_company(office_company);
									company.setOffice_floor_of_company(office_floor_of_company);
									company.setOffice_id(office_id);
									companyManager.save(company);
								}
							}
						}
						saveAttachment(f.getName()+ip, f.getPath(), "写字楼文件");
					} else {
						throw new RuntimeException(stringBuffer.toString());
					}
				}
			}
		}

	}
	// 读取上传后的文件
		public void readOfficeExcel(File officefile,Attachment attachment) {
			AttachmentManager attachmentManager=(AttachmentManager)SpringHelper.getBean("attachmentManager");
					// 文件是Excel
					if (officefile.getName().indexOf("xlsx") > 0 || officefile.getName().indexOf("xls") > 0
							|| officefile.getName().indexOf("xlsm") > 0) {
						ReadExcelToDB readExcelToDB = new ReadExcelToDB();
						StringBuffer stringBuffer = readExcelToDB.getVerification(officefile.getPath(), officefile.getName());
						if (stringBuffer == null) {
							String village_gb_code = null;
							Long village_id = null;
							Long town_id = null;
							Company company = null;
							Office office = null;
							Integer office_id = null;// 写字楼id
							String office_name_ert = null;// 写字楼名称
							CompanyManager companyManager = (CompanyManager) SpringHelper.getBean("companyManager");
							OfficeManager officeManager = (OfficeManager) SpringHelper.getBean("officeManager");
							// 查找社区
							List<Map<String, String>> list = readExcelToDB.getDataFormExcel(officefile.getPath(), officefile.getName());
							for (Map<String, String> map : list) {
								String gb_code = map.get("village_gb_code");
								if (village_gb_code != gb_code) {
									Village village = getVillage(gb_code);
									if (village != null) {
										village_id = village.getId();
										village_gb_code = village.getGb_code();
										town_id = village.getTown_id();
									}else if(gb_code!=null&&!gb_code.equals("0")){
										throw new RuntimeException("社区不存在");
									} else {
										TownManager townManager=(TownManager)SpringHelper.getBean("townManager");
										Town town = townManager.getTownByGb_code_vir(map.get("town_gb_code"));
										if(town!=null){
											town_id=town.getId();
											village_gb_code=null;
											village_id=null;
										}else{
											throw new RuntimeException("街道不存在;gb_code为:"+map.get("town_gb_code"));
										}
									}
								}
								String office_company = map.get("office_company");
								String office_floor_of_company = map.get("office_floor_of_company");
								String office_address = map.get("office_address");
								String office_area = map.get("office_area");
								String office_type = map.get("office_type");
								String office_floor = map.get("office_floor");
								String office_parking = map.get("office_parking");
								String office_name = map.get("office_name");
								if (office_name_ert !=office_name) {
									Office office2 = getOffice(office_name, village_id);
									if (office2 == null) {
										office = new Office();
										office.setOffice_address(office_address);
										office.setOffice_area(office_area);
										office.setOffice_floor(office_floor);
										office.setOffice_name(office_name);
										office.setOffice_parking(office_parking);
										office.setOffice_type(office_type);
										office.setVillage_id(village_id);
										office.setTown_id(town_id);
										preObject(office);
										if(office.getCreate_user()==null){
											office.setCreate_user(attachment.getCreate_user());
											office.setCreate_user_id(Integer.parseInt(attachment.getCreate_user_id()+""));
											office.setUpdate_user(attachment.getCreate_user());
											office.setUpdate_user_id(Integer.parseInt(attachment.getCreate_user_id()+""));
										}
										officeManager.save(office);
										office_id = office.getOffice_id();
									} else {
										office_id = office2.getOffice_id();
									}
								}
								if (!"无信息".equals(office_company)) {
									Company company2 = getCompany(office_company, office_floor_of_company, office_id);
									if (company2 == null) {
										company = new Company();
										company.setOffice_company(office_company);
										company.setOffice_floor_of_company(office_floor_of_company);
										company.setOffice_id(office_id);
										companyManager.save(company);
									}
								}
							}
							attachment.setUploadType("上传成功");
							attachment.setMessage("上传成功");
							attachmentManager.saveObject(attachment);
							attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
						} else {
							attachment.setUploadType("上传失败");
							attachment.setMessage(stringBuffer.toString());
							attachmentManager.saveObject(attachment);
							attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
							throw new RuntimeException(stringBuffer.toString());
						}
					}
				}
	
	
	
	
	
	
	
	
	
	
	
	

	/**
	 * 更新用写字楼信息
	 * 
	 * @param listfile
	 */
	public void updateOffice(List<File> listfile) {
		for (File f : listfile) {
			// 遇到目录，打开目录取文件
			if (f.isDirectory()) {
				System.out.println("这是一个文件夹");
			} else {
				// 文件是Excel
				if (f.getName().indexOf("xlsx") > 0 || f.getName().indexOf("xls") > 0
						|| f.getName().indexOf("xlsm") > 0) {
					ReadExcelToDB readExcelToDB = new ReadExcelToDB();
					//System.out.println(f.getPath() + "         " + f.getName());
					//StringBuffer stringBuffer = readExcelToDB.getVerification(f.getPath(), f.getName());
					// 获取表中的内容
					String[] clums = { /* "city_id","city_name", */"office_id", "office_name", "town_id",
							"town_name", "village_id", "village_name", "office_address", "office_type",
							"office_area", "office_parking", "office_floor", "company_id", "office_company",
							"office_floor_of_company" };
					List<Map<String, String>> list = readExcelToDB.getExcelContent(f.getPath(), f.getName(), clums);
		            //封装
					List<Office> officeList = new ArrayList<Office>();
					List<Company> companyList = new ArrayList<Company>();
					int officeId = 0 ;
					for (Map<String, String> map : list) {
						if(!StringUtils.isBlank(map.get("office_id"))){
							officeId = Integer.valueOf(map.get("office_id"));
							Office o = new Office();
							 o.setOffice_id(Integer.valueOf(map.get("office_id")));
							 o.setOffice_name(map.get("office_name"));
							 o.setOffice_address(map.get("office_address"));
							 o.setTown_id(Long.valueOf(map.get("town_id")));
							 o.setVillage_id(Long.valueOf(map.get("village_id")));
							 o.setOffice_parking(map.get("office_parking"));
							 o.setOffice_floor(map.get("office_floor"));
							 o.setOffice_type(map.get("office_type"));
							 o.setOffice_area(map.get("office_area"));
							 officeList.add(o);
						}
						if(map.get("company_id")!=null&&!"".equals(map.get("company_id"))){
							 Company company = new Company();
							 company.setOffice_id(officeId);
							 company.setCompany_id(Integer.valueOf(map.get("company_id")));
							 company.setOffice_company(map.get("office_company"));
							 company.setOffice_floor_of_company(map.get("office_floor_of_company"));
							 companyList.add(company);
						}
					}
					OfficeManager officeManager = (OfficeManager) SpringHelper.getBean("officeManager");
					officeManager.saveList(officeList,companyList);

				}
			}
		}
	}
	
	
//	/**
//	 * 生成sql 单表根据id更新
//	 * @param tableName 表名
//	 * @param colums 列数组，主键放第一位
//	 * @param datas 数据
//	 * @return
//	 */
//	public String createBatchSql (String tableName,String[] colums,List<Map<String,String>> datas){
//		StringBuffer sBuffer = new StringBuffer();
//		for (Map<String, String> map : datas) {
//			if(!StringUtils.isBlank(map.get(colums[0])) ){
//				sBuffer.append(" UPDATE ").append(tableName).append(" SET ");
//				for (int i = 1; i < colums.length; i++) {
//					sBuffer.append(colums[i]).append(" = '").append(map.get(colums[i])).append("',");
//				}
//				sBuffer.deleteCharAt(sBuffer.lastIndexOf(","));
//				sBuffer.append(" where ").append(colums[0]).append(" = '")
//					.append(map.get(colums[0])).append("';");
//			}
//		}
//		return sBuffer.toString();
//	}

	// 读取上传后的街道商业信息文件
	public void readBusinessExcel(List<File> listfile,String ip) {
		//System.out.println("读取文件中商业信息"+listfile.size());
		ReadExcelToDB excelToDB = new ReadExcelToDB();
		for (File f : listfile) {
			// 遇到目录，打开目录取文件
			if (f.isDirectory()) {
				System.out.println("这是一个文件夹");
			} else {
				// 文件是Excel
				if (f.getName().indexOf("xlsx") > 0 || f.getName().indexOf("xls") > 0
						|| f.getName().indexOf("xlsm") > 0) {
					StringBuffer buffer = excelToDB.verificationBusinessInfo(f.getPath(), f.getName());
					if (buffer != null) {
						if ("".equals(buffer.toString())) {
							List<Map<String, String>> businessInfo = excelToDB.getBusinessInfo(f.getPath(),
									f.getName());
							//System.out.println("文件中商业信息数量"+businessInfo.size());
							saveBusinessInfo(businessInfo,null,null);
							//System.out.println("商业信息添加完成,开始执行添加附件信息数据");
							saveAttachment(f.getName()+ip, f.getPath(), "商业信息文件");
						} else {
							throw new RuntimeException(buffer.toString());
						}
					}

				}
			}
		}
	}

	// 读取上传后的街道商业信息文件
		public void readBusinessDataExcel(File businessfile,Attachment attachment) {
			ReadExcelToDB excelToDB = new ReadExcelToDB();
			AttachmentManager attachmentManager=(AttachmentManager)SpringHelper.getBean("attachmentManager");
					// 文件是Excel
					if (businessfile.getName().indexOf("xlsx") > 0 || businessfile.getName().indexOf("xls") > 0
							|| businessfile.getName().indexOf("xlsm") > 0) {
						StringBuffer buffer = excelToDB.verificationBusinessInfo(businessfile.getPath(), businessfile.getName());
						if (buffer != null) {
							if ("".equals(buffer.toString())) {
								List<Map<String, String>> businessInfo = excelToDB.getBusinessInfo(businessfile.getPath(),
										businessfile.getName());
								//System.out.println("文件中商业信息数量"+businessInfo.size());
								saveBusinessInfo(businessInfo,attachment.getCreate_user_id(),attachment.getCreate_user());
								//System.out.println("商业信息添加完成,开始执行添加附件信息数据");
								attachment.setUploadType("上传成功");
								attachment.setMessage("上传成功");
								attachmentManager.saveObject(attachment);
								attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
							} else {
								attachment.setUploadType("上传失败");
								attachment.setMessage(buffer.toString());
								attachmentManager.saveObject(attachment);
								attachmentManager.updateAttachmentUploadType(attachment.getFile_name(), attachment.getFile_type_name(), attachment.getMessage(), attachment.getUploadType(),null);
								throw new RuntimeException(buffer.toString());
							}
						}

					}
				}
	
	
	// 根据社区gb_code获取社区信息
	public Village getVillage(String gb_code) {
		VillageManager villageManager = (VillageManager) SpringHelper.getBean("villageManager");
		return villageManager.getVillageByGb_code(gb_code);

	}
	
	// 根据街道gb_code获取街道信息
		public Town getTown(String gb_code) {
			TownManager townManager = (TownManager) SpringHelper.getBean("townManager");
			return townManager.getTownByGb_code_vir(gb_code);

		}
	
	

	/**
	 * 根据公司名称和写字楼查询公司信息
	 * 
	 * @param office_company
	 * @return
	 */
	public Company getCompany(String office_company, String office_floor_of_company, int office_id) {
		CompanyManager companyManager = (CompanyManager) SpringHelper.getBean("companyManager");
		return companyManager.getCompanyByOffice_company(office_company, office_floor_of_company, office_id);
	}

	public Office getOffice(String office_name, Long village_id) {
		OfficeManager officeManager = (OfficeManager) SpringHelper.getBean("officeManager");
		return officeManager.getOfficeByOffice_name(office_name, village_id);
	}

	/**
	 * 保存商业信息数据
	 * 
	 * @param list
	 */
	public void saveBusinessInfo(List<Map<String, String>> list,Long crateUser,String createUserName) {
		//System.out.println("读取list中的商业信息"+list.size());
		String village_gb_code = null;
		Long village_id = null;
		Long town_id=null;
		BusinessInfo businessInfo = null;
		BusinessTypeManager businessTypeManager = (BusinessTypeManager) SpringHelper.getBean("businessTypeManager");
		for (Map<String, String> map : list) {
			String gb_code = map.get("village_gb_code");
			String town_gb_code = map.get("town_gb_code");
			if (village_gb_code != gb_code) {
				if("0".equals(gb_code)){
					Town town = getTown(town_gb_code);
					if(town!=null){
						town_id=town.getId();
						village_id = null;
					}else{
						throw new RuntimeException("街道不存在;gb_code为：" + town_gb_code);
					}
				}else{
					Village village = getVillage(gb_code);
					if (village != null) {
						village_gb_code = village.getGb_code();
						village_id = village.getId();
						town_id=village.getTown_id();
					} else {
						throw new RuntimeException("社区不存在;gb_code为：" + gb_code);
					}
				}
				
				String address = map.get("address");
				String business_name = map.get("business_name");
				String two_level_index = map.get("two_level_index");
				String three_level_index = map.get("three_level_index");
				String four_level_index = map.get("four_level_index");
				String five_level_index = map.get("five_level_index");
				String shop_area = map.get("shop_area");
				String range_eshop = map.get("range_eshop");
				String isdistribution = map.get("isdistribution");
				String start_business_house = map.get("start_business_house");
				String end_business_house = map.get("end_business_house");
				BusinessType businessType = businessTypeManager.getBusinessTypeByStringArray(two_level_index,
						getArraySplit(three_level_index)[1], getArraySplit(four_level_index), five_level_index);
				if (businessType == null) {
					throw new RuntimeException("请核对商家名称为：" + business_name + "的指标");
				}
				businessInfo = new BusinessInfo();
				businessInfo.setVillage_id(village_id);
				businessInfo.setAddress(address);
				businessInfo.setBusiness_name(business_name);
				businessInfo.setTwo_level_index(businessType.getLevel1_name());
				businessInfo.setThree_level_index(businessType.getLevel2_name());
				businessInfo.setFour_level_index(businessType.getLevel3_name());
				businessInfo.setFive_level_index(businessType.getLevel4_name());
				businessInfo.setTwo_level_code(businessType.getLevel1_code());
				businessInfo.setThree_level_code(businessType.getLevel2_code());
				businessInfo.setFour_level_code(businessType.getLevel3_code());
				businessInfo.setFive_level_code(businessType.getLevel4_code());
				businessInfo.setType_id(businessType.getId());
				businessInfo.setShop_area(shop_area);
				businessInfo.setRange_eshop(range_eshop);
				businessInfo.setIsdistribution(isdistribution);
				businessInfo.setStart_business_house(start_business_house);
				businessInfo.setEnd_business_house(end_business_house);
				businessInfo.setTown_id(town_id);
				BusinessInfoManager businessInfoManager = (BusinessInfoManager) SpringHelper
						.getBean("businessInfoManager");
				preObject(businessInfo);
				if(businessInfo.getCreate_user_id()==null){
					businessInfo.setCreate_user(createUserName);
					businessInfo.setCreate_user_id(crateUser);
					businessInfo.setUpdate_user(createUserName);
					businessInfo.setUpdate_user_id(crateUser);
				}
				BusinessInfo infoBy = businessInfoManager.getBusinessInfoBy(businessInfo.getVillage_id(),businessInfo.getBusiness_name().replaceAll("\'", "\'\'"),businessInfo.getAddress());
				if(infoBy==null){
					businessInfoManager.save(businessInfo);
				}else{
					infoBy.setTwo_level_index(businessType.getLevel1_name());
					infoBy.setThree_level_index(businessType.getLevel2_name());
					infoBy.setFour_level_index(businessType.getLevel3_name());
					infoBy.setFive_level_index(businessType.getLevel4_name());
					infoBy.setTwo_level_code(businessType.getLevel1_code());
					infoBy.setThree_level_code(businessType.getLevel2_code());
					infoBy.setFour_level_code(businessType.getLevel3_code());
					infoBy.setFive_level_code(businessType.getLevel4_code());
					infoBy.setType_id(businessType.getId());
					infoBy.setShop_area(shop_area);
					infoBy.setRange_eshop(range_eshop);
					infoBy.setIsdistribution(isdistribution);
					infoBy.setStart_business_house(start_business_house);
					infoBy.setEnd_business_house(end_business_house);
					infoBy.setTown_id(town_id);
					preObject(infoBy);
					if(infoBy.getCreate_user_id()==null){
						infoBy.setCreate_user(createUserName);
						infoBy.setCreate_user_id(crateUser);
						infoBy.setUpdate_user(createUserName);
						infoBy.setUpdate_user_id(crateUser);
					}
					businessInfoManager.saveObject(infoBy);
				}
			}
		}
	}
	

	/**
	 * 添加附件
	 */
	public Attachment saveAttachment(String fileName, String filePath, String file_type) {
		//System.out.println("调用保存附件方法");
		Attachment attachment = new Attachment();
		attachment.setFile_name(fileName);
		attachment.setFile_path(filePath);
		attachment.setFile_type_name(file_type);
		preObject(attachment);
		//System.out.println("正在保存附件"+fileName+"-------------"+new Date());
		AttachmentsManager attachmentsManager = (AttachmentsManager) SpringHelper.getBean("attachmentsManager");
		attachmentsManager.saveObject(attachment);
		//System.out.println("结束调用保存附件方法");
		return attachment;
	}

	// 按下划线切割
	public String[] getArraySplit(String str) {
		String[] strings = str.split("_");
		return strings;
	}

}
