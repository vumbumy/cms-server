package com.cms.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "verification_token")
public class VerificationCode {
	private static final int EXPIRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Getter
	@Column(name="code")
	private String code;

	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	@Getter
	private User user;

//	@Column(name="created_date")
//	private Date createdDate;

//	@Column(name="expiry_date")
//	private Date expiryDate;

//	private Date calculateExpiryDate(long thisTime, int expiryTimeInMinutes) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Timestamp(thisTime));
//		calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
//		return new Date(calendar.getTime().getTime());
//	}

//	public boolean checkNowInExpiryDate() {
//		if (Util.checkNowBetweenDates(this.createdDate, this.expiryDate)) {
//			return true;
//		}
//
//		return false;
//	}

	public VerificationCode(String code, User user) {
//		Calendar calendar = Calendar.getInstance();
//		long thisTime = calendar.getTime().getTime();

//		this.createdDate = new Date(thisTime);
//		this.expiryDate = this.calculateExpiryDate(thisTime, EXPIRATION);

		this.user = user;
		this.code = code;
	}
}