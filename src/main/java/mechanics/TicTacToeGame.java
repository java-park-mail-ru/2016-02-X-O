package mechanics;

import account.User;
import util.ResponseJson;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kvukolov on 26.05.16.
 */
public class TicTacToeGame {
    private static final int FIELD_SIZE = 3;
    private Field mainField;

    User firstUser;
    User secondUser;
    User currentUser;

    public TicTacToeGame(User firstUser, User secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.currentUser = firstUser;
        final SquareField[] squareFields = new SquareField[FIELD_SIZE * FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE * FIELD_SIZE; i++)
        {
            final SimpleField[] simpleFields = new SimpleField[FIELD_SIZE * FIELD_SIZE];
            for (int k = 0; k < FIELD_SIZE * FIELD_SIZE; k++)
            {
                simpleFields[k] = new SimpleField(null);
            }
            squareFields[i] = new SquareField(simpleFields);
        }
        mainField = new MainField(squareFields);
    }

    public void makeTurn(GameTurn turn) throws GameException
    {
        if (!currentUser.equals(turn.getUser()))
        {
            throw new GameException(GameEvents.ERROR_WRONG_USER_TURN);
        }
        final ArrayList<Integer> ids = new ArrayList<>();
        ids.add(turn.getId1());
        ids.add(turn.getId2());
        ids.add((int)(long)turn.getUser().getId());
        mainField.fill(ids);

        currentUser = currentUser.equals(firstUser) ? secondUser : firstUser;
    }

    ResponseJson getJSONMap()
    {
        final List<String> map = mainField.getMap();
        final ResponseJson responseJson = new ResponseJson();
        responseJson.append("map", map);
        return responseJson;
    }

    private abstract static class Field{
        Field[] fields;
        Integer filled = null;

        Field(Field[] fields) {
            this.fields = fields;
        }

        abstract List<String> getMap();

        public Integer getFilledCount()
        {
            Integer count = 0;
            for (Field field: fields)
            {
                if (field.filled != null)
                {
                    count++;
                }
            }
            return count;
        }

        public void fill(ArrayList<Integer> ids) throws GameException
        {
            if (ids.get(0) >= fields.length)
            {
                throw new GameException(GameEvents.ERROR_WRONG_DATA);
            }
            final Field field = fields[ids.get(0)];
            if (field.filled != null)
            {
                throw new GameException(GameEvents.ERROR_FIELD_BUSY);
            }
            ids.remove(0);
            field.fill(ids);
        }

        Integer haveCompleteHorizontalLines()
        {
            for (int i = 0; i < FIELD_SIZE; i++)
            {
                final List<Field> checkFileds = new ArrayList<>();
                for (int k = i ; k < FIELD_SIZE * FIELD_SIZE; k += FIELD_SIZE)
                {
                    checkFileds.add(fields[k]);
                }
                if (checkAllFilled(checkFileds))
                {
                    return checkFileds.get(0).filled;
                }
            }
            return null;
        }

        Integer haveCompleteVerticalLines()
        {
            for (int i = 0; i < FIELD_SIZE * FIELD_SIZE; i += FIELD_SIZE)
            {
                final List<Field> checkFileds = new ArrayList<>();
                for (int k = i ; k < i + FIELD_SIZE; k ++)
                {
                    checkFileds.add(fields[k]);
                }
                if (checkAllFilled(checkFileds))
                {
                    return checkFileds.get(0).filled;
                }
            }
            return null;
        }

        Integer haveCompleteDiagonalLines()
        {
            final List<Field> checkFileds = new ArrayList<>();
            for (int i = 0; i < FIELD_SIZE * FIELD_SIZE; i += FIELD_SIZE + 1)
            {
                checkFileds.add(fields[i]);
            }
            if (checkAllFilled(checkFileds))
            {
                return checkFileds.get(0).filled;
            }
            checkFileds.clear();
            for (int i = FIELD_SIZE - 1; i < FIELD_SIZE * FIELD_SIZE - 1; i += FIELD_SIZE - 1)
            {
                checkFileds.add(fields[i]);
            }
            if (checkAllFilled(checkFileds))
            {
                return checkFileds.get(0).filled;
            }
            return null;
        }

        private boolean checkAllFilled(List<Field> checkFields)
        {
            final Integer tempFilled = checkFields.get(0).filled;
            if (tempFilled == null)
            {
                return false;
            }
            for (Field field: checkFields)
            {
                if (field.filled == null || !field.filled.equals(tempFilled))
                {
                    return false;
                }
            }
            return true;
        }
    }

    private static class SquareField extends Field {
        SquareField(Field[] fields) {
            super(fields);
        }

        @Override
        List<String> getMap() {
            final List<String> map = new ArrayList<String>();
            for (int i = 0; i < fields.length; i++)
            {
                if (fields[i].filled != null)
                {
                    map.add(i + "." + fields[i].filled);
                }
            }
            return map;
        }

        @Override
        public void fill(ArrayList<Integer> ids) throws GameException {
            super.fill(ids);

            final List<Integer> completeList = new ArrayList<>();
            completeList.add(haveCompleteDiagonalLines());
            completeList.add(haveCompleteHorizontalLines());
            completeList.add(haveCompleteVerticalLines());

            for (Integer complete: completeList)
            {
                if (complete != null)
                {
                    this.filled = complete;
                    break;
                }
            }
        }
    }

    private static class SimpleField extends Field{
        SimpleField(Field[] fields) {
            super(fields);
        }

        @Override
        List<String> getMap() {
            return null;
        }

        @Override
        public void fill(ArrayList<Integer> ids) throws GameException {
            this.filled = ids.get(0);
        }
    }

    private static class MainField extends Field{
        MainField(Field[] fields) {
            super(fields);
        }

        @Override
        List<String> getMap() {
            final List<String> map = new ArrayList<>();
            for (int i = 0; i < fields.length; i++)
            {
                List<String> tempMap = fields[i].getMap();
                for (String mapItem: tempMap)
                {
                    mapItem = i + "." + mapItem;
                    map.add(mapItem);
                }
            }
            return map;
        }

        int currentId = -1;

        @Override
        public void fill(ArrayList<Integer> ids) throws GameException {
            final int id = ids.get(0);
            final int setId = ids.get(1);
            if (this.currentId != -1 && id != currentId)
            {
                throw new GameException(GameEvents.ERROR_WRONG_SQUARE);
            }

            super.fill(ids);

            currentId = setId;

            final Field field = fields[setId];
            if (field.filled != null || field.getFilledCount().equals(fields.length))
            {
                currentId = -1;
            }

            if (haveCompleteHorizontalLines() != null ||
                    haveCompleteVerticalLines() != null ||
                    haveCompleteDiagonalLines() != null)
            {
                throw new GameException(GameEvents.GAME_END);
            }
            if (getFilledCount().equals(fields.length))
            {
                throw new GameException(GameEvents.DRAW);
            }
        }
    }
}
