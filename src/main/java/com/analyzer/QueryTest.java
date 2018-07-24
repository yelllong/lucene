package com.analyzer;

import com.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.List;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 19:59
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class QueryTest {
    private int doc;

    // BooleanQuery 组合条件查询
    @Test
    public void testBoolean() throws IOException {
        BooleanQuery booleanClauses = new BooleanQuery();
        booleanClauses.add(NumericRangeQuery.newIntRange("id", 3, 7, true, true), BooleanClause.Occur.MUST);
        booleanClauses.add(new FuzzyQuery(new Term("content", "风雨"), 2), BooleanClause.Occur.MUST);
        search(booleanClauses);
    }
    // FuzzyQuery 模糊查询
    @Test
    public void testFuzzy() throws IOException {
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("content", "风雨"), 1);
        search(fuzzyQuery);
    }
    // WildCardQuery
    @Test
    public void testWild() throws IOException {
        // ? 代表单个任意字符  * 代表多个任意字符
        WildcardQuery wildcardQuery = new WildcardQuery(new Term("content", "风雨?"));
        search(wildcardQuery);
    }
    //  NumericRangeQuery int值的域 范围查询
    @Test
    public void testNumric() throws IOException {
        NumericRangeQuery<Integer> rangeQuery = NumericRangeQuery.newIntRange("id", 4, 6, false, true);
        search(rangeQuery);
    }
    //MatchAllDocsQuery 查询所有
    @Test
    public void testAll() throws IOException {
        MatchAllDocsQuery matchAllDocsQuery = new MatchAllDocsQuery();
        search(matchAllDocsQuery);
    }
    //使用 MultiFieldQueryParser 多域查询
    @Test
    public void testMulti() throws Exception {
        //String[] fields = {"title", "content"};
        String[] fields = {"date"};
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(Version.LUCENE_44, fields, new IKAnalyzer());
        search(multiFieldQueryParser.parse("745-9-9"));
    }

    public static void search(Query query) throws IOException {
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        TopDocs topDocs = indexSearcher.search(query, 100);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        System.out.println("文档数量："+topDocs.totalHits);
        // 遍历文档数组， 获取文档信息
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            //获得文档中的所有属性
            List<IndexableField> fields = doc.getFields();
            System.out.println("文档得分："+scoreDoc.score);
            for (IndexableField field : fields) {
                System.out.println(field.name()+"---->"+doc.get(field.name()));
            }
        }
    }
}

