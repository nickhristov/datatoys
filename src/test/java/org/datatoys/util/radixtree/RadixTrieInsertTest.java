package org.datatoys.util.radixtree;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class RadixTrieInsertTest {

    @Test
    public void testNumCharsInCommonPrefix() {
        Assert.assertEquals(0, RadixTrie.numCharsInCommonPrefix("foo", "bar"));
        Assert.assertEquals(0, RadixTrie.numCharsInCommonPrefix("foo", "boo"));
        Assert.assertEquals(3, RadixTrie.numCharsInCommonPrefix("foo", "foobar"));
        Assert.assertEquals(3, RadixTrie.numCharsInCommonPrefix("foobar", "foo"));
    }


    @Test
    public void testNumCharsInCommonPrefix2() {
        Assert.assertEquals(0, RadixTrie.numCharsInCommonPrefix("foo".toCharArray(), "bar".toCharArray(), 0));
        Assert.assertEquals(0, RadixTrie.numCharsInCommonPrefix("foo".toCharArray(), "boo".toCharArray(), 0));
        Assert.assertEquals(3, RadixTrie.numCharsInCommonPrefix("foo".toCharArray(), "foobar".toCharArray(), 0));
        Assert.assertEquals(3, RadixTrie.numCharsInCommonPrefix("foobar".toCharArray(), "foo".toCharArray(), 0));

        Assert.assertEquals(3, RadixTrie.numCharsInCommonPrefix("foo".toCharArray(), "ffoo".toCharArray(), 1));
        Assert.assertEquals(2, RadixTrie.numCharsInCommonPrefix("foo".toCharArray(), "ffo".toCharArray(), 1));
        Assert.assertEquals(0, RadixTrie.numCharsInCommonPrefix("foo".toCharArray(), "f".toCharArray(), 1));
        Assert.assertEquals(0, RadixTrie.numCharsInCommonPrefix("foobar".toCharArray(), "".toCharArray(), 1));
    }

    @Test
    public void testInsertAtRoot() {
        RadixTrie<Integer> trie = new RadixTrie<>();
        RadixTrieNode<Integer> root = new RadixTrieNode<>();
        RadixTrie.putAtNode(trie, root, "testing".toCharArray(), 0, 1, true);
        Assert.assertEquals(1, root.children.size());
        Assert.assertNotNull(root.children.get(0));
        Assert.assertTrue(Arrays.equals("testing".toCharArray(), root.children.get(0).chars));
        Assert.assertEquals(new Integer(1), root.children.get(0).value);
    }

    @Test
    public void testReparentLeaf() {
        RadixTrieNode<Integer> root = new RadixTrieNode<>();
        RadixTrieNode<Integer> child = new RadixTrieNode<>();
        child.chars ="this_is_a_very_long_string".toCharArray();
        child.value = 1;
        root.children = (new ArrayList<>());
        root.children.add(child);

        RadixTrie<Integer> trie = new RadixTrie<>();
        RadixTrie.putAtNode(trie, root, "this_is_a_not_very_long_string".toCharArray(), 0, 2,true);

        // verify the parent
        Assert.assertEquals(1, root.children.size());
        Assert.assertNotNull(root.children.get(0));
        Assert.assertTrue(Arrays.equals("this_is_a_".toCharArray(),root.children.get(0).chars));
        Assert.assertNull(root.children.get(0).value);

        // verify the children
        RadixTrieNode<Integer> parent = root.children.get(0);
        Assert.assertEquals(2, parent.children.size());

        RadixTrieNode<Integer> firstChild = parent.children.get(0);
        Assert.assertTrue(Arrays.equals("not_very_long_string".toCharArray(), firstChild.chars));
        Assert.assertEquals(new Integer(2), firstChild.value);

        RadixTrieNode<Integer> secondChild = parent.children.get(1);
        Assert.assertTrue(Arrays.equals("very_long_string".toCharArray(), secondChild.chars));
        Assert.assertEquals(new Integer(1), secondChild.value);
    }

    @Test
    public void testInsertAtInnerNode() {

        RadixTrie<Integer> trie = new RadixTrie<>();
        RadixTrieNode<Integer> root = new RadixTrieNode<>();

        RadixTrieNode<Integer> comNode = new RadixTrieNode<>();
        comNode.chars = "com.".toCharArray();

        RadixTrieNode<Integer> googleNode = new RadixTrieNode<>();
        googleNode.chars = "google".toCharArray();

        RadixTrieNode<Integer> mailNode = new RadixTrieNode<>();
        mailNode.chars = "mail".toCharArray();
        mailNode.value = (1);

        root.children = (new ArrayList<>());
        root.children.add(comNode);

        comNode.children = (new ArrayList<>());
        comNode.children.add(googleNode);

        googleNode.children = (new ArrayList<>());
        googleNode.children.add(mailNode);

        RadixTrie.putAtNode(trie, root, "com.google".toCharArray(), 0, 3, true);

        Assert.assertEquals(1, root.children.size());   //root node children
        Assert.assertEquals(1, root.children.get(0).children.size()); // com node children
        Assert.assertEquals(1, root.children.get(0).children.get(0).children.size()); // google node children
        Assert.assertNull(root.children.get(0).children.get(0).children.get(0).children);   // mail node children

        Assert.assertTrue(Arrays.equals("google".toCharArray(), root.children.get(0).children.get(0).chars));
        Assert.assertEquals(new Integer(3), root.children.get(0).children.get(0).value);
    }

    @Test
    public void testReplaceLeaf() {
        RadixTrieNode<Integer> root = new RadixTrieNode<>();
        RadixTrieNode<Integer> child = new RadixTrieNode<>();
        child.chars = "this_is_a_very_long_string".toCharArray();
        child.value = (1);
        root.children = new ArrayList<>();
        root.children.add(child);

        RadixTrie<Integer> trie = new RadixTrie<>();
        RadixTrie.putAtNode(trie, root, "this_is_a_very_long_string".toCharArray(), 0, 2, true);

        Assert.assertEquals(1, root.children.size());
        Assert.assertTrue(Arrays.equals("this_is_a_very_long_string".toCharArray(), root.children.get(0).chars));
        Assert.assertEquals(new Integer(2), root.children.get(0).value);
    }
}