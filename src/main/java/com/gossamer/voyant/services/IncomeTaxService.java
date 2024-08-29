package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.IncomeTaxBracketsDao;
import com.gossamer.voyant.entities.IncomeTaxBrackets;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class IncomeTaxService {

    private final IncomeTaxBracketsDao incomeTaxBracketsDao;


    public IncomeTaxService(IncomeTaxBracketsDao incomeTaxBracketsDao) {
        this.incomeTaxBracketsDao = incomeTaxBracketsDao;
    }
    public List<IncomeTaxBrackets> getAllTaxBrackets() {
        return (List<IncomeTaxBrackets>) incomeTaxBracketsDao.findAll();
    }

    public List<IncomeTaxBrackets> getIncomeBracketsForIncomeLowToHigh(BigDecimal income ) {
        return incomeTaxBracketsDao.findIncomeTaxBracketsByLowerLimitIsLessThanEqualOrderByLowerLimit(income);
    }

    public BigDecimal calculateIncomeTax(BigDecimal income ) {

        if (income.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        List<IncomeTaxBrackets> incomeTaxBracketsList = getIncomeBracketsForIncomeLowToHigh(income);

        BigDecimal taxOwed = BigDecimal.ZERO;

        for (IncomeTaxBrackets currentTaxBracket : incomeTaxBracketsList) {
            BigDecimal taxRange = currentTaxBracket.getHigherLimit().subtract(currentTaxBracket.getLowerLimit());
            // see if the tax range is lower than current taxable income
            if (income.compareTo(taxRange) >= 0) {
                taxOwed = taxOwed.add(taxRange.multiply(currentTaxBracket.getTaxRate()));
                income = income.subtract(taxRange);
            } else {
                taxOwed = taxOwed.add(income.multiply(currentTaxBracket.getTaxRate()));
            }
        }

        return taxOwed;
    }

    public BigDecimal determineMarginalTaxRate(BigDecimal income) {

        List<IncomeTaxBrackets> incomeTaxBracketsList = getIncomeBracketsForIncomeLowToHigh(income);
        if (incomeTaxBracketsList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        for (IncomeTaxBrackets currentTaxBracket : incomeTaxBracketsList) {
            // if income is equal to the bracket split, marginal Tax is the lower tax... confirm with team
            if (income.compareTo(currentTaxBracket.getHigherLimit()) == 0) {
                return currentTaxBracket.getTaxRate();
            }
        }

        return incomeTaxBracketsList.get(incomeTaxBracketsList.size() - 1).getTaxRate();
    }

    public BigDecimal determineEffectiveTaxRate(BigDecimal income) {

        if (income.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return calculateIncomeTax(income).divide(income, RoundingMode.HALF_EVEN);
    }


}
