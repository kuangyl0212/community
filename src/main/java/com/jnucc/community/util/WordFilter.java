package com.jnucc.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class WordFilter {

    private class TireNode {

        private boolean isEnd = false;
        private Map<Character, TireNode> children = new HashMap<>();

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }
    }

    private TireNode root = new TireNode();

    @PostConstruct
    private void init() {
        try (
            InputStream inputStream =
                    this.getClass().getClassLoader().getResourceAsStream("sensitive-word.txt");
            Reader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
        ) {
            String word;
            while ((word = reader.readLine()) != null) {
                this.add(word);
            }
        } catch (IOException e) {
            // TODO handle exception
        }
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text))
            return null;

        TireNode p = root;

        StringBuffer stringBuffer = new StringBuffer();

        for (int i= 0; i < text.length(); i ++ ) {
            if (isSymbol(text.charAt(i))) {
                stringBuffer.append(text.charAt(i));
                continue;
            }
            p = root;
            boolean sensitive = false;
            int j = i;
            for (; j < text.length(); j ++ ) {
                char c = text.charAt(j);
                if (isSymbol(c)) continue;
                TireNode child = p.children.get(c);
                if (child == null) break;

                if (child.isEnd) {
                    sensitive = true;
                    break;
                }

                p = child;
            }
            if (!sensitive) {
                stringBuffer.append(text.charAt(i));
            }
            else {
                stringBuffer.append("***");
                i = j;
            }
        }
        return stringBuffer.toString();
    }

    private boolean isSymbol(char c) {
        // 0x2E80~ox9FFF 属于东亚字符
        return !CharUtils.isAsciiAlphanumeric(c) && c < 0x2E80 || c > 0x9FFF;
    }

    private void add(String word) {
        TireNode p = root;
        for (int i = 0; i < word.length(); i ++) {
            char c = word.charAt(i);
            TireNode child = p.children.get(c);
            if (child == null) {
                child = new TireNode();
                p.children.put(c, child);
            }

            if (i == word.length() - 1)
                child.setEnd(true);

            p = child;
        }
    }
}
