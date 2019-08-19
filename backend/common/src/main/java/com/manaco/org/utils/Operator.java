package com.manaco.org.utils;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class Operator {
    private BigDecimal ufvValue;

    public Operator() {
        this.ufvValue = new BigDecimal(0);
    }

    public BigDecimal calculateTotal(BigDecimal quantity, BigDecimal price) {
        if (quantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return quantity.multiply(price).setScale(6, BigDecimal.ROUND_CEILING);
    }

    public BigDecimal calculateUpdate(BigDecimal total, BigDecimal actualUfv, BigDecimal beforeUfv) {
        if (total.compareTo(BigDecimal.ZERO) != 0) {
            return total.multiply(actualUfv.divide(beforeUfv, RoundingMode.CEILING));
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal newPrice(BigDecimal totalUpdate, BigDecimal quantity) {
        if (totalUpdate.compareTo(BigDecimal.ZERO) != 0 && quantity.compareTo(BigDecimal.ZERO) != 0) {
            return totalUpdate.divide(quantity, RoundingMode.CEILING);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal caclulateUfvValue(BigDecimal totalUpdate, BigDecimal totalNormal) {
        return totalUpdate.subtract(totalNormal);
    }

    public BigDecimal calculateUfv(BigDecimal actualUfv, BigDecimal beforeUfv) {
        return actualUfv.divide(beforeUfv, RoundingMode.DOWN);
    }

    public BigDecimal calculateQuantityTotal(BigDecimal quantity, BigDecimal price) {
        return quantity.multiply(price);
    }

    public BigDecimal calculateEgressBalance(BigDecimal itemQuantityTotal, BigDecimal quantity) {
        return itemQuantityTotal.subtract(quantity);
    }
}


