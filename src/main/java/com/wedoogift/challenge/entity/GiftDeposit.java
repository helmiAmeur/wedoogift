package com.wedoogift.challenge.entity;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@DiscriminatorValue("GIFT")
public class GiftDeposit extends Deposit {
}
