package org.datatoys.util.radixtree;

import java.util.ArrayList;

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
    public void testInsertAtRoot() {
        RadixTrie<Integer> trie = new RadixTrie<>();
        RadixTrieNode<Integer> root = new RadixTrieNode<>();
        RadixTrie.putAtNode(trie, root, "testing", 1);
        Assert.assertEquals(1, root.children.size());
        Assert.assertNotNull(root.children.get(0));
        Assert.assertEquals("testing", root.children.get(0).prefix);
        Assert.assertEquals(new Integer(1), root.children.get(0).value);
    }

    @Test
    public void testReparentLeaf() {
        RadixTrieNode<Integer> root = new RadixTrieNode<>();
        RadixTrieNode<Integer> child = new RadixTrieNode<>();
        child.setPrefix("this_is_a_very_long_string");
        child.value = 1;
        root.children = (new ArrayList<RadixTrieNode<Integer>>());
        root.children.add(child);

        RadixTrie<Integer> trie = new RadixTrie<>();
        RadixTrie.putAtNode(trie, root, "this_is_a_not_very_long_string", 2);

        // verify the parent
        Assert.assertEquals(1, root.children.size());
        Assert.assertNotNull(root.children.get(0));
        Assert.assertEquals("this_is_a_",root.children.get(0).prefix);
        Assert.assertNull(root.children.get(0).value);

        // verify the children
        RadixTrieNode<Integer> parent = root.children.get(0);
        Assert.assertEquals(2, parent.children.size());

        RadixTrieNode<Integer> firstChild = parent.children.get(0);
        Assert.assertEquals("not_very_long_string", firstChild.prefix);
        Assert.assertEquals(new Integer(2), firstChild.value);

        RadixTrieNode<Integer> secondChild = parent.children.get(1);
        Assert.assertEquals("very_long_string", secondChild.prefix);
        Assert.assertEquals(new Integer(1), secondChild.value);
    }

    @Test
    public void testInsertAtInnerNode() {

        RadixTrie<Integer> trie = new RadixTrie<>();
        RadixTrieNode<Integer> root = new RadixTrieNode<>();

        RadixTrieNode<Integer> comNode = new RadixTrieNode<>();
        comNode.setPrefix("com.");

        RadixTrieNode<Integer> googleNode = new RadixTrieNode<>();
        googleNode.setPrefix("google");

        RadixTrieNode<Integer> mailNode = new RadixTrieNode<>();
        mailNode.setPrefix("mail");
        mailNode.value = (1);

        root.children = (new ArrayList<RadixTrieNode<Integer>>());
        root.children.add(comNode);

        comNode.children = (new ArrayList<RadixTrieNode<Integer>>());
        comNode.children.add(googleNode);

        googleNode.children = (new ArrayList<RadixTrieNode<Integer>>());
        googleNode.children.add(mailNode);

        RadixTrie.putAtNode(trie, root, "com.google", 3);

        Assert.assertEquals(1, root.children.size());   //root node children
        Assert.assertEquals(1, root.children.get(0).children.size()); // com node children
        Assert.assertEquals(1, root.children.get(0).children.get(0).children.size()); // google node children
        Assert.assertNull(root.children.get(0).children.get(0).children.get(0).children);   // mail node children

        Assert.assertEquals("google", root.children.get(0).children.get(0).prefix);
        Assert.assertEquals(new Integer(3), root.children.get(0).children.get(0).value);
    }

    @Test
    public void testReplaceLeaf() {
        RadixTrieNode<Integer> root = new RadixTrieNode<>();
        RadixTrieNode<Integer> child = new RadixTrieNode<>();
        child.setPrefix("this_is_a_very_long_string");
        child.value = (1);
        root.children = new ArrayList<RadixTrieNode<Integer>>();
        root.children.add(child);

        RadixTrie<Integer> trie = new RadixTrie<>();
        RadixTrie.putAtNode(trie, root, "this_is_a_very_long_string", 2);

        Assert.assertEquals(1, root.children.size());
        Assert.assertEquals("this_is_a_very_long_string", root.children.get(0).prefix);
        Assert.assertEquals(new Integer(2), root.children.get(0).value);
    }
}