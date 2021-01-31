# Connect Four

A repository for implementing **"Connect Four"** game in as a group project for academic purpose.

**Developers:**

Yasin Sazid (BSSE 1006)

Atkia Akila Karim (BSSE 1015)

Abdullah-Al-Jahid (BSSE 1030)

**Technology Used:** Java, JavaFX

**Features:**

1.Connect 4 board game

2.Choosing disc colour

3.Turn indicator

4.Help button for gameplay details

**Game Mechanics:**

1.Base algorithm used for AI: Minimax

2.Alpha-Beta Pruning is used to minimise the game tree.

3.Heuristic Method:

4.The number of discs a player has in a winning pattern is a heuristic to estimate his chances of winning by completing that particular pattern (given that     there is no opposing disc in that particular pattern).

**Evaluation Function:**

At a particular state, for every winning pattern, we count the number of discs for AI and Human.

If, there is no opposing discs in a pattern,
For AI, score = pow(count, count)
For Human, score = - pow(count, count)

Then all the scores for every winning pattern are summed to get the final evaluation for that particular state.

Cutting off search is done at game tree depth = 5

**Additional Logic:**

1.In cases where Human can win with a single move, AI will block that move without making the game tree.

2.In cases where AI can win with a single move, AI will make that move without making the game tree.

3.Utility value for winning is inversely proportional to depth of the winning node in the game tree.



