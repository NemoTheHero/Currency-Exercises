package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.KeywordsDao;
import com.gossamer.voyant.entities.Keyword;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {
    private final KeywordsDao keywordsDao;

    public KeywordService(KeywordsDao keywordsDao) {
        this.keywordsDao = keywordsDao;
    }

    public void addNewKeywords(List<String> keywordNames) {
        List<Keyword> existingKeywords = keywordsDao.getKeywordsByNames(keywordNames);
        Set<String> existingNames = existingKeywords.stream().map(Keyword::getKeyword).collect(Collectors.toSet());
        List<String> missingNames = keywordNames.stream()
                .filter(name -> !existingNames.contains(name))
                .toList();

        List<Keyword> toBeAdded = missingNames.stream().map(name -> Keyword.builder().keyword(name).build()).toList();
        keywordsDao.saveAll(toBeAdded);
    }

    public List<Keyword> getKeywordsByNames(List<String> names) {
        return keywordsDao.getKeywordsByNames(names);
    }
}
