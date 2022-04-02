package com.wedoogift.challenge.entity;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@DiscriminatorValue("MEAL")
public class MealDeposit extends Deposit{
}
