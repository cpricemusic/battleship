battleship
==========

battleship coding project for voodoo lunchbox

executable JAR file can be found in the bin/ directory.

it can be executed like this:
java -jar BattleshipMatch.jar [-e] 

this client assumes there is a rails server running locally (http://localhost:3000/) with the battleship-master ruby application up and running.

the "-e" switch is optional.  if passed, it will turn player 1 to "expert" mode, which *should* result in a pretty consistent winning record.  "expert" mode implements some additional logic and strategy that makes the Player with that mode enabled a much stronger opponent.

without the "-e" switch, both player 1 and player 2 will compete in "non-expert" mode.
