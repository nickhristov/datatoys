package org.datatoys.util.radixtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

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
    public void mainTestInsert() {
        int[] nums = new int[]{-1000008135, -1001544913, -1001748507,
            -1004988372, -1005011640,
            -1009148618, -1009798692, -1024677020};

        RadixTrie<Integer> voidRadixTrie = new RadixTrie<>();
        for(int i = 0; i < nums.length; i++) {
            int beforeEntrySize = voidRadixTrie.entrySet().size();
            voidRadixTrie.put(String.valueOf(nums[i]), 0);
            int afterEntrySize = voidRadixTrie.entrySet().size();
            if (afterEntrySize != (beforeEntrySize+1)) {
                Assert.fail("at index " + i);
            }

        }
    }

    @Test
    public void putGetMany() {
        List<String> inserts = new ArrayList<>(2000000);
        Random random = new Random();
        for(int i = 0; i < 2000000; i++) {
            inserts.add(String.valueOf(random.nextInt()));
        }
        RadixTrie<Integer> voidRadixTrie = new RadixTrie<>();
        int counter = 0;

        // make the data unique:
        Collections.sort(inserts);
        List<String> ll = new LinkedList<>();
        ll.addAll(inserts);

        Iterator<String> it = ll.iterator();
        String prev = null;
        while (it.hasNext()) {
            String current = it.next();
            if (current.equals(prev)) {
                it.remove();
            } else {
                prev = current;
            }
        }
        inserts.clear();
        inserts.addAll(ll);
        for(int i = 0; i < inserts.size(); i++) {
            String val = inserts.get(i);
            voidRadixTrie.put(val, counter);
        }
        counter = 0;
        for(int i = 0; i < inserts.size(); i++) {
            String val = inserts.get(i);
            Integer v = voidRadixTrie.get(val);
            if (v == null) {
                v = voidRadixTrie.get(val);
                Assert.fail("Result should not be null for key " +  val);
            }
            counter++;
        }
    }

    @Test
    public void testInsertPerformance() {
        List<String> inserts = new LinkedList<>();
        Random random = new Random();
        for(int i = 0; i < 900000; i++) {
            inserts.add(String.valueOf(random.nextInt()));
        }
        long start = System.currentTimeMillis();
        List<String> copy = new LinkedList<>();
        for(String i : inserts) {
            copy.add(i);
        }
        long end = System.currentTimeMillis();

        System.out.println("time taken for list:" + (end-start));
        RadixTrie<Void> voidRadixTrie = new RadixTrie<>();
        System.out.println("starting trie");
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

        TreeMap<String, Void> treeMap = new TreeMap<>();

        start = System.currentTimeMillis();
        for(String i : inserts) {
            treeMap.put(i, null);
        }
        end = System.currentTimeMillis();
        System.out.println("time taken for tree map:" + (end-start));

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
