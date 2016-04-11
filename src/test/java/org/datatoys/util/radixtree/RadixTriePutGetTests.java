package org.datatoys.util.radixtree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class RadixTriePutGetTests {

    @Test
    public void singlePutGetTest() {
        RadixTrie<Integer> radixTrie = new RadixTrie<>();
        radixTrie.put("com.google.mail", 1);
        Assert.assertNotNull(radixTrie.get("com.google.mail"));
        Assert.assertEquals(new Integer(1), radixTrie.get("com.google.mail"));
    }

    @Test
    public void testPutGetUnrelatedStrings() {
        RadixTrie<Integer> radixTrie = new RadixTrie<>();
        radixTrie.put("com.google.mail", 1);
        radixTrie.put("org.foo.bar", 2);

        Assert.assertNotNull(radixTrie.get("com.google.mail"));
        Assert.assertEquals(new Integer(1), radixTrie.get("com.google.mail"));

        Assert.assertNotNull(radixTrie.get("org.foo.bar"));
        Assert.assertEquals(new Integer(2), radixTrie.get("org.foo.bar"));
    }

    @Test
    public void testInsertPerformance() {
        List<String> inserts = new LinkedList<>();
        Random random = new Random();
        for(int i = 0; i < 1000000; i++) {
            inserts.add(String.valueOf(random.nextInt()));
        }
        long start = System.currentTimeMillis();
//        List<String> copy = new LinkedList<>();
//        for(String i : inserts) {
//            copy.add(i);
//        }
        long end = System.currentTimeMillis();

        System.out.println("time taken for list:" + (end-start));
        RadixTrie<Void> voidRadixTrie = new RadixTrie<>();
        start = System.currentTimeMillis();
        for(String i : inserts) {
            voidRadixTrie.put(i, null);
        }
        end = System.currentTimeMillis();
        System.out.println("time taken for trie:" + (end-start));

        HashMap<String, Void> map = new HashMap<>();
        start = System.currentTimeMillis();
        for(String i : inserts) {
            map.put(i, null);
        }
        end = System.currentTimeMillis();
        System.out.println("time taken for map:" + (end-start));


    }


    @Test
    public void testPutRelatedStrings() {
        RadixTrie<Integer> radixTrie = new RadixTrie<>();
        radixTrie.put("com.google.mail", 1);
        radixTrie.put("com.google.plus", 2);

        Assert.assertNotNull(radixTrie.get("com.google.mail"));
        Assert.assertEquals(new Integer(1), radixTrie.get("com.google.mail"));

        Assert.assertNotNull(radixTrie.get("com.google.plus"));
        Assert.assertEquals(new Integer(2), radixTrie.get("com.google.plus"));
    }

    @Test
    public void testPutRelatedAfterUnrelated() {
        RadixTrie<Integer> radixTrie = new RadixTrie<>();
        radixTrie.put("com.google.mail", 1);
        radixTrie.put("org.wikipedia", 2);
        radixTrie.put("com.google.plus", 3);

        Assert.assertNotNull(radixTrie.get("com.google.mail"));
        Assert.assertEquals(new Integer(1), radixTrie.get("com.google.mail"));

        Assert.assertNotNull(radixTrie.get("org.wikipedia"));
        Assert.assertEquals(new Integer(2), radixTrie.get("org.wikipedia"));

        Assert.assertNotNull(radixTrie.get("com.google.plus"));
        Assert.assertEquals(new Integer(3), radixTrie.get("com.google.plus"));
    }


}
