package com.fullstackbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Proxy(lazy = false)
public class Account {
    @Id
    @GeneratedValue
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    @Min(0)
    private BigDecimal balance;
    @NotNull
    private BigDecimal startBalance;

    public Account(BigDecimal balance){
        this.balance = balance;
        this.startBalance = balance;
    }

    public Account(){
    }

    public void increaseBalance() {
        BigDecimal newBalance = balance.multiply(BigDecimal.valueOf(1.1)); // Увеличиваем на 10%
        BigDecimal maxBalance = startBalance.multiply(BigDecimal.valueOf(2.07)); // Максимальный баланс

        if (!maxBalance.equals(maxBalance.max(newBalance))) {
            balance = maxBalance; // Устанавливаем максимальный баланс
        } else {
            balance = newBalance; // Устанавливаем новый баланс
        }
    }
}
