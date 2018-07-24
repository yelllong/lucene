package com.service;

import com.dao.ProductLuenceDAO;
import com.util.LuceneUtil;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;

import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 13:48
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class ProductLuenceService {
    @Test
    public void testCreate() {
        ProductLuenceDAO luenceDAO = new ProductLuenceDAO();
        luenceDAO.createIndex(null);
    }
    @Test
    public void testSearch() {
        ProductLuenceDAO luenceDAO = new ProductLuenceDAO();
        luenceDAO.searchIndex(null);
    }
    @Test
    public void testMerge() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        try {
            indexWriter.forceMerge(3);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            LuceneUtil.rollback(indexWriter);
        }
    }
}

