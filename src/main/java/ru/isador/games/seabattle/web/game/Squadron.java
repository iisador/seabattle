package ru.isador.games.seabattle.web.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Squadron {

    private final Map<Byte, List<Ship>> ships;
    private final char[][] matrix;

    public Squadron(int fieldSize) {
        ships = new HashMap<>();
        matrix = new char[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                matrix[i][j] = '-';
            }
        }
    }

    public void addShip(Ship s) {
        ships.computeIfAbsent((byte) s.getDecks().size(), integer -> new ArrayList<>()).add(s);
    }

    public FireResult fire(int x, int y) {
        FireResult result = FireResult.MISSED;
        for (List<Ship> ships : ships.values()) {
            for (Ship ship : ships) {
                for (Deck deck : ship.getDecks()) {
                    if (deck.getX() == x && deck.getY() == y) {
                        deck.setAlive(false);
                        result = FireResult.WOUNDED;
                        matrix[x][y] = 'w';
                        break;
                    }
                }
                if (result.equals(FireResult.WOUNDED) && !ship.isAlive()) {
                    result = FireResult.KILLED;
                    ship.getDecks()
                        .forEach(d -> matrix[d.getX()][d.getY()] = 'k');
                }
            }
        }

        if (result.equals(FireResult.KILLED) && !isAlive()) {
            result = FireResult.ALL_KILLED;
        }

        if (result.equals(FireResult.MISSED)) {
            matrix[x][y] = '.';
        }
        return result;
    }

    public boolean isAlive() {
        boolean alive = false;
        for (List<Ship> ships : ships.values()) {
            for (Ship ship : ships) {
                if (ship.isAlive()) {
                    alive = true;
                }
            }
        }
        return alive;
    }

    public Map<Byte, Byte> getAliveShips() {
        Map<Byte, Byte> aliveShips = new HashMap<>();
        for (Map.Entry<Byte, List<Ship>> e : ships.entrySet()) {
            for (Ship ship : e.getValue()) {
                aliveShips.putIfAbsent(e.getKey(), (byte) 0);
                if (ship.isAlive()) {
                    aliveShips.put(e.getKey(), (byte) (aliveShips.get(e.getKey()) + 1));
                }
            }
        }
        return aliveShips;
    }

    public byte getHitCount() {
        byte count = 0;
        for (List<Ship> ships : ships.values()) {
            for (Ship ship : ships) {
                for (Deck deck : ship.getDecks()) {
                    if (!deck.isAlive()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public char[][] getMatrix() {
        return matrix;
    }
}
