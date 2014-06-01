SimpleTokeniser
===============

Very simple tokeniser built for an exercise. The goal is simple: for some given text build a "concordance". A concordance is an alphabetical list of all words in the text, with the frequency they appeared followed by citations on which sentence each word appears (word   {frequency:citations})

For example, here's the output for: "_There are only two hard things in Computer Science. Cache invalidation and naming things._"

```
and                       {1:1}
are                       {1:0}
cache                     {1:1}
computer                  {1:0}
hard                      {1:0}
in                        {1:0}
invalidation              {1:1}
naming                    {1:1}
only                      {1:0}
science                   {1:0}
there                     {1:0}
things                    {2:0,1}
two                       {1:0}
```

The code is relatively short and -hopefully- self explanatory. A custom "TokenMap" class was implemented to store the concordance, that provides some convenience methods. The "EarlyTokeniser.java" is an earlier attempt with different logic that doesn't match the requirements. 

The bulk of the solution is in "Tokeniser.java". You can look at the first commit of that file for an early version of the algorithm before it was refactored to its current form.
