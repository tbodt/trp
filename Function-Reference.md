---
layout: page
title: Function Reference
permalink: /function-reference/
---

This page summarizes all the functions in TRP. It also documents them in depth.

You can browse through the table of functions. If you find a function to like, click its name to see it in detail. If you want to purchase it, click the big, prominent, flashing button.

If you're going to add to this, it's important to say what a function does in *every possible case*, including multiple words, string not present for `remove`, the JVM not having a garbage collector, the JVM crashing, the computer crashing, nuclear war, the universe exploding, and anything else you might think of.

## Transformers

Transformers replace each word sequence with one or more other word sequences.

Name                                 | Summary
-------------------------------------|------------
[`anagram`](#anagram)                | Replaces the word with all anagrams of the word.
[`splitWords(int...)`](#splitWords)  | Splits the word into words with the given lengths.
[`remove(str)`](#remove)             | Removes the specified text from a word.
[`append(str)`](#append)             | Adds the specified text to the end of a word.

&nbsp;

# anagram

Replaces the word with all anagrams of the word. Multiple words are anagrammed individually.

    trp> "hi": anagram
    hi
    ih
    trp> "hi lo": anagram
    hi lo
    ih lo
    hi ol
    ih ol
    trp> "metlub": anagram [words]
    tumble
    
# splitWords

Splits the word into multiple words with the given length. Multiple words are removed.

    trp> "hellohowareyou": splitWords(5, 3, 3, 3)
    hello how are you
    trp> "multiple words": splitWords(1, 2, 3)
    trp> "sfkwufnte": anagram splitWords(4, 5) all([words])
    weft funks
    funk wefts
    knew stuff

# remove

Removes the text from each word. Words that do not contain the text are filtered out.

    trp> "the rapid permuter": remove("e")
    th rapid prmutr
    trp> "iioxdoo xdooww": remove("doo")
    iiox xww
    
<sub> If someone can think of a better example, please edit this.</sub>

# append

Adds the specified text to the end of a word. Multiple words are filtered out.

    trp> "trp": append("!")
    trp!
    trp> "the rapid permuter": append("!")

## Filters

Filters remove some word sequences. They are separate from transformers mainly for the purposes of `any` and `all`.

Name                                          | What Stays
----------------------------------------------|------------
[`length(int)`](#length)                      | Words with the given length.
[`length(int, int)`](#length)                 | Words with length in the given range.
[`startsWith(str)`](#startswithendswith) | Words that start with the string.
[`endsWith(str)`](#startswithendswith)   | Words that end with the string.
[`contains(str)`](#contains)                  | Words that contain the string.
[`countWords(int)`](#countWords)              | Word sequences with exactly the right number of words.
[`in(data)`](#in)                              | Word sequences in the data.
[`all(filter)`](#anyall)                 | All words in sequence match filter.
[`any(filter)`](#anyall)                 | Any words in sequence match filter.

&nbsp;

# length

Keeps words with the given length, or in the given range. Multiple words are removed.

    trp> "ooosdg": length(6)
    ooosdg
    trp> "iisogh": length(9)
    trp> "isougw": length(2, 9)
    isougw
    trp> [words]: length(1)
    a
    i

# startsWith/endsWith

Keeps words starting with or ending with the given text. Multiple words are removed.

    trp> "ooosdg": startsWith("ooo")
    ooosdg
    trp> "iisogh": endsWith("ogh")
    iisogh
    trp> [words]: startsWith("qu") endsWith("tion")
    question
    quantification
    quotation
    qualification
    quadruplication
    quantization

# contains

Keeps words containing the string. Multiple words are filtered out.

    trp> "ooosdg": contains("oos")
    ooosdg
    trp> "iisogh ooosdg": contains("oos")

# countWords

Keeps word sequences with exactly the right number of words.

    trp> "ooosdg": countWords(1)
    ooosdg
    trp> "iisogh ooosdg": countWords(2)

# in

Keeps word sequences in the given set of data.

    trp> "hello": in([words])
    trp> "hello goodbye": any(contains("ood"))
    hello goodbye

# any/all

Keeps word sequences where any or all of the words match the specified filter.

    trp> "hello goodbye": all(contains("ood"))
    trp> "hello goodbye": any(contains("ood"))
    hello goodbye