battleship
==========

battleship coding project for voodoo lunchbox

- prebuilt executable JAR file can be found in the bin/ directory.

- program can be run like this:

> java -jar BattleshipMatch.jar [-e] 

- this client assumes a rails server is up locally (http://localhost:3000/) with the battleship-master ruby application loaded and running.

- the "-e" command-line switch is optional.  

- without the "-e" switch, both player 1 and player 2 will compete in "non-expert" mode.

- if passed, it will initialize player 1 with "expert" mode, which *should* result in a pretty consistent winning record.  "expert" mode implements some additional logic and strategy that makes the Player with that mode enabled a much stronger opponent.

