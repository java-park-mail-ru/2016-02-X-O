package mechanics;

import account.User;
import database.DBService;
import util.Context;
import util.ResponseJson;

import java.io.IOException;

/**
 * Created by kvukolov on 25.05.16.
 */
public class GameSession {
    User firstPlayer;
    User secondPlayer;
    TicTacToeGame ticTacToeGame;

    @Override
    public int hashCode() {
        return (firstPlayer.getLogin() + secondPlayer.getLogin()).hashCode();
    }

    public GameSession(User firstPlayer, User secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.ticTacToeGame = new TicTacToeGame(firstPlayer, secondPlayer);

        final WebSocketService webSocketService = (WebSocketService) Context
                .getInstance()
                .get(WebSocketService.class);
        try
        {
            webSocketService.getSocketByUser(firstPlayer).send(buildStartGameMessage(secondPlayer).toString());
            webSocketService.getSocketByUser(firstPlayer).send(new ResponseJson().put("status", GameEvents.CONTINUE_GAME.getValue()).append("map", null).toString());
            webSocketService.getSocketByUser(secondPlayer).send(buildStartGameMessage(firstPlayer).toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage(User fromUser, String mesage)
    {
        final User toUser = fromUser.equals(firstPlayer) ? secondPlayer : firstPlayer;
        final WebSocketService webSocketService = Context
                .getInstance()
                .get(WebSocketService.class);
        final GameWebSocket gameWebSocket = webSocketService.getSocketByUser(toUser);
        final ResponseJson responseJson = new ResponseJson();
        responseJson.put("status", GameEvents.NEW_MESSAGE.getValue());
        responseJson.put("message", mesage);
        try
        {
            gameWebSocket.send(responseJson.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void processTurn(GameTurn turn){
        final WebSocketService webSocketService = (WebSocketService) Context
                .getInstance()
                .get(WebSocketService.class);

        try{
            ticTacToeGame.makeTurn(turn);
            final ResponseJson gameMap = ticTacToeGame.getJSONMap();

            final GameWebSocket webSocket = turn.getUser().equals(firstPlayer) ?
                    webSocketService.getSocketByUser(secondPlayer):
                    webSocketService.getSocketByUser(firstPlayer);
            gameMap.put("status", GameEvents.CONTINUE_GAME.getValue());
            try
            {
                webSocket.send(gameMap.toString());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (GameException e) {
            switch (e.getReason()) {
                case GAME_END:
                    try
                    {
                        final Long winner = turn.getUser().getId();
                        turn.getUser().setScore(turn.getUser().getScore() + 3);
                        turn.getUser().save();
                        final ResponseJson responseJson = new ResponseJson();
                        responseJson.put("status", GameEvents.GAME_END.getValue());
                        responseJson.put("winner", winner);
                        webSocketService.getSocketByUser(firstPlayer).send(responseJson.toString());
                        webSocketService.getSocketByUser(secondPlayer).send(responseJson.toString());
                    }
                    catch (IOException err)
                    {
                        err.printStackTrace();
                    }
                    endGame();
                    break;

                case DRAW:
                    try
                    {
                        final ResponseJson responseJson = new ResponseJson();
                        firstPlayer.setScore(firstPlayer.getScore() + 1);
                        secondPlayer.setScore(secondPlayer.getScore() + 1);
                        firstPlayer.save();
                        secondPlayer.save();
                        responseJson.put("status", GameEvents.DRAW.getValue());
                        webSocketService.getSocketByUser(firstPlayer).send(responseJson.toString());
                        webSocketService.getSocketByUser(secondPlayer).send(responseJson.toString());
                    }
                    catch (IOException err)
                    {
                        err.printStackTrace();
                    }
                    endGame();

                default:
                    final GameWebSocket webSocket = turn.getUser().equals(firstPlayer) ?
                            webSocketService.getSocketByUser(firstPlayer):
                            webSocketService.getSocketByUser(secondPlayer);
                    final ResponseJson responseJson = new ResponseJson();
                    responseJson.put("status", e.getReason().getValue());
                    try
                    {
                        webSocket.send(responseJson.toString());
                    }
                    catch (IOException err)
                    {
                        err.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void disconnect(User user)
    {
        final WebSocketService webSocketService = (WebSocketService) Context
                .getInstance()
                .get(WebSocketService.class);
        final GameWebSocket webSocket = user.equals(firstPlayer) ?
                webSocketService.getSocketByUser(secondPlayer):
                webSocketService.getSocketByUser(firstPlayer);

            final ResponseJson responseJson = new ResponseJson();
        responseJson.put("status", GameEvents.OPPONENT_DISCONNECT.getValue());
        try
        {
            if (webSocket != null)
            {
                webSocket.send(responseJson.toString());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        endGame();
    }

    private void endGame()
    {
        final WebSocketService webSocketService = (WebSocketService) Context
                .getInstance()
                .get(WebSocketService.class);

        webSocketService.closeSocket(firstPlayer);
        webSocketService.closeSocket(secondPlayer);

        final GameMecahnics gameMecahnics = (GameMecahnics) Context.getInstance().get(GameMecahnics.class);
        gameMecahnics.endSession(firstPlayer);
        gameMecahnics.endSession(secondPlayer);
    }

    private ResponseJson buildStartGameMessage(User opponent)
    {
        final ResponseJson responseJson = new ResponseJson();
        responseJson.put("status", GameEvents.START_GAME.getValue());
        responseJson.put("opponentName", opponent.getLogin());
        return responseJson;
    }
}
