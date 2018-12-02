### Some of the assumptions I've made

#### Users

There was no user story about adding/editing/removing user accounts therefore
I've decided to provide fake user service implementation with some hardcoded 
users. I can imagine multiple sources to take user information from, for example:
* possibility to add user accounts directly in parking meter app
* some city-wide integrated system for managing citizen personal data

#### Calculating parking rate for 3rd and each next hour

In requirements there is a notion that price for 3rd and each next hour is
calculated as 

> MULTIPLIER times more than the hour before

Theoretically price of nth hour could be calculated by raising second hour's price 
to a given power. However money have a limited accuracy and we could have some results 
that are fractions of a grosz. I've decided to use 
[half-even rounding](https://en.wikipedia.org/wiki/Rounding#Round_half_to_even)
which according to [this](https://stackoverflow.com/questions/8208922/which-rounding-mode-to-be-used-for-currency-manipulation-in-java)
stack overflow discussion seems to be the most common one.
