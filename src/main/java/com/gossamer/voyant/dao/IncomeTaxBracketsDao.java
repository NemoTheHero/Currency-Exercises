package com.gossamer.voyant.dao;

import com.gossamer.voyant.entities.IncomeTaxBrackets;
import jakarta.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Table(name = "income_tax_brackets")
public interface IncomeTaxBracketsDao extends CrudRepository<IncomeTaxBrackets, Long> {
}
