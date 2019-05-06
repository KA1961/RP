var board,game = new Chess();

var MINIMAX_ALGO = function(depth, game, player_Colour,MaximizingPlayer) 
{
  if (depth === 0) 
  {
    current_score = evaluateBoard(game.board(), player_Colour);
    return [current_score, null];  
  }
  var GameMoves = game.moves();

  var bestMove = null;  
  var bestMoveScore;

  if(MaximizingPlayer == true)
    bestMoveScore=Number.NEGATIVE_INFINITY;
  else
    bestMoveScore=Number.POSITIVE_INFINITY;

  for (var i = 0; i < GameMoves.length; i++)//MINIMAX ALGO STARTS HERE
  {
    var move = GameMoves[i];
    game.move(move);
    current_score = MINIMAX_ALGO(depth-1, game, player_Colour, !MaximizingPlayer)[0];
    if (MaximizingPlayer && (current_score > bestMoveScore)) 
    {
        bestMoveScore = current_score;
        bestMove = move;  
    }
    else
    if (!MaximizingPlayer && (current_score < bestMoveScore))
    {
        bestMoveScore = current_score;
        bestMove = move;
    }
    game.undo();
  }
  return [bestMoveScore,bestMove];//returning bestscore and bestmove
};
//evaulation function for score calculation
var evaluateBoard = function(board, colour)
{
  var score = 0;
  board.forEach(function(row) 
  {
    row.forEach(function(piece) 
    {
      if (piece) 
      {
        if (piece.type === 'p')
        score = score + (100 * (piece['color'] === colour ? 1:-1));//adds 100 score for current player's pawn and subtracts 100 if opp move
        else if (piece.type === 'n')
        score = score + (300 * (piece['color'] === colour ? 1:-1));//adds 300 score for current player's knight and subtracts 300 if opp move  
        else if (piece.type === 'b')
        score = score + (300 * (piece['color'] === colour ? 1:-1));//adds 300 score for current player's bishop and subtracts 300 if opp move
        else if (piece.type === 'r')
        score = score + (500 * (piece['color'] === colour ? 1:-1));//adds 500 score for current player's rook and subtracts 500 if opp move
        else if (piece.type === 'q')
        score = score + (1000 * (piece['color'] === colour ? 1:-1));//adds 1000 score for current player's queen and subtracts 1000 if opp move
        else if (piece.type === 'k')
        score = score + (10000 * (piece['color'] === colour ? 1:-1));//adds 10000 score for current player's king and subtracts 10000 if opp move              
      }
    });
  });
  return score;
};
// Computer makes the move with the depth value set as the parameter for ex : depth=3
var makeMove = function(depth=3) {
  // exit if the game is over
  if (game.game_over() === true) 
  {
    return;
  }
  var move = MINIMAX_ALGO(depth, game,'b',true)[1];
  
  game.move(move);// It makes the move returned by minmax algo
  board.position(game.fen());//After making the move,updation of the board position is required
};
// After human move what move is taken by computer i.e in our case minmax algo is used by computer
var onDrop = function(source, target) {
  // To check whether the move is legal
  var move = game.move({
    from: source,
    to: target,
    promotion: 'q'
  });
 // If there is illegal move then it snapbacks
  if (move === null) return 'snapback';
  
  window.setTimeout(function() {makeMove(3);}, 2000);
};
// If game is over alert message to the user is shown
var onMoveEnd = function(oldPositon, newPosition) {
  if (game.game_over() === true) 
  {
    alert('Game is over try again!!');
  }
};
// Check before pick pieces that it is white and game is not over
var onDragStart = function(source, piece, position, orientation) {
  if (game.game_over() === true || piece.search(/^b/) !== -1) {
    return false;
  }
};
// This updates the board position after the piece snap for castling, for en passant move , and for pawn promotion purpose
var onSnapEnd = function() {
  board.position(game.fen());
};
// Configuring the board
var cfg = {
  draggable: true,
  position: 'start',
  // Handlers required for the actions taken by the user
  onMoveEnd: onMoveEnd,
  onDragStart: onDragStart,
  onDrop: onDrop,
  onSnapEnd: onSnapEnd
};
board = ChessBoard('board', cfg);