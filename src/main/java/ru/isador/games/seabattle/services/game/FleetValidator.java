package ru.isador.games.seabattle.services.game;

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
        int[][] fleetMatrix = mapper.readValue(fleet, int[][].class);

        int fieldSize = predefinedConfig.getFieldSize();
        if (fleetMatrix.length != fieldSize) {
            throw new FleetValidationException();
        }

        boolean bordersAllowed = predefinedConfig.isBordersAllowed();
        boolean cornersAllowed = predefinedConfig.isCornersAllowed();
        for (int i = 0; i < fleetMatrix.length; i++) {
            if (fleetMatrix[i].length != fieldSize) {
                throw new FleetValidationException();
            }
            for (int j = 0; j < fleetMatrix[i].length; j++) {
                if (!bordersAllowed
                    && (i == 0 || i == (fleetMatrix.length - 1) || j == 0 || j == (fleetMatrix[i].length - 1))
                    && (fleetMatrix[i][j] != 0)) {
                    throw new FleetValidationException();
                }

                if (fleetMatrix[i][j] != 0 && hasCollision(cornersAllowed, i, j, fieldSize, fleetMatrix)) {
                    throw new FleetValidationException();
                }
            }
        }
        // проверить матрицу на ограничения (границы, углы)
        // проверить размер флота
        // профит
    }

    private boolean hasCollision(boolean cornersAllowed, int x, int y, int fleetSize, int[][] fleetMatrix) {
        return hasTopCollision(x, y, fleetSize, fleetMatrix)
               || hasBottomCollision(x, y, fleetSize, fleetMatrix)
               || hasLeftCollision(x, y, fleetSize, fleetMatrix)
               || hasRightCollision(x, y, fleetSize, fleetMatrix);
    }

    private boolean hasTopCollision(int x, int y, int fleetSize, int[][] fleetMatrix) {
        return isPointMatrix(x - 1, y, fleetSize)
               && (fleetMatrix[x - 1][y] != fleetMatrix[x][y] && fleetMatrix[x - 1][y] != 0);
    }

    private boolean isPointMatrix(int x, int y, int fieldSize) {
        return ((x > -1) && (x < fieldSize)) && ((y > -1) && (y < fieldSize));
    }

    private boolean hasBottomCollision(int x, int y, int fleetSize, int[][] fleetMatrix) {
        return isPointMatrix(x + 1, y, fleetSize)
               && (fleetMatrix[x + 1][y] != fleetMatrix[x][y] && fleetMatrix[x + 1][y] != 0);
    }

    private boolean hasLeftCollision(int x, int y, int fleetSize, int[][] fleetMatrix) {
        return isPointMatrix(x, y - 1, fleetSize)
               && (fleetMatrix[x][y - 1] != fleetMatrix[x][y] && fleetMatrix[x][y - 1] != 0);
    }

    private boolean hasRightCollision(int x, int y, int fleetSize, int[][] fleetMatrix) {
        return isPointMatrix(x, y + 1, fleetSize)
               && (fleetMatrix[x][y + 1] != fleetMatrix[x][y] && fleetMatrix[x][y + 1] != 0);
    }
}
