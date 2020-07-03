package ub.fet.sms.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
	private String name;

	@NotBlank
	private String pin;

	public String getName() {
		return name;
	}

	public void setName(String username) {
		this.name = username;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
}
