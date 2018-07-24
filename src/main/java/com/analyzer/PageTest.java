package com.analyzer;

import com.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 21:31
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class PageTest {
    //MatchAllDocsQuery 查询所有
    @Test
    public void testAll() throws IOException {
        MatchAllDocsQuery matchAllDocsQuery = new MatchAllDocsQuery();
        search(matchAllDocsQuery, 3, 5);
    }

    //分页查询
    public static void search(Query query, Integer nowPage, Integer pageSize) throws IOException {
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        int begin = (nowPage - 1) * pageSize;
        int end = nowPage * pageSize;
        TopDocs topDocs = indexSearcher.search(query, end);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //查询文档符合结果的数量
        System.out.println("查询的文档总数:"+topDocs.totalHits);
        for (int i = begin; i < end; i++) {
            //遍历文档数组， 获取文档信息
            ScoreDoc scoreDoc = scoreDocs[i];
            Document document = indexSearcher.doc(scoreDoc.doc);
            List<IndexableField> fields = document.getFields();
            System.out.println("文档得分："+scoreDoc.score);
            for (IndexableField field : fields) {
                System.out.println(field.name()+":"+document.get(field.name()));
            }
        }
    }
}

