package ru.isador.games.seabattle.services.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.isador.games.seabattle.domain.config.Config;

@ApplicationScoped
public class FleetValidator {

    private final ObjectMapper mapper;

    @Inject
    public FleetValidator(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void validate(Config predefinedConfig, String fleet) throws FleetValidationException, JsonProcessingException {
        byte[][] fleetMatrix = mapper.readValue(fleet, byte[][].class);

        byte fieldSize = predefinedConfig.getFieldSize();
        if (fleetMatrix.length != fieldSize) {
            throw new FleetValidationException("Высота поля не соответствует настройкам");
        }

        boolean bordersAllowed = predefinedConfig.isBordersAllowed();
        boolean cornersAllowed = predefinedConfig.isCornersAllowed();
        FleetHolder holder = new FleetHolder();
        Map<Byte, Byte> actualFleet = new HashMap<>();

        // TODO: вставить проверку размера корабля

        for (byte i = 0; i < fleetMatrix.length; i++) {
            if (fleetMatrix[i].length != fieldSize) {
                throw new FleetValidationException("Ширина поля не соответствует настройкам");
            }
            for (byte j = 0; j < fleetMatrix[i].length; j++) {
                if (fleetMatrix[i][j] < 1 || holder.isCoordsVisited(i, j)) {
                    continue;
                }

                byte deckCount = fleetMatrix[i][j];
                Direction shipDirection = getDirection(i, j, fleetMatrix);

                // Проверка целостности корабля
                if (shipDirection.equals(Direction.VERTICAL) && !isPointMatrix((byte) (i + deckCount - 1), j, (byte) fleetMatrix.length)) {
                    throw new FleetValidationException("Корабль [" + i + ", " + j + ", " + shipDirection + " выходит за границы поля");
                }

                if (shipDirection.equals(Direction.HORIZONTAL) && !isPointMatrix(i, (byte) (j + deckCount - 1), (byte) fleetMatrix.length)) {
                    throw new FleetValidationException("Корабль [" + i + ", " + j + ", " + shipDirection + "] выходит за границы поля");
                }

                // Проверка касаний границ поля кораблём
                if (!bordersAllowed) {
                    if (i == 0
                        || j == 0
                        || i == fleetMatrix.length - 1
                        || j == fleetMatrix.length - 1
                        || (shipDirection.equals(Direction.VERTICAL) && i + deckCount == fleetMatrix.length)
                        || (shipDirection.equals(Direction.HORIZONTAL) && j + deckCount == fleetMatrix.length)) {
                        throw new FleetValidationException("Корабль [" + i + ", " + j + "] касается границы поля");
                    }
                }

                if (!cornersAllowed) {
                    if (hasCornerCollision(i, j, fleetMatrix)) {
                        throw new FleetValidationException("Корабль [" + i + ", " + j + "] касается углом другого корабля");
                    }

                    if (shipDirection.equals(Direction.VERTICAL)) {
                        if (hasCornerCollision((byte) (i + deckCount - 1), j, fleetMatrix)) {
                            throw new FleetValidationException(
                                "Корабль [" + (i + deckCount - 1) + ", " + j + ", " + shipDirection + "] касается углом другого корабля");
                        }
                    }

                    if (shipDirection.equals(Direction.HORIZONTAL)) {
                        if (hasCornerCollision(i, (byte) (j + deckCount - 1), fleetMatrix)) {
                            throw new FleetValidationException(
                                "Корабль [" + i + ", " + (j + deckCount - 1) + ", " + shipDirection + "] касается углом другого корабля");
                        }
                    }
                }

                // Проверка коллизий по вертикали и горизонтали
                for (int shipIndex = 0; shipIndex < deckCount; shipIndex++) {
                    if (shipDirection.equals(Direction.VERTICAL)) {
                        if (fleetMatrix[i + shipIndex][j] != deckCount) {
                            throw new FleetValidationException("Найдена коллизия [" + (i + shipIndex) + ", " + j + "]");
                        }

                        if ((shipIndex == 0) && hasTopCollision((byte) (i + shipIndex), j, fleetMatrix)) {
                            throw new FleetValidationException("Найдена коллизия [" + (i + shipIndex) + ", " + j + "]");
                        }

                        // Корабли не должны касаться друг друга по бокам
                        if (hasLeftCollision((byte) (i + shipIndex), j, fleetMatrix)
                            || hasRightCollision((byte) (i + shipIndex), j, fleetMatrix)) {
                            throw new FleetValidationException("Найдена коллизия [" + (i + shipIndex) + ", " + j + "]");
                        }

                        if ((shipIndex == deckCount - 1) && hasBottomCollision((byte) (i + shipIndex), j, fleetMatrix)) {
                            throw new FleetValidationException("Найдена коллизия [" + (i + shipIndex) + ", " + j + "]");
                        }
                    }

                    if (shipDirection.equals(Direction.HORIZONTAL)) {
                        if (fleetMatrix[i][j + shipIndex] != deckCount) {
                            throw new FleetValidationException("Найдена коллизия [" + i + ", " + (j + shipIndex) + "]");
                        }

                        if ((shipIndex == 0) && hasLeftCollision(i, (byte) (j + shipIndex), fleetMatrix)) {
                            throw new FleetValidationException("Найдена коллизия [" + i + ", " + (j + shipIndex) + "]");
                        }
                        // Корабли не должны касаться друг друга сверху и снизу
                        if (hasTopCollision(i, (byte) (j + shipIndex), fleetMatrix)
                            || hasBottomCollision(i, (byte) (j + shipIndex), fleetMatrix)) {
                            throw new FleetValidationException("Найдена коллизия [" + i + ", " + (j + shipIndex) + "]");
                        }

                        if ((shipIndex == deckCount - 1) && hasRightCollision(i, (byte) (j + shipIndex), fleetMatrix)) {
                            throw new FleetValidationException("Найдена коллизия [" + i + ", " + (j + shipIndex) + "]");
                        }
                    }
                }

                holder.addShip(i, j, deckCount, shipDirection);
                actualFleet.compute(deckCount, (k, v) -> (byte) (v == null ? 1 : v + 1));
            }
        }

        Map<Byte, Byte> expectedFleet = getFleet(predefinedConfig.getShips());
        if (!expectedFleet.equals(actualFleet)) {
            throw new FleetValidationException("Набор кораблей не совпадает с настройками");
        }
    }

    private boolean hasTopCollision(byte x, byte y, byte[][] fleetMatrix) {
        return isPointMatrix((byte) (x - 1), y, (byte) fleetMatrix.length)
               && (fleetMatrix[x - 1][y] != fleetMatrix[x][y] && fleetMatrix[x - 1][y] != 0);
    }

    private boolean hasBottomCollision(byte x, byte y, byte[][] fleetMatrix) {
        return isPointMatrix((byte) (x + 1), y, (byte) fleetMatrix.length)
               && (fleetMatrix[x + 1][y] != fleetMatrix[x][y] && fleetMatrix[x + 1][y] != 0);
    }

    private boolean hasLeftCollision(byte x, byte y, byte[][] fleetMatrix) {
        return isPointMatrix(x, (byte) (y - 1), (byte) fleetMatrix.length)
               && (fleetMatrix[x][y - 1] != fleetMatrix[x][y] && fleetMatrix[x][y - 1] != 0);
    }

    private boolean hasRightCollision(byte x, byte y, byte[][] fleetMatrix) {
        return isPointMatrix(x, (byte) (y + 1), (byte) fleetMatrix.length)
               && (fleetMatrix[x][y + 1] != fleetMatrix[x][y] && fleetMatrix[x][y + 1] != 0);
    }

    private Direction getDirection(byte x, byte y, byte[][] fleetMatrix) {
        if (isPointMatrix(x, (byte) (y + 1), (byte) fleetMatrix.length) && fleetMatrix[x][y] == fleetMatrix[x][y + 1]) {
            return Direction.HORIZONTAL;
        }

        return Direction.VERTICAL;
    }

    private boolean isPointMatrix(byte x, byte y, byte fieldSize) {
        return ((x > -1) && (x < fieldSize)) && ((y > -1) && (y < fieldSize));
    }

    private boolean hasCornerCollision(byte x, byte y, byte[][] fleetMatrix) {
        return (isPointMatrix((byte) (x + 1), (byte) (y + 1), (byte) fleetMatrix.length) && fleetMatrix[x + 1][y + 1] != 0)
               || (isPointMatrix((byte) (x + 1), (byte) (y - 1), (byte) fleetMatrix.length) && fleetMatrix[x + 1][y - 1] != 0)
               || (isPointMatrix((byte) (x - 1), (byte) (y - 1), (byte) fleetMatrix.length) && fleetMatrix[x - 1][y - 1] != 0)
               || (isPointMatrix((byte) (x - 1), (byte) (y + 1), (byte) fleetMatrix.length) && fleetMatrix[x - 1][y + 1] != 0);
    }

    private Map<Byte, Byte> getFleet(String fleetConfig) {
        String[] ships = fleetConfig.split("\\s*;\\s*");
        if (ships.length == 0) {
            return Map.of();
        }

        Map<Byte, Byte> shipConfig = new HashMap<>();
        for (String ship : ships) {
            String[] s = ship.split("\\s*x\\s*");
            if (s.length != 2) {
                return Map.of();
            }

            byte deckCount = Byte.parseByte(s[0]);
            byte count = Byte.parseByte(s[1]);
            shipConfig.compute(deckCount, (k, v) -> v == null ? count : (byte) (v + count));
        }

        return shipConfig;
    }

    private enum Direction {
        HORIZONTAL,
        VERTICAL;
    }

    private static class FleetHolder {

        private final List<VisitedBlock> ships;

        private FleetHolder() {
            this.ships = new ArrayList<>();
        }

        public void addShip(byte x, byte y, byte deckCount, Direction direction) {
            if (direction.equals(Direction.VERTICAL)) {
                for (byte i = 0; i < deckCount; i++) {
                    ships.add(new VisitedBlock((byte) (x + i), y));
                }
            }

            if (direction.equals(Direction.HORIZONTAL)) {
                for (byte i = 0; i < deckCount; i++) {
                    ships.add(new VisitedBlock(x, (byte) (y + i)));
                }
            }
        }

        public boolean isCoordsVisited(byte x, byte y) {
            return ships.stream()
                       .anyMatch(s -> s.x == x && s.y == y);
        }

        private record VisitedBlock(byte x, byte y) {
        }
    }
}
