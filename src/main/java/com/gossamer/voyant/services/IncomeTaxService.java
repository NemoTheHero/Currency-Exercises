package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.IncomeTaxBracketsDao;
import com.gossamer.voyant.entities.IncomeTaxBrackets;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class IncomeTaxService {

    private final IncomeTaxBracketsDao incomeTaxBracketsDao;


    public IncomeTaxService(IncomeTaxBracketsDao incomeTaxBracketsDao) {
        this.incomeTaxBracketsDao = incomeTaxBracketsDao;
    }
    public List<IncomeTaxBrackets> getAllTaxBrackets() {
        return (List<IncomeTaxBrackets>) incomeTaxBracketsDao.findAll();
    }

    public List<IncomeTaxBrackets> getIncomeBracketsForIncome(BigDecimal income ) {
        return incomeTaxBracketsDao.findIncomeTaxBracketsByLowerLimitIsLessThanEqualOrderByLowerLimit(income);
    }

    public BigDecimal calculateIncomeTax(BigDecimal income ) {

        if (income.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        List<IncomeTaxBrackets> incomeTaxBracketsList = getIncomeBracketsForIncome(income);

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
}
