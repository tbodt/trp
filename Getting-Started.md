---
layout: page
title: Getting Started
permalink: /getting-started/
---

# TRP Commands

A TRP command has one format:

    data

That's all. Data is a big collection of sequences of words. There are several ways to specify data:
 - Text, in quotes, such as `"hello"`.
 - A category, such as `[words]`. A category is data that was put in a file for easy access.

Let's try this out at the TRP prompt.

    trp> "hello"
    hello
    trp> "yes no"
    yes no
    trp> [words]

That gives you a list of 118,619 words. I am not liable if your computer crashes.

Another way to specify data is by specifying data through another method, then transforming it. This is how:

    data: transformation1 transformation2 transformation3...

## Example

    trp> "tunoby": anagram in([words])
    bounty

Your questions:

 1. What is it doing?

    Answer: It's unscrambling "tunoby" into a word.

 2. What does the `anagram` transformation do?

    Answer: `anagram` transforms each item of data into every way to rearrange its letters. Try this:

        trp> "tunoby": anagram

    You get all 720 ways to rearrange the letter in "tunoby".

 3. What does the `in([words])` transformation do?

    Answer: Transformations look like functions in virtually every programming language. The argument to `in` is data, which is the `[words]` category. The `in` function filters out everything that isn't in the data, leaving what is in the data.

So, it's basically permuting "tunoby", then printing out the permutations that are words. Now you know why I called it The Rapid Permuter.

# Solving a Jumble

Recently, I used TRP to solve an entire Jumble puzzle. Here it is:

![Jumble puzzle](http://i.imgur.com/ED2osk6l.png)

<sub>(Reprinted without permission from Jumble. Actually, I think it's fair use. I found this on their website and extracted it from a PDF.)</sub>

Let's solve the puzzle!

    trp> "snukk": anagram in([words])
    skunk
    trp> "wreef": anagram in([words])
    fewer
    trp> "fuminf": anagram in([words])
    muffin
    trp> "rettul": anagram in([words])
    turtle

Now, if we pick out the letters in the circles, we get "skfwufnte". Let's try that:

    trp> "skfwufnte": anagram in([words])
    trp>

We get nothing. Why?

Well, suppose we come upon the permutation "knewstuff". Even though "knew" and "stuff" are words, "knewstuff" isn't. So the correct answer is ruled out.

So maybe we need to split words like "knewstuff" into "knew stuff":

    trp> "sfkwufnte": anagram splitWords(4, 5) in([words])

But "knew stuff" isn't a word either, even though the words in it are not. What we need to do is more complicated:

    trp> "sfkwufnte": anagram splitWords(4, 5) all(in([words]))

The `all` function, or filter in this case, is the logical AND of the given filter applied to every word. In other words, it allows word sequences where each word passes the filter. There is a similar `any` function.

The `splitWords` function splits the item into multiple words of the given lengths. For example, "knewstuff" is split into "knew stuff" in this case. If it said `splitWords(3, 6)`, it would be split into "kne wstuff". If it said `splitWords(4, 4)`, it would be eliminated as a possible choice. (Be careful there!)

If we do it right, we get this output:

    weft funks
    funk wefts
    knew stuff

This is where the computer reaches its limit. A computer cannot tell that a taxidermist cannot possibly weft his "funks". But if we try "knew stuff", we get this:

> He was named the taxidermist of the year because he knew his "stuff".

Ha ha. Not funny at all. But that's what Jumbles are like, right?

If you want a list of all the functions and documentation, check the [[Function Reference|Function Reference]] page.