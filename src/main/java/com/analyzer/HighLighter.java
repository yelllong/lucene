package com.analyzer;

import com.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.List;


/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 22:02
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class HighLighter {
    @Test
    public void testMulti() throws Exception {
        String[] fields = {"title", "content"};
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(Version.LUCENE_44, fields, new IKAnalyzer());
        search(multiFieldQueryParser.parse("明月"));
    }
    //高亮展示
    public static void search(Query query) throws IOException, InvalidTokenOffsetsException {
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        TopDocs topDocs = indexSearcher.search(query, 20);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        System.out.println("文档数量为："+topDocs.totalHits);
        //设置 高亮器 检索是使用的query
        Scorer scorer = new QueryTermScorer(query);
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        Highlighter highlighter = new Highlighter(formatter, scorer);
        //遍历文档
        for (ScoreDoc scoreDoc : scoreDocs) {
            //该文档的相关度得分
            System.out.println("文档得分："+scoreDoc.score);
            Document doc = indexSearcher.doc(scoreDoc.doc);
            IKAnalyzer ikAnalyzer = new IKAnalyzer();
            List<IndexableField> fields = doc.getFields();
            for (IndexableField field : fields) {
                String fragment = highlighter.getBestFragment(ikAnalyzer, field.name(), doc.get(field.name()));
                if (fragment == null) {
                    System.out.println(field.name()+":"+doc.get(field.name()));
                } else {
                    System.out.println(field.name()+":"+ fragment);
                }
            }
        }
    }
}

