package com.analyzer;

import com.util.LuceneUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: lenovo
 * \* Date: 2018/7/23
 * \* Time: 19:20
 * \* To change this template use File | Settings | File Templates.
 * \* Description: 对于IKanalyzer 分词器的使用
 * \
 */
public class IKAnalyzerTest {
    private static String text = "说是寂寞的秋的清愁，说是辽远的海的相思，假如有人问我的烦忧，我不敢说出你的名字。我不敢说出你的名字，假如有人问我的烦忧，说是辽远的海的相思，说是寂寞的秋的清愁。";

    @Test
    public void ikAnalyzer() throws IOException {
        test(new IKAnalyzer(), text);
    }
    //输出分词的结果
    public static void test(Analyzer analyzer, String text) throws IOException {
        System.out.println("当前分词器：---》"+analyzer.getClass().getName());
        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
        tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
            //打印的是分词器，分过后的词
            System.out.println(attribute.toString());
        }
        tokenStream.end();
    }
    //测试数据
    @Test
    public void indexWriterTest() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        for (int i = 1; i <= 5; i++) {
            Document document = new Document();
            document.add(new IntField("id",i, Field.Store.YES));
            document.add(new StringField("title","春晓",Field.Store.YES));
            document.add(new StringField("author","贺知章",Field.Store.YES));
            document.add(new StringField("date","657-8-5",Field.Store.YES));
            document.add(new TextField("content","春眠不觉晓，处处闻啼鸟，夜来风雨声，花落知多少。", Field.Store.YES));
            /*document.add(new IntField("id",i, Field.Store.YES));
            document.add(new StringField("title","烦忧",Field.Store.YES));
            document.add(new StringField("author","戴望舒",Field.Store.YES));
            document.add(new StringField("date","1957-8-5",Field.Store.YES));
            document.add(new TextField("content","说是寂寞的秋的清愁，说是辽远的海的相思，假如有人问我的烦忧，我不敢说出你的名字。我不敢说出你的名字，假如有人问我的烦忧，说是辽远的海的相思，说是寂寞的秋的清愁。", Field.Store.YES));*/
            try {
                indexWriter.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LuceneUtil.commit(indexWriter);
    }
    //删除
    @Test
    public void testDelete() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        try {
            indexWriter.deleteAll();
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
        }
    }
}

