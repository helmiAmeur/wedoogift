package com.wedoogift.challenge.service.factory;

import com.wedoogift.challenge.entity.Account;
import com.wedoogift.challenge.entity.Deposit;
import com.wedoogift.challenge.entity.GiftDeposit;
import com.wedoogift.challenge.entity.MealDeposit;
import com.wedoogift.challenge.helper.DepositType;

import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Factory to create Depopsit with deferent Type ( meal and gift ) {@link com.wedoogift.challenge.entity.Deposit}.
 */

public class DepositFactory {

    private final static long GIFT_PERIOD =365;

	public Deposit getDeposit(DepositType type, Double amount, Account acount) {

		if (type == null || amount == null || acount == null) {
			return null;
		}

		switch (type) {

			case GIFT:
				GiftDeposit giftDeposit = createGiftDeposit(amount, acount);
				return giftDeposit;

			case MEAL:
				MealDeposit mealDeposit = createMealDeposit(amount, acount);
				return mealDeposit;

			default:
				return null;
		}
	}

	private GiftDeposit createGiftDeposit(double amount, Account acount) {
		GiftDeposit giftDeposit = new GiftDeposit();
		setCommunAttribute(amount, acount, giftDeposit);
		giftDeposit.setExpirationDate(getExpiredDate(DepositType.GIFT, giftDeposit.getCreationDate()));
		return giftDeposit;
	}

	private MealDeposit createMealDeposit(double amount, Account acount) {
		MealDeposit mealDeposit = new MealDeposit();
		setCommunAttribute(amount, acount, mealDeposit);
		mealDeposit.setExpirationDate(getExpiredDate(DepositType.MEAL, mealDeposit.getCreationDate()));
		return mealDeposit;
	}

	private void setCommunAttribute(double amount, Account account, Deposit deposit) {
		deposit.setAmount(amount);
		deposit.setCreationDate(new Date());
		deposit.setExpired(false);
		account.addDeposit(deposit);
	}

	private Date getExpiredDate(DepositType type, Date creationDate) {
		switch (type) {

			case GIFT:
				return calculeGiftExpirationDate(creationDate);

			case MEAL:
				return calculeMealExpirationDate(creationDate);

			default:
				return null;
		}
	}

	private Date calculeMealExpirationDate(Date creationDate) {
		Instant mealExpiration = creationDate.toInstant();
		ZonedDateTime utc = mealExpiration.atZone(ZoneId.systemDefault());
		ZonedDateTime zonedDateTime = utc.withMonth(Month.MARCH.getValue())
				.withDayOfMonth(1)
				.plusYears(1)
				.minus(1, ChronoUnit.DAYS);
		return Date.from(zonedDateTime.toInstant());
	}

	private Date calculeGiftExpirationDate(Date creationDate) {
		Instant instant = creationDate.toInstant();
		Instant expiration = instant.plus(GIFT_PERIOD, ChronoUnit.DAYS);
		return Date.from(expiration);
	}

}
