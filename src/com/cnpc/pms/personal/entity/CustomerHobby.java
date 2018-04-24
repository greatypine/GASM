package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_customer_hobby")
public class CustomerHobby extends DataEntity {
	// 主键
	@Id
	@GeneratedValue
	private Long id;

	// 客户id
	@Column(name = "customer_id")
	private Long customer_id;
	
	//饮食名称
	@Column(name = "food_type")
	private String food_type;
	
	//菜系名称
	@Column(name = "cuisine")
	private String cuisine;
	
	//娱乐类别
	@Column(name = "entertainment")
	private String entertainment;
	
	//服饰风格
	@Column(name = "fashion_style")
	private String fashion_style;
	
	//休闲兴趣类别
	@Column(name = "leisure")
	private String leisure;
	
	//运动名称
	@Column(name = "sports")
	private String sports;
	
	//游戏类别
	@Column(name = "game")
	private String game;
	
	//摄影类别
	@Column(name = "photography")
	private String photography;
	
	//乐器类别
	@Column(name = "music_instr")
	private String music_instr;
	
	//绘画种类
	@Column(name = "painting_type")
	private String painting_type;
	
	//音乐类别
	@Column(name = "music_type")
	private String music_type;
	
	//茶艺名称
	@Column(name = "tea_process")
	private String tea_process;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	public String getFood_type() {
		return food_type;
	}

	public void setFood_type(String food_type) {
		this.food_type = food_type;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public String getEntertainment() {
		return entertainment;
	}

	public void setEntertainment(String entertainment) {
		this.entertainment = entertainment;
	}

	public String getFashion_style() {
		return fashion_style;
	}

	public void setFashion_style(String fashion_style) {
		this.fashion_style = fashion_style;
	}

	public String getLeisure() {
		return leisure;
	}

	public void setLeisure(String leisure) {
		this.leisure = leisure;
	}

	public String getSports() {
		return sports;
	}

	public void setSports(String sports) {
		this.sports = sports;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getPhotography() {
		return photography;
	}

	public void setPhotography(String photography) {
		this.photography = photography;
	}

	public String getMusic_instr() {
		return music_instr;
	}

	public void setMusic_instr(String music_instr) {
		this.music_instr = music_instr;
	}

	public String getPainting_type() {
		return painting_type;
	}

	public void setPainting_type(String painting_type) {
		this.painting_type = painting_type;
	}

	public String getMusic_type() {
		return music_type;
	}

	public void setMusic_type(String music_type) {
		this.music_type = music_type;
	}

	public String getTea_process() {
		return tea_process;
	}

	public void setTea_process(String tea_process) {
		this.tea_process = tea_process;
	}
}
