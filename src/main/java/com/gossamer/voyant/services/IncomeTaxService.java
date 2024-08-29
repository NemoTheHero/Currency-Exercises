package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.IncomeTaxBracketsDao;
import com.gossamer.voyant.entities.IncomeTaxBrackets;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public List<IncomeTaxBrackets> getIncomeBracketsForIncome(BigDecimal income ) {
        return incomeTaxBracketsDao.findIncomeTaxBracketsByLowerLimitIsLessThanEqual(income);
    }
}
