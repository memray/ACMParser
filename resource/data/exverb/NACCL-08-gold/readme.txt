This folder contains 500 labeled sentences.  Annotation for each sentence looks like the following:
-----------
OriginalSnt:finally <p1> google </p1> bought <p2> youtube </p2>
Snt:finally google bought youtube .
google;@;bought;@;youtube
========================
where, "OriginalSnt:" is the sentence with subject (<p1>)and object(<p2>) marked. "Snt:" is the cleaned sentence. The line below is a labeled  truth triple with the format of "arg1;@;predicate;@;arg2".  "arg1", "predicate" and "arg2" might contain ";;" separated fields which are considered as alternative(and correct) expressions.

Note, the 500 sentences are taken from the dataset(http://www.cs.washington.edu/research/knowitall/hlt-naacl08-data.txt) published in the following paper
"The Tradeoffs Between Open and Traditional Relation Extraction"
Michele Banko and Oren Etzioni
Proceedings of the 46th Annual Meeting of the Association for Computational Linguistics (ACL 2008)
