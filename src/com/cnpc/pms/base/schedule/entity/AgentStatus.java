// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AgentStatus.java

package com.cnpc.pms.base.schedule.entity;

public enum AgentStatus {

	INIT("INIT", 0, 0), RUNNING("RUNNING", 1, 10), ENDED("ENDED", 2, 20), ERROR(
			"ERROR", 3, 100);

	private AgentStatus(String s, int i, int status) {
		this.name = s;
		this.status = status;
	}

	public int getValue() {
		return status;
	}

	private String name;
	private int status;

}
