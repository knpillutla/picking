package com.threedsoft.picking.util;

public enum PickStatus {
	CREATED("Created"), LOCKED("Locked"), RELEASED("Released"), ASSIGNED("Assigned"), PICKED("Picked"), SHORTED("Shorted"), CANCELLED("Cancelled");
	PickStatus(String status) {
		this.status = status;
	}

	private String status;

	public String getStatus() {
		return status;
	}
}

