package com.gossamer.voyant.dao;

import com.gossamer.voyant.entities.Country;
import com.gossamer.voyant.entities.IncomeTaxBrackets;
import jakarta.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Table(name = "income_tax_brackets")
public interface IncomeTaxBracketsDao extends CrudRepository<IncomeTaxBrackets, Long> {
    List<IncomeTaxBrackets> findIncomeTaxBracketsByLowerLimitIsLessThanEqual(BigDecimal income);

}
