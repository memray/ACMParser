This folder contains three dataset for testing open IE. The three datasets are randomly annotated sentences from the general Web, Wikipedia and WSJ. The truth triples are picked manually and verified by Amazon Mechanical Turks(each candiate triple is evaluated by 5 turks and is considered as a final truth triple iff more than 3 turks agreed)

300Web:
I first used "http://www.randomwebsite.com/" to randomly generate web pages and then randomly select sentences from those pages. Annotation for each sentence looks like the following:
-----------
Snt:Four professional calligraphers spend three hours on each page every single day to put out this daily paper.
Four professional calligraphers;@;spend three hours;;spend hours;@;every single day
Four professional calligraphers;@;spend;@;three hours
Four professional calligraphers;@;spend three hours on;;spend hours on;@;each page
========================
where, "Snt:" is the random sentence. The lines below are labeled truth triples with the format of "arg1;@;predicate;@;arg2".  "arg1", "predicate" and "arg2" might contain ";;" separated fields which are considered as alternative(and correct) expressions.


300Wikipedia:
The sentences are randomly selected from Wikipedia corpus. The annotation format is similar to the WebSnts_gold, as described above.


300WSJ:
The sentences are randomly generated from the WSJ corpus in Penn Treebank. The annotation format is also the same